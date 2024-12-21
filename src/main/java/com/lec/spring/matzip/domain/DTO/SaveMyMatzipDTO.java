package com.lec.spring.matzip.domain.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lec.spring.matzip.domain.MyMatzip;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaveMyMatzipDTO extends MyMatzip {
    private List<Long> tagIds;
    private Long kindId;
}
