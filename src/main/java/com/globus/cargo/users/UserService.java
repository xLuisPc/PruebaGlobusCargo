package com.globus.cargo.users;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    // Mantiene el orden de inserción
    private final Map<String, User> usersByEmail = new LinkedHashMap<>();

    public synchronized void register(User user) {
        String key = normalize(user.getEmail());
        if (usersByEmail.containsKey(key)) {
            throw new DuplicateEmailException("El email ya está registrado.");
        }
        usersByEmail.put(key, user);
    }

    public synchronized List<User> list() {
        return new ArrayList<>(usersByEmail.values());
    }

    private String normalize(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }
}

