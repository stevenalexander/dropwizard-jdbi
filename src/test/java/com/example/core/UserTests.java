package com.example.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.junit.Assert.assertEquals;

public class UserTests {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private static final String NULL_ERROR_MESSAGE = "may not be null";
    private static final String EMPTY_ERROR_MESSAGE = "may not be empty";

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void serializesToJson() throws Exception {
        final User user = getUser();
        assertEquals(fixture("fixtures/user.json"), MAPPER.writeValueAsString(user));
    }

    @Test
    public void deserializesFromJSON() throws Exception {
        final User user = getUser();
        assertEquals(user, MAPPER.readValue(fixture("fixtures/user.json"), User.class));
    }

    // Should be replaced with individual class field validator tests
    @Test
    public void validate_not_null_or_empty() throws Exception {
        User user = new User();

        Set<ConstraintViolation<User>> constraintViolations =
                validator.validate(user);

        assertEquals(4, constraintViolations.size());
        assertEquals(EMPTY_ERROR_MESSAGE, constraintViolations.iterator().next().getMessage());
    }

    public static User getUser() {
        User user = new User();
        user.setUsername("myName");
        user.setPassword("myPassword");
        user.setDisplayName("myDisplayName");
        user.setDisplayRole("myDisplayRole");

        return user;
    }
}
