package kr.tgwing.tech.user.entity;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;


public class UserSpecification {

    // 다중 검색 기능(학번, 이름, 이메일) -> 동아리원
    public static Specification<User> hasKeywordInUser(String keyword) {
        return (root, query, cb) -> {
            String pattern = "%" + keyword + "%";

            Predicate studentNumberPredicate = cb.like(root.get("studentNumber"), pattern);
            Predicate namePredicate = cb.like(root.get("name"), pattern);
            Predicate emailPredicate = cb.like(root.get("email"), pattern);

            return cb.or(studentNumberPredicate, namePredicate, emailPredicate);
        };
    }

    public static Specification<TempUser> hasKeywordInTempUser(String keyword) {
        return (root, query, cb) -> {
            String pattern = "%" + keyword + "%";

            Predicate studentNumberPredicate = cb.like(root.get("studentNumber"), pattern);
            Predicate namePredicate = cb.like(root.get("name"), pattern);
            Predicate emailPredicate = cb.like(root.get("email"), pattern);

            return cb.or(studentNumberPredicate, namePredicate, emailPredicate);
        };
    }
}
