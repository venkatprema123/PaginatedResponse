package com.example.firestore.pagination;

import com.google.cloud.firestore.QueryDocumentSnapshot;

public class UserPaginatedRepository extends AbstractFirestorePaginatedRepository<User> {

    @Override
    protected String getCollectionName() {
        return "users";
    }

    @Override
    protected String getOrderByField() {
        return "name";
    }

    @Override
    protected User fromDocument(QueryDocumentSnapshot document) {
        String id = document.getId();
        String name = document.getString("name");
        return new User(id, name);
    }
}