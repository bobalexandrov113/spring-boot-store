package com.cba.store;

import com.cba.store.entities.User;
import com.cba.store.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class StoreApplication {

    public static void main(String[] args) {
       ApplicationContext context = SpringApplication.run(StoreApplication.class, args);
       var repository = context.getBean(UserRepository.class);
//       var user = User.builder()
//               .name("Bob")
//               .password("password")
//               .email("bob@myhost.com")
//               .build();
//       repository.save(user);



//      var user = repository.findById(2L).orElseThrow(()->new RuntimeException("User not found"));
//        System.out.println(user.getEmail());
//        System.out.println(user);

//     repository.deleteAll();
    }


}
