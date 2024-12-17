package com.github.filslo.simplesurveytool.dto;


import java.util.List;
import java.util.Objects;

/**
 * DTO for {@link com.github.filslo.simplesurveytool.data.entity.Survey}
 */
public class SurveyDTO {

    private Long id;
    private String name;
    private List<QuestionDTO> questions;

    public SurveyDTO() {
    }

    public SurveyDTO(Long id, String name, List<QuestionDTO> questions) {
        this.id = id;
        this.name = name;
        this.questions = questions;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public List<QuestionDTO> getQuestions() {
        return this.questions;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }

    public String toString() {
        return "SurveyDTO(id=" + this.getId() + ", name=" + this.getName() + ", questions=" + this.getQuestions() + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof SurveyDTO other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (!Objects.equals(this$id, other$id)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (!Objects.equals(this$name, other$name)) return false;
        final Object this$questions = this.getQuestions();
        final Object other$questions = other.getQuestions();
        return Objects.equals(this$questions, other$questions);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof SurveyDTO;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $questions = this.getQuestions();
        result = result * PRIME + ($questions == null ? 43 : $questions.hashCode());
        return result;
    }
}
