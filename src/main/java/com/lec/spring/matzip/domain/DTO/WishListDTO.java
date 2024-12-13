package com.lec.spring.matzip.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishListDTO {
    Long id;
    String name;
    String imgUrl;
}
