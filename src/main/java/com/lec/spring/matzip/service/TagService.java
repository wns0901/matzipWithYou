package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.Tag;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService {
    List<Tag> getAllTags();

    int deleteById(Long id);
}

