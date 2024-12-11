package com.lec.spring.matzip.controller;

import com.lec.spring.matzip.domain.DTO.MatzipDTO;
import com.lec.spring.matzip.domain.Matzip;
import com.lec.spring.matzip.service.MatzipService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/matzip")
public class MatzipController {

    private final MatzipService matzipService;

    public MatzipController(MatzipService matzipService) {
        this.matzipService = matzipService;
    }

    @PostMapping("")
    public ResponseEntity<Map<String, String>> saveMatZip(@RequestBody MatzipDTO matzipDTO) {
        return matzipService.saveMatzip((Matzip) matzipDTO.getData(), matzipDTO.getKind());
    }

    @GetMapping("/homework/{id}")
    public String getHomework(@PathVariable Long id, Model model) {
        Matzip matzip = matzipService.getMatzipById(id, model);
        model.addAttribute("matzip", matzip);
        return "matzip/detail";
    }

}
