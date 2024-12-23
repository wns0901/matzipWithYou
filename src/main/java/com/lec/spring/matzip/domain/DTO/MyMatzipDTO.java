package com.lec.spring.matzip.domain.DTO;


import com.lec.spring.matzip.domain.MyMatzip;
import com.lec.spring.matzip.domain.Tag;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MyMatzipDTO extends MyMatzip {
    private String name;
    private String imgUrl;
    private String kindName;
    private String address;
    private List<Tag> tagList;
}
