package com.github.filslo.simplesurveytool.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.filslo.simplesurveytool.data.entity.Survey;
import com.github.filslo.simplesurveytool.data.repository.SurveyRepository;
import com.github.filslo.simplesurveytool.dto.SurveyDTO;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class SurveyService {

    private final ObjectMapper objectMapper;

    private final SurveyRepository surveyRepository;

    @Autowired
    public SurveyService(ObjectMapper objectMapper, SurveyRepository surveyRepository) {
        this.objectMapper = objectMapper;
        this.surveyRepository = surveyRepository;
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
}
