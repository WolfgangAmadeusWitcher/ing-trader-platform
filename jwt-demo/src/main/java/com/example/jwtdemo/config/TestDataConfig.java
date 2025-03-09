package com.example.jwtdemo.config;

import com.example.jwtdemo.model.*;
import com.example.jwtdemo.repository.AssetRepository;
import com.example.jwtdemo.repository.OrderRepository;
import com.example.jwtdemo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@Configuration
public class TestDataConfig {

    @Bean
    public CommandLineRunner loadTestUsers(UserRepository userRepository, PasswordEncoder passwordEncoder, AssetRepository assetRepository, OrderRepository orderRepository) {
        return args -> {
            if(userRepository.count() == 0) {

                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setUserRole(UserRole.ADMIN);
                userRepository.save(admin);

                User testUser1 = new User();
                testUser1.setUsername("ingHubTestTraderOne");
                testUser1.setPassword(passwordEncoder.encode("testUser123"));
                testUser1.setUserRole(UserRole.STANDARD);
                userRepository.save(testUser1);

                User testUser2 = new User();
                testUser2.setUsername("ingHubTestTraderTwo");
                testUser2.setPassword(passwordEncoder.encode("testUser123"));
                testUser2.setUserRole(UserRole.STANDARD);
                userRepository.save(testUser2);

                User testUser3 = new User();
                testUser3.setUsername("ingHubTestTraderThree");
                testUser3.setPassword(passwordEncoder.encode("testUser123"));
                testUser3.setUserRole(UserRole.STANDARD);
                userRepository.save(testUser3);

                Asset tryAssetUserOne = new Asset();
                tryAssetUserOne.setSize(10000);
                tryAssetUserOne.setOwner(testUser1);
                tryAssetUserOne.setUsableSize(9000);
                tryAssetUserOne.setAssetName("TRY");
                assetRepository.save(tryAssetUserOne);

                Asset tryAssetUserTwo = new Asset();
                tryAssetUserTwo.setSize(10000);
                tryAssetUserTwo.setOwner(testUser2);
                tryAssetUserTwo.setUsableSize(10000);
                tryAssetUserTwo.setAssetName("TRY");
                assetRepository.save(tryAssetUserTwo);

                Asset tryAssetUserThree = new Asset();
                tryAssetUserThree.setSize(50);
                tryAssetUserThree.setOwner(testUser1);
                tryAssetUserThree.setUsableSize(50);
                tryAssetUserThree.setAssetName("DEF");
                assetRepository.save(tryAssetUserThree);

                Asset tryAssetUserFour = new Asset();
                tryAssetUserFour.setSize(100);
                tryAssetUserFour.setOwner(testUser1);
                tryAssetUserFour.setUsableSize(0);
                tryAssetUserFour.setAssetName("ABC");
                assetRepository.save(tryAssetUserFour);

                Asset tryAssetUserFive = new Asset();
                tryAssetUserFive.setSize(50);
                tryAssetUserFive.setOwner(testUser2);
                tryAssetUserFive.setUsableSize(50);
                tryAssetUserFive.setAssetName("ABC");
                assetRepository.save(tryAssetUserFive);

                Order testUserOrderOne = new Order();
                testUserOrderOne.setOwner(testUser1);
                testUserOrderOne.setAssetName("ABC");
                testUserOrderOne.setPrice(10);
                testUserOrderOne.setSize(100);
                testUserOrderOne.setOrderStatus(Status.PENDING);
                testUserOrderOne.setOrderSide(OrderSide.BUY);
                testUserOrderOne.setCreateDate(new Date());
                orderRepository.save(testUserOrderOne);

                Order testUserOrderTwo = new Order();
                testUserOrderTwo.setOwner(testUser1);
                testUserOrderTwo.setAssetName("DEF");
                testUserOrderTwo.setPrice(2);
                testUserOrderTwo.setSize(50);
                testUserOrderTwo.setOrderStatus(Status.MATCHED);
                testUserOrderTwo.setOrderSide(OrderSide.BUY);
                testUserOrderTwo.setCreateDate(new Date());
                orderRepository.save(testUserOrderTwo);

                Order testUserOrderThree = new Order();
                testUserOrderThree.setOwner(testUser2);
                testUserOrderThree.setAssetName("ABC");
                testUserOrderThree.setPrice(10);
                testUserOrderThree.setSize(50);
                testUserOrderThree.setOrderStatus(Status.MATCHED);
                testUserOrderThree.setOrderSide(OrderSide.BUY);
                testUserOrderThree.setCreateDate(new Date());
                orderRepository.save(testUserOrderThree);
            }
        };
    }
}
