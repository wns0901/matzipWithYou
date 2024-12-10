package com.lec.spring.matzip.controller;

import com.lec.spring.matzip.domain.KakaoPlaceDTO;
import com.lec.spring.matzip.domain.Matzip;
import com.lec.spring.matzip.service.MatzipService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.net.http.HttpRequest;

@Controller
@RequestMapping("/matzip")
public class MatzipController {

    private final MatzipService matzipService;

    public MatzipController(MatzipService matzipService) {
        this.matzipService = matzipService;
    }

    @PostMapping("/test")
    @ResponseBody
    public KakaoPlaceDTO saveMatZip(@RequestBody KakaoPlaceDTO data) {
        matzipService.saveMatzip((Matzip) data.getKakao(), data.getKind());
        return data;
    }

    @GetMapping("/seoul-map")
    public ModelAndView showSeoulMap() {
        return new ModelAndView("matzip/seoul-map");
    }


}

