package com.scg.stop.aihub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AiHubModelResponse {
    private String object;
    private String id;
    private String cover;
    private String name;
    private List<String> task = new ArrayList<>();
    private List<String> dataType = new ArrayList<>();
    private List<String> framework = new ArrayList<>();
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
        if (cover != null && cover.has("external")) {
            this.cover = cover.get("external").get("url").asText();
        }
    }

    @JsonProperty("properties")
    public void setProperties(JsonNode properties) {
        this.name = extractName(properties);
        this.task = extractMultiSelect(properties, "Task");
        this.dataType = extractMultiSelect(properties, "DataType");
        this.framework = extractMultiSelect(properties, "Framework");
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    private String extractName(JsonNode properties) {
        if (properties.has("Name") && properties.get("Name").has("title") && properties.get("Name").get("title").isArray() && properties.get("Name").get("title").size() > 0) {
            return properties.get("Name").get("title").get(0).get("text").get("content").asText();
        }
        return null;
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
