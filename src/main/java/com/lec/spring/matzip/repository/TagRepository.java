package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagRepository {
    int save(Tag tag);

    boolean saveMatzipTags(List<Long> tagIds, Long myMatzipId);

    Tag findById(Long id);

    List<Tag> findByIds(List<Long> ids);

    List<Tag> findNonMatchingTags(@Param("tagIds") List<Long> tagIds, @Param("myMatzipId") Long myMatzipId);

    List<Tag> findAll();

    int deleteById(Long id);
}
