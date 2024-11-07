package com.example.demo.controllers;


import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.example.demo.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {
    @InjectMocks
    private OrderController orderController;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Before
    public void setup(){
        when(userRepository.findByUsername("test")).thenReturn(createUser());
        when(orderRepository.findByUser(any())).thenReturn(createOrders());
    }
    @Test
    public void testSubmitOrder(){
        ResponseEntity<UserOrder> orderResponseEntity = orderController.submit("test");
        assertNotNull(orderResponseEntity);
        assertEquals(200, orderResponseEntity.getStatusCodeValue());
        UserOrder order = orderResponseEntity.getBody();
        assertNotNull(order);
        assertEquals(createItems(),order.getItems());
        assertEquals(createUser().getId(), order.getUser().getId());
        assertEquals(createUser().getUsername(), order.getUser().getUsername());
    }

    @Test
    public void testRetrieveOrder(){
        ResponseEntity<List<UserOrder>> orderResponseEntity = orderController.getOrdersForUser("test");
        assertNotNull(orderResponseEntity);
        assertEquals(200,orderResponseEntity.getStatusCodeValue());
        List<UserOrder> orderList = orderResponseEntity.getBody();
        assertNotNull(orderList);
        assertEquals(createOrders().size(), orderList.size());
    }
}