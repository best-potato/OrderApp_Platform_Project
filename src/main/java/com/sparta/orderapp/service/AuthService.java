package com.sparta.orderapp.service;

import com.sparta.orderapp.config.JwtUtil;
import com.sparta.orderapp.config.PasswordEncoder;
import com.sparta.orderapp.dto.sign.DeleteAccountRequestDto;
import com.sparta.orderapp.dto.sign.SignupRequestDto;
import com.sparta.orderapp.dto.user.AuthUser;
import com.sparta.orderapp.dto.user.LoginRequestDto;
import com.sparta.orderapp.entity.User;
import com.sparta.orderapp.entity.UserStatusEnum;
import com.sparta.orderapp.exception.NoSignedUserException;
import com.sparta.orderapp.exception.WrongPasswordException;
import com.sparta.orderapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BlacklistTokenService blacklistTokenService;
    private final JwtUtil jwtUtil;

    /**
     * 유저가 기입한 정보를 토대로 토큰을 발행하는 메서드
     * @param requestDto 유저 기입 정보
     * @return JWT 토큰
     */
    public String login(LoginRequestDto requestDto) {
        //입력된 이메일로 유저찾기
        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(() -> new NoSignedUserException());

        //탈퇴유저 로그인 방지
        if(user.getUser_status().equals(UserStatusEnum.DISABLE)) {
            throw new NoSignedUserException();
        }

        //비밀번호 일치하는지 확인
        if(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())){
            throw new WrongPasswordException();
        }

        //존재하는 유저가 비밀번호를 알맞게 입력시 JWT토큰반환
        return jwtUtil.createToken(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUserRole()
        );
    }

    /**
     * 회원가입하는 메서드
     * @param signupRequestDto 회원가입과 관련된 기입 정보
     */
    public void signUp(SignupRequestDto signupRequestDto) {
        Optional<User> foundUser = userRepository.findByEmail(signupRequestDto.getEmail());
        // 이미 존재하는 email인 경우
        if (foundUser.isPresent()) {
            log.error("이미 존재하는 ID입니다");
        }

        // 비밀번호 암호화
        String encodePassword = passwordEncoder.encode(signupRequestDto.getPassword());
        User user = new User(signupRequestDto, encodePassword);

        // DB에 저장
        userRepository.save(user);
    }

    /**
     * Id로 유저 정보를 찾는 메서드
     * @param id 찾고자 하는 유저 Id
     * @return 검색 결과
     */
    @Transactional(readOnly = true)
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);

        return user.orElseThrow(NoSignedUserException::new);
    }

    /**
     * Email로 유저 정보를 찾는 메서드
     * @param email 찾고자 하는 유저 email
     * @return 검색 결과
     */
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        return user.orElseThrow(NoSignedUserException::new);
    }

    /**
     * 유저를 삭제하고 BlacklistToken에 데이터를 추가하는 메서드
     * @param token jwt토큰 정보
     * @param user 현재 토큰에 저장된 유저 정보
     * @param requestDto 유저가 기입한 비밀번호
     */
    @Transactional
    public void deleteAccount(String token, AuthUser user, DeleteAccountRequestDto requestDto) {
        User userToDelete = userRepository.findById(user.getId()).orElseThrow(NoSignedUserException::new);

        // 비밀번호가 틀린 경우 에러
        if (passwordEncoder.matches(requestDto.getPassword(), userToDelete.getPassword())) {
            throw new WrongPasswordException();
        }

        userToDelete.update();
        blacklistTokenService.addBlacklistToken(token);
    }
}

