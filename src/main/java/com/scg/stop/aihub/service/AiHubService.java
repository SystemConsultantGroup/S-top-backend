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
        Map<String, Object> filter = new HashMap<>();
        List<Map<String, Object>> andConditions = new ArrayList<>();

        // Add filter conditions for title
        if (aiHubModelRequest.getTitle() != null && !aiHubModelRequest.getTitle().isEmpty()) {
            Map<String, Object> titleCondition = new HashMap<>();
            titleCondition.put("property", "제목");
            titleCondition.put("title", Map.of("contains", aiHubModelRequest.getTitle()));
            andConditions.add(titleCondition);
        }

        // Add filter conditions for professor
        if (aiHubModelRequest.getProfessor() != null && !aiHubModelRequest.getProfessor().isEmpty()) {
            Map<String, Object> professorCondition = new HashMap<>();
            professorCondition.put("property", "담당 교수");
            professorCondition.put("rich_text", Map.of("contains", aiHubModelRequest.getProfessor()));
            andConditions.add(professorCondition);
        }

        // Add filter conditions for participants
        if (aiHubModelRequest.getParticipants() != null && !aiHubModelRequest.getParticipants().isEmpty()) {
            for (String participant : aiHubModelRequest.getParticipants()) {
                Map<String, Object> participantsCondition = new HashMap<>();
                participantsCondition.put("property", "참여 학생");
                participantsCondition.put("rich_text", Map.of("contains", participant));
                andConditions.add(participantsCondition);
            }
        }

        // Add filter conditions for learning models, topics, and development years
        addMultiSelectFilterConditions(aiHubModelRequest.getLearningModels(), "학습 모델", andConditions);
        addMultiSelectFilterConditions(aiHubModelRequest.getTopics(), "주제 분류", andConditions);
        addMultiSelectFilterConditions(aiHubModelRequest.getDevelopmentYears(), "개발 년도", andConditions);

        if (!andConditions.isEmpty()) {
            filter.put("and", andConditions);
            requestBody.put("filter", filter);
        }

        return requestBody;
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
