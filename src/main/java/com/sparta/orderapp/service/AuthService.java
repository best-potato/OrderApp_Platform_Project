package com.sparta.orderapp.service;

import com.sparta.orderapp.config.JwtUtil;
import com.sparta.orderapp.config.PasswordEncoder;
import com.sparta.orderapp.dto.user.LoginRequestDto;
import com.sparta.orderapp.entity.User;
import com.sparta.orderapp.entity.UserStatusEnum;
import com.sparta.orderapp.exception.NoSignedUserException;
import com.sparta.orderapp.exception.WrongPasswordException;
import com.sparta.orderapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String login(LoginRequestDto requestDto) {

        //입력된 이메일로 유저찾기
        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(() -> new NoSignedUserException());

        //비밀번호 일치하는지 확인
        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())){
            throw new WrongPasswordException();
        }

        //탈퇴유저 로그인 방지
        if(user.getUser_status().equals(UserStatusEnum.ABLE)) {

            //존재하는 유저가 비밀번호를 알맞게 입력시 JWT토큰반환
            return jwtUtil.createToken(
                    user.getId(),
                    user.getName(),
                    user.getEmail()
            );
        }
        else throw new NoSignedUserException();
    }
}

