package com.zhilong.springcloud.service.impl;

import com.zhilong.springcloud.entity.Cart;
import com.zhilong.springcloud.entity.CartItem;
import com.zhilong.springcloud.entity.Item;
import com.zhilong.springcloud.entity.ItemCategory;
import com.zhilong.springcloud.repository.CartRepository;
import com.zhilong.springcloud.repository.ItemCategoryRepository;
import com.zhilong.springcloud.repository.ItemRepository;
import com.zhilong.springcloud.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemCategoryRepository itemCategoryRepository;

    @Override
    public Cart addCartItem(Long itemId, Integer num, String username) {
        Cart cart = cartRepository.findFirstByUsername(username);
        if (cart != null) {
            CartItem newCartItem = null;
            List<CartItem> cartItems = cart.getCartItems();
            for (CartItem cartItem : cartItems) {
                if (cartItem.getItemId().equals(itemId)) {
                    cartItem.setNum(cartItem.getNum() + num);
                    newCartItem = cartItem;
                    cart.setTotal(cart.getTotal().add(cartItem.getPrice().multiply(BigDecimal.valueOf(num))));
                    cart.setDisacountTotal(cart.getDisacountTotal().add(cartItem.getCurrentprice().multiply(BigDecimal.valueOf(num))));
                }
            }
            if (newCartItem == null) {
                newCartItem = newInstanceCartItem(itemId, num);
                cart.setTotal(cart.getTotal().add(newCartItem.getPrice().multiply(BigDecimal.valueOf(num))));
                cart.setDisacountTotal(cart.getDisacountTotal().add(newCartItem.getCurrentprice().multiply(BigDecimal.valueOf(num))));
                cartItems.add(newCartItem);
            }
            cart.setCartItems(cartItems);
        } else {
            cart = new Cart();
            cart.setUsername(username);
            cart.setCreateDate(new Date());
            List<CartItem> cartItems = new ArrayList<>();
            CartItem cartItem = newInstanceCartItem(itemId, num);
            cartItems.add(cartItem);
            cart.setCartItems(cartItems);
            cart.setTotal(cartItem.getPrice().multiply(BigDecimal.valueOf(num)));
            cart.setDisacountTotal(cartItem.getCurrentprice().multiply(BigDecimal.valueOf(num)));
        }
        return cartRepository.save(cart);
    }

    @Override
    public Cart deleteCartItem(Long itemId, Integer num, String username) {
        Cart cart = cartRepository.findFirstByUsername(username);
        if (cart != null) {
            int removeIndex = -1;
            List<CartItem> cartItems = cart.getCartItems();
            for (int i = 0; i < cartItems.size(); i++) {
                if (cartItems.get(i).getItemId().equals(itemId)) {
                    CartItem cartItem = cartItems.get(i);
                    int currentNum = cartItem.getNum() - num;
                    if (currentNum > 0) {
                        cartItem.setNum(currentNum);
                        cartItems.set(i,cartItem);
                        cart.setTotal(cart.getTotal().subtract(cartItem.getPrice().multiply(BigDecimal.valueOf(num))));
                        cart.setDisacountTotal(cart.getDisacountTotal().subtract(cartItem.getCurrentprice().multiply(BigDecimal.valueOf(num))));
                    } else {
                        removeIndex = i;
                        cart.setTotal(cart.getTotal().subtract(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getNum()))));
                        cart.setDisacountTotal(cart.getDisacountTotal().subtract(cartItem.getCurrentprice().multiply(BigDecimal.valueOf(cartItem.getNum()))));
                    }
                    break;
                }
            }
            if (removeIndex > -1) {
                cartItems.remove(removeIndex);
            }
            cart.setCartItems(cartItems);
        } else {
            cart = new Cart();
            cart.setUsername(username);
            cart.setCreateDate(new Date());
            List<CartItem> cartItems = new ArrayList<>();
            cart.setCartItems(cartItems);
        }
        return cartRepository.save(cart);
    }

    @Override
    public Cart getUserCart(String username) {
        return cartRepository.findFirstByUsername(username);
    }

    private CartItem newInstanceCartItem(Long itemId, Integer num) {
        CartItem newCartItem = new CartItem();
        Item item = itemRepository.findById(itemId).get();
        ItemCategory itemCategory = itemCategoryRepository.findById(item.getCatId()).get();
        newCartItem.setNum(num);
        newCartItem.setImage(item.getImage());
        newCartItem.setTitle(item.getTitle());
        newCartItem.setItemId(item.getId());
        newCartItem.setPrice(item.getPrice());
        newCartItem.setCurrentprice(item.getCurrentprice());
        newCartItem.setCategory(itemCategory.getName());
        return newCartItem;
    }
}
