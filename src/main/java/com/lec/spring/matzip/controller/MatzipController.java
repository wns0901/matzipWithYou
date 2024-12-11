package com.lec.spring.matzip.controller;

import com.lec.spring.matzip.domain.KakaoPlaceDTO;
import com.lec.spring.matzip.domain.Matzip;
import com.lec.spring.matzip.domain.Tag;
import com.lec.spring.matzip.service.MatzipService;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;


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


    @GetMapping("/homework/{id}")
    public String getHomework(@PathVariable Long id, Model model) {

        Matzip matzip = matzipService.getMatzipById(id, model);
        // 제가 만든 뭐 이것저것...
        List<String> listTags = matzipService.listTagName(id, model);
        List<String> listKind = matzipService.listKindName(id, model);

        model.addAttribute("tagName", listTags);
        model.addAttribute("matzip", matzip);
        model.addAttribute("kindName", listKind);

        System.out.println("##########matzip" + matzip);
        System.out.println("##########taglist" + listTags);
        System.out.println("##########matzip" + listKind);


        return "matzip/detail";
    }


    @GetMapping("/seoul-map")
    public ModelAndView showSeoulMap() {
        return new ModelAndView("matzip/seoul-map");
    }



}

