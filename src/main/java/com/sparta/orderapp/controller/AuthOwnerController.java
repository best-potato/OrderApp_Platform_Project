package com.sparta.orderapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.orderapp.service.KakaoService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class AuthOwnerController {

    private final KakaoService kakaoService;

    @GetMapping("/owners/kakao")
    public ResponseEntity<Object> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        String token = kakaoService.kakaoOwnerLogin(code);

        return ResponseEntity.status(HttpStatus.OK).header("Authorization",token).build();
    }
}