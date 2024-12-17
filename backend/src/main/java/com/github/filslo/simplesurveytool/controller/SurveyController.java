package com.github.filslo.simplesurveytool.controller;


import com.github.filslo.simplesurveytool.dto.SurveyDTO;
import com.github.filslo.simplesurveytool.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.NoResultException;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/api/surveys")
@CrossOrigin(origins = "*", allowedHeaders = "*") // For development purposes only
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @Operation(
        summary = "Get all surveys"
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SurveyDTO> getSurveys() {

        assert this.surveyService != null;
        return this.surveyService.getAllSurveys();
    }

    @Operation(
        summary = "Get survey by Id"
    )
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SurveyDTO getSurvey(@PathVariable("id") Long id) {

        assert this.surveyService != null;
        try {
            return this.surveyService.getSurvey(id);
        } catch (NoResultException e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Survey Not Found",e
            );
        }
     }


}
