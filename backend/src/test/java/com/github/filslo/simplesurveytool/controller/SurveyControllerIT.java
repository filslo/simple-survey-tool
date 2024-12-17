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
import static org.apache.http.HttpStatus.SC_OK;
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
            .assertThat()
            .body("size()", is(2))
            .extract()
            .as(SurveyDTO[].class);

    }

}
