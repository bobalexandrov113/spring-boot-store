package com.cba.store;

import com.cba.store.entities.Address;
import com.cba.store.entities.Product;
import com.cba.store.entities.User;
import com.cba.store.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
class StoreApplicationTests {
    @Value("${spring.application.name}")
    String name;
    @Value("${datasets.ccdp_dataset_name}")
    String datasetName;
    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        System.out.println("***************** TESTING app values ******************");
        System.out.println(name);
        System.out.println(datasetName);
        System.out.println("***************** DONE TESTING app values ******************");
    }

//    @Test
//    void testUser() {
//        var user =
//                .name("John")
//                .password("1234")
//                .id(1L)
//                .email("bob@myhost.com")
//                .build();
//
//
//
//
//        System.out.println("***************** TESTING USER ******************");
//        System.out.printf("User: %s%n", user);
//        System.out.println("********** Done testing USER ***************");
//    }

    @Test
    void testUserRepository() {

//       var user = User.builder()
//               .name("Bob")
//               .password("password")
//               .email("bob@myhost.com")
//               .build();
//       repository.save(user);



        var user = userRepository.findById(2L).orElseThrow(()->new RuntimeException("User not found"));


        System.out.println("***************** TESTING USER REPOSITORY******************");
        System.out.printf("User: %s%n", user);
        System.out.println(user.getEmail());
        System.out.println(user.getPassword());
        System.out.println("********** Done testing USER REPOSITORY***************");
    }

    @Test
    void testAddress() {
        var address = Address.builder()
                .id(1)
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
    void testProduct() {
        var product = Product.builder()
                .id(1L)
                .name("John wax")
                .price(BigDecimal.valueOf(125.23))
                .categoryId((byte) 1)
                .build();
        System.out.println("***************** TESTING PRODUCT ******************");
        System.out.printf("Address: %s%n", product);
        System.out.println("********** Done testing PRODUCT ***************");
    }








}
