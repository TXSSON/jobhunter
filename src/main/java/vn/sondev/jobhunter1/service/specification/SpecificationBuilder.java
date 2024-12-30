package vn.sondev.jobhunter1.service.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecificationBuilder<T> {
    public Specification<T> whereNameLike(String key, Object value) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(key), "%" + value + "%"));
    }

    public Specification<T> whereNameIn(String key, List<String> values) {
        return ((root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Object> inClause = criteriaBuilder.in(root.get(key));
            for (Object value : values) {
                inClause.value(value);
            }
            return inClause;
        });
    }
    public Specification<T> whereNameEqual(String key, Object value){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(key), value));
    }
}
