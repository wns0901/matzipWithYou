package com.lec.spring.matzip.domain.DTO;

import com.lec.spring.matzip.domain.Matzip;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MatzipListDataDTO extends Matzip {
    private String visibility;
}
