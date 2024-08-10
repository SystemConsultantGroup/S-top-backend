package com.scg.stop.aihub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AiHubDatasetResponse {
    private String object;
    private String id;
    private String cover;
    private String title; // 제목
    private List<String> dataTypes = new ArrayList<>(); // 데이터 유형
    private List<String> topics = new ArrayList<>(); // 주제 분류
    private List<Integer> developmentYears = new ArrayList<>(); // 구축년도
    private String professor; // 담당 교수
    private List<String> participants = new ArrayList<>(); // 참여 학생
    private String url;

    @JsonProperty("object")
    public void setObject(String object) {
        this.object = object;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("cover")
    public void setCover(JsonNode cover) {
        if (cover != null) {
            if (cover.has("external")) {
                this.cover = cover.get("external").get("url").asText();
            } else if (cover.has("file")) {
                this.cover = cover.get("file").get("url").asText();
            }
        }
    }

    @JsonProperty("properties")
    public void setProperties(JsonNode properties) {
        if (properties != null) {
            this.title = extractTextFromProperty(properties, "제목", "title");
            this.dataTypes = extractMultiSelect(properties, "데이터 유형");
            this.topics = extractMultiSelect(properties, "주제 분류");
            this.developmentYears = extractIntegerMultiSelect(properties, "구축 년도");
            this.professor = extractTextFromProperty(properties, "담당 교수", "rich_text");
            this.participants = extractParticipants(properties);
        }
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    private String extractTextFromProperty(JsonNode properties, String fieldName, String textType) {
        if (properties.has(fieldName)) {
            JsonNode fieldNode = properties.get(fieldName).get(textType);
            if (fieldNode.isArray() && fieldNode.size() > 0) {
                return fieldNode.get(0).get("text").get("content").asText();
            }
        }
        return null;
    }

    private List<String> extractParticipants(JsonNode properties) {
        String participantsString = extractTextFromProperty(properties, "참여 학생", "rich_text");
        if (participantsString != null && !participantsString.isEmpty()) {
            return Arrays.asList(participantsString.split("\\s*,\\s*"));
        }
        return new ArrayList<>();
    }

    private List<String> extractMultiSelect(JsonNode properties, String fieldName) {
        List<String> result = new ArrayList<>();
        if (properties.has(fieldName)) {
            JsonNode multiSelectNode = properties.get(fieldName).get("multi_select");
            if (multiSelectNode.isArray()) {
                multiSelectNode.forEach(node -> result.add(node.get("name").asText()));
            }
        }
        return result;
    }

    private List<Integer> extractIntegerMultiSelect(JsonNode properties, String fieldName) {
        List<Integer> result = new ArrayList<>();
        if (properties.has(fieldName)) {
            JsonNode multiSelectNode = properties.get(fieldName).get("multi_select");
            if (multiSelectNode.isArray()) {
                multiSelectNode.forEach(node -> result.add(Integer.parseInt(node.get("name").asText())));
            }
        }
        return result;
    }
}
