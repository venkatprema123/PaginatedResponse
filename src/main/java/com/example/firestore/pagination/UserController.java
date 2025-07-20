package com.example.firestore.pagination;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserPaginatedRepository userRepository = new UserPaginatedRepository();

    @GetMapping
    public PaginatedResponse<User> getUsers(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String lastDocId
    ) throws Exception {
        DocumentSnapshot lastDoc = null;
        if (lastDocId != null && !lastDocId.isEmpty()) {
            Firestore db = FirestoreClient.getFirestore();
            lastDoc = db.collection(userRepository.getCollectionName()).document(lastDocId).get().get();
        }

        List<User> users = userRepository.getPaginatedResults(limit, lastDoc);

        // Prepare nextCursor
        String nextCursor = null;
        if (!users.isEmpty()) {
            Firestore db = FirestoreClient.getFirestore();
            var query = db.collection(userRepository.getCollectionName())
                    .orderBy(userRepository.getOrderByField())
                    .limit(limit);
            if (lastDoc != null) {
                query = query.startAfter(lastDoc);
            }
            var docs = query.get().get().getDocuments();
            if (!docs.isEmpty()) {
                nextCursor = docs.get(docs.size() - 1).getId();
            }
        }

        return new PaginatedResponse<>(users, nextCursor);
    }
}