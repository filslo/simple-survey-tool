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

    @Test
    void test_getSurveyAnswers_with_unknown_surveyId_should_throw_noResultException() {
        //GIVEN
        Long surveyId = 199L;

        //WHEN
        assertThatThrownBy(() -> {
            this.surveyService.getSurveyAnswers(surveyId);

        //THEN
        }).isInstanceOf(NoResultException.class);

        verifyNoInteractions(this.ratingRepository);

    }

    @Test
    void test_getSurveyAnswers_with_known_surveyId_should_return_expected_surveyDTO() {

        //GIVEN
        Long surveyId = 200L;
        Survey survey = new Survey(surveyId, "Test Survey", new ArrayList<>());
        Question question1 = new Question(1L, "Test Question", survey);
        Question question2 = new Question(2L, "Test Question 2", survey);
        Question question3 = new Question(3L, "Test Question 3", survey);


        List<Question> surveyQuestions = List.of(question1, question2, question3);
        survey.setQuestions(surveyQuestions);


        when(this.surveyRepository.findById(surveyId))
            .thenReturn(
                Optional.of(survey)
            );


        when(this.questionRepository.findBySurvey(survey))
            .thenReturn(
                Optional.of(surveyQuestions)
            );

        //WHEN
        SurveyResultsDTO surveyAnswers = this.surveyService.getSurveyAnswers(surveyId);

        //THEN
        List<QuestionResultsDTO> questionResults = surveyAnswers.getQuestionResults();
        assertThat(questionResults).hasSize(surveyQuestions.size());

        questionResults.forEach(
            questionResultsDTO -> {
                int maxRating = 5;
                List<AnswerResultsDTO> answerResults = questionResultsDTO.getAnswerResults();
                assertThat(answerResults).hasSize(maxRating + 1); // include 0
                answerResults.forEach(
                    answerResultsDTO ->
                        assertThat(answerResultsDTO.getCount()).isEqualTo(0L)
                );
            }
        );
    }

    @Test
    void test_getSurveyAnswers_with_known_surveyId_with_noanswers_should_return_expected_surveyDTO() {

        //GIVEN
        Long surveyId = 300L;
        Survey survey = new Survey(surveyId, "Test Survey", new ArrayList<>());
        Question question1 = new Question(1L, "Test Question", survey);
        Question question2 = new Question(2L, "Test Question 2", survey);
        Question question3 = new Question(3L, "Test Question 3", survey);


        List<Question> surveyQuestions = List.of(question1, question2, question3);
        survey.setQuestions(surveyQuestions);

        Rating rating11 = new Rating(11L, question1, 1);
        Rating rating11_1 = new Rating(111L, question1, 1);

        Rating rating12 = new Rating(12L, question1, 5);

        Rating rating21 = new Rating(21L, question2, 1);
        Rating rating22 = new Rating(22L, question2, 5);
        Rating rating23 = new Rating(23L, question2, 3);

        when(this.surveyRepository.findById(surveyId))
            .thenReturn(
                Optional.of(survey)
            );


        when(this.questionRepository.findBySurvey(survey))
            .thenReturn(
                Optional.of(surveyQuestions)
            );

        when(this.ratingRepository.findByQuestion(question1))
            .thenReturn(
                List.of(rating11, rating11_1, rating12)
            );
        when(this.ratingRepository.findByQuestion(question2))
            .thenReturn(
                List.of(rating21, rating22, rating23)
            );

        //WHEN
        SurveyResultsDTO surveyAnswers = this.surveyService.getSurveyAnswers(surveyId);

        //THEN
        List<QuestionResultsDTO> questionResults = surveyAnswers.getQuestionResults();

        assertThat(questionResults).hasSize(surveyQuestions.size());

        questionResults.forEach(
            questionResultsDTO -> {
                int maxRating = 5;
                List<AnswerResultsDTO> answerResults = questionResultsDTO.getAnswerResults();
                assertThat(answerResults).hasSize(maxRating + 1); // include 0
                answerResults.forEach(
                    answerResultsDTO ->
                        assertThat(answerResultsDTO.getRating()).isBetween(0,5)
                );
            }
        );


        QuestionResultsDTO question1ResultsDTO = getQuestionResultsDTO(questionResults, question1);
        assertThat(question1ResultsDTO).isNotNull();

        assertThat(getAnswerResultsDTO(question1ResultsDTO, 0).getCount()).isEqualTo(0L);
        assertThat(getAnswerResultsDTO(question1ResultsDTO, 1).getCount()).isEqualTo(2L);
        assertThat(getAnswerResultsDTO(question1ResultsDTO, 2).getCount()).isEqualTo(0L);
        assertThat(getAnswerResultsDTO(question1ResultsDTO, 3).getCount()).isEqualTo(0L);
        assertThat(getAnswerResultsDTO(question1ResultsDTO, 4).getCount()).isEqualTo(0L);
        assertThat(getAnswerResultsDTO(question1ResultsDTO, 5).getCount()).isEqualTo(1L);

        QuestionResultsDTO question2ResultsDTO = getQuestionResultsDTO(questionResults, question2);
        assertThat(question2ResultsDTO).isNotNull();

        assertThat(getAnswerResultsDTO(question2ResultsDTO,0).getCount()).isEqualTo(0L);
        assertThat(getAnswerResultsDTO(question2ResultsDTO,1).getCount()).isEqualTo(1L);
        assertThat(getAnswerResultsDTO(question2ResultsDTO,2).getCount()).isEqualTo(0L);
        assertThat(getAnswerResultsDTO(question2ResultsDTO,3).getCount()).isEqualTo(1L);
        assertThat(getAnswerResultsDTO(question2ResultsDTO,4).getCount()).isEqualTo(0L);
        assertThat(getAnswerResultsDTO(question2ResultsDTO,5).getCount()).isEqualTo(1L);

        QuestionResultsDTO question3ResultsDTO = getQuestionResultsDTO(questionResults, question3);
        assertThat(question3ResultsDTO).isNotNull();

        assertThat(getAnswerResultsDTO(question3ResultsDTO,0).getCount()).isEqualTo(0L);
        assertThat(getAnswerResultsDTO(question3ResultsDTO,1).getCount()).isEqualTo(0L);
        assertThat(getAnswerResultsDTO(question3ResultsDTO,2).getCount()).isEqualTo(0L);
        assertThat(getAnswerResultsDTO(question3ResultsDTO,3).getCount()).isEqualTo(0L);
        assertThat(getAnswerResultsDTO(question3ResultsDTO,4).getCount()).isEqualTo(0L);
        assertThat(getAnswerResultsDTO(question3ResultsDTO,5).getCount()).isEqualTo(0L);

    }

    private static QuestionResultsDTO getQuestionResultsDTO(List<QuestionResultsDTO> questionResults, Question question1) {
        return questionResults.stream().filter(
            questionResultsDTO -> Objects.equals(questionResultsDTO.getQuestionId(), question1.getId())
        ).findFirst().orElse(null);
    }

    private static AnswerResultsDTO getAnswerResultsDTO(QuestionResultsDTO question1ResultsDTO, int rating0) {
        return question1ResultsDTO.getAnswerResults().stream().filter(
            answerResultsDTO -> Objects.equals(answerResultsDTO.getRating(), rating0)
        ).findFirst().orElse(null);
    }


}
