package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    public static void injectObject(Object target, String fieldName, Object toInject){
        boolean wasPrivate = false;

        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            if(!field.isAccessible()){
                field.setAccessible(true);
                wasPrivate = true;
            }
            field.set(target,toInject);
            if(wasPrivate){
                field.setAccessible(false);
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static User createUser(){
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setCart(createCart(user));
        return user;
    }
    public static Cart createCart(User user){
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setId(1L);
        List<Item> items = createItems();
        cart.setItems(createItems());
        cart.setTotal(items.stream().map(Item::getPrice).reduce(BigDecimal::add).get());
        return cart;
    }
    public static List<Item> createItems() {
        List<Item> items = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            items.add(createItem(i));
        }
        return items;
    }
    public static Item createItem(long id){
        Item item = new Item();
        item.setId(id);
        item.setPrice(BigDecimal.valueOf(id * 5));
        item.setName("Item" + item.getId());
        item.setDescription("Here is Description");
        return item;
    }
    public static List<UserOrder> createOrders(){
        List<UserOrder> orders = new ArrayList<>();
        for(int i = 0; i <=2; i++){
            UserOrder userOrder = new UserOrder();
            Cart cart = createCart(createUser());
            userOrder.setUser(createUser());
            userOrder.setId(Long.valueOf(i));
            userOrder.setItems(cart.getItems());
            userOrder.setTotal(cart.getTotal());
            orders.add(userOrder);
        }
        return orders;
    }
}
