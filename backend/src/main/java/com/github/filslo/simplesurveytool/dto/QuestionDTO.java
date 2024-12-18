package com.github.filslo.simplesurveytool.dto;

import java.util.Objects;

/**
 * DTO for {@link com.github.filslo.simplesurveytool.data.entity.Question}
 */
public class QuestionDTO {
    private Long id;
    private String text;

    public QuestionDTO() {
    }

    public QuestionDTO(Long id, String text) {
        this.id = id;
        this.text = text;
    }


    public Long getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String toString() {
        return "QuestionDTO(id=" + this.getId() + ", text=" + this.getText() + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof QuestionDTO other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (!Objects.equals(this$id, other$id)) return false;
        final Object this$text = this.getText();
        final Object other$text = other.getText();
        return Objects.equals(this$text, other$text);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof QuestionDTO;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $text = this.getText();
        result = result * PRIME + ($text == null ? 43 : $text.hashCode());
        return result;
    }
}
