package com.lec.spring.matzip.controller;

import com.lec.spring.matzip.domain.KakaoPlaceDTO;
import com.lec.spring.matzip.domain.Matzip;
import com.lec.spring.matzip.service.MatzipService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.http.HttpRequest;

@RestController
@RequestMapping("/matzip")
public class MatzipController {

    private final MatzipService matzipService;

    public MatzipController(MatzipService matzipService) {
        this.matzipService = matzipService;
    }

    @PostMapping("/test")
    public KakaoPlaceDTO saveMatZip(@RequestBody KakaoPlaceDTO data) {
        matzipService.saveMatzip((Matzip) data.getKakao(), data.getKind());
        return data;
    }

}
