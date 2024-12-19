package com.lec.spring.matzip.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishList {
    private Long memberId;
    private Long matzipId;
    private LocalDateTime regdate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WishList that)) return false;
        return memberId.equals(that.memberId) && matzipId.equals(that.matzipId);
    }
}
