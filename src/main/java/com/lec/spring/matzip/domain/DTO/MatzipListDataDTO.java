package com.lec.spring.matzip.domain.DTO;

import com.lec.spring.matzip.domain.Matzip;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MatzipListDataDTO {

    private String visibility;
    private Long memberId;
    private Long myMatzipId;
    private Long matzipId;
    private String name;
    private String address;
    private Double lat;
    private Double lng;
    private String imgUrl;
    private String regdate;

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof TotalMatzipListDataDTO that)) return false;
        return Objects.equals(matzipId, that.getMatzipId()) && visibility.equals(that.getVisibility());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
