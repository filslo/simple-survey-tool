package com.github.filslo.simplesurveytool.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

import java.util.Objects;

@Valid
public class AnswerDTO {

    @NotNull
    @JsonAlias("id")
    private Long questionId;

    @Range(min = 0, max = 5)
    private Integer rating;

    public AnswerDTO(@NotNull Long questionId, Integer rating) {
        this.questionId = questionId;
        this.rating = rating;
    }

    public AnswerDTO() {
    }

    public @NotNull Long getQuestionId() {
        return this.questionId;
    }

    public Integer getRating() {
        return this.rating;
    }

    public void setQuestionId(@NotNull Long questionId) {
        this.questionId = questionId;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof AnswerDTO other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$questionId = this.getQuestionId();
        final Object other$questionId = other.getQuestionId();
        if (!Objects.equals(this$questionId, other$questionId))
            return false;
        final Object this$rating = this.getRating();
        final Object other$rating = other.getRating();
        return Objects.equals(this$rating, other$rating);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof AnswerDTO;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $questionId = this.getQuestionId();
        result = result * PRIME + ($questionId == null ? 43 : $questionId.hashCode());
        final Object $rating = this.getRating();
        result = result * PRIME + ($rating == null ? 43 : $rating.hashCode());
        return result;
    }

    public String toString() {
        return "AnswerDTO(questionId=" + this.getQuestionId() + ", rating=" + this.getRating() + ")";
    }
}
