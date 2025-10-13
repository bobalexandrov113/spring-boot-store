package com.cba.store;

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

}
