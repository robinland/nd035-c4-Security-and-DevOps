package com.example.demo.controllers;


import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static com.example.demo.TestUtils.createItem;
import static com.example.demo.TestUtils.createItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {
    @InjectMocks
    private ItemController itemController;
    @Mock
    private ItemRepository itemRepository;

    @Before
    public void setup(){
        when(itemRepository.findById(1L)).thenReturn(Optional.of(createItem(1)));
        when(itemRepository.findAll()).thenReturn(createItems());
    }

    @Test
    public void testRetrieveAllItems(){
        ResponseEntity<List<Item>> itemResponse = itemController.getItems();
        assertNotNull(itemResponse);
        assertEquals(200, itemResponse.getStatusCodeValue());
        List<Item> itemList =itemResponse.getBody();
        assertNotNull(itemList);
        assertEquals(createItems(), itemList);
        assertEquals(5, itemList.size());
    }
    @Test
    public void testRetrieveItemById(){
        ResponseEntity<Item> itemResponse = itemController.getItemById(1L);
        assertNotNull(itemResponse);
        assertEquals(200,itemResponse.getStatusCodeValue());
        Item item= itemResponse.getBody();
        assertNotNull(item);
        assertEquals(createItem(1L), item);
    }
}
