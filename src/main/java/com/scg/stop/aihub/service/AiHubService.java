package com.scg.stop.aihub.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scg.stop.aihub.dto.AiHubModelRequest;
import com.scg.stop.aihub.dto.AiHubModelResponse;
import com.scg.stop.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.scg.stop.global.exception.ExceptionCode.FAILED_TO_FETCH_NOTION_DATA;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@RequiredArgsConstructor
@Transactional
public class AiHubService {

    @Value("${spring.notion.secretKey}")
    private String secretKey;

    @Value("${spring.notion.version}")
    private String version;

    @Value("${spring.notion.databaseId.model}")
    private String modelDatabaseId;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // Constants for Notion property names
    private static final String TITLE_PROPERTY = "제목";
    private static final String PROFESSOR_PROPERTY = "담당 교수";
    private static final String PARTICIPANTS_PROPERTY = "참여 학생";
    private static final String LEARNING_MODELS_PROPERTY = "학습 모델";
    private static final String TOPICS_PROPERTY = "주제 분류";
    private static final String DEVELOPMENT_YEARS_PROPERTY = "개발 년도";

    public Page<AiHubModelResponse> getAiHubModels(AiHubModelRequest aiHubModelRequest, Pageable pageable) {
        try {
            String url = "https://api.notion.com/v1/databases/" + modelDatabaseId + "/query";

            HttpHeaders headers = createHeaders();
            Map<String, Object> requestBody = createRequestBody(aiHubModelRequest);
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            JsonNode root = objectMapper.readTree(response.getBody()).get("results");
            List<AiHubModelResponse> models = objectMapper.convertValue(root, new TypeReference<List<AiHubModelResponse>>() {
            });

            int start = Math.min((int) pageable.getOffset(), models.size());
            int end = Math.min(start + pageable.getPageSize(), models.size());
            return new PageImpl<>(models.subList(start, end), pageable, models.size());

        } catch (Exception e) {
            throw new BadRequestException(FAILED_TO_FETCH_NOTION_DATA);
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(secretKey);
        headers.setContentType(APPLICATION_JSON);
        headers.set("Notion-Version", version);
        return headers;
    }

    private Map<String, Object> createRequestBody(AiHubModelRequest aiHubModelRequest) {
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> filter = new HashMap<>();
        List<Map<String, Object>> andConditions = new ArrayList<>();

        // convert Integer year to String
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
