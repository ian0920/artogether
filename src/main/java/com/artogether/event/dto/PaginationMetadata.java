package com.artogether.event.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PaginationMetadata {

    private int currentPage;
    private int pageSize;
    private int totalPages;

    public PaginationMetadata(Page<?> page) {
        this.currentPage = page.getNumber();
        this.pageSize = page.getSize();
        this.totalPages = page.getTotalPages();
    }
}
