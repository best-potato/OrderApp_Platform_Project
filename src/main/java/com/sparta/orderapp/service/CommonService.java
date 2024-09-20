package com.sparta.orderapp.service;

import com.sparta.orderapp.entity.Shop;
import com.sparta.orderapp.entity.User;
import com.sparta.orderapp.exception.NotFoundException;
import com.sparta.orderapp.repository.ShopRepository;
import com.sparta.orderapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CommonService {
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;

    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("선택한 유저는 존재하지 않습니다.")
        );
    }

    public Shop findShop(Long shopId) {
        return shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("해당하는 상점이 없습니다."));
    }

    public void confirmCreator(Long userAId, Long userBId, boolean delete) {
        if (!Objects.equals(userAId, userBId)){
            if(delete) throw new IllegalArgumentException("작성자가 아니므로 삭제가 불가능합니다.");
            else throw new IllegalArgumentException("작성자가 아니므로 수정이 불가능합니다.");
        }
    }
}
