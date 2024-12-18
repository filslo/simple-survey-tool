package com.github.filslo.simplesurveytool.data.entity;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.Range;

@Entity
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    private Question question;

    @Column(name = "rating_value", nullable = false)
    @Range(min = 0, max = 5)
    private int value;

    public Rating(Long id, Question question, int value) {
        this.id = id;
        this.question = question;
        this.value = value;
    }

    public Rating() {}

    public Long getId() {
        return this.id;
    }

    public Question getQuestion() {
        return this.question;
    }

    public int getValue() {
        return this.value;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
