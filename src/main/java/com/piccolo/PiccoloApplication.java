package com.piccolo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Piccolo - 帮你做选择，投票更有趣 🎲
 */
@SpringBootApplication
@MapperScan("com.piccolo.mapper")
public class PiccoloApplication {

    public static void main(String[] args) {
        SpringApplication.run(PiccoloApplication.class, args);
    }
}
