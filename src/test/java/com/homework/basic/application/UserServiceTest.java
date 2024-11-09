package com.homework.basic.application;

import com.homework.basic.application.service.UserService;
import com.homework.basic.domain.entity.User;
import com.homework.basic.domain.entity.UserRole;
import com.homework.basic.domain.repository.UserRepository;
import com.homework.basic.presentation.error.UserException;
import com.homework.basic.presentation.request.LoginDto;
import com.homework.basic.presentation.request.SignupDto;
import com.homework.basic.presentation.response.ResponseDto;
import com.homework.basic.presentation.response.UserInfoDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {


    @Autowired
    private UserService userService;
    private static UserRepository staticUserRepository;

    @BeforeAll
    public static void setUp(@Autowired UserRepository userRepository) {
        staticUserRepository = userRepository;
    }

    @AfterAll
    public static void tearDown() {
        if (staticUserRepository != null) {
            staticUserRepository.deleteAll(); // 모든 데이터 삭제
        }
    }

    @Test
    @DisplayName("회원가입")
    public void signup() {
        //given
        SignupDto requestDto = new SignupDto("user", "password", "01012345678");

        //when
        ResponseDto<?> responseDto = userService.signup(requestDto);

        //then
        assertThat(responseDto.getError()).isEqualTo(null);
        assertThat(responseDto.getData()).isEqualTo("success signup.");
    }

    @Test
    @DisplayName("로그인")
    public void sign() {
        //given
        LoginDto requestDto = new LoginDto("user", "password");

        //when
        ResponseDto<?> responseDto = userService.sign(requestDto);

        //then
        assertThat(responseDto.getError()).isEqualTo(null);
        assertThat(responseDto.getData()).isInstanceOf(String.class);
    }

    @Test
    @DisplayName("룰 체크 - ROLE_USER")
    public void RoleCheck_USER() {
        //given
        User user = staticUserRepository.findByUsername("user").orElseThrow(() -> new UserException(HttpStatus.UNAUTHORIZED, "unauthorized."));

        //when
        ResponseDto<?> responseDto = userService.accessUser(user);

        //then
        assertThat(responseDto.getError()).isEqualTo(null);
        assertThat(responseDto.getData()).isInstanceOf(UserInfoDto.class);

    }

    @Test
    @DisplayName("룰 체크 - ROLE_ADMIN")
    public void RoleCheck_ADMIN() {
        //given
        User user = new User(99L, "admin", "password", "01012345678", UserRole.ADMIN);

        //when
        ResponseDto<?> responseDto = userService.accessAdmin(user);

        //then
        assertThat(responseDto.getError()).isEqualTo(null);
        assertThat(responseDto.getData()).isInstanceOf(UserInfoDto.class);

    }


}
