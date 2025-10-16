package com.cba.store;

import com.cba.store.entities.Address;
import com.cba.store.entities.Profile;
import com.cba.store.entities.Tag;
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
        System.out.println("***************** TESTING app values ******************");
        System.out.println(name);
        System.out.println(datasetName);
        System.out.println("***************** DONE TESTING app values ******************");
    }

    @Test
    void testUser() {
        var user = User.builder()
                .name("John")
                .password("1234")
                .id(1L)
                .email("bob@myhost.com")
                .build();
        var address = Address.builder()
                .Id(1L)
                .street("123 Main St")
                .city("Berlin")
                .state("Nike")
                .zip("12345")
                .build();
        user.addAddress(address);


        System.out.println("***************** TESTING USER ******************");
        System.out.printf("User: %s%n", user);
        System.out.println("********** Done testing USER ***************");
    }

    @Test
    void testAddress() {
        var address = Address.builder()
                .Id(1L)
                .street("123 Main St")
                .city("Berlin")
                .state("Nike")
                .zip("12345")
                .build();
        System.out.println("***************** TESTING ADDRESS ******************");
        System.out.printf("Address: %s%n", address);
        System.out.println("********** Done testing ADDRESS ***************");
    }


    @Test
    void tesTag() {
        var tag = Tag.builder()
                .id(1L)
                .name("developer")
                .build();
        System.out.println("***************** TESTING Tag ******************");
        System.out.printf("Tag: %s%n", tag);
        System.out.println("********** Done testing Tag ***************");
    }

    @Test
    void testUserTag() {
        var user = User.builder()
                .name("John")
                .password("1234")
                .id(1L)
                .email("bob@myhost.com")
                .build();
        var tag = new Tag("developer");
        user.getTags().add(tag);
        System.out.println("***************** TESTING USER TAG ******************");


    }

    @Test
    void testUserProfile() {
        var user = User.builder()
                .name("John")
                .password("1234")
                .id(1L)
                .email("bob@myhost.com")
                .build();

        var profile = Profile.builder()
                        .bio("bio")
                                .build();
        profile.setUser(user);
        user.setProfile(profile);


        System.out.println("***************** TESTING PROFILE ******************");
        System.out.printf("Tag: %s%n", user);
        System.out.println("********** Done testing PROFILE ***************");
    }

}
