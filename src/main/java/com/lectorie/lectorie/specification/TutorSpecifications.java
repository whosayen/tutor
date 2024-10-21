package com.lectorie.lectorie.specification;

import com.lectorie.lectorie.model.Language;
import com.lectorie.lectorie.model.Tutor;
import com.lectorie.lectorie.enums.Level;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class TutorSpecifications {

    public static Specification<Tutor> hasLanguageLevel(Level level, Language language) {
        return (root, query, criteriaBuilder) -> {
            if (level == null || language == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.join("languageLevels", JoinType.LEFT).get("language"), language),
                    criteriaBuilder.greaterThanOrEqualTo(root.join("languageLevels", JoinType.LEFT).get("level"), level)
            );
        };
    }

    public static Specification<Tutor> hasLanguageToTeach(Language language) {
        return (root, query, criteriaBuilder) -> {
            if (language == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("languageToTeach"), language);
        };
    }

    public static Specification<Tutor> hasHourlyRateBetween(Double minRate, Double maxRate) {
        return (root, query, criteriaBuilder) -> {
            if (minRate == null && maxRate == null) {
                return criteriaBuilder.conjunction();
            }
            if (minRate != null && maxRate != null) {
                return criteriaBuilder.between(root.get("hourlyRate"), minRate, maxRate);
            }
            if (minRate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("hourlyRate"), minRate);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("hourlyRate"), maxRate);
        };
    }


    public static Specification<Tutor> hasShortDescriptionOrFirstNameOrLastNameContaining(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String[] keywords = keyword.trim().toLowerCase().split("\\s+");
            List<Predicate> predicates = new ArrayList<>();

            for (String part : keywords) {
                String likePattern = "%" + part + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.join("user", JoinType.LEFT).get("userSettings").get("firstName")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.join("user", JoinType.LEFT).get("userSettings").get("lastName")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("shortDescription")), likePattern)
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }}