package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.UserMatzipTagStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserMatzipTagStatusService {


//    // 저장
   void save(UserMatzipTagStatus userMatzipTagStatus);

//    //조회
   Optional<List<UserMatzipTagStatus>> findTagByIdAndMember(Long memberId, Long myMatzipId);




}
