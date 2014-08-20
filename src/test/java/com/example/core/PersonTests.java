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

public class PersonTests {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private static final String NULL_ERROR_MESSAGE = "may not be null";

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void serializesToJson() throws Exception {
        final Person person = getPerson();
        assertEquals(fixture("fixtures/person.json"), MAPPER.writeValueAsString(person));
    }

    @Test
    public void deserializesFromJSON() throws Exception {
        final Person person = getPerson();
        assertEquals(person, MAPPER.readValue(fixture("fixtures/person.json"), Person.class));
    }

    // Should be replaced with individual class field validator tests
    @Test
    public void validate_not_null() throws Exception {
        Person person = new Person();

        Set<ConstraintViolation<Person>> constraintViolations =
                validator.validate(person);

        assertEquals(2, constraintViolations.size());
        assertEquals(NULL_ERROR_MESSAGE, constraintViolations.iterator().next().getMessage());
    }

    public static Person getPerson() {
        return new Person()
                .setId(10)
                .setName("person10");
    }
}
