package com.artogether.common.business_member;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.Map;

public class BMemberSpecification {

    public static Specification<BusinessMember> dynamicQuery(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction(); // 初始條件為 true

            params.forEach((key, value) -> {
                if (value != null && !value.trim().isEmpty()) { // 檢查非空值
                    switch (key) {
                        case "name":
                            predicate.getExpressions()
                                     .add(criteriaBuilder.like(root.get("name"), "%" + value + "%"));
                            break;
                        case "email":
                            predicate.getExpressions()
                                     .add(criteriaBuilder.like(root.get("email"), "%" + value + "%"));
                            break;
                        case "addr":
                            predicate.getExpressions()
                                     .add(criteriaBuilder.like(root.get("addr"), "%" + value + "%"));
                            break;
                        case "taxId":
                            predicate.getExpressions()
                                     .add(criteriaBuilder.equal(root.get("taxId"), value));
                            break;
                        case "status":
                            try {
                                Byte status = Byte.valueOf(value);
                                predicate.getExpressions()
                                         .add(criteriaBuilder.equal(root.get("status"), status));
                            } catch (NumberFormatException e) {
                                // 處理無效的數字格式（例如空字串或非數字）
                                // 如果需要，可以在此處記錄錯誤或抛出異常
                            }
                            break;
                        case "startDate":
                            try {
                                // 假設 value 是一個日期格式的字串，將其轉換為 Timestamp
                                // 需要先解析日期字串為日期或Timestamp類型
                                predicate.getExpressions()
                                         .add(criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), (Comparable) value));
                            } catch (Exception e) {
                                // 處理日期格式錯誤
                            }
                            break;
                        case "approveDate":
                            try {
                                // 處理 approveDate
                                predicate.getExpressions()
                                         .add(criteriaBuilder.lessThanOrEqualTo(root.get("approveDate"), (Comparable) value));
                            } catch (Exception e) {
                                // 處理日期格式錯誤
                            }
                            break;
                        case "id":
                            try {
                                Integer id = Integer.valueOf(value);
                                predicate.getExpressions()
                                         .add(criteriaBuilder.equal(root.get("id"), id));
                            } catch (NumberFormatException e) {
                                // 處理無效的數字格式
                            }
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
