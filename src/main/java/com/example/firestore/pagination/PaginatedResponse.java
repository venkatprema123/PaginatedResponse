package com.example.firestore.pagination;

import java.util.List;

public class PaginatedResponse<T> {
    private List<T> items;
    private String nextCursor;

    public PaginatedResponse(List<T> items, String nextCursor) {
        this.items = items;
        this.nextCursor = nextCursor;
    }

    public List<T> getItems() {
        return items;
    }

    public String getNextCursor() {
        return nextCursor;
    }
}