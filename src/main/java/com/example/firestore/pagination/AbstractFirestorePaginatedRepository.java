package com.example.firestore.pagination;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFirestorePaginatedRepository<T> implements PaginatedRepository<T> {

    protected Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }
    protected abstract String getCollectionName();
    protected abstract String getOrderByField();
    protected abstract T fromDocument(QueryDocumentSnapshot document);

    @Override
    public List<T> getPaginatedResults(int pageSize, DocumentSnapshot lastDocument) throws Exception {
        Firestore db = getFirestore();
        CollectionReference collection = db.collection(getCollectionName());
        Query query = collection.orderBy(getOrderByField()).limit(pageSize);

        if (lastDocument != null) {
            query = query.startAfter(lastDocument);
        }

        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        List<T> result = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            result.add(fromDocument(doc));
        }
        return result;
    }
}