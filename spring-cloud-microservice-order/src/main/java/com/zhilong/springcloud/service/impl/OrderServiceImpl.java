package com.zhilong.springcloud.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhilong.springcloud.contonst.PurchaseOnlieGlobalConstant;
import com.zhilong.springcloud.controller.OrderController;
import com.zhilong.springcloud.entity.Order;
import com.zhilong.springcloud.entity.OrderItem;
import com.zhilong.springcloud.entity.TokenResponse;
import com.zhilong.springcloud.entity.enu.OrderStatus;
import com.zhilong.springcloud.entity.to.CartItem;
import com.zhilong.springcloud.entity.to.CartItemTo;
import com.zhilong.springcloud.fegin.ItemFeginClient;
import com.zhilong.springcloud.repository.OrderRepository;
import com.zhilong.springcloud.service.OrderService;
import com.zhilong.springcloud.utils.RedisUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    ItemFeginClient itemFeginClient;

    @Autowired
    OrderRepository orderRepository;

    @Override
    public ResponseEntity generateOrder(CartItemTo cartItemTo,Boolean isNormalOrder) {

        String tokenKey = PurchaseOnlieGlobalConstant.OAUTH2_TOKEN_IN_REDIS_PREFIX + cartItemTo.getUsername();
        TokenResponse tokenResponse = (TokenResponse)redisUtils.getObj(tokenKey);
        Order orderInDb = null;

        if (tokenResponse != null) {
            Order order = new Order();
            order.setUsername(cartItemTo.getUsername());
            order.setCreateDate(new Date());
            order.setTotal(new BigDecimal(0));
            order.setDisacountTotal(new BigDecimal(0));
            List<CartItem> cartItems = cartItemTo.getCartItems();

            if (isNormalOrder.equals(true)) {
                normalOrder(cartItemTo, order, cartItems);
            } else {
                seckillOrder(cartItemTo,order,cartItems);
            }

            if ((cartItemTo.getCartItems().size() > 0)) {
                // save order into DB
                order.setOrderStatus(OrderStatus.PENDING);
                order.setTransactionCode(DateFormatUtils.format(new Date(),PurchaseOnlieGlobalConstant.YYYYMMDDHHMMSSSSS));
                orderInDb = orderRepository.save(order);

            } else {
                return ResponseEntity.badRequest().body("Empty items.");
            }
        } else {
            return ResponseEntity.badRequest().body("User is not online.");
        }

        return ResponseEntity.ok(orderInDb);
    }

    private void normalOrder(CartItemTo cartItemTo, Order order, List<CartItem> cartItems) {
        cartItems.forEach(cartItem -> {
            // 1. add item into order
            ResponseEntity itemResponse = itemFeginClient.item(cartItem.getId());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            try {
                OrderItem orderItem = objectMapper.readValue(itemResponse.getBody().toString(), OrderItem.class);

                // This logic is processed if the item exists in DB and the item stock number is more than 1.
                if (orderItem != null && (orderItem.getStockNum() - cartItem.getNum()) >= 0) {
                    orderItem.setNum(cartItem.getNum());
                    order.getOrderItems().add(orderItem);
                    order.setTotal(order.getTotal().add(
                            orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getNum()))
                    ));
                    order.setDisacountTotal(order.getDisacountTotal().add(
                            orderItem.getCurrentprice().multiply(BigDecimal.valueOf(orderItem.getNum()))
                    ));
                    orderItem.setStockNum(orderItem.getStockNum() - cartItem.getNum());
                    // 2. delete item from cart
                    itemFeginClient.deleteCartItem(cartItem.getId(), cartItem.getNum(), cartItemTo.getUsername());
                    // 3. deduct item stock in DB
                    itemFeginClient.itemDeductStockNum(orderItem.getId(),orderItem.getNum());
                }
            } catch (IOException e) {
                logger.error("",e);
            }

        });
    }

    private void seckillOrder(CartItemTo cartItemTo, Order order, List<CartItem> cartItems) {
        cartItems.forEach(cartItem -> {
            // 1. add item into order
            ResponseEntity itemResponse = itemFeginClient.secKillitem(cartItem.getId());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            try {
                OrderItem orderItem = objectMapper.readValue(itemResponse.getBody().toString(), OrderItem.class);

                // This logic is processed if the item exists in DB and the item stock number is more than -1.
                // because logic is deduct stock num first, so when the item sold out, the expression is
                // orderItem.getStockNum() - cartItem.getNum()) >= -1
                if (orderItem != null && (orderItem.getStockNum() - cartItem.getNum()) >= -1) {
                    orderItem.setNum(cartItem.getNum());
                    order.getOrderItems().add(orderItem);
                    order.setTotal(order.getTotal().add(
                            orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getNum()))
                    ));
                    order.setDisacountTotal(order.getDisacountTotal().add(
                            orderItem.getCurrentprice().multiply(BigDecimal.valueOf(orderItem.getNum()))
                    ));
                }
            } catch (IOException e) {
                logger.error("",e);
            }
        });
    }

    @Override
    public ResponseEntity getOrderInfo(Long id) {
        Order order = orderRepository.findById(id).get();
        String tokenKey = PurchaseOnlieGlobalConstant.OAUTH2_TOKEN_IN_REDIS_PREFIX + order.getUsername();
        TokenResponse tokenResponse = (TokenResponse)redisUtils.getObj(tokenKey);
        return (tokenResponse != null ? ResponseEntity.ok(order) : ResponseEntity.ok(""));
    }
}
