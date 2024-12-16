package org.vitalii.fedyk.librarygenerated.search;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.vitalii.fedyk.librarygenerated.model.Book;

public class BookSpecification extends GenericSpecification<Book> {
    public BookSpecification(final SearchCriteria searchCriteria) {
        super(searchCriteria);
    }

    @Override
    public Predicate toPredicate(final Root<Book> root,
                                 final CriteriaQuery<?> query,
                                 final CriteriaBuilder criteriaBuilder) {
        switch (searchCriteria.getOperation()) {
            case EQUALS:
                if (searchCriteria.getKey().equals("title")) {
                    return criteriaBuilder.equal(root.get("title"), searchCriteria.getValue());
                } else if (searchCriteria.getKey().equals("genre")) {
                    return criteriaBuilder.equal(root.get("genre"), searchCriteria.getValue());
                }
                break;
            case NOT_EQUALS:
                if (searchCriteria.getKey().equals("title")) {
                    return criteriaBuilder.notEqual(root.get("title"), searchCriteria.getValue());
                } else if (searchCriteria.getKey().equals("genre")) {
                    return criteriaBuilder.notEqual(root.get("genre"), searchCriteria.getValue());
                }
                break;
            case GREATER_THAN:
                if (searchCriteria.getKey().equals("pagesCount")) {
                    return criteriaBuilder.greaterThan(root.get("pagesCount"), Integer.valueOf((String) searchCriteria.getValue()));
                }
                break;
            case LESS_THAN:
                if (searchCriteria.getKey().equals("pagesCount")) {
                    return criteriaBuilder.lessThan(root.get("pagesCount"), Integer.valueOf((String) searchCriteria.getValue()));
                }
                break;
            case GREATER_THAN_EQUALS:
                if (searchCriteria.getKey().equals("pagesCount")) {
                    return criteriaBuilder.ge(root.get("pagesCount"), Integer.valueOf((String) searchCriteria.getValue()));
                }
                break;
            case LESS_THAN_EQUALS:
                if (searchCriteria.getKey().equals("pagesCount")) {
                    return criteriaBuilder.le(root.get("pagesCount"), Integer.valueOf((String) searchCriteria.getValue()));
                }
                break;
            case CONTAINS:
                if (searchCriteria.getKey().equals("title")) {
                    return criteriaBuilder.like(root.get("title"), "%" + searchCriteria.getValue() + "%");
                }
                break;
        }
        return criteriaBuilder.conjunction();
    }
}
