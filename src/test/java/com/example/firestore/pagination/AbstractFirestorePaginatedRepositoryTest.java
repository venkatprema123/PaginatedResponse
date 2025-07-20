package com.example.firestore.pagination;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AbstractFirestorePaginatedRepositoryTest {

    @Mock
    Firestore firestore;
    @Mock
    CollectionReference collectionReference;
    @Mock
    Query query;
    @Mock
    ApiFuture<QuerySnapshot> apiFuture;
    @Mock
    QuerySnapshot querySnapshot;
    @Mock
    QueryDocumentSnapshot documentSnapshot;

    static class DummyEntity {
        String id;
        DummyEntity(String id) { this.id = id; }
    }

    static class DummyRepo extends AbstractFirestorePaginatedRepository<DummyEntity> {
        private final Firestore db;
        DummyRepo(Firestore db) { this.db = db; }
        @Override protected String getCollectionName() { return "dummy"; }
        @Override protected String getOrderByField() { return "id"; }
        @Override protected DummyEntity fromDocument(QueryDocumentSnapshot doc) { return new DummyEntity(doc.getId()); }
        @Override protected Firestore getFirestore() { return db; }
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPaginatedResults() throws Exception {
        when(firestore.collection("dummy")).thenReturn(collectionReference);
        when(collectionReference.orderBy("id")).thenReturn(query);
        when(query.limit(10)).thenReturn(query);
        when(query.get()).thenReturn(apiFuture);
        when(apiFuture.get()).thenReturn(querySnapshot);
        when(querySnapshot.getDocuments()).thenReturn(List.of(documentSnapshot));
        when(documentSnapshot.getId()).thenReturn("test-id");

        DummyRepo repo = new DummyRepo(firestore);
        List<DummyEntity> result = repo.getPaginatedResults(10, null);

        assertEquals(1, result.size());
        assertEquals("test-id", result.get(0).id);
    }
}