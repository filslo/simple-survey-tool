package com.github.filslo.simplesurveytool.data.repository;

import com.github.filslo.simplesurveytool.data.entity.*;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface RatingRepository extends Repository<Rating, Long> {

    Rating save(Rating rating);

    List<Rating> findByQuestion(Question question);
}
