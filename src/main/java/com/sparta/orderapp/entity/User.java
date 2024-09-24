package com.sparta.orderapp.entity;

import com.sparta.orderapp.dto.sign.SignupRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class User extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = false, length = 255)
    private String email;
    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 100)
    private UserStatusEnum user_status = UserStatusEnum.ABLE;

    @Column(nullable = true)
    private Long kakaoId;

    @Column(unique = true,length = 100)
    private String name;
    @Column(length = 100)
    private String userRole;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Orders> orders = new ArrayList<>();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.PERSIST)
    private List<Shop> shops = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Review> reviews = new ArrayList<>();


    public User(SignupRequestDto requestDto, String password) {
        this.email = requestDto.getEmail();
        this.password = password;
        this.name = requestDto.getName();
        this.userRole = requestDto.getRole();
    }

    public User(String email, String password, String name, String userRole) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.userRole = userRole;
    }

    public User(Long id){
        this.id = id;
    }

    public void updatePassword(String password){
        this.password = password;
    }
    public void update() {
        this.user_status = UserStatusEnum.DISABLE;
    }
}
