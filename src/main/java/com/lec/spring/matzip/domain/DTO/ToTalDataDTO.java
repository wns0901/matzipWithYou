package com.lec.spring.matzip.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ToTalDataDTO {
    private Map<String, Integer> publicGu;
    private Map<String, Integer> hiddenGu;
}
