package com.scg.stop.aihub.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scg.stop.aihub.dto.AiHubDatasetRequest;
import com.scg.stop.aihub.dto.AiHubDatasetResponse;
import com.scg.stop.aihub.dto.AiHubModelRequest;
import com.scg.stop.aihub.dto.AiHubModelResponse;
import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AiHubService {

    // Notion integration details
    @Value("${spring.notion.secretKey}")
    private String secretKey;

    @Value("${spring.notion.version}")
    private String version;

    @Value("${spring.notion.databaseId.model}")
    private String modelDatabaseId;

    @Value("${spring.notion.databaseId.dataset}")
    private String datasetDatabaseId;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // Constants for Notion property names
    private static final String TITLE_PROPERTY = "제목";
    private static final String PROFESSOR_PROPERTY = "담당 교수";
    private static final String PARTICIPANTS_PROPERTY = "참여 학생";
    private static final String LEARNING_MODELS_PROPERTY = "학습 모델";
    private static final String TOPICS_PROPERTY = "주제 분류";
    private static final String DEVELOPMENT_YEARS_PROPERTY = "개발 년도";
    private static final String DATA_TYPES_PROPERTY = "데이터 유형";
    private static final String CONSTRUCTION_YEARS_PROPERTY = "구축 년도"; // equivalent to "개발 년도" for datasets

    public Page<AiHubModelResponse> getAiHubModels(AiHubModelRequest aiHubModelRequest, Pageable pageable) {
        return fetchNotionData(modelDatabaseId, aiHubModelRequest, pageable, AiHubModelResponse.class);
    }

    public Page<AiHubDatasetResponse> getAiHubDatasets(AiHubDatasetRequest aiHubDatasetRequest, Pageable pageable) {
        return fetchNotionData(datasetDatabaseId, aiHubDatasetRequest, pageable, AiHubDatasetResponse.class);
    }

    private <T> Page<T> fetchNotionData(String databaseId, Object requestObject, Pageable pageable, Class<T> responseType) {
        try {
            String url = "https://api.notion.com/v1/databases/" + databaseId + "/query";

            HttpHeaders headers = createHeaders();
            Map<String, Object> requestBody = createRequestBody(requestObject);
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            JsonNode root = objectMapper.readTree(response.getBody()).get("results");
            List<T> entities = objectMapper.convertValue(root, objectMapper.getTypeFactory().constructCollectionType(List.class, responseType));

            int start = Math.min((int) pageable.getOffset(), entities.size());
            int end = Math.min(start + pageable.getPageSize(), entities.size());
            return new PageImpl<>(entities.subList(start, end), pageable, entities.size());

        } catch (Exception e) {
            throw new BadRequestException(ExceptionCode.FAILED_TO_FETCH_NOTION_DATA);
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Notion-Version", version);
        return headers;
    }

    private Map<String, Object> createRequestBody(Object requestObject) {
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> filter = new HashMap<>();
        List<Map<String, Object>> andConditions = new ArrayList<>();

        if (requestObject instanceof AiHubModelRequest) {
            AiHubModelRequest aiHubModelRequest = (AiHubModelRequest) requestObject;

            // Convert Integer year to String
            List<String> developmentYears = new ArrayList<>();
            if (aiHubModelRequest.getDevelopmentYears() != null) {
                aiHubModelRequest.getDevelopmentYears().forEach(year -> developmentYears.add(year.toString()));
            }

            addStringFilterCondition(aiHubModelRequest.getTitle(), TITLE_PROPERTY, "title", andConditions);
            addStringFilterCondition(aiHubModelRequest.getProfessor(), PROFESSOR_PROPERTY, "rich_text", andConditions);
            addMultipleStringFilterConditions(aiHubModelRequest.getParticipants(), PARTICIPANTS_PROPERTY, "rich_text", andConditions);
            addMultiSelectFilterConditions(aiHubModelRequest.getLearningModels(), LEARNING_MODELS_PROPERTY, andConditions);
            addMultiSelectFilterConditions(aiHubModelRequest.getTopics(), TOPICS_PROPERTY, andConditions);
            addMultiSelectFilterConditions(developmentYears, DEVELOPMENT_YEARS_PROPERTY, andConditions);

        } else if (requestObject instanceof AiHubDatasetRequest) {
            AiHubDatasetRequest aiHubDatasetRequest = (AiHubDatasetRequest) requestObject;

            // Convert Integer year to String
            List<String> constructionYears = new ArrayList<>();
            if (aiHubDatasetRequest.getDevelopmentYears() != null) {
                aiHubDatasetRequest.getDevelopmentYears().forEach(year -> constructionYears.add(year.toString()));
            }

            addStringFilterCondition(aiHubDatasetRequest.getTitle(), TITLE_PROPERTY, "title", andConditions);
            addMultiSelectFilterConditions(aiHubDatasetRequest.getDataTypes(), DATA_TYPES_PROPERTY, andConditions);
            addMultiSelectFilterConditions(aiHubDatasetRequest.getTopics(), TOPICS_PROPERTY, andConditions);
            addMultiSelectFilterConditions(constructionYears, CONSTRUCTION_YEARS_PROPERTY, andConditions);
            addStringFilterCondition(aiHubDatasetRequest.getProfessor(), PROFESSOR_PROPERTY, "rich_text", andConditions);
            addMultipleStringFilterConditions(aiHubDatasetRequest.getParticipants(), PARTICIPANTS_PROPERTY, "rich_text", andConditions);
        }

        if (!andConditions.isEmpty()) {
            filter.put("and", andConditions);
            requestBody.put("filter", filter);
        }

        return requestBody;
    }

    private void addStringFilterCondition(String value, String property, String filterType, List<Map<String, Object>> conditions) {
        if (value != null && !value.isEmpty()) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("property", property);
            condition.put(filterType, Map.of("contains", value));
            conditions.add(condition);
        }
    }

    private void addMultipleStringFilterConditions(List<String> values, String property, String filterType, List<Map<String, Object>> conditions) {
        if (values != null && !values.isEmpty()) {
            values.forEach(value -> addStringFilterCondition(value, property, filterType, conditions));
        }
    }

    private void addMultiSelectFilterConditions(List<String> criteria, String property, List<Map<String, Object>> conditions) {
        if (criteria != null && !criteria.isEmpty()) {
            criteria.forEach(criterion -> {
                Map<String, Object> filterCondition = new HashMap<>();
                filterCondition.put("property", property);
                filterCondition.put("multi_select", Map.of("contains", criterion));
                conditions.add(filterCondition);
            });
        }
    }
}
