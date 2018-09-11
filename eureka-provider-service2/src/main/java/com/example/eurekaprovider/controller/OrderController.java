package com.example.eurekaprovider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author: MrWang
 * @date: 2018/9/6
 */


@RestController
public class OrderController {

    @GetMapping("/order")
    public Object student(){
        Order order = new Order();
        order.setOrderName("订单名称");
        order.setOrderNumber("订单号");
        order.setOrderQuantity("2");
        return order;
    }

    class Order{
        private String orderName;
        private String orderNumber;
        private String orderQuantity;

        public String getOrderName() {
            return orderName;
        }

        public void setOrderName(String orderName) {
            this.orderName = orderName;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getOrderQuantity() {
            return orderQuantity;
        }

        public void setOrderQuantity(String orderQuantity) {
            this.orderQuantity = orderQuantity;
        }
    }

}
