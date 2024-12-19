package com.github.filslo.simplesurveytool.dto;


import java.util.List;
import java.util.Objects;

public class SurveyResultsDTO {

    private Long surveyId;
    private String surveyName;
    // private Map<String, Map<Integer, Long>> countPerAnswerPerQuestion;
    private List<QuestionResultsDTO> questionResults;

    public SurveyResultsDTO() {
    }

    public SurveyResultsDTO(Long surveyId,
                            String surveyName,
                            List<QuestionResultsDTO> questionResults
    ) {
        this.surveyId = surveyId;
        this.surveyName = surveyName;
        this.questionResults = questionResults;
    }

    public Long getSurveyId() {
        return this.surveyId;
    }

    public List<QuestionResultsDTO> getQuestionResults() {
        return this.questionResults;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public void setQuestionResults(List<QuestionResultsDTO> questionResults) {
        this.questionResults = questionResults;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof SurveyResultsDTO other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$surveyId = this.getSurveyId();
        final Object other$surveyId = other.getSurveyId();
        if (!Objects.equals(this$surveyId, other$surveyId)) return false;
        final Object this$questionResults = this.getQuestionResults();
        final Object other$questionResults = other.getQuestionResults();
        return Objects.equals(this$questionResults, other$questionResults);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof SurveyResultsDTO;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $surveyId = this.getSurveyId();
        result = result * PRIME + ($surveyId == null ? 43 : $surveyId.hashCode());
        final Object $questionResults = this.getQuestionResults();
        result = result * PRIME + ($questionResults == null ? 43 : $questionResults.hashCode());
        return result;
    }


    public String getSurveyName() {
        return this.surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String toString() {
        return "SurveyResultsDTO(surveyId=" + this.getSurveyId() + ", surveyName=" + this.getSurveyName() + ", questionResults=" + this.getQuestionResults() + ")";
    }
}
