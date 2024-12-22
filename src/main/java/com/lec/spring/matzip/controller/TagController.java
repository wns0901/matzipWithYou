package com.lec.spring.matzip.controller;

import com.lec.spring.matzip.domain.Tag;
import com.lec.spring.matzip.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/tags")
public class TagController {

    private final TagRepository tagRepository;

    @Autowired
    public TagController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveTag(@RequestBody Tag tag) {
        try {
            tagRepository.save(tag);
            return ResponseEntity.ok(Map.of(
                    "status", "SUCCESS",
                    "message", "태그가 성공적으로 추가되었습니다."
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                    "status", "FAIL",
                    "message", "태그 추가에 실패했습니다."
            ));
        }
    }

    @GetMapping("")
    @ResponseBody
    public ResponseEntity<List<Tag>> getAllTags() {
        return ResponseEntity.ok(tagRepository.findAll());
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteTag(@PathVariable Long id) {
        try {
            tagRepository.deleteById(id);
            return ResponseEntity.ok("삭제 성공");
        } catch (Exception e) {
            return ResponseEntity.ok("삭제 실패");
        }
    }
}