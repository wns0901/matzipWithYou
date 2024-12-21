package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.DTO.*;
import com.lec.spring.matzip.domain.GuCenterLatLng;
import com.lec.spring.matzip.domain.MyMatzip;
import com.lec.spring.matzip.domain.WishList;
import com.lec.spring.matzip.repository.FoodKindRepository;
import com.lec.spring.matzip.repository.MyMatzipRepository;
import com.lec.spring.matzip.repository.TagRepository;
import com.lec.spring.matzip.repository.WishListRepository;
import com.lec.spring.member.domain.FriendDetailsDTO;
import com.lec.spring.member.repository.MemberRepository;
import com.lec.spring.member.repository.ProfileImgRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MyMatzipServiceImpl implements MyMatzipService {


    private final MyMatzipRepository myMatzipRepository;
    private final WishListRepository wishListRepository;
    private final MemberRepository memberRepository;
    private final TagRepository tagRepository;
    private final FoodKindRepository foodKindRepository;
    private final ProfileImgRepository profileImgRepository;

    public MyMatzipServiceImpl(SqlSession sqlSession) {
        this.myMatzipRepository = sqlSession.getMapper(MyMatzipRepository.class);
        this.wishListRepository = sqlSession.getMapper(WishListRepository.class);
        this.memberRepository = sqlSession.getMapper(MemberRepository.class);
        this.tagRepository = sqlSession.getMapper(TagRepository.class);
        this.foodKindRepository = sqlSession.getMapper(FoodKindRepository.class);
        this.profileImgRepository = sqlSession.getMapper(ProfileImgRepository.class);
    }


    @Override
    public FindingResultMyMatzipDTO findByMemberId(Long id) {
        List<MyMatzipDTO> result = myMatzipRepository.findAll(id);
        Long memberId = result.get(0).getMemberId();
        return FindingResultMyMatzipDTO.builder()
                .cnt(myMatzipRepository.listCntByMemberId(id))
                .list(result)
                .memberId(memberId)
                .profileImg(profileImgRepository.findByMemberId(memberId).getFilename())
                .allKindList(foodKindRepository.findAll())
                .allTagList(tagRepository.findAll())
                .nickname(memberRepository.findById(memberId).getNickname())
                .build();
    }

    @Override
    public ResponseEntity<Map<String, String>> updateMyMatzipVisibility(UpdateMyMatzipVisibility updateMyMatzipVisibility) {
        if (myMatzipRepository.updateMyMatzipVisibility(updateMyMatzipVisibility.getMyMatzipId(), updateMyMatzipVisibility.getVisibility())) {
            return ResponseEntity.ok(Map.of("status", "SUCCESS"));
        } else {
            return ResponseEntity.ok(Map.of(
                    "status", "FAIL",
                    "msg", "수정에 실패했습니다."
            ));
        }
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteMyMatzip(Long id) {
        if (myMatzipRepository.deleteMyMatzip(id)) {
            return ResponseEntity.ok(Map.of("status", "SUCCESS"));
        } else {
            return ResponseEntity.ok(Map.of(
                    "status", "FAIL",
                    "msg", "삭제에 실패했습니다."
            ));
        }
    }

    @Override
    public SeoulMapDataDTO findSeoulMapDataById(Long id) {
        List<SeoulMapSqlDataDTO> seoulMapDBDataDTOS = myMatzipRepository.findSeoulMapData(id);
        List<FriendDataDTO> friendDataDTOS = new ArrayList<>();
        Map<String, Integer> totalPublicGuMap = new HashMap<>();
        Map<String, Integer> totalHiddenGuMap = new HashMap<>();

        seoulMapDBDataDTOS.forEach(data -> {
            List<String> publicGu = data.getPublicGu();
            List<String> hiddenGu = data.getHiddenGu();
            publicGu.removeIf(Objects::isNull);
            hiddenGu.removeIf(Objects::isNull);

            Map<String, Integer> publicGuMap = new HashMap<>();
            Map<String, Integer> hiddenGuMap = new HashMap<>();

            publicGu.forEach(e -> {
                publicGuMap.put(e, publicGuMap.getOrDefault(e, 0) + 1);
                totalPublicGuMap.put(e, totalPublicGuMap.getOrDefault(e, 0) + 1);
            });
            hiddenGu.forEach(e -> {
                hiddenGuMap.put(e, hiddenGuMap.getOrDefault(e, 0) + 1);
                totalHiddenGuMap.put(e, totalHiddenGuMap.getOrDefault(e, 0) + 1);
            });

            friendDataDTOS.add(FriendDataDTO.builder()
                    .friendId(data.getFriendId())
                    .nickname(data.getNickname())
                    .profileImg(data.getProfileImg())
                    .publicGu(publicGuMap)
                    .hiddenGu(hiddenGuMap)
                    .build());
        });

        return SeoulMapDataDTO.builder()
                .friendData(friendDataDTOS)
                .toTalData(ToTalDataDTO.builder()
                        .publicGu(totalPublicGuMap)
                        .hiddenGu(totalHiddenGuMap)
                        .build())
                .build();
    }

    @Override
    public DetailMapDataDTO findGuMapDataById(Long id, String gu) {
        List<FriendDataWithMatzipDTO> sqlResult = myMatzipRepository.findGuMapData(id, gu);
        List<TotalMatzipListDataDTO> totalMatzipList = new ArrayList<>();
        List<WishList> wishList = wishListRepository.getWishListByMemberId(id);
        List<Long> wishIdList = wishList.stream().map(WishList::getMatzipId).toList();

        sqlResult.forEach(data -> {
            data.getMatzipList().forEach(matzip -> {
                if (matzip.getVisibility().equals("HIDDEN")) chachRandomLatLng(matzip);
                int index = totalMatzipList.indexOf(matzip);
                if (index == -1) {
                    totalMatzipList.add(new TotalMatzipListDataDTO(matzip));
                } else {
                    totalMatzipList.get(index).getMemberIds().add(matzip.getMemberId());
                    totalMatzipList.get(index).getMyMatzipIds().add(matzip.getMyMatzipId());
                }
            });

        });

        return DetailMapDataDTO.builder()
                .memberId(id)
                .gu(gu)
                .friendList(sqlResult)
                .totalMatzipList(totalMatzipList)
                .centerLatLng(new GuCenterLatLng().getGuCenterMap().get(gu))
                .wishList(wishIdList)
                .build();
    }

    private void chachRandomLatLng(MatzipListDataDTO matzip) {
        double EARTH_RADIUS = 6371000;
        double radius = 180;
        double centerLat = matzip.getLat();
        double centerLng = matzip.getLng();

        Random random = new Random();

        double distance = radius * Math.sqrt(random.nextDouble());
        double angle = 2 * Math.PI * random.nextDouble();

        double deltaLat = (distance / EARTH_RADIUS) * (180 / Math.PI);
        double deltaLng = (distance / EARTH_RADIUS) * (180 / Math.PI);

        double randomLat = centerLat + deltaLat * Math.sin(angle);
        double randomLng = centerLng + deltaLng * Math.cos(angle);

        matzip.setLat(randomLat);
        matzip.setLng(randomLng);
    }

    @Override
    public ResponseEntity<ReviewSubmitModalDTO> saveMyMatzip(SaveMyMatzipDTO saveMyMatzipDTO) {
        boolean saveResult = myMatzipRepository.save(saveMyMatzipDTO);
        if (!saveResult) return ResponseEntity.badRequest().build();

        boolean tagResult = tagRepository.saveMatzipTags(saveMyMatzipDTO.getTagIds(), saveMyMatzipDTO.getId());
        if (!tagResult) return ResponseEntity.badRequest().build();

        List<FriendDetailsDTO> hiddenFirendProfiles = myMatzipRepository.findHiddenFriendDetails(saveMyMatzipDTO.getMemberId(),saveMyMatzipDTO.getMatzipId());

        boolean isOpenHiddenMatzip = !hiddenFirendProfiles.isEmpty();

        int point = isOpenHiddenMatzip ? 100 : 10;

        memberRepository.updatePoint(saveMyMatzipDTO.getMemberId(), point);

        int intimacy = 0;
        if(isOpenHiddenMatzip) {
            intimacy = 100;
            List<Long> friendIds = hiddenFirendProfiles.stream().map(FriendDetailsDTO::getFriendId).toList();
            myMatzipRepository.updateIntimacyByFriendsIds(friendIds, intimacy, saveMyMatzipDTO.getMatzipId());
        }

        FriendDetailsDTO topFriend = hiddenFirendProfiles.get(0);

        int friendCnt = hiddenFirendProfiles.size();

        return ResponseEntity.ok(ReviewSubmitModalDTO.builder()
                        .friendCount(friendCnt)
                        .hiddenFriends(hiddenFirendProfiles)
                        .intimacyIncrease(intimacy)
                        .rewardPoints(point)
                        .topFriendName(topFriend.getNickname())
                .build());
    }
}
