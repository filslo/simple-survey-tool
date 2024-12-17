package com.github.filslo.simplesurveytool.data.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    private Survey survey;

    public Question() {}

    public Question(Long id, String text, Survey survey) {
        this.id = id;
        this.text = text;
        this.survey = survey;
    }


}
