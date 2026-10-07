package com.example.order.exception;

import io.grpc.StatusRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GrpcExceptionHandler {
    @ExceptionHandler(StatusRuntimeException.class)
    public ResponseEntity<ApiError> handleGrpc(StatusRuntimeException exception) {
        System.out.println("gRPC code: " + exception.getStatus().getCode());
        System.out.println("gRPC description: "
                + exception.getStatus().getDescription());
       return switch (exception.getStatus().getCode()) {
            case INVALID_ARGUMENT ->  ResponseEntity.badRequest().body(
                    new ApiError(
                            "INVALID_PAYMENT",
                            exception.getStatus().getDescription()
                    )
            );

            case UNAVAILABLE ->
                    ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                            .body(new ApiError(
                                    "PAYMENT_UNAVAILABLE",
                                    exception.getStatus().getDescription()
                            ));
            case DEADLINE_EXCEEDED ->  ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
                        .body(new ApiError(
                                "PAYMENT_TIMEOUT",
                                "Payment response was not received in time; "
                                        + "payment outcome is unknown"
                        ));
           case INTERNAL ->  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(new ApiError(
                           "INTERNAL_SERVER_ERROR",
                           "Payment failed; "+exception.getStatus().getDescription()
                   ));
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiError(
                                exception.getStatus().getCode().name(),
                                exception.getStatus().getDescription()
                        ));

        };

    }
}
