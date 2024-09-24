package com.sparta.orderapp.config;

import com.sparta.orderapp.TestUtil;
import com.sparta.orderapp.entity.User;
import org.hibernate.cfg.Environment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class JwtUtilTest {
    @InjectMocks
    private JwtUtil jwtUtil;

    @Mock
    private Environment env;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(jwtUtil, "secretKey", "123456789123456789123456789123456789123456789123");

        jwtUtil.init();
    }

    @Test
    public void 토큰_생성에_성공한다() {
        User user = TestUtil.getUser();
        String result = jwtUtil.createToken(user);

        assertNotNull(result);
    }

    @Test
    public void 잘못된_토큰이_들어온_경우_NPE를_반환한다() {
        User user = TestUtil.getUser();
        String token = "token";

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            jwtUtil.substringToken(token);
        });

        // then
        assertEquals("Not Found Token", exception.getMessage());
    }

    @Test
    public void 정상적인_토큰이_들어온_경우_문자열_분리에_성공한다() {
        User user = TestUtil.getUser();
        String token = jwtUtil.createToken(user);
        String subStringToken = jwtUtil.substringToken(token);

        assertEquals(token.substring(7), subStringToken);
    }

    @Test
    public void 정상적인_토큰이_들어온_경우_True를_반환한다() {
        User user = TestUtil.getUser();
        String token = jwtUtil.createToken(user);
        String subStringToken = jwtUtil.substringToken(token);

        boolean validToken = jwtUtil.validateToken(subStringToken);

        assertTrue(validToken);
    }
}
