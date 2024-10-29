package kr.tgwing.tech.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import kr.tgwing.tech.blog.dto.HashtagQuery;
import kr.tgwing.tech.blog.dto.HashtagView;
import kr.tgwing.tech.blog.entity.Hashtag;
import kr.tgwing.tech.blog.entity.HashtagSpecifications;
import kr.tgwing.tech.blog.repository.HashtagRepository;

/**
 * HashtagServiceImpl
 */
@Service
@AllArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;

    @Override
    public Page<HashtagView> getHashtags(HashtagQuery query, String userStudentNumber, Pageable pageable) {
        Specification<Hashtag> spec = HashtagSpecifications.hasNameLike(query.getKeyword());

        if (query.isMe()) {
            spec = spec.and(HashtagSpecifications.hasStudentWithStudentNumber(userStudentNumber));
        }
        Page<Hashtag> result = hashtagRepository.findAll(spec, pageable);
        return result.map(HashtagView::of);
    }

}
