package com.artogether.event.event;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.Map;

public class EventSpecifications {

    public static Specification<Event> dynamicQuery(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction(); // 初始條件為 true

            params.forEach((key, value) -> {
                if (value != null && !value.trim().isEmpty()) { // 忽略空值
                    switch (key) {
                        case "name":
                            predicate.getExpressions()
                                     .add(criteriaBuilder.like(root.get("name"), "%" + value + "%"));
                            break;
                        case "location":
                            predicate.getExpressions()
                                     .add(criteriaBuilder.like(root.get("location"), "%" + value + "%"));
                            break;
                        case "catalog":
                            predicate.getExpressions()
                                     .add(criteriaBuilder.equal(root.get("catalog"), value));
                            break;
                        case "status":
                            predicate.getExpressions()
                                     .add(criteriaBuilder.equal(root.get("status"), Integer.valueOf(value)));
                            break;
                        case "minPrice":
                            predicate.getExpressions()
                                     .add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), Integer.valueOf(value)));
                            break;
                        case "maxPrice":
                            predicate.getExpressions()
                                     .add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), Integer.valueOf(value)));
                            break;
                        case "startDate":
                            predicate.getExpressions()
                                     .add(criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), (Comparable) value));
                            break;
                        case "endDate":
                            predicate.getExpressions()
                                     .add(criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), (Comparable) value));
                            break;
                        case "businessId":
                            predicate.getExpressions()
                                     .add(criteriaBuilder.equal(root.get("businessMember").get("id"), Integer.valueOf(value)));
                            break;
                        case "id":
                        	predicate.getExpressions()
                        	.add(criteriaBuilder.equal(root.get("id"), Integer.valueOf(value)));
                        	break;
                        default:
                            // 如果有未知字段，可以選擇忽略或記錄錯誤
                            break;
                    }
                }
            });

            return predicate;
        };
    }
}
