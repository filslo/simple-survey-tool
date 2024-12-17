package com.github.filslo.simplesurveytool.data.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String text;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Survey survey;

    public Question() {
    }

    public Question(Long id, String text, Survey survey) {
        this.id = id;
        this.text = text;
        this.survey = survey;
    }


    public Long getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public Survey getSurvey() {
        return this.survey;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    @JsonIgnore
    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public String toString() {
        return "Question(id=" + this.getId() + ", text=" + this.getText() + ", survey=" + this.getSurvey() + ")";
    }
}
