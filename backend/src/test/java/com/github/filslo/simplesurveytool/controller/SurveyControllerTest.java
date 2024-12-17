package com.github.filslo.simplesurveytool.controller;
import com.github.filslo.simplesurveytool.SimpleSurveyToolApplication;
import com.github.filslo.simplesurveytool.dto.SurveyDTO;
import com.github.filslo.simplesurveytool.service.SurveyService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
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

    }

}
