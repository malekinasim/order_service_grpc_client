package com.example.order;

import com.example.order.model.OrderRequest;
import com.example.order.model.OrderResponse;
import com.example.order.servie.OrderService;
import io.grpc.StatusRuntimeException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/api/orders")
public class OrderController  {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> run(@RequestBody OrderRequest request) {

            var response = orderService.payOrder(101, request.amount(), request.currency(),request.key());
             return  new ResponseEntity<>(new OrderResponse(response.getPaymentId(),response.getApproved()),HttpStatus.OK);

    }
}