package com.bwagih.scheduler.commons;

import com.bwagih.scheduler.model.response.ResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractController {

    public <T> ResponseEntity<T> handle(T data) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                .body(data);
    }

    public <T extends ResponseModel> ResponseEntity<T> handleResponses(T data) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                .body(data);
    }

    public <T> CompletableFuture<ResponseEntity<T>> handleAsyncResponses(T data) {
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK)
                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                .body(data));
    }

}
