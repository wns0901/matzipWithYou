package com.lec.spring.matzip.domain.DTO;

import com.lec.spring.matzip.domain.LatLng;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailMapDataDTO {
    private Long memberId;
    private String gu;
    private List<FriendDataWithMatzipDTO> friendList;
    private List<TotalMatzipListDataDTO> totalMatzipList;
    private List<Long> wishList;
    private LatLng centerLatLng;
}
