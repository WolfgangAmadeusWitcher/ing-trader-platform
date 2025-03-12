package com.example.jwtdemo.service;

import com.example.jwtdemo.exception.AssetNotFoundException;
import com.example.jwtdemo.exception.OrderFailedException;
import com.example.jwtdemo.model.*;
import com.example.jwtdemo.repository.AssetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private AssetService assetService;

    private User testUser;
    private User testUserTwo;
    private User adminTestUser;

    private Order testOrder;
    private Asset testAsset;
    private Asset testAssetTwo;

    @BeforeEach
    void setup() {

        adminTestUser = new User("admin", "adminpass", UserRole.ADMIN);
        testUser = new User("test", "testpass", UserRole.STANDARD);
        testUserTwo = new User("testTwo", "testPass", UserRole.STANDARD);

        testUser.setId(1L);
        testUserTwo.setId(2L);

        testAsset = new Asset();
        testAsset.setOwner(testUser);
        testAsset.setAssetName("TestAsset");
        testAsset.setSize(new BigDecimal("1000"));
        testAsset.setUsableSize(new BigDecimal("100"));


        testAssetTwo = new Asset();
        testAssetTwo.setOwner(testUserTwo);
        testAssetTwo.setAssetName("TestAssetTwo");
        testAssetTwo.setSize(new BigDecimal("1000"));
        testAssetTwo.setUsableSize(new BigDecimal("100"));
    }


    @Test
    public void testGetAssetsForStandardUserRole() {

        when(assetRepository.findByOwner_Id(1)).thenReturn(List.of(testAsset));

        List<Asset> assets = assetService.getAssets(testUser);
        assertEquals(1, assets.size());
        assertEquals("TestAsset", assets.get(0).getAssetName());
    }

    //Admin user sees All Users' assets
    @Test
    public void testGetAssetsForAllUsersWithAdminUserRole() {
        when(assetRepository.findAll()).thenReturn(List.of(testAsset, testAssetTwo));

        List<Asset> assets = assetService.getAssets(adminTestUser);
        assertEquals(2, assets.size());
        assertEquals("TestAsset", assets.get(0).getAssetName());
        assertEquals("TestAssetTwo", assets.get(1).getAssetName());
    }

    @Test
    public void testFindAssetThatDoesNotExistForUser(){
        assertThrows(AssetNotFoundException.class, () -> assetService.findAssetForUser(testUser, "TestAsset2"));
    }

    @Test
    public void testFindAssetThatExistsForUser(){
        when(assetRepository.findByOwner_IdAndAssetName(testUser.getId(), "TestAsset")).thenReturn(Optional.of(testAsset));
        assertDoesNotThrow(() -> assetService.findAssetForUser(testUser, "TestAsset"));
    }
}
