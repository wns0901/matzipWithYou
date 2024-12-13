package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.Tag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TagRepository {
    int save(Tag tag);

    Tag findById(Long id);

    List<Tag> findByIds(List<Long> ids);

    List<Tag> findNonMatchingTags(@Param("tagIds") List<Long> tagIds, @Param("myMatzipId") Long myMatzipId);

    List<Tag> findAll();

    int deleteById(Long id);
}
