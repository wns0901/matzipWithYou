package com.lec.spring.matzip.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TotalMatzipListDataDTO {
    private String visibility;
    private List<Long> memberIds;
    private Long matzipId;
    private String name;
    private String address;
    private Double lat;
    private Double lng;
    private String imgUrl;
    private String regdate;

    public TotalMatzipListDataDTO(MatzipListDataDTO other) {
        memberIds = new ArrayList<>(List.of(other.getMemberId()));
        visibility = other.getVisibility();
        matzipId = other.getMatzipId();
        name = other.getName();
        address = other.getAddress();
        lat = other.getLat();
        lng = other.getLng();
        imgUrl = other.getImgUrl();
        regdate = other.getRegdate();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof MatzipListDataDTO that)) return false;
        return Objects.equals(matzipId, that.getMatzipId());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
