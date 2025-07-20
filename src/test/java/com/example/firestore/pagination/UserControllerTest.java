package com.example.firestore.pagination;

import com.google.cloud.firestore.DocumentSnapshot;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserPaginatedRepository userRepository;

    @Test
    void getUsers_returnsPaginatedUsers() throws Exception {
        User user = new User("id1", "Alice");
        when(userRepository.getPaginatedResults(anyInt(), any(DocumentSnapshot.class)))
                .thenReturn(List.of(user));

        mockMvc.perform(get("/api/users")
                .param("limit", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value("id1"))
                .andExpect(jsonPath("$.items[0].name").value("Alice"));
    }
}