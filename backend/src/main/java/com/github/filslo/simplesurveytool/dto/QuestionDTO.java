package com.github.filslo.simplesurveytool.dto;

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
}
