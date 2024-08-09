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
public class AiHubModelResponse {
    private String object;
    private String id;
    private String cover;
    private String title; // 제목
    private String professor; // 담당 교수
    private List<String> participants = new ArrayList<>(); // 참여 학생 (comma-separated string -> List)
    private List<String> learningModels = new ArrayList<>(); // 학습 모델
    private List<String> topics = new ArrayList<>(); // 주제 분류
    private List<String> developmentYears = new ArrayList<>(); // 개발 년도
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
        this.title = extractTitle(properties);
        this.professor = extractProfessor(properties);
        this.participants = extractParticipants(properties); // Extract and split participants
        this.learningModels = extractMultiSelect(properties, "학습 모델");
        this.topics = extractMultiSelect(properties, "주제 분류");
        this.developmentYears = extractMultiSelect(properties, "개발 년도");
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    private String extractTitle(JsonNode properties) {
        if (properties.has("제목") && properties.get("제목").has("title") && properties.get("제목").get("title").isArray() && properties.get("제목").get("title").size() > 0) {
            return properties.get("제목").get("title").get(0).get("text").get("content").asText();
        }
        return null;
    }

    private String extractProfessor(JsonNode properties) {
        if (properties.has("담당 교수") && properties.get("담당 교수").has("rich_text") && properties.get("담당 교수").get("rich_text").isArray() && properties.get("담당 교수").get("rich_text").size() > 0) {
            return properties.get("담당 교수").get("rich_text").get(0).get("text").get("content").asText();
        }
        return null;
    }

    private List<String> extractParticipants(JsonNode properties) {
        if (properties.has("참여 학생") && properties.get("참여 학생").has("rich_text") && properties.get("참여 학생").get("rich_text").isArray() && properties.get("참여 학생").get("rich_text").size() > 0) {
            String participantsString = properties.get("참여 학생").get("rich_text").get(0).get("text").get("content").asText();
            return Arrays.asList(participantsString.split("\\s*,\\s*")); // Split by ',' and trim whitespace
        }
        return new ArrayList<>();
    }

    private List<String> extractMultiSelect(JsonNode properties, String fieldName) {
        List<String> result = new ArrayList<>();
        if (properties.has(fieldName) && properties.get(fieldName).has("multi_select") && properties.get(fieldName).get("multi_select").isArray()) {
            for (JsonNode tagNode : properties.get(fieldName).get("multi_select")) {
                result.add(tagNode.get("name").asText());
            }
        }
        return result;
    }
}
