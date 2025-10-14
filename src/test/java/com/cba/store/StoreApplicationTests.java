package com.cba.store;

import com.cba.store.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StoreApplicationTests {
    @Value("${spring.application.name}")
    String name;
    @Value("${datasets.ccdp_dataset_name}")
    String datasetName;

    @Test
    void contextLoads() {
        System.out.println(name);
        System.out.println(datasetName);
    }

    @Test
    void testUser() {
        var user = User.builder()
                .name("John")
                .password("1234")
                .id(1L)
                .email("bob@myhost.com")
                .build();

        System.out.printf("User: %s%n", user);
    }

}
