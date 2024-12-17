package com.github.filslo.simplesurveytool.data.repository;

import com.github.filslo.simplesurveytool.data.entity.Survey;
import org.springframework.data.repository.ListCrudRepository;

public interface SurveyRepository extends ListCrudRepository<Survey, Long> {}
