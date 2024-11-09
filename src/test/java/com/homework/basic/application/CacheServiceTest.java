package com.homework.basic.application;

import com.homework.basic.application.service.CacheService;
import com.homework.basic.domain.entity.User;
import com.homework.basic.domain.entity.UserRole;
import com.homework.basic.domain.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CacheServiceTest {

    @Autowired
    private CacheService cacheService;

    private static UserRepository staticUserRepository;

    @BeforeAll
    public static void setUp(@Autowired UserRepository userRepository) {
        userRepository.save(new User(1L, "user", "encodedPassword", "01012345678", UserRole.USER));
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
    @DisplayName("getUser - cache miss")
    public void cache_miss() {
        System.out.println("query 확인용 miss - start");
        User user = cacheService.getUser("user");

        assertThat(user.getUsername()).isEqualTo("user");
        assertThat(user.getPassword()).isEqualTo("encodedPassword");
        assertThat(user.getPhoneNumber()).isEqualTo("01012345678");
        assertThat(user.getUserRole()).isEqualTo(UserRole.USER);
        System.out.println("query 확인용 miss - end");
    }

    @Test
    @Order(2)
    @DisplayName("getUser - cache hit")
    public void cache_hit() {
        System.out.println("query 확인용 hit - start");
        User user = cacheService.getUser("user");

        assertThat(user.getUsername()).isEqualTo("user");
        assertThat(user.getPassword()).isEqualTo("encodedPassword");
        assertThat(user.getPhoneNumber()).isEqualTo("01012345678");
        assertThat(user.getUserRole()).isEqualTo(UserRole.USER);
        System.out.println("query 확인용 hit - end");
    }

    @Test
    @Order(3)
    @DisplayName("invalidateUser - cache 무효화 이후 query 확인")
    public void cache_invalidate() {
        System.out.println("query 확인용 invalidate cache - start");
        cacheService.invalidateUser("user");
        User user = cacheService.getUser("user");

        assertThat(user.getUsername()).isEqualTo("user");
        assertThat(user.getPassword()).isEqualTo("encodedPassword");
        assertThat(user.getPhoneNumber()).isEqualTo("01012345678");
        assertThat(user.getUserRole()).isEqualTo(UserRole.USER);

        System.out.println("query 확인용 invalidate cache - end");
    }

}
