package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.FoodKind;
import com.lec.spring.matzip.domain.Matzip;
import com.lec.spring.matzip.domain.Tag;
import com.lec.spring.matzip.repository.FoodKindRepository;
import com.lec.spring.matzip.repository.MatzipRepository;
import org.apache.ibatis.session.SqlSession;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class MatzipServiceImpl implements MatzipService {
    private final MatzipRepository matzipRepository;
    private final FoodKindRepository foodKindRepository;

    public MatzipServiceImpl(SqlSession sqlSession) {
        this.matzipRepository = sqlSession.getMapper(MatzipRepository.class);
        this.foodKindRepository = sqlSession.getMapper(FoodKindRepository.class);
    }


    @Override
    @Transactional
    public ResponseEntity<Map<String, String>> saveMatzip(Matzip matzip, String kind) {
        String kakaoImgUrl = getImgUrlFromKakao(matzip.getKakaoMapUrl());

        FoodKind foodKind = foodKindRepository.findByKindName(kind);

        matzip.setImgUrl(kakaoImgUrl);
        matzip.setKindId(foodKind.getId());

        String gu = matzip.getAddress().split(" ")[1];
        matzip.setGu(gu);

        boolean result = matzipRepository.save(matzip);

        if (result) {
            return ResponseEntity.ok(Map.of(
                    "status","SUCCESS"
            ));
        } else {
            return ResponseEntity.ok(Map.of(
                    "status","FAIL",
                    "msg","맛집 저장에 실패했습니다."
            ));
        }
    }

    @Override
    public String getImgUrlFromKakao(String kakaoPageUrl) {
        WebDriver driver = new ChromeDriver();
        driver.get(kakaoPageUrl);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        WebElement doc = driver.findElement(By.cssSelector(".bg_present"));
        String str = doc.getAttribute("style");
        driver.quit();
        String url = str.substring(str.indexOf("url(\"") + 5, str.indexOf("\")"));

        return url;
    }

    @Override
    public Matzip getMatzipById(Long id, Model model) {
        return matzipRepository.findById(id);
    }

    @Override

    public List<String> listTagName(Long id) {
        return matzipRepository.listTagName(id);
    }

    @Override
    public List<String> listKindName(Long id) {
       return matzipRepository.listKindName(id);
    }


    public List<Matzip> getAllMatzips() {
        return matzipRepository.findAll();
    }

    @Override
    public int deleteById(Long id) {
        return matzipRepository.deleteById(id);
    }
}

