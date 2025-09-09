package com.globus.cargo.users;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void register_addsUser_andListPreservesInsertionOrder() {
        UserService service = new UserService();

        User u1 = new User(1L, "Juan", "juan@example.com");
        User u2 = new User(2L, "Ana", "ana@example.com");

        service.register(u1);
        service.register(u2);

        List<User> list = service.list();
        assertEquals(2, list.size());
        assertEquals("juan@example.com", list.get(0).getEmail());
        assertEquals("ana@example.com", list.get(1).getEmail());
    }

    @Test
    void register_throwsOnDuplicateEmail_caseInsensitive() {
        UserService service = new UserService();

        service.register(new User(1L, "Juan", "DUP@example.com"));

        Executable attempt = () -> service.register(new User(2L, "Ana", "dup@example.com"));
        assertThrows(DuplicateEmailException.class, attempt);
    }
}

