package com.lec.spring.matzip.controller;

import com.lec.spring.matzip.domain.Matzip;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/matzip")
public class MatzipController {
    @PostMapping("")
    public String saveMatZip(Matzip matzip) {

        return null;
    }

}
