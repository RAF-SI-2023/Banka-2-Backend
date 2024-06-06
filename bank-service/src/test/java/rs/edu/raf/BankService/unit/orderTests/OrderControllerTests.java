package rs.edu.raf.BankService.unit.orderTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.BankService.controller.OrderController;
import rs.edu.raf.BankService.data.dto.OrderDto;
import rs.edu.raf.BankService.data.enums.OrderStatus;
import rs.edu.raf.BankService.service.OrderService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTests {
    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @Test
    public void testCreateOrder_Success() {
        // Arrange
        OrderDto orderDto = new OrderDto();
        boolean createStatus = true;

        when(orderService.createOrder(orderDto)).thenReturn(createStatus);

        // Act
        ResponseEntity<Boolean> response = (ResponseEntity<Boolean>) orderController.createOrder(orderDto);

        // Assert
        assertEquals(true, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testCreateOrder_Failure() {
        // Arrange
        OrderDto orderDto = new OrderDto();
        Exception exception = new RuntimeException("Insufficient funds for reservation");

        when(orderService.createOrder(orderDto)).thenThrow(exception);

        // Act
        ResponseEntity<Exception> response = (ResponseEntity<Exception>) orderController.createOrder(orderDto);

        // Assert
        assertEquals(exception, response.getBody());
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void testGetAllOrders() {
        //arrange
        OrderDto orderDto = new OrderDto();
        OrderDto orderDto1 = new OrderDto();
        List<OrderDto> expectedList = Arrays.asList(orderDto, orderDto1);
        when(orderService.getAll()).thenReturn(expectedList);

        //act
        ResponseEntity<List<OrderDto>> response = (ResponseEntity<List<OrderDto>>) orderController.getAllOrders();

        //assert
        assertEquals(expectedList, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(response.getBody().size(), expectedList.size());

    }

    @Test
    public void testGetNonApprovedOrders() {
        // Arrange
        OrderDto orderDto1 = new OrderDto();
        orderDto1.setOrderStatus(OrderStatus.WAITING_FOR_APPROVAL);
        OrderDto orderDto2 = new OrderDto();
        orderDto2.setOrderStatus(OrderStatus.WAITING_FOR_APPROVAL);
        OrderDto orderDto3 = new OrderDto();
        orderDto3.setOrderStatus(OrderStatus.APPROVED);

        List<OrderDto> allOrders = Arrays.asList(orderDto1, orderDto2, orderDto3);
        List<OrderDto> expectedList = Arrays.asList(orderDto1, orderDto2);

        when(orderService.getAll()).thenReturn(allOrders);

        // Act
        ResponseEntity<List<OrderDto>> response = (ResponseEntity<List<OrderDto>>) orderController.getNonApprovedOrders();

        // Assert
        assertEquals(expectedList, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedList.size(), response.getBody().size());
    }

    @Test
    public void testGetApprovedOrders() {
        // Arrange
        OrderDto orderDto1 = new OrderDto();
        orderDto1.setOrderStatus(OrderStatus.APPROVED);
        OrderDto orderDto2 = new OrderDto();
        orderDto2.setOrderStatus(OrderStatus.APPROVED);
        OrderDto orderDto3 = new OrderDto();
        orderDto3.setOrderStatus(OrderStatus.WAITING_FOR_APPROVAL);

        List<OrderDto> allOrders = Arrays.asList(orderDto1, orderDto2, orderDto3);
        List<OrderDto> expectedList = Arrays.asList(orderDto1, orderDto2);

        when(orderService.getAll()).thenReturn(allOrders);

        // Act
        ResponseEntity<List<OrderDto>> response = (ResponseEntity<List<OrderDto>>) orderController.getApprovedOrders();

        // Assert
        assertEquals(expectedList, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedList.size(), response.getBody().size());
    }

    @Test
    public void testGetDeniedOrders() {
        // Arrange
        OrderDto orderDto1 = new OrderDto();
        orderDto1.setOrderStatus(OrderStatus.DENIED);
        OrderDto orderDto2 = new OrderDto();
        orderDto2.setOrderStatus(OrderStatus.DENIED);
        OrderDto orderDto3 = new OrderDto();
        orderDto3.setOrderStatus(OrderStatus.APPROVED);

        List<OrderDto> allOrders = Arrays.asList(orderDto1, orderDto2, orderDto3);
        List<OrderDto> expectedList = Arrays.asList(orderDto1, orderDto2);

        when(orderService.getAll()).thenReturn(allOrders);

        // Act
        ResponseEntity<List<OrderDto>> response = (ResponseEntity<List<OrderDto>>) orderController.getDeniedOrders();

        // Assert
        assertEquals(expectedList, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedList.size(), response.getBody().size());
    }

    @Test
    public void testApproveOrder() {
        // Arrange
        Long orderId = 1L;
        boolean updateStatus = true;

        // Mock the updateOrderStatus method to return true
        when(orderService.updateOrderStatus(orderId, OrderStatus.APPROVED)).thenReturn(updateStatus);

        // Act
        ResponseEntity<Boolean> response = (ResponseEntity<Boolean>) orderController.approveOrder(orderId);

        // Assert
        assertEquals(true, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testRejectOrder() {
        // Arrange
        Long orderId = 1L;
        boolean updateStatus = true;

        when(orderService.updateOrderStatus(orderId, OrderStatus.DENIED)).thenReturn(updateStatus);

        // Act
        ResponseEntity<Boolean> response = (ResponseEntity<Boolean>) orderController.rejectOrder(orderId);

        // Assert
        assertEquals(true, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testFindOrderById() {
        // Arrange
        Long orderId = 1L;
        OrderDto orderDto = new OrderDto();
        orderDto.setId(orderId);

        when(orderService.findDtoById(orderId)).thenReturn(orderDto);

        // Act
        ResponseEntity<OrderDto> response = (ResponseEntity<OrderDto>) orderController.findOrderById(orderId);

        // Assert
        assertEquals(orderDto, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }
}
