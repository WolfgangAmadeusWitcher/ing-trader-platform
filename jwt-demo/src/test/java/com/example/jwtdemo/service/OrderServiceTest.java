package com.example.jwtdemo.service;

import com.example.jwtdemo.exception.OrderFailedException;
import com.example.jwtdemo.exception.UnauthorizedException;
import com.example.jwtdemo.model.*;
import com.example.jwtdemo.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AssetService assetService;

    @InjectMocks
    private OrderService orderService;

    private User adminUser;
    private User standardUserOne;
    private User standardUserTwo;

    private Order order;



    @BeforeEach
    void setup() {
        adminUser = new User("admin", "adminpass", UserRole.ADMIN);
        adminUser.setId(1L);
        standardUserOne = new User("trader1", "traderpass", UserRole.STANDARD);
        standardUserOne.setId(2L);
        standardUserTwo = new User("trader2", "traderpass2", UserRole.STANDARD);
        standardUserTwo.setId(3L);

        order = new Order();
        order.setOwner(standardUserOne);
        order.setPrice(100.0);
        order.setSize(2.0);
        order.setAssetName("ABC");
        order.setOrderSide(OrderSide.BUY);
    }

    @Test
    void testCreateOrderFailWhenAssetIsTRY() {
        // You can not trade TRY directly, it's reference valuation and you trade against it
        assertThrows(OrderFailedException.class, () -> {
            orderService.create(100.0, 2.0, OrderSide.BUY, "TRY", standardUserOne);
        });
    }

    @Test
    void testCheckUserAuthorization_AdminUser() throws UnauthorizedException {
        // Order Belongs to standardUserOne, Admin Should be authorized to access
        assertDoesNotThrow(() -> orderService.CheckUserAuthorization(adminUser, order));
    }

    @Test
    void testCheckUserAuthorization_OrderOwnerUser() throws UnauthorizedException {
        // Order Belongs to standardUserOne, standardUserOne Should be authorized to access
        assertDoesNotThrow(() -> orderService.CheckUserAuthorization(standardUserOne, order));
    }

    @Test
    void testCheckUserAuthorization_OrderNotOwnerUser() throws UnauthorizedException {
        // Order Belongs to standardUserOne, standardUserTwo Should NOT be authorized to access
        assertThrows(UnauthorizedException.class,() -> orderService.CheckUserAuthorization(standardUserTwo, order));
    }

    @Test
    void testCheckUserTryAssetAgainstOrder_WhenEnoughTRY() throws OrderFailedException {
        Asset mockAsset = Mockito.mock(Asset.class);
        Mockito.when(mockAsset.getUsableSize()).thenReturn(1500.0);

        double orderTotalPrice = 1000.0;

        assertDoesNotThrow(() -> orderService.CheckUserTryAssetAgainstOrder(mockAsset, orderTotalPrice));
    }

    @Test
    void testCheckUserTryAssetAgainstOrder_WhenNotEnoughTRY() throws OrderFailedException {
        Asset mockAsset = Mockito.mock(Asset.class);
        Mockito.when(mockAsset.getUsableSize()).thenReturn(1500.0);

        double orderTotalPrice = 10000.0;

        assertThrows(OrderFailedException.class, () -> orderService.CheckUserTryAssetAgainstOrder(mockAsset, orderTotalPrice));
    }

    @Test
    void testHandleSellOrder_WhenEnoughAsset() throws OrderFailedException {

        // User opens an order to sell an asset with a size that is less than the Asset's usable size

        Order mockOrder = Mockito.mock(Order.class);
        Asset mockTryAsset = Mockito.mock(Asset.class);
        Asset mockSellAsset = Mockito.mock(Asset.class);

        Mockito.when(mockSellAsset.getUsableSize()).thenReturn(150.0);
        Mockito.when(mockOrder.getAssetName()).thenReturn("ABC");
        Mockito.when(mockOrder.getSize()).thenReturn(100.0);
        Mockito.when(mockOrder.getPrice()).thenReturn(10.0);

        Mockito.when(mockTryAsset.getSize()).thenReturn(200.0);

        Mockito.when(assetService.findAssetForUser(Mockito.any(), Mockito.eq("TRY")))
                .thenReturn(mockTryAsset);
        Mockito.when(assetService.findAssetForUser(Mockito.any(), Mockito.anyString()))
                .thenReturn(mockSellAsset);

        Mockito.when(assetService.findAssetForUser(Mockito.any(), Mockito.anyString()))
                .thenReturn(mockSellAsset)
                .thenReturn(mockTryAsset);

        assertDoesNotThrow(() -> orderService.handleSellOrder(mockOrder));
    }

    @Test
    void testHandleSellOrder_WhenNotEnoughAsset() throws OrderFailedException {

        // User opens an order to sell an asset with a size that is more than the Asset's usable size
        // Expected Behaviour -> Order Failed Exception

        Order mockOrder = Mockito.mock(Order.class);
        Asset mockTryAsset = Mockito.mock(Asset.class);
        Asset mockSellAsset = Mockito.mock(Asset.class);

        Mockito.when(mockSellAsset.getUsableSize()).thenReturn(150.0);
        Mockito.when(mockOrder.getAssetName()).thenReturn("ABC");
        Mockito.when(mockOrder.getSize()).thenReturn(500.0);
        Mockito.when(mockOrder.getPrice()).thenReturn(10.0);

        Mockito.when(mockTryAsset.getSize()).thenReturn(200.0);

        Mockito.when(assetService.findAssetForUser(Mockito.any(), Mockito.eq("TRY")))
                .thenReturn(mockTryAsset);
        Mockito.when(assetService.findAssetForUser(Mockito.any(), Mockito.anyString()))
                .thenReturn(mockSellAsset);

        Mockito.when(assetService.findAssetForUser(Mockito.any(), Mockito.anyString()))
                .thenReturn(mockSellAsset)
                .thenReturn(mockTryAsset);

        assertThrows(OrderFailedException.class, () -> orderService.handleSellOrder(mockOrder));
    }
}
