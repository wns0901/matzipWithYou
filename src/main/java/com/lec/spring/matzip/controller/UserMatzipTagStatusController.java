package com.lec.spring.matzip.controller;


import com.lec.spring.matzip.domain.UserMatzipTagStatus;
import com.lec.spring.matzip.service.UserMatzipTagStatusService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/matzip")
public class UserMatzipTagStatusController {

    private final UserMatzipTagStatusService userMatzipTagStatusService;

    @Autowired
    public UserMatzipTagStatusController(UserMatzipTagStatusService userMatzipTagStatusService) {
        this.userMatzipTagStatusService = userMatzipTagStatusService;
    }

    //저장
    @CrossOrigin
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody @RequestParam UserMatzipTagStatus userMatzipTagStatus) {
        // 요청 처리 로직
        System.out.println(userMatzipTagStatus);
        return ResponseEntity.ok("저장 성공");
    }

    //조회
    @CrossOrigin
    @GetMapping("/find/{memberId}/{myMatzipId}")
    public ResponseEntity<List<UserMatzipTagStatus>> findTagByIdAndMember(
            @PathVariable @RequestParam Long memberId,
            @PathVariable @RequestParam Long myMatzipId) {
        Optional<List<UserMatzipTagStatus>> tagStatus = userMatzipTagStatusService.findTagByIdAndMember(memberId, myMatzipId);
        return tagStatus.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
        /*todo
        *  tagStatus가 값을 가지고 있을 경우 그  값을 ResposeEntitiy.ok() 메서드에 전달하여 http200OK응답을 생성함.
        *  orElseGet()... => 만약 tagStatus가 비어있다면 즉 값이 없으면 orElse..가 호출된다.
        *   이 경우 ResposnEntitiy.notFound().build() 가 실행되어 404 not found 가 호출한다.*/
    }



}// end useMatzipTagStatusController