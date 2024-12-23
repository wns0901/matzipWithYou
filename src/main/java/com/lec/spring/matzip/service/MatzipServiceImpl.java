package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.DTO.DetailMatzipDTO;
import com.lec.spring.matzip.domain.DTO.MatzipDTO;
import com.lec.spring.matzip.domain.FoodKind;
import com.lec.spring.matzip.domain.Matzip;
import com.lec.spring.matzip.repository.FoodKindRepository;
import com.lec.spring.matzip.repository.MatzipRepository;
import org.apache.ibatis.session.SqlSession;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ResponseEntity<Map<String, Object>> saveMatzip(Matzip matzip) {
        Matzip duplicateCheck = matzipRepository.findByName(matzip.getName());
        if (duplicateCheck != null) {
            System.out.println("#".repeat(30));
            System.out.println(duplicateCheck);
            return ResponseEntity.ok(Map.of(
                    "status", "SUCCESS",
                    "data", duplicateCheck
            ));
        }

        String kakaoImgUrl = getImgUrlFromKakao(matzip.getKakaoMapUrl());

        matzip.setImgUrl(kakaoImgUrl);

        String gu = matzip.getAddress().split(" ")[1];
        matzip.setGu(gu);

        boolean result = matzipRepository.save(matzip);
        matzip = new Matzip((MatzipDTO) matzip);
        if (result) {
            return ResponseEntity.ok(Map.of(
                    "status", "SUCCESS",
                    "data", matzip
            ));
        } else {
            return ResponseEntity.ok(Map.of(
                    "status", "FAIL",
                    "msg", "음식점 로딩에 실패했습니다."
            ));
        }
    }

    @Override
    public String getImgUrlFromKakao(String kakaoPageUrl) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("disable-infobars"); // 정보 표시 비활성화
        options.addArguments("--disable-gpu");
        options.addArguments("window-size=1920,1080"); // 창 크기 설정
        options.setBinary("/usr/bin/google-chrome");
        WebDriver driver = new ChromeDriver(options);
        driver.get(kakaoPageUrl);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        WebElement doc;
        try {
            doc = driver.findElement(By.cssSelector(".bg_present"));
        } catch (Exception e) {
            return "/IMG/defaultStoreImg.png";
        }
        String str = doc.getAttribute("style");
        driver.quit();
        String url = str.substring(str.indexOf("url(\"") + 5, str.indexOf("\")"));

        return url;
    }

    @Override
    public Matzip getMatzipById(Long id) {
        return matzipRepository.findById(id);
    }

    @Override
    public ResponseEntity<DetailMatzipDTO> getDetailMatzip(Long matzipId, Long friendId) {
        return ResponseEntity.ok(matzipRepository.findDetailMatzipByMatzipIdWithFriendId(matzipId, friendId));
    }

    @Override

    public List<String> listTagName(Long id) {
        return matzipRepository.listTagName(id);
    }

    @Override
    public List<String> listKindName(Long id) {
        return matzipRepository.listKindName(id);
    }


    @Override
    public List<Matzip> getAllMatzips() {
        return matzipRepository.findAll();
    }

    @Override
    public int deleteById(Long id) {
        return matzipRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateMatzipFoodKind(Long matzipId, Long foodKindId) {
        Matzip matzip = matzipRepository.findById(matzipId);
        if (matzip == null) {
            throw new IllegalArgumentException("맛집을 찾을 수 없습니다.");
        }

        // 이미 foodKind가 설정되어 있는지 확인
        if (matzip.getKindId() != null) {
            throw new IllegalStateException("이미 음식 종류가 설정되어 있는 맛집입니다.");
        }

        // foodKind 존재 여부 확인
        FoodKind foodKind = foodKindRepository.findByKindId(foodKindId);
        if (foodKind == null) {
            throw new IllegalArgumentException("존재하지 않는 음식 종류입니다.");
        }

        int updated = matzipRepository.updateFoodKind(matzipId, foodKindId);
        if (updated != 1) {
            throw new RuntimeException("음식 종류 업데이트에 실패했습니다.");
        }
    }
}

