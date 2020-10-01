package com.mypack.todo.util;

import com.mypack.todo.dto.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ToDoJpaSpecification<T> implements Specification<T> {

    private transient List<SearchCriteria> searchCriterias;

    public ToDoJpaSpecification() {
        this.searchCriterias = new ArrayList<>();
    }

    public void add(final List<SearchCriteria> criteria) {
        if(!CollectionUtils.isEmpty(criteria)) {
            this.searchCriterias.addAll(criteria);
        }
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
        criteriaQuery.distinct(true);

        List<Predicate> predicates = new ArrayList<>();
        for (final SearchCriteria criteria : searchCriterias) {
            switch (criteria.getOperation()) {
                case EQUALS:
                    predicates.add(builder.equal(root.get(criteria.getKey()), criteria.getValue()));
                    break;
                case LIKE:
                    predicates.add(builder.like(builder.lower(root.get(criteria.getKey())), criteria.getValue().toString().toLowerCase() + "%"));
                    break;
                case CONTAINS:
                    predicates.add(builder.like(builder.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%"));
                    break;
                case DATE:
                    predicates.add(builder.equal(root.get(criteria.getKey()), formatDate(criteria.getValue())));
                    break;
                case GREATER_THAN:
                    predicates.add(builder.greaterThan(root.get(criteria.getKey()), new BigDecimal(criteria.getValue().toString())));
                    break;
                case LESS_THAN:
                    predicates.add(builder.lessThan(root.get(criteria.getKey()), new BigDecimal(criteria.getValue().toString())));
                    break;
                case GREATER_THAN_EQUALS:
                    predicates.add(builder.greaterThanOrEqualTo(root.get(criteria.getKey()), new BigDecimal(criteria.getValue().toString())));
                    break;
                case LESS_THAN_EQUALS:
                    predicates.add(builder.lessThanOrEqualTo(root.get(criteria.getKey()), new BigDecimal(criteria.getValue().toString())));
                    break;
                case NOT_EQUALS:
                    predicates.add(builder.notEqual(root.get(criteria.getKey()), criteria.getValue()));
                    break;
                case NOT_IN:
                    predicates.add(root.get(criteria.getKey()).in(((List<?>)criteria.getValue()).toArray()).not());
                    break;
                case IN:
                    predicates.add(root.get(criteria.getKey()).in(((List<?>)criteria.getValue()).toArray()));
                    break;
            }
        }
        return builder.and(predicates.toArray(new Predicate[0]));
    }

    private LocalDate formatDate(final Object date) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AppConstants.SERVER_DATE_FORMAT);
        return LocalDate.parse((String) date, formatter);
    }
}
