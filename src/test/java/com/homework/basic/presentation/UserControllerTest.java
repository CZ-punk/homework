package com.homework.basic.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.basic.domain.repository.UserRepository;
import com.homework.basic.presentation.request.LoginDto;
import com.homework.basic.presentation.request.SignupDto;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String accessToken;

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
    @Order(1)
    @DisplayName("회원가입")
    public void signup() throws Exception {
        //given
        SignupDto request = new SignupDto("username", "password", "01012345678");

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/user/signup")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        resultActions
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @DisplayName("로그인")
    public void sign() throws Exception {
        //given
        LoginDto request = new LoginDto("username", "password");

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/user/sign")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        resultActions
                .andExpect(status().isOk());
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();

        accessToken = JsonPath.read(responseBody, "$.data");
        System.out.println("responseBody = " + responseBody);
        System.out.println("accessToken = " + accessToken);
    }

    @Test
    @Order(3)
    @DisplayName("룰 체크 - ROLE_USER")
    public void RoleCheck_USER() throws Exception {
        //given
        sign();

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/user/user")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());

        //then
        resultActions
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void RoleCheck_ADMIN() throws Exception {
        //given
        sign();

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/user/admin")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
        //then
        resultActions
                .andExpect(status().isForbidden());
    }

}
