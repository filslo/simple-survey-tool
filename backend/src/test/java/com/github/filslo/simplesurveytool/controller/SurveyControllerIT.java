package com.github.filslo.simplesurveytool.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.filslo.simplesurveytool.SimpleSurveyToolApplication;
import com.github.filslo.simplesurveytool.data.repository.RatingRepository;
import com.github.filslo.simplesurveytool.dto.*;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.apache.http.HttpStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@ActiveProfiles("db")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {SimpleSurveyToolApplication.class}
)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@SqlGroup({
    @Sql(value = "classpath:db/data-h2.sql", executionPhase = BEFORE_TEST_METHOD),
})
class SurveyControllerIT {

    private static final long SURVEY1_ID = 10L;
    private static final String SURVEY1_NAME = "Survey1";

    private static final int SURVEY2_ID = 20;
    private static final String SURVEY2_NAME = "Survey2";

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void reInit() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void test_get_all_surveys_should_return_all_surveys_with_status_OK() {

        //GIVEN

        // WHEN
        given().log().ifValidationFails()
            .accept(JSON)
            .when()
            .get("/api/surveys")
            // THEN
            .then().log().ifError()
            .statusCode(SC_OK)
            .body("size()", is(2))
            .extract()
            .as(SurveyDTO[].class);
    }

    @Test
    void test_get_a_survey_should_return_a_survey_including_all_questions_with_status_OK() {

        //GIVEN
        int surveyId = SURVEY2_ID;
        String expectedSurveyName = SURVEY2_NAME;

        // WHEN
        SurveyDTO surveyDTO = given().log().ifValidationFails()
            .accept(JSON)
            .when()
            .get("/api/surveys/{id}", surveyId)
            // THEN
            .then().log().ifError()
            .statusCode(SC_OK)
            .body("id", equalTo(surveyId))
            .body("name", equalTo(expectedSurveyName))
            .extract()
            .as(SurveyDTO.class);

        assertThat(surveyDTO.getQuestions().size()).isEqualTo(4);
        assertThat(surveyDTO.getName()).isEqualTo(expectedSurveyName);
        assertThat(surveyDTO.getId()).isEqualTo(surveyId);
        assertThat(surveyDTO.getQuestions().get(0).getText()).isEqualTo("Question3");
        assertThat(surveyDTO.getQuestions().get(1).getText()).isEqualTo("Question4");
        assertThat(surveyDTO.getQuestions().get(2).getText()).isEqualTo("Question5");
        assertThat(surveyDTO.getQuestions().get(3).getText()).isEqualTo("Question6");


    }

    @Test
    void test_get_a_survey_should_return_404_status_when_survey_does_not_exist() {

        //GIVEN
        int surveyId = 99999;

        // WHEN
        given().log().ifValidationFails()
            .accept(JSON)
            .when()
            .get("/api/surveys/{id}", surveyId)
            // THEN
            .then().log().ifError()
            .statusCode(SC_NOT_FOUND);

    }

    @Test
    void test_submitting_answers_should_return_a_404_status_when_survey_does_not_exist() {

        //GIVEN
        long surveyId = 1001L;


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
            .statusCode(SC_NOT_FOUND);

    }

    @Test
    void should_return_NO_CONTENT_when_submitting_answer() {

        //GIVEN

        // WHEN
        List<AnswerDTO> answers = List.of(
            new AnswerDTO( 2L,  3),
            new AnswerDTO( 3L,  5)
        );
        given().log().ifValidationFails()
            .accept(JSON)
            .body(answers)
            .contentType(MediaType.APPLICATION_JSON)

            .when()
            .post("/api/surveys/{id}/answers", SURVEY1_ID)
            // THEN
            .then().log().ifError()
            .statusCode(SC_NO_CONTENT);

    }

    @Test
    void test_get_survey_result_byid_when_survey_does_not_exist_with_status_NO_FOUND() {

        //GIVEN
        long surveyId = 10002L;

        // WHEN
        given().log().ifValidationFails()
            .accept(JSON)

            .when()
            .get("/api/surveys/{id}/results", surveyId)
            // THEN
            .then().log().ifError()
            .statusCode(SC_NOT_FOUND);

    }


    @Test
    void test_get_surveyresult_should_return_existing_survey_result_from_service_with_status_OK() throws JsonProcessingException {

        //GIVEN
        SurveyResultsDTO expectedSurveyResultsDTO = new SurveyResultsDTO(
            10L,
            SURVEY1_NAME,
            List.of(
                new QuestionResultsDTO(
                    1L,
                    "Question1",
                    List.of(
                        new AnswerResultsDTO(0, 0),
                        new AnswerResultsDTO(1, 2),
                        new AnswerResultsDTO(2, 0),
                        new AnswerResultsDTO(3, 1),
                        new AnswerResultsDTO(4, 0),
                        new AnswerResultsDTO(5, 0)
                    )
                ),
                new QuestionResultsDTO(
                    2L,
                    "Question2",
                    List.of(
                        new AnswerResultsDTO(0, 0),
                        new AnswerResultsDTO(1, 0),
                        new AnswerResultsDTO(2, 1),
                        new AnswerResultsDTO(3, 0),
                        new AnswerResultsDTO(4, 1),
                        new AnswerResultsDTO(5, 0)
                    )
                )
            )
        );

        // WHEN
        SurveyResultsDTO actualSurveyResultsDTO = given().log().ifValidationFails()
            .accept(JSON)
            .when()
            .get("/api/surveys/{id}/results", SURVEY1_ID)
            // THEN
            .then().log().ifError()
            .statusCode(SC_OK)
            .body("surveyId", equalTo(10))
            .body("surveyName", equalTo(SURVEY1_NAME))
            .extract()
            .as(SurveyResultsDTO.class);

        assertThat(actualSurveyResultsDTO).isEqualTo(expectedSurveyResultsDTO);
    }


}
