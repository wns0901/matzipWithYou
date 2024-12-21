package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.DTO.*;
import com.lec.spring.matzip.domain.MyMatzip;
import com.lec.spring.member.domain.FriendDetailsDTO;


import java.util.List;

public interface MyMatzipRepository {
    List<MyMatzipDTO> findAll(Long id);

    int listCntByMemberId(Long id);

    boolean save(SaveMyMatzipDTO myMatzip);

    boolean updateMyMatzipVisibility(Long id, String visibility);

    boolean deleteMyMatzip(Long id);

    List<SeoulMapSqlDataDTO> findSeoulMapData(Long id);

    List<FriendDataWithMatzipDTO> findGuMapData(Long id, String gu);

    List<FriendDetailsDTO> findHiddenFriendDetails(Long memberId, Long matzipId);

    boolean updateIntimacyByFriendsIds(List<Long> friendIds, int intimacy, Long memberId);
}
