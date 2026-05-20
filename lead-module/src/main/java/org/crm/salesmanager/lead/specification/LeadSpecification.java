package org.crm.salesmanager.lead.specification;

import jakarta.persistence.criteria.Predicate;
import org.crm.salesmanager.lead.domain.Lead;
import org.crm.salesmanager.lead.enums.LeadStage;
import org.crm.salesmanager.lead.enums.LeadTemperature;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class LeadSpecification {

    private LeadSpecification() {
    }

    public static Specification<Lead> hasTemperature(LeadTemperature temperature) {
        return (root, query, criteriaBuilder) ->
                temperature == null ? null : criteriaBuilder.equal(root.get("temperature"), temperature);
    }

    public static Specification<Lead> hasStage(LeadStage stage) {
        return (root, query, criteriaBuilder) ->
                stage == null ? null : criteriaBuilder.equal(root.get("stage"), stage);
    }

    public static Specification<Lead> assignedTo(Long assignedTo) {
        return (root, query, criteriaBuilder) ->
                assignedTo == null ? null : criteriaBuilder.equal(root.get("assignedTo"), assignedTo);
    }

    public static Specification<Lead> valueBetween(Double minValue, Double maxValue) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (minValue != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("expectedValue"), minValue));
            }

            if (maxValue != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("expectedValue"), maxValue));
            }

            return predicates.isEmpty() ? null : criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Lead> search(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search == null || search.isBlank()) {
                return null;
            }

            String pattern = "%" + search.trim().toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("customerName")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("businessName")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("contactNumber")), pattern)
            );
        };
    }
}
