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

}
