package com.homework.basic.infrastructure.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {

    Server server = new Server(); // API 서버 설정
    server.setUrl("http://localhost:8080/swagger");

    Server prodServer = new Server(); // 운영서버에 따로 띄우기 위해 서버를 추가할 수 있다.
    server.setUrl("http://ec2-3-38-93-48.ap-northeast-2.compute.amazonaws.com:8080/swagger");

    Info info = new Info().title("USER API").version("v1.0.0").description("USER API");

    return new OpenAPI().info(info).servers(List.of(server, prodServer));
  }
}
