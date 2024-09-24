package com.sparta.orderapp.service;

import com.sparta.orderapp.entity.Shop;
import com.sparta.orderapp.entity.User;
import com.sparta.orderapp.exception.NotFoundException;
import com.sparta.orderapp.repository.ShopRepository;
import com.sparta.orderapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CommonService {
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;

    /**
     * 해당 userId를 갖고 있는 User를 반환하는 메서드
     * @param userId 조회할 userId
     * @return 해당 userId를 갖고 있는 User객체
     */
    @Transactional(readOnly = true)
    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("선택한 유저는 존재하지 않습니다.")
        );
    }

    /**
     * 해당 shopId를 갖고 있는 Shop을 반환하는 메서드
     * @param shopId 조회할 shopId
     * @return 해당 shopId를 갖고 있는 shop객체
     */
    @Transactional(readOnly = true)
    public Shop findShop(Long shopId) {
        return shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("해당하는 상점이 없습니다."));
    }

    /**
     * 유저A와 유저B가 일치하는지 확인하는 메서드
     * @param userAId 유저 A
     * @param userBId 유저 B
     * @param delete 삭제 메서드인지에 대한 여부
     */
    public void confirmCreator(Long userAId, Long userBId, boolean delete) {
        if (!Objects.equals(userAId, userBId)){
            if(delete) throw new IllegalArgumentException("작성자가 아니므로 삭제가 불가능합니다.");
            else throw new IllegalArgumentException("작성자가 아니므로 수정이 불가능합니다.");
        }
    }
}
