package com.scg.stop.jobInfo.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobInfoResponse {
    private String object;
    private String id; // 페이지 id
    private String company; // 기업명
    private List<String> jobTypes; // 고용 형태
    private String region; // 근무 지역
    private String position; // 채용 포지션
    private String logo; // logo
    private String salary; // 연봉
    private String website; // 웹사이트
    private List<String> state; // 채용 상태
    private String hiringTime; // 채용 시점
    private String url; // redirect url

    @JsonProperty("object")
    public void setObject(String object) {
        this.object = object;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("properties")
    public void setProperties(JsonNode properties) {
        if (properties != null) {
            this.company = extractTextFromProperty(properties, "기업명", "title");
            this.jobTypes = extractMultiSelect(properties, "고용 형태");
            this.region = extractTextFromProperty(properties, "근무 지역", "rich_text");
            this.position = extractTextFromProperty(properties, "채용 포지션", "rich_text");
            this.logo = extractLogoUrl(properties, "logo");
            this.salary = extractTextFromProperty(properties, "연봉", "rich_text");
            this.website = extractTextFromProperty(properties, "웹사이트", "url");
            this.state = extractMultiSelect(properties, "채용 상태");
            this.hiringTime = extractTextFromProperty(properties, "채용 시점", "rich_text");
        }
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    // Helper methods
    // Extract text from the text property
    private String extractTextFromProperty(JsonNode properties, String fieldName, String textType) {
        if (properties.has(fieldName)) {
            JsonNode fieldNode = properties.get(fieldName).get(textType);
            if (fieldNode.isArray() && fieldNode.size() > 0) {
                return fieldNode.get(0).get("text").get("content").asText();
            }
        }
        return null;
    }

    // Extract participants from the comma-separated text property
    private List<String> extractParticipants(JsonNode properties) {
        String participantsString = extractTextFromProperty(properties, "참여 학생", "rich_text");
        if (participantsString != null && !participantsString.isEmpty()) {
            return Arrays.asList(participantsString.split("\\s*,\\s*"));
        }
        return new ArrayList<>();
    }

    // Extract multi-select property
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

    // Extract logo url from the files property
    private String extractLogoUrl(JsonNode properties, String fieldName) {
        if (properties.has(fieldName)) {
            JsonNode filesNode = properties.get(fieldName).get("files");
            if (filesNode.isArray() && filesNode.size() > 0) {
                JsonNode firstFileNode = filesNode.get(0).get("file");
                if (firstFileNode != null && firstFileNode.has("url")) {
                    return firstFileNode.get("url").asText();
                }
            }
        }
        return null;
    }

}
