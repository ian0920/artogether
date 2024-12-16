package com.artogether.venue;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class VenueExceptions {
    //400 Bad Request
//當請求參數無效或不符合條件時，使用 400。
//適合像是資料缺失、不符合上架資格等業務規則違反的情況。
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class InvalidListingException extends RuntimeException {
        private PublishErrorResponse errorResponse;

        public InvalidListingException(String message) {
            super(message);
        }

        public InvalidListingException(PublishErrorResponse errorResponse) {
            super(errorResponse.getMessage());
            this.errorResponse = errorResponse;
        }

        public PublishErrorResponse getErrorResponse() {
            return errorResponse;
        }
    }

    //409 Conflict
    //當操作因資源狀態衝突而失敗（例如商品已上架、不能重複上架），使用 409。
    //適合處理與資源狀態相關的業務錯誤。
    @ResponseStatus(HttpStatus.CONFLICT)
    public static class DateAlreadyLockedException extends RuntimeException {
        public DateAlreadyLockedException(String message) {
            super(message);
        }
    }
}
