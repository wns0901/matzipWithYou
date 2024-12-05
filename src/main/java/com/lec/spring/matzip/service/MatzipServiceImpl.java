package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.FoodKind;
import com.lec.spring.matzip.domain.Matzip;
import com.lec.spring.matzip.repository.FoodKindRepository;
import com.lec.spring.matzip.repository.MatzipRepository;
import org.apache.ibatis.session.SqlSession;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Duration;

@Service
public class MatzipServiceImpl implements MatzipService {
    private final MatzipRepository matzipRepository;
    private final FoodKindRepository foodKindRepository;

    public MatzipServiceImpl(SqlSession sqlSession) {
        this.matzipRepository = sqlSession.getMapper(MatzipRepository.class);
        this.foodKindRepository = sqlSession.getMapper(FoodKindRepository.class);
    }


    @Override
    public int saveMatzip(Matzip matzip, String kind) {
        String kakaoImgUrl = getImgUrlFromKakao(matzip.getKakaoMapUrl());

        FoodKind foodKind = foodKindRepository.findByKindName(kind);

        matzip.setImgUrl(kakaoImgUrl);
        matzip.setKindId(foodKind.getId());

        return matzipRepository.save(matzip);
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
}
