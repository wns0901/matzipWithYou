package com.lec.spring.matzip.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyMatzipDTO {
    private MyMatzip             myMatzip;
    private String imgUrl;
    private String name;
    private String kindName;
    private List<String> tagName;
}
