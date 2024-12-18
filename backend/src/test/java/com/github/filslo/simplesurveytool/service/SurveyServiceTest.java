package com.github.filslo.simplesurveytool.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.filslo.simplesurveytool.data.entity.*;
import com.github.filslo.simplesurveytool.data.repository.*;
import com.github.filslo.simplesurveytool.dto.*;
import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class SurveyServiceTest {

    private SurveyService surveyService;

    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private QuestionRepository questionRepository;
    
    @Spy
    private ObjectMapper objectMapper;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        surveyService = new SurveyService(
            this.objectMapper,
            this.surveyRepository,
            this.ratingRepository,
            this.questionRepository
        );
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
        assertThat(allSurveys).isEqualTo(expectedSurveyDTOs);
    }

    @Test
    void test_getSurvey_with_unknown_surveyId_should_throw_noResultException() {
        //GIVEN
        Long surveyId = 199L;

        when(this.surveyRepository.findById(surveyId)).thenReturn(Optional.empty());

        //WHEN
        assertThatThrownBy(() -> {
            this.surveyService.getSurvey(surveyId);

        //THEN
        }).isInstanceOf(NoResultException.class);

    }

    @Test
    void test_getSurvey_with_known_surveyId_should_return_expected_surveyDTO() {
        //GIVEN
        Long surveyId = 199L;

        String surveyName = "Test Survey";
        when(this.surveyRepository.findById(surveyId))
            .thenReturn(
                Optional.of(new Survey(surveyId, surveyName, null))
        );

        //WHEN
        SurveyDTO survey = this.surveyService.getSurvey(surveyId);

        //THEN
        assertThat(survey.getId()).isEqualTo(surveyId);
        assertThat(survey.getName()).isEqualTo(surveyName);
    }

    @Test
    void test_storeAnswers_with_unknown_surveyId_should_throw_noResultException() {
        //GIVEN
        Long surveyId = 199L;

        List<AnswerDTO> answerDTOS = List.of(new AnswerDTO(1L, 1));

            //WHEN
        assertThatThrownBy(() -> {
            this.surveyService.storeAnswers(surveyId, answerDTOS);

            //THEN
        }).isInstanceOf(NoResultException.class);

        verifyNoInteractions(this.ratingRepository);

    }

    @Test
    void test_storeAnswers_with_known_surveyId_should_return_expected_surveyDTO() {
        //GIVEN
        Long surveyId = 199L;
        long questionId = 1L;

        when(this.questionRepository.findById(questionId))
            .thenReturn(
                Optional.of(new Question(questionId, "Test Question", null))
            );
        List<AnswerDTO> answerDTOS = List.of(new AnswerDTO(questionId, 1));

        when(this.surveyRepository.findById(surveyId))
            .thenReturn(
                Optional.of(new Survey(surveyId, "Test Survey", null))
            );

        //WHEN
        this.surveyService.storeAnswers(surveyId, answerDTOS);

        //THEN
        verify(this.ratingRepository, times(answerDTOS.size())).save(any());

    }

}
