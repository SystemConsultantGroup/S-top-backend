package com.scg.stop.jobInfo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import com.scg.stop.jobInfo.dto.request.JobInfoRequest;
import com.scg.stop.jobInfo.dto.response.JobInfoResponse;
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
public class JobInfoService {

    // Notion API values
    @Value("${spring.notion.secretKey}")
    private String secretKey;

    @Value("${spring.notion.version}")
    private String version;

    @Value("${spring.notion.databaseId.jobInfo}")
    private String jobInfoDatabaseId;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // Constants for Notion DB property names
    private static final String COMPANY_NAME_PROPERTY = "기업명";
    private static final String JOB_TYPES_PROPERTY = "고용 형태";
    private static final String REGION_PROPERTY = "근무 지역";
    private static final String POSITION_PROPERTY = "채용 포지션";
    private static final String STATE_PROPERTY = "채용 상태";
    private static final String HIRING_TIME_PROPERTY = "채용 시점";

    /**
     * Fetches job info from Notion database
     *
     * @param jobInfoRequest Request object containing search criteria
     * @param pageable       Pageable object containing page number and size
     * @return Page of job info
     */
    public Page<JobInfoResponse> getJobInfos(JobInfoRequest jobInfoRequest, Pageable pageable) {
        return fetchNotionData(jobInfoDatabaseId, jobInfoRequest, pageable, JobInfoResponse.class);
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

    // Helper methods for creating request header
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Notion-Version", version);
        return headers;
    }

    // Helper method for creating request body
    private Map<String, Object> createRequestBody(Object requestObject) {
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> filter = new HashMap<>();
        List<Map<String, Object>> andConditions = new ArrayList<>();

        // Extract search criteria from request object
        JobInfoRequest jobInfoRequest = (JobInfoRequest) requestObject;
        addStringFilterCondition(jobInfoRequest.getCompany(), COMPANY_NAME_PROPERTY, "title", andConditions);
        addMultiSelectFilterConditions(jobInfoRequest.getJobTypes(), JOB_TYPES_PROPERTY, andConditions);
        addStringFilterCondition(jobInfoRequest.getRegion(), REGION_PROPERTY, "rich_text", andConditions);
        addStringFilterCondition(jobInfoRequest.getPosition(), POSITION_PROPERTY, "rich_text", andConditions);
        addMultiSelectFilterConditions(jobInfoRequest.getState(), STATE_PROPERTY, andConditions);
        addStringFilterCondition(jobInfoRequest.getHiringTime(), HIRING_TIME_PROPERTY, "rich_text", andConditions);


        // AND conditions to filter
        if (!andConditions.isEmpty()) {
            filter.put("and", andConditions);
            requestBody.put("filter", filter);
        }

        return requestBody;
    }

    // Helper method for adding String filter condition
    private void addStringFilterCondition(String value, String property, String filterType, List<Map<String, Object>> conditions) {
        if (value != null && !value.isEmpty()) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("property", property);
            condition.put(filterType, Map.of("contains", value));
            conditions.add(condition);
        }
    }

    // Helper method for adding MultiSelect filter conditions
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
