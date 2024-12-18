package com.github.filslo.simplesurveytool.data.repository;

import com.github.filslo.simplesurveytool.data.entity.Rating;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface RatingRepository extends Repository<Rating, Long> {

    Rating save(Rating rating);

    Optional<Rating> findById(long id);
}
