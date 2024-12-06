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
    private MyMatzip MyMatzip;
    private String name;
    private String imgUrl;
    private String kindName;
    private String tagName;
}
