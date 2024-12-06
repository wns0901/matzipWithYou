package com.lec.spring.matzip.controller;


import com.lec.spring.matzip.service.MyMatzipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/myMatzip")
public class MyMatzipController {

    private final MyMatzipService myMatzipService;


    @Autowired
    public MyMatzipController(MyMatzipService myMatzipService) {
        this.myMatzipService = myMatzipService;
    }


    @GetMapping("/list/{id}")
    public String list(
            @PathVariable Long id,
            @RequestParam String query,
            @RequestParam(required = false) String kindName,
            @RequestParam(required = false) List<String> tagName,
            Model model) {

        model.addAttribute("findList",myMatzipService.findSeries(id, query, kindName, tagName));
        model.addAttribute("allList",myMatzipService.myMatzipListAll());

        return "/myMatzip/list";

    }


//    @PostMapping("/list")
//    public String visiUpdateList(Long id, String visibility, Model model) {
//         model.addAttribute("update", myMatzipService.myMatzipvisibilityUpdate(id, visibility));
//        return "/myMatzip/listOK";
//    }
//
//    @PostMapping("/list")
//    public String listDelete(Long id,Model model) {
//        model.addAttribute("delete", myMatzipService.myMatzipDelete(id));
//        return "/myMatzip/listOK";
//    }
}
