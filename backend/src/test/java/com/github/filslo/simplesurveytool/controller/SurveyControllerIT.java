package com.github.filslo.simplesurveytool.controller;

import com.github.filslo.simplesurveytool.SimpleSurveyToolApplication;
import com.github.filslo.simplesurveytool.dto.SurveyDTO;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
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
        int surveyId = 20;
        String expectedSurveyName = "Survey2";

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
}
