package com.github.filslo.simplesurveytool.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.filslo.simplesurveytool.SimpleSurveyToolApplication;
import com.github.filslo.simplesurveytool.dto.*;
import com.github.filslo.simplesurveytool.service.SurveyService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.apache.http.HttpStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {SimpleSurveyToolApplication.class}
)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class SurveyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SurveyService surveyService;

    @Autowired
    @InjectMocks
    private SurveyController surveyController;

    @BeforeEach
    void reInit() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void test_get_all_surveys_returns_surveys_from_service_with_status_OK() {

        //GIVEN
        List<SurveyDTO> surveyDT0s = Arrays.asList(
            new SurveyDTO(1L, "Survey1", new ArrayList<>()),
            new SurveyDTO(2L, "Survey2", new ArrayList<>())
        );

        when(this.surveyService.getAllSurveys()).thenReturn(surveyDT0s);

        // WHEN
        SurveyDTO[] surveyDTOS = given().log().ifValidationFails()
            .accept(JSON)
            .when()
            .get("/api/surveys")
            // THEN
            .then().log().ifError()
            .statusCode(SC_OK)
            .assertThat()
            .body("size()", is(surveyDT0s.size()))
            .extract()
            .as(SurveyDTO[].class);

        assertThat(surveyDTOS).containsExactlyElementsOf(surveyDT0s);

        verify(this.surveyService).getAllSurveys();

    }


    @Test
    void test_get_surveybyid_returns_existing_survey_from_service_with_status_OK() {

        //GIVEN
        long surveyId = 10L;
        String surveyName = "Survey1";
        SurveyDTO surveyDT0 = new SurveyDTO(surveyId, surveyName, new ArrayList<>());

        when(this.surveyService.getSurvey(surveyId)).thenReturn(surveyDT0);

        // WHEN
        given().log().ifValidationFails()
            .accept(JSON)
            .when()
            .get("/api/surveys/{id}", surveyId)
            // THEN
            .then().log().ifError()
            .statusCode(SC_OK)
            .assertThat()
            .body("id", equalTo(10))
            .body("name", equalTo(surveyName))        ;

        verify(this.surveyService).getSurvey(surveyId);
    }

    @Test
    void test_get_surveybyid_when_survey_does_not_exist_with_status_NO_FOUND() {

        //GIVEN
        long surveyId = 10000L;

        when(this.surveyService.getSurvey(surveyId)).thenThrow(NoResultException.class);

        // WHEN
        given().log().ifValidationFails()
            .accept(JSON)

            .when()
            .get("/api/surveys/{id}", surveyId)
            // THEN
            .then().log().ifError()
            .statusCode(SC_NOT_FOUND);

        verify(this.surveyService).getSurvey(surveyId);
    }

    @Test
    void should_return_NO_CONTENT_when_submitting_answer() {

        //GIVEN
        long surveyId = 10001L;


        // WHEN
        List<AnswerDTO> answers = List.of(new AnswerDTO());
        given().log().ifValidationFails()
            .accept(JSON)
            .body(answers)
            .contentType(MediaType.APPLICATION_JSON)

            .when()
            .post("/api/surveys/{id}/answers", surveyId)
            // THEN
            .then().log().ifError()
            .statusCode(SC_NO_CONTENT);

        verify(this.surveyService).storeAnswers(surveyId, answers);
    }

    @Test
    void test_get_survey_result_byid_when_survey_does_not_exist_with_status_NO_FOUND() {

        //GIVEN
        long surveyId = 10002L;

        when(this.surveyService.getSurveyAnswers(surveyId)).thenThrow(NoResultException.class);

        // WHEN
        given().log().ifValidationFails()
            .accept(JSON)

            .when()
            .get("/api/surveys/{id}/results", surveyId)
            // THEN
            .then().log().ifError()
            .statusCode(SC_NOT_FOUND);

        verify(this.surveyService).getSurveyAnswers(surveyId);
    }


    @Test
    void test_get_surveyresult_byid_returns_existing_survey_result_from_service_with_status_OK() throws JsonProcessingException {

        //GIVEN
        long surveyId = 100L;
        String surveyName = "Survey1";
        List<QuestionResultsDTO> questionResultDTOs = List.of(
            new QuestionResultsDTO(
                5L,
                "Question1",
                List.of(new AnswerResultsDTO(5, 1)))
        );

        SurveyResultsDTO surveyResultsDTO = new SurveyResultsDTO(
            surveyId,
            surveyName,
            questionResultDTOs
        );

        when(this.surveyService.getSurveyAnswers(surveyId)).thenReturn(surveyResultsDTO);

        // WHEN
        SurveyResultsDTO actualSurveyResultsDTO = given().log().ifValidationFails()
            .accept(JSON)
            .when()
            .get("/api/surveys/{id}/results", surveyId)
            // THEN
            .then().log().ifError()
            .statusCode(SC_OK)
            .body("surveyId", equalTo(100))
            .body("surveyName", equalTo(surveyName))
            .extract()
            .as(SurveyResultsDTO.class);
        ;

        verify(this.surveyService).getSurveyAnswers(surveyId);

        assertThat(actualSurveyResultsDTO).isEqualTo(surveyResultsDTO);
    }

}
