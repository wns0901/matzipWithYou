package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.UserMatzipTagStatus;
import com.lec.spring.matzip.repository.TagRepository;
import com.lec.spring.matzip.repository.UserMatzipTagStatusRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMatzipTagStatusServiceImpl implements UserMatzipTagStatusService {
    private final UserMatzipTagStatusRepository userMatzipTagStatusRepository;
    private final TagRepository tagRepository;

    @Autowired
    public UserMatzipTagStatusServiceImpl(SqlSession sqlSession) {
        this.userMatzipTagStatusRepository = sqlSession.getMapper(UserMatzipTagStatusRepository.class);
        this.tagRepository = sqlSession.getMapper(TagRepository.class);
    }

    // 히든 맛집의 태그조회
    @Override
    public List<Long> listHiddenMatzipTagIds(Long myMatzipId) {
        // myMatzipId에 해당하는 히든 맛집의 태그 상태를 조회합니다.
        List<UserMatzipTagStatus> hiddenTags = userMatzipTagStatusRepository.listHiddenMatzipTagIds(myMatzipId);

        // UserMatzipTagStatus 객체에서 ID만 추출하여 리스트로 변환
        List<Long> tagIds = hiddenTags.stream()
                .map(UserMatzipTagStatus::getTagId)
                .collect(Collectors.toList());

        //System.out.println("히든 맛집의 태그 ID: " + tagIds);

        return tagIds;

    }

    @Override
    public String listKindName(Long myMatzipId) {
        String result = userMatzipTagStatusRepository.listKindName(myMatzipId).toString();
        return result;

    }

    @Override
    public List<UserMatzipTagStatus> finddeleteDuplicateMyMatzipId() {
        List<UserMatzipTagStatus> duplicateId = userMatzipTagStatusRepository.finddeleteDuplicateMyMatzipId();
        System.out.println("################duplicateId is " + duplicateId);
        return duplicateId;
    }




    // 오픈된 태그(userMatzipTagStatus : 구매된 태그) 개수와
    // 미공개된 태그(구매하지 않은 태그: 히든맛집인데 구매되지 않은 태그\]]])의 개수를 리턴해 주는 메소드
    // (리턴타입 List(int)(index 0: open tag, index 1: close tag))
    //1. 히든 맛집의 태그 리스트 불러오기
    //(5,6,1) -> 히든 맛집
//         태그 아이디 : (2,7,9), (5)번 맛집

    //2. 제가 만든 메소드에 찾은 태드 id 보내주기 (list<long>
    // 해당 맛집의 태그 중 매개변수로 전해준 제외한 태그드를 전해줌 return (2, 7)
//        List<Tag> tags = tagRepository.findNonMatchingTags(List.of(7L), 5L); // 이미 구매한 태그 넣어주기


    @Override
    public List<UserMatzipTagStatus> listWholeHiddenList() {
        List<UserMatzipTagStatus> result = userMatzipTagStatusRepository.listWholeHiddenList();
        System.out.println("result:" + result);
        return result;
    }

    @Override
    public List<UserMatzipTagStatus> deleteDuplicateMyMatzipId() {
        // 숨겨진 맛집 리스트
        List<UserMatzipTagStatus> hiddenList = userMatzipTagStatusRepository.listWholeHiddenList();

        // 중복된 맛집 리스트
        List<UserMatzipTagStatus> duplicateList = userMatzipTagStatusRepository.finddeleteDuplicateMyMatzipId();

        // 중복 제거 로직
        hiddenList.removeIf(duplicateList::contains);

        System.out.println("result (after removing duplicates): " + hiddenList);
        return hiddenList;
    }

    @Override
    public List<UserMatzipTagStatus> userMatzipTagStatus() {
        return userMatzipTagStatusRepository.userMatzipTagStatus();
    }

    @Override
    public void tagSave(UserMatzipTagStatus userMatzipTagStatus) {
        userMatzipTagStatusRepository.tagSave(userMatzipTagStatus);
    }// end tagSave

    public UserMatzipTagStatus findTagByMemberIdAndMatzipId(Long memberId, Long myMatzipId) {
        return userMatzipTagStatusRepository.findTagByMemberIdAndMatzipId(memberId, myMatzipId);
    }// end findTagByMemberIdAndMatzipId

    @Override
    public List<UserMatzipTagStatus> findTagsAndMatzipIdByMember(Long memberId) {
        return userMatzipTagStatusRepository.findTagsAndMatzipIdByMember(memberId);
    }// end findTAgsAndMatzipIdByMember

    @Override
    public List<UserMatzipTagStatus> findMemberAndTagByMatzipId(Long myMatzipId) {
        return userMatzipTagStatusRepository.findMemberAndTagByMatzipId(myMatzipId);
    }// end findMemberAndTagByMatzipId



}// userMatzipTagStatusImple
