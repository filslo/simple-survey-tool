package com.github.filslo.simplesurveytool.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @OneToMany(mappedBy = "survey", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Question> questions;

    public Survey() {}

    public Survey(Long id, @NotBlank String name, List<Question> questions) {
        this.id = id;
        this.name = name;
        this.questions = questions;
    }

}
