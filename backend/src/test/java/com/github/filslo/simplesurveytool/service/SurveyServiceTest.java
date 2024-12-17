package com.github.filslo.simplesurveytool.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.filslo.simplesurveytool.data.entity.Survey;
import com.github.filslo.simplesurveytool.data.repository.SurveyRepository;
import com.github.filslo.simplesurveytool.dto.SurveyDTO;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class SurveyServiceTest {

    private SurveyService surveyService;

    @Mock
    private SurveyRepository surveyRepository;

    @Spy
    private ObjectMapper objectMapper;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        surveyService = new SurveyService(this.objectMapper, this.surveyRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        this.autoCloseable.close();
    }

    @Test
    void getAllSurveys_should_convert_SurveyToDTO() {
        //GIVEN

        List<Survey> surveys = List.of(
            new Survey(1L, "Test Survey", null),
            new Survey(2L, "Test Survey 2", null)
        );

        when(this.surveyRepository.findAll()).thenReturn(surveys);

        List<SurveyDTO> expectedSurveyDTOs = List.of(
            new SurveyDTO(1L, "Test Survey", null),
            new SurveyDTO(2L, "Test Survey 2", null)
        );

        //WHEN
        List<SurveyDTO> allSurveys = this.surveyService.getAllSurveys();

        //THEN
        assertThat(allSurveys).containsAll(expectedSurveyDTOs);
    }
}
