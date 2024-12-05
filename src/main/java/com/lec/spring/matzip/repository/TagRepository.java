package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.Tag;

import java.util.List;

public interface TagRepository {
    int save(Tag tag);

    Tag findById(int id);

    List<Tag> findByIds(List<Long> ids);

    List<Tag> findAll();

    int deleteById(Long id);
}
