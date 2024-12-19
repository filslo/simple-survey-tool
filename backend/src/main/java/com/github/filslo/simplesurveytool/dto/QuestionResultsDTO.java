package com.github.filslo.simplesurveytool.dto;

import java.util.List;
import java.util.Objects;

public class QuestionResultsDTO {

    private Long questionId;
    private String questionText;
    private List<AnswerResultsDTO> answerResults;

    public QuestionResultsDTO(Long questionId, String questionText, List<AnswerResultsDTO> answerResults) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.answerResults = answerResults;
    }

    public QuestionResultsDTO() {}

    public Long getQuestionId() {
        return this.questionId;
    }

    public String getQuestionText() {
        return this.questionText;
    }

    public List<AnswerResultsDTO> getAnswerResults() {
        return this.answerResults;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setAnswerResults(List<AnswerResultsDTO> answerResults) {
        this.answerResults = answerResults;
    }

    public String toString() {
        return "QuestionResultsDTO(questionId=" + this.getQuestionId() + ", questionText=" + this.getQuestionText() + ", answerResults=" + this.getAnswerResults() + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof QuestionResultsDTO other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$questionId = this.getQuestionId();
        final Object other$questionId = other.getQuestionId();
        if (!Objects.equals(this$questionId, other$questionId))
            return false;
        final Object this$questionText = this.getQuestionText();
        final Object other$questionText = other.getQuestionText();
        if (!Objects.equals(this$questionText, other$questionText))
            return false;
        final Object this$answerResults = this.getAnswerResults();
        final Object other$answerResults = other.getAnswerResults();
        return Objects.equals(this$answerResults, other$answerResults);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof QuestionResultsDTO;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $questionId = this.getQuestionId();
        result = result * PRIME + ($questionId == null ? 43 : $questionId.hashCode());
        final Object $questionText = this.getQuestionText();
        result = result * PRIME + ($questionText == null ? 43 : $questionText.hashCode());
        final Object $answerResults = this.getAnswerResults();
        result = result * PRIME + ($answerResults == null ? 43 : $answerResults.hashCode());
        return result;
    }
}
