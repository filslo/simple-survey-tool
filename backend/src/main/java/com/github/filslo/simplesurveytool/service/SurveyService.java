package com.github.filslo.simplesurveytool.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.filslo.simplesurveytool.data.entity.*;
import com.github.filslo.simplesurveytool.data.repository.*;
import com.github.filslo.simplesurveytool.dto.*;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class SurveyService {

    private final ObjectMapper objectMapper;

    private final SurveyRepository surveyRepository;

    private final RatingRepository ratingRepository;

    private final QuestionRepository questionRepository;


    @Autowired
    public SurveyService(ObjectMapper objectMapper,
                         SurveyRepository surveyRepository,
                         RatingRepository ratingRepository,
                         QuestionRepository questionRepository) {
        this.objectMapper = objectMapper;
        this.surveyRepository = surveyRepository;
        this.ratingRepository = ratingRepository;
        this.questionRepository = questionRepository;
    }

    public List<SurveyDTO> getAllSurveys() {
        List<Survey> surveys = this.surveyRepository.findAll();
        return this.objectMapper.convertValue(surveys, new TypeReference<>() {});

    }

    public SurveyDTO getSurvey(Long id) {
        return this.surveyRepository.findById(id)

                .map(survey ->
                    this.objectMapper.convertValue(survey, SurveyDTO.class)
                )
                .orElseThrow(NoResultException::new);
    }

    public void storeAnswers(Long surveyId, List<AnswerDTO> answers) {

        this.surveyRepository.findById(surveyId).orElseThrow(NoResultException::new);

        List<Rating> ratings = this.convertAnswersToRatings(answers);

        ratings.forEach(
            this.ratingRepository::save
        );
    }

    private List<Rating> convertAnswersToRatings(List<AnswerDTO> answers) {
        return answers.stream().map(
            answer -> new Rating(
                null,
                questionRepository.findById(answer.getQuestionId()).orElseThrow(NoResultException::new),
                answer.getRating()
            )
        ).toList();
    }

    public SurveyResultsDTO getSurveyAnswers(Long surveyId) {

        Survey survey = this.surveyRepository.findById(surveyId).orElseThrow(NoResultException::new);

        Optional<List<Question>> surveyQuestions = this.questionRepository.findBySurvey(survey);

        Map<Question, Map<Integer, Long>> collect2 = surveyQuestions.orElse(new ArrayList<>()).stream()
            .map(question ->
                {
                    Map<Integer, Long> collect1 = this.ratingRepository.findByQuestion(question)
                        .stream()
                        .collect(
                            Collectors.groupingBy(
                                Rating::getValue,
                                Collectors.counting()
                            )
                        );

                    for (int i = 0; i <= 5; i++) {
                        collect1.putIfAbsent(i, 0L);
                    }
                    return new AbstractMap.SimpleEntry<>(question, collect1);
                }
            ).collect(
                Collectors.toMap(AbstractMap.SimpleEntry::getKey,
                    AbstractMap.SimpleEntry::getValue)
            );

        List<QuestionResultsDTO> questionResults = new ArrayList<>();

        collect2.forEach(
            (question, countPerAnswer) -> {

                List<AnswerResultsDTO> answerResultsDTOS = new ArrayList<>();
                countPerAnswer.forEach(
                    (rate, count) -> {
                        answerResultsDTOS.add(new AnswerResultsDTO(rate, count));
                    }
                );
                answerResultsDTOS.sort(Comparator.comparing(AnswerResultsDTO::getRating));
                questionResults.add(
                    new QuestionResultsDTO(question.getId(), question.getText(), answerResultsDTOS)
                );
            }
        );

        questionResults.sort(Comparator.comparing(QuestionResultsDTO::getQuestionId));

        SurveyResultsDTO results = new SurveyResultsDTO();
        results.setSurveyId(surveyId);
        results.setSurveyName(survey.getName());
        results.setQuestionResults(questionResults);

        return results;


    }
}
