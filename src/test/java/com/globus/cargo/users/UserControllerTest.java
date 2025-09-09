package com.globus.cargo.users;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /users -> 201 Created con payload válido")
    void createUser_returnsCreated() throws Exception {
        String body = "{" +
                "\"id\": 101," +
                "\"name\": \"Usuario OK\"," +
                "\"email\": \"ok101@example.com\"" +
                "}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Usuario registrado exitosamente."));
    }

    @Test
    @DisplayName("POST /users -> 409 Conflict por email duplicado")
    void createUser_duplicateEmail_returnsConflict() throws Exception {
        String body1 = "{" +
                "\"id\": 201," +
                "\"name\": \"Usuario 1\"," +
                "\"email\": \"dup201@example.com\"" +
                "}";
        String body2 = "{" +
                "\"id\": 202," +
                "\"name\": \"Usuario 2\"," +
                "\"email\": \"DUP201@example.com\"" +
                "}";

        // Primero crea
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body1))
                .andExpect(status().isCreated());

        // Luego duplica (case-insensitive)
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body2))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error", containsString("registrado")));
    }

    @Test
    @DisplayName("POST /users -> 400 Bad Request por validación")
    void createUser_validationError_returnsBadRequest() throws Exception {
        // Falta 'name'
        String invalid = "{" +
                "\"id\": 301," +
                "\"email\": \"sin-nombre@example.com\"" +
                "}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalid))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("name")));
    }
}

