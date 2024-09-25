package com.sparta.orderapp.repository;

import com.sparta.orderapp.entity.BlackListToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistTokenRepository extends JpaRepository<BlackListToken, String> {
}
