package kr.tgwing.tech.project.domain;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import kr.tgwing.tech.user.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class ProjectSpecification {
    public static Specification<Project> hasKeywordInProject(String keyword) {
        return (root, query, cb) -> {
            String pattern = "%" + keyword + "%";

            Predicate title = cb.like(root.get("title"), pattern);
            Predicate description = cb.like(root.get("description"), pattern);

            Join<Project, Participant> participantJoin = root.join("participants");
            Predicate name = cb.like(participantJoin.get("name"), pattern);
            Predicate studentNumber = cb.like(participantJoin.get("studentNumber"), pattern);

            return cb.or(title, description, name, studentNumber);
        };
    }

    public static Specification<Project> hasKeywordInMyProject(String keyword) {
        return (root, query, cb) -> {
            String pattern = "%" + keyword + "%";

            Predicate title = cb.like(root.get("title"), pattern);
            Predicate description = cb.like(root.get("description"), pattern);

            Join<Project, Participant> participantJoin = root.join("participants");
            Predicate studentNumber = cb.equal(participantJoin.get("studentNumber"), pattern);

            return cb.or(title, description, studentNumber);
        };
    }

}
