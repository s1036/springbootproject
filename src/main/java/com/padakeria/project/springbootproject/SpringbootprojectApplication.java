package com.padakeria.project.springbootproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringbootprojectApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootprojectApplication.class, args);

        // TODO: 2020-03-06 개발서버용 DB 설정파일,인증 설정파일 말고 운영서버에서 사용할 설정파일 운영서버에 하나 만들기(운영 사양으로 ex) jpa ddl auto), rds에 ec2 인바운드 설정해놓기, 젠킨스에 배포 스크립트 작성해놓기
    }
}
