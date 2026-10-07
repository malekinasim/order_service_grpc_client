package com.example.order.client;

import com.example.payment.PaymentServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelBuilderCustomizer;
import org.springframework.grpc.client.GrpcChannelFactory;

import java.util.List;
import java.util.Map;

@Configuration
public class PaymentClientConfig {

    @Bean
    public PaymentServiceGrpc.PaymentServiceBlockingStub paymentStub(
            GrpcChannelFactory channelFactory) {
        return PaymentServiceGrpc.newBlockingStub(
                channelFactory.createChannel("payment")
        );
    }
    @Bean
    public GrpcChannelBuilderCustomizer<?> paymentRetryCustomizer() {

        Map<String, ?> serviceConfig = Map.of(
                "methodConfig", List.of(
                        Map.of(
                                "name", List.of(
                                        Map.of(
                                                "service",
                                                PaymentServiceGrpc.SERVICE_NAME,
                                                "method",
                                                "Pay"
                                        )
                                ),
                                "retryPolicy", Map.of(
                                        "maxAttempts", 3.0,
                                        "initialBackoff", "0.2s",
                                        "maxBackoff", "1s",
                                        "backoffMultiplier", 2.0,
                                        "retryableStatusCodes",
                                        List.of("UNAVAILABLE")
                                )
                        )
                )
        );

        return GrpcChannelBuilderCustomizer.matching(
                "payment",
                builder -> builder
                        .enableRetry()
                        .defaultServiceConfig(serviceConfig)
        );
    }
}