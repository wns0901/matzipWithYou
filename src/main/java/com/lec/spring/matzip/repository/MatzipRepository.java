package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.DTO.DetailMatzipDTO;
import com.lec.spring.matzip.domain.Matzip;

import java.util.List;

public interface MatzipRepository {
    boolean save(Matzip matzip);

    List<Matzip> findAll();

    Matzip findByName(String name);

    Matzip findById(Long id);

    int deleteById(Long id);

    List<String> listTagName(Long id);

    List<String> listKindName(Long id);

    int updateFoodKind(Long matzipId, Long foodKindId);

    DetailMatzipDTO findDetailMatzipByMatzipIdWithFriendId(Long matzipId, Long friendId);
}
