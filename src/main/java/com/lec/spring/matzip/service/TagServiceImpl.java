package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.Tag;
import com.lec.spring.matzip.repository.TagRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(SqlSession sqlSession) {
        this.tagRepository = sqlSession.getMapper(TagRepository.class);
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public int deleteById(Long id) {
        return tagRepository.deleteById(id);
    }
}

