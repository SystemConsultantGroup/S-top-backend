package com.scg.stop.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scg.stop.configuration.AbstractControllerTest;
import com.scg.stop.project.domain.AwardStatus;
import com.scg.stop.project.domain.ProjectCategory;
import com.scg.stop.project.domain.ProjectType;
import com.scg.stop.project.dto.response.FileResponse;
import com.scg.stop.project.dto.response.ProjectResponse;
import com.scg.stop.user.domain.FavoriteType;
import com.scg.stop.user.domain.User;
import com.scg.stop.user.domain.UserType;
import com.scg.stop.user.dto.request.UserUpdateRequest;
import com.scg.stop.user.dto.response.FavoriteResponse;
import com.scg.stop.user.dto.response.UserInquiryResponse;
import com.scg.stop.user.dto.response.UserProposalResponse;
import com.scg.stop.user.dto.response.UserResponse;
import com.scg.stop.user.service.UserService;
import com.scg.stop.video.domain.JobInterviewCategory;
import com.scg.stop.video.domain.QuizInfo;
import com.scg.stop.video.dto.response.JobInterviewUserResponse;
import com.scg.stop.video.dto.response.QuizResponse;
import com.scg.stop.video.dto.response.TalkUserResponse;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class UserControllerTest extends AbstractControllerTest {

    private static final String ACCESS_TOKEN = "user_access_token";
    private static final String REFRESH_TOKEN = "refresh_token";

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("사용자 정보를 조회할 수 있다.")
    void getMe() throws Exception {
        // given
        UserResponse response = new UserResponse(
                1L,
                "이름",
                "010-1234-5678",
                "student@g.skku.edu",
                UserType.STUDENT,
                null,
                null,
                "2000123456",
                "학과",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        when(userService.getMe(any(User.class))).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
                get("/users/me")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
        );

        // then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestCookies(
                                cookieWithName("refresh-token")
                                        .description("갱신 토큰")
                        ),
                        requestHeaders(
                                headerWithName("Authorization")
                                        .description("access token")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("사용자 ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름"),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("사용자 전화번호"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일"),
                                fieldWithPath("userType").type(JsonFieldType.STRING).description("사용자 유형"),
                                fieldWithPath("division").type(JsonFieldType.STRING).description("소속").optional(),
                                fieldWithPath("position").type(JsonFieldType.STRING).description("직책").optional(),
                                fieldWithPath("studentNumber").type(JsonFieldType.STRING).description("학번").optional(),
                                fieldWithPath("departmentName").type(JsonFieldType.STRING).description("학과 이름").optional(),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성일"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("수정일")
                        )
                ));
    }

    @Test
    @DisplayName("사용자 정보를 수정할 수 있다.")
    void updateMe() throws Exception {
        // given
        UserUpdateRequest request = new UserUpdateRequest(
                "이름",
                "010-1234-5678",
                "student@g.skku.edu",
                null,
                null,
                "2000123456",
                "학과"
        );
        UserResponse response = new UserResponse(
                1L,
                "이름",
                "010-1234-5678",
                "student@g.skku.edu",
                UserType.STUDENT,
                null,
                null,
                "2000123456",
                "학과",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(userService.updateMe(any(User.class), any(UserUpdateRequest.class))).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
                put("/users/me")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestCookies(
                                cookieWithName("refresh-token")
                                        .description("갱신 토큰")
                        ),
                        requestHeaders(
                                headerWithName("Authorization")
                                        .description("access token")
                        ),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("전화번호"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("division").type(JsonFieldType.STRING).description("소속").optional(),
                                fieldWithPath("position").type(JsonFieldType.STRING).description("직책").optional(),
                                fieldWithPath("studentNumber").type(JsonFieldType.STRING).description("학번").optional(),
                                fieldWithPath("department").type(JsonFieldType.STRING).description("학과").optional()
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("사용자 ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름"),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("사용자 전화번호"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일"),
                                fieldWithPath("userType").type(JsonFieldType.STRING).description("사용자 유형"),
                                fieldWithPath("division").type(JsonFieldType.STRING).description("소속").optional(),
                                fieldWithPath("position").type(JsonFieldType.STRING).description("직책").optional(),
                                fieldWithPath("studentNumber").type(JsonFieldType.STRING).description("학번").optional(),
                                fieldWithPath("departmentName").type(JsonFieldType.STRING).description("학과 이름").optional(),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성일"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("수정일")
                        )
                ));
    }

    @Test
    @DisplayName("사용자 계정을 삭제할 수 있다.")
    void deleteMe() throws Exception {
        // given
        doNothing().when(userService).deleteMe(any(User.class));

        // when
        ResultActions result = mockMvc.perform(
                delete("/users/me")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
        );

        // then
        result.andExpect(status().isNoContent())
                .andDo(restDocs.document(
                        requestCookies(
                                cookieWithName("refresh-token")
                                        .description("갱신 토큰")
                        ),
                        requestHeaders(
                                headerWithName("Authorization")
                                        .description("access token")
                        )
                ));
    }

    @Test
    @DisplayName("로그인 유저의 프로젝트 문의 리스트를 조회할 수 있다.")
    void getUserInquiries() throws Exception {
        // given
        List<UserInquiryResponse> inquiryResponses = Arrays.asList(
                new UserInquiryResponse(1L, "Title 1", 1L, LocalDateTime.now(), true),
                new UserInquiryResponse(2L, "Title 2", 2L, LocalDateTime.now(), false)
        );
        when(userService.getUserInquiries(any(User.class))).thenReturn(inquiryResponses);

        // when
        ResultActions result = mockMvc.perform(
                get("/users/inquiries")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
        );

        // then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestCookies(
                                cookieWithName("refresh-token").description("갱신 토큰")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("Access Token")
                        ),
                        responseFields(
                                fieldWithPath("[].id").description("문의 ID"),
                                fieldWithPath("[].title").description("문의 제목"),
                                fieldWithPath("[].projectId").description("프로젝트 ID"),
                                fieldWithPath("[].createdDate").description("문의 생성일"),
                                fieldWithPath("[].hasReply").description("답변 여부")
                        )
                ));

    }

    @Test
    @DisplayName("로그인 유저의 과제 제안 리스트를 조회할 수 있다.")
    void getUserProposals() throws Exception {
        // given
        List<UserProposalResponse> proposalResponses = Arrays.asList(
                new UserProposalResponse(1L, "Title 1", LocalDateTime.now(), true),
                new UserProposalResponse(2L, "Title 2", LocalDateTime.now(), false)
        );
        when(userService.getUserProposals(any(User.class))).thenReturn(proposalResponses);

        // when
        ResultActions result = mockMvc.perform(
                get("/users/proposals")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
        );

        // then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestCookies(
                                cookieWithName("refresh-token").description("갱신 토큰")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("Access Token")
                        ),
                        responseFields(
                                fieldWithPath("[].id").description("과제 제안 ID"),
                                fieldWithPath("[].title").description("프로젝트명"),
                                fieldWithPath("[].createdDate").description("과제 제안 생성일"),
                                fieldWithPath("[].hasReply").description("답변 여부")
                        )
                ));

    }

    @Test
    @DisplayName("유저의 관심 프로젝트를 조회할 수 있다.")
    void getUserFavoriteProjects() throws Exception {
        // given
        List<ProjectResponse> responses = Arrays.asList(
                new ProjectResponse(
                        1L,
                        new FileResponse(
                                1L,
                                "썸네일 uuid 1",
                                "썸네일 파일 이름 1",
                                "썸네일 mime 타입 1"
                        ),
                        "프로젝트 이름 1",
                        "팀 이름 1",
                        List.of("학생 이름 1", "학생 이름 2"),
                        List.of("교수 이름 1"),
                        ProjectType.STARTUP,
                        ProjectCategory.BIG_DATA_ANALYSIS,
                        AwardStatus.FIRST,
                        2023,
                        100,
                        false,
                        false,
                        "프로젝트 URL",
                        "프로젝트 설명"
                ),
                new ProjectResponse(
                        2L,
                        new FileResponse(
                                2L,
                                "썸네일 uuid 2",
                                "썸네일 파일 이름 2",
                                "썸네일 mime 타입 2"
                        ),
                        "프로젝트 이름 2",
                        "팀 이름 2",
                        List.of("학생 이름 3", "학생 이름 4"),
                        List.of("교수 이름 2"),
                        ProjectType.LAB,
                        ProjectCategory.AI_MACHINE_LEARNING,
                        AwardStatus.SECOND,
                        2023,
                        100,
                        false,
                        true,
                        "프로젝트 URL",
                        "프로젝트 설명"
                )
        );
        when(userService.getUserFavoriteProjects(any(User.class))).thenReturn(responses);

        // when
        ResultActions result = mockMvc.perform(
                get("/users/favorites/projects")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
        );

        // then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestCookies(
                                cookieWithName("refresh-token").description("갱신 토큰")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("Access Token")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("프로젝트 ID"),
                                fieldWithPath("[].thumbnailInfo").type(JsonFieldType.OBJECT).description("썸네일 정보"),
                                fieldWithPath("[].thumbnailInfo.id").type(JsonFieldType.NUMBER).description("썸네일 ID"),
                                fieldWithPath("[].thumbnailInfo.uuid").type(JsonFieldType.STRING).description("썸네일 UUID"),
                                fieldWithPath("[].thumbnailInfo.name").type(JsonFieldType.STRING).description("썸네일 파일 이름"),
                                fieldWithPath("[].thumbnailInfo.mimeType").type(JsonFieldType.STRING).description("썸네일 MIME 타입"),
                                fieldWithPath("[].projectName").type(JsonFieldType.STRING).description("프로젝트 이름"),
                                fieldWithPath("[].teamName").type(JsonFieldType.STRING).description("팀 이름"),
                                fieldWithPath("[].studentNames[]").type(JsonFieldType.ARRAY).description("학생 이름"),
                                fieldWithPath("[].professorNames[]").type(JsonFieldType.ARRAY).description("교수 이름"),
                                fieldWithPath("[].projectType").type(JsonFieldType.STRING).description("프로젝트 타입: RESEARCH_AND_BUSINESS_FOUNDATION, LAB, STARTUP, CLUB"),
                                fieldWithPath("[].projectCategory").type(JsonFieldType.STRING).description("프로젝트 카테고리: COMPUTER_VISION, SYSTEM_NETWORK, WEB_APPLICATION, SECURITY_SOFTWARE_ENGINEERING, NATURAL_LANGUAGE_PROCESSING, BIG_DATA_ANALYSIS, AI_MACHINE_LEARNING, INTERACTION_AUGMENTED_REALITY"),
                                fieldWithPath("[].awardStatus").type(JsonFieldType.STRING).description("수상 여부: NONE, FIRST, SECOND, THIRD, FOURTH, FIFTH"),
                                fieldWithPath("[].year").type(JsonFieldType.NUMBER).description("프로젝트 년도"),
                                fieldWithPath("[].likeCount").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                fieldWithPath("[].like").type(JsonFieldType.BOOLEAN).description("좋아요 여부"),
                                fieldWithPath("[].bookMark").type(JsonFieldType.BOOLEAN).description("북마크 여부"),
                                fieldWithPath("[].url").type(JsonFieldType.STRING).description("프로젝트 URL"),
                                fieldWithPath("[].description").type(JsonFieldType.STRING).description("프로젝트 설명")
                        )
                ));
    }

    @Test
    @DisplayName("유저의 관심 대담영상을 조회할 수 있다.")
    void getUserFavoriteTalks() throws Exception {
        // given
        QuizResponse quizResponse = new QuizResponse(
                List.of(
                        new QuizInfo("질문1", 0, List.of("선지1","선지2")),
                        new QuizInfo("질문2", 0, List.of("선지1","선지2"))
                )
        );
        List<TalkUserResponse> responses = Arrays.asList(
                new TalkUserResponse(1L, "제목1", "유튜브 고유ID", 2024,  "대담자 소속1","대담자 성명1" ,true, quizResponse,LocalDateTime.now(), LocalDateTime.now()),
                new TalkUserResponse(2L, "제목2", "유튜브 고유ID", 2024,  "대담자 소속2","대담자 성명2" ,true, quizResponse,LocalDateTime.now(), LocalDateTime.now())
        );
        when(userService.getUserFavoriteTalks(any(User.class))).thenReturn(responses);

        // when
        ResultActions result = mockMvc.perform(
                        get("/users/favorites/talks")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
        );

        // then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestCookies(
                                cookieWithName("refresh-token").description("갱신 토큰")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("Access Token")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("대담 영상 ID"),
                                fieldWithPath("[].title").type(JsonFieldType.STRING).description("대담 영상 제목"),
                                fieldWithPath("[].youtubeId").type(JsonFieldType.STRING).description("유튜브 영상의 고유 ID"),
                                fieldWithPath("[].year").type(JsonFieldType.NUMBER).description("대담 영상 연도"),
                                fieldWithPath("[].talkerBelonging").type(JsonFieldType.STRING).description("대담자의 소속된 직장/단체"),
                                fieldWithPath("[].talkerName").type(JsonFieldType.STRING).description("대담자의 성명"),
                                fieldWithPath("[].favorite").type(JsonFieldType.BOOLEAN).description("관심한 대담영상의 여부"),
                                fieldWithPath("[].quiz").type(JsonFieldType.ARRAY).description("퀴즈 데이터, 없는경우 null").optional(),
                                fieldWithPath("[].quiz[].question").type(JsonFieldType.STRING).description("퀴즈 1개의 질문").optional(),
                                fieldWithPath("[].quiz[].answer").type(JsonFieldType.NUMBER).description("퀴즈 1개의 정답선지 인덱스").optional(),
                                fieldWithPath("[].quiz[].options").type(JsonFieldType.ARRAY).description("퀴즈 1개의 정답선지 리스트").optional(),
                                fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("대담 영상 생성일"),
                                fieldWithPath("[].updatedAt").type(JsonFieldType.STRING).description("대담 영상 수정일")
                        )
                ));
    }

    @Test
    @DisplayName("유저의 관심 잡페어 영상을 조회할 수 있다.")
    void getUserFavoriteInterviews() throws Exception {
        // given
        List<JobInterviewUserResponse> responses = Arrays.asList(
                new JobInterviewUserResponse(1L,"잡페어 인터뷰의 제목1", "유튜브 고유 ID1", 2023,"대담자의 소속1", "대담자의 성명1", false,  JobInterviewCategory.INTERN, LocalDateTime.now(), LocalDateTime.now()),
                new JobInterviewUserResponse(2L, "잡페어 인터뷰의 제목2", "유튜브 고유 ID2", 2024,"대담자의 소속2", "대담자의 성명2", true,  JobInterviewCategory.INTERN, LocalDateTime.now(), LocalDateTime.now())
        );
        when(userService.getUserFavoriteInterviews(any(User.class))).thenReturn(responses);

        // when
        ResultActions result = mockMvc.perform(
                get("/users/favorites/jobInterviews")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
        );

        // then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestCookies(
                                cookieWithName("refresh-token").description("갱신 토큰")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("Access Token")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("잡페어 인터뷰 ID"),
                                fieldWithPath("[].title").type(JsonFieldType.STRING).description("잡페어 인터뷰 제목"),
                                fieldWithPath("[].youtubeId").type(JsonFieldType.STRING).description("유튜브 영상의 고유 ID"),
                                fieldWithPath("[].year").type(JsonFieldType.NUMBER).description("잡페어 인터뷰 연도"),
                                fieldWithPath("[].talkerBelonging").type(JsonFieldType.STRING).description("잡페어 인터뷰 대담자의 소속"),
                                fieldWithPath("[].talkerName").type(JsonFieldType.STRING).description("잡페어 인터뷰 대담자의 성명"),
                                fieldWithPath("[].favorite").type(JsonFieldType.BOOLEAN).description("관심에 추가한 잡페어 인터뷰 여부"),
                                fieldWithPath("[].category").type(JsonFieldType.STRING).description("잡페어 인터뷰 카테고리: SENIOR, INTERN"),
                                fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("잡페어 인터뷰 생성일"),
                                fieldWithPath("[].updatedAt").type(JsonFieldType.STRING).description("잡페어 인터뷰 수정일")
                        )
                ));
    }

}
