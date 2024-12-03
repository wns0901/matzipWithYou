package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.Matzip;
import com.lec.spring.matzip.repository.MatzipRepository;
import org.apache.ibatis.session.SqlSession;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Duration;

@Service
public class MatzipServiceImpl implements MatzipService {
    private final MatzipRepository matzipRepository;

    public MatzipServiceImpl(SqlSession sqlSession) {
        this.matzipRepository = sqlSession.getMapper(MatzipRepository.class);
    }


    @Override
    public int saveMatzip(Matzip matzip, String kind) {
        String kakaoPageUrl = matzip.getKakaoMapUrl();
        String kakaoImgUrl = getImgUrlFromKakao(kakaoPageUrl);
        matzip.setImgUrl(kakaoImgUrl);
        matzipRepository.save(matzip);
        System.out.println(matzip);
        return 0;
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
    public int deleteMatzip(Long id) {
        return 0;
    }

    @Override
    public int findById(Long id) {
        return 0;
    }
}
