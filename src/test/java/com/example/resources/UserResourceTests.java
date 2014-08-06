package com.example.resources;

import com.example.core.User;
import com.example.core.UserTests;
import com.sun.jersey.api.client.GenericType;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

/**
 * Tests {@link io.dropwizard.testing.junit.ResourceTestRule}
 */
public class UserResourceTests {

    static {
        Logger.getLogger("com.sun.jersey").setLevel(Level.OFF);
    }

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new UserResource())
            .build();

    @Test
    public void getAll() throws Exception {
        List<User> users = resources.client().resource("/user").get(new GenericType<List<User>>() {});
        assertEquals(2, users.size());
        assertEquals("user1", users.get(0).getUsername());
    }

    @Test
    public void get() throws Exception {
        User user = resources.client().resource("/user/test1").get(User.class);
        assertEquals("test1", user.getUsername());
    }

    @Test
    public void update() throws Exception {
        User user = UserTests.getUser();

        User updatedUser = resources.client().resource("/user/test1")
                .type(MediaType.APPLICATION_JSON)
                .put(User.class, user);

        assertEquals(user, updatedUser);
    }

    @Test(expected = ConstraintViolationException.class)
    public void update_invalid_user() throws Exception {
        User user = UserTests.getUser().setDisplayName("");

        User updatedUser = resources.client().resource("/user/test1")
                .type(MediaType.APPLICATION_JSON)
                .put(User.class, user);
    }

    @Test()
    public void add() throws Exception {
        User newUser = UserTests.getUser();

        User user = resources.client().resource("/user")
                .type(MediaType.APPLICATION_JSON)
                .post(User.class, newUser);

        assertEquals(newUser, user);
    }

    @Test(expected = ConstraintViolationException.class)
    public void add_invalid_user() throws Exception {
        User newUser = UserTests.getUser().setUsername(null);

        User user = resources.client().resource("/user")
                .type(MediaType.APPLICATION_JSON)
                .post(User.class, newUser);
    }

    @Test()
    public void delete() throws Exception {
        resources.client().resource("/user/test1").delete();
    }
}
