package com.scg.stop.aihub.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scg.stop.aihub.dto.AiHubModelRequest;
import com.scg.stop.aihub.dto.AiHubModelResponse;
import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
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

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Page<AiHubModelResponse> getAiHubModels(AiHubModelRequest aiHubModelRequest, Pageable pageable) {
        try {

            // url
            String url = "https://api.notion.com/v1/databases/" + modelDatabaseId + "/query";

            // headers
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(secretKey);
            headers.setContentType(APPLICATION_JSON);
            headers.set("Notion-Version", version);

            // request for filtering
            Map<String, Object> requestBody = createRequestBody(aiHubModelRequest);
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            // response
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            JsonNode root = objectMapper.readTree(response.getBody()).get("results");
            List<AiHubModelResponse> models = objectMapper.convertValue(root, new TypeReference<>() {
            });

            // pagination
            int start = Math.min((int) pageable.getOffset(), models.size());
            int end = Math.min((start + pageable.getPageSize()), models.size());
            return new PageImpl<>(models.subList(start, end), pageable, models.size());

        } catch (Exception e) {
            throw new BadRequestException(ExceptionCode.FAILED_TO_FETCH_NOTION_DATA);
        }
    }

    private Map<String, Object> createRequestBody(AiHubModelRequest aiHubModelRequest) {

        Map<String, Object> requestBody = new HashMap<>();
        List<Map<String, Object>> conditions = new ArrayList<>();

        // add filter conditions
        addFilterConditions(aiHubModelRequest.getTask(), "Task", conditions);
        addFilterConditions(aiHubModelRequest.getDataType(), "DataType", conditions);
        addFilterConditions(aiHubModelRequest.getFramework(), "Framework", conditions);

        // AND operation for multiple conditions
        if (!conditions.isEmpty()) {
            Map<String, Object> filter = new HashMap<>();
            filter.put("and", conditions);
            requestBody.put("filter", filter);
        }

        return requestBody;
    }

    private void addFilterConditions(List<String> criteria, String property, List<Map<String, Object>> conditions) {
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
