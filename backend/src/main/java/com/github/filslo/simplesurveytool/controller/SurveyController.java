package com.github.filslo.simplesurveytool.controller;


import com.github.filslo.simplesurveytool.dto.*;
import com.github.filslo.simplesurveytool.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    public SurveyDTO getSurvey(
        @PathVariable("id") Long id
    ) {

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

    @Operation(
        summary = "Submit answers for a survey"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "404",
            description = "Survey not found",
            content = @Content
        ) }
    )
    @PostMapping("/{id}/answers")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void submitAnswers(
        @PathVariable("id") @Parameter(description = "Identifier of the survey to submit answers for") Long surveyId,
        @RequestBody List<AnswerDTO> answers
    ) {

        assert this.surveyService != null;
        try {
            this.surveyService.storeAnswers(surveyId, answers);
        } catch (NoResultException e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Survey Not Found",e
            );
        }
    }

}
