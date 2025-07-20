package com.example.firestore.pagination;

import com.google.cloud.firestore.DocumentSnapshot;
import java.util.List;

public interface PaginatedRepository<T> {
    List<T> getPaginatedResults(int pageSize, DocumentSnapshot lastDocument) throws Exception;
}