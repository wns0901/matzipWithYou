package com.lec.spring.matzip.domain;

import java.util.HashMap;
import java.util.Map;

public class GuCenterLatLng {
    private Map<String, LatLng> guCenterMap;

    public GuCenterLatLng() {
        guCenterMap = new HashMap<>();
        guCenterMap.put("강동구", new LatLng(37.530126, 127.1237708));
        guCenterMap.put("송파구", new LatLng(37.5145636, 127.1059186));
        guCenterMap.put("강남구", new LatLng(37.517305, 127.047502));
        guCenterMap.put("서초구", new LatLng(37.483569, 127.032598));
        guCenterMap.put("관악구", new LatLng(37.4781549, 126.9514847));
        guCenterMap.put("동작구", new LatLng(37.51245, 126.9395));
        guCenterMap.put("영등포구", new LatLng(37.526436, 126.896004));
        guCenterMap.put("금천구", new LatLng(37.4568644, 126.8955105));
        guCenterMap.put("구로구", new LatLng(37.495472, 126.887536));
        guCenterMap.put("강서구", new LatLng(37.550937, 126.849642));
        guCenterMap.put("양천구", new LatLng(37.517016, 126.866642));
        guCenterMap.put("마포구", new LatLng(37.5663245, 126.901491));
        guCenterMap.put("서대문구", new LatLng(37.579225, 126.9368));
        guCenterMap.put("은평구", new LatLng(37.602784, 126.929164));
        guCenterMap.put("노원구", new LatLng(37.654358, 127.056473));
        guCenterMap.put("도봉구", new LatLng(37.668768, 127.047163));
        guCenterMap.put("강북구", new LatLng(37.6397819, 127.0256135));
        guCenterMap.put("성북구", new LatLng(37.5894, 127.016749));
        guCenterMap.put("중랑구", new LatLng(37.6063242, 127.0925842));
        guCenterMap.put("동대문구", new LatLng(37.574524, 127.03965));
        guCenterMap.put("광진구", new LatLng(37.538617, 127.082375));
        guCenterMap.put("성동구", new LatLng(37.563456, 127.036821));
        guCenterMap.put("용산구", new LatLng(37.532527, 126.99049));
        guCenterMap.put("중구", new LatLng(37.563843, 126.997602));
        guCenterMap.put("종로구", new LatLng(37.5735207, 126.9788345));
    }

    public Map<String, LatLng> getGuCenterMap() {
        return guCenterMap;
    }
}
