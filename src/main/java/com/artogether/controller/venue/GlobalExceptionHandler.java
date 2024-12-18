package com.artogether.controller.venue;

import com.artogether.venue.PublishErrorResponse;
import com.artogether.venue.VenueExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(VenueExceptions.InvalidListingException.class)
    public ResponseEntity<PublishErrorResponse> handleInvalidListingException(VenueExceptions.InvalidListingException ex) {
        // 提取異常中的錯誤資料並返回 JSON 格式
        PublishErrorResponse errorResponse = ex.getErrorResponse();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}