package com.example.resources;

import com.example.core.Person;
import com.example.core.PersonTests;
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
public class PersonResourceTests {

    static {
        Logger.getLogger("com.sun.jersey").setLevel(Level.OFF);
    }

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new PersonResource())
            .build();

    @Test
    public void getAll() throws Exception {
        List<Person> persons = resources.client().resource("/person").get(new GenericType<List<Person>>() {});
        assertEquals(2, persons.size());
        assertEquals("person1", persons.get(0).getName());
    }

    @Test
    public void get() throws Exception {
        Person person = resources.client().resource("/person/1").get(Person.class);
        assertEquals("person1", person.getName());
    }

    @Test
    public void update() throws Exception {
        Person person = PersonTests.getPerson();

        Person updatedPerson = resources.client().resource("/person/1")
                .type(MediaType.APPLICATION_JSON)
                .put(Person.class, person);

        assertEquals(person, updatedPerson);
    }

    @Test(expected = ConstraintViolationException.class)
    public void update_invalid_person() throws Exception {
        Person person = PersonTests.getPerson().setName(null);

        Person updatedPerson = resources.client().resource("/person/1")
                .type(MediaType.APPLICATION_JSON)
                .put(Person.class, person);
    }

    @Test()
    public void add() throws Exception {
        Person newPerson = PersonTests.getPerson();

        Person person = resources.client().resource("/person")
                .type(MediaType.APPLICATION_JSON)
                .post(Person.class, newPerson);

        assertEquals(newPerson, person);
    }

    @Test(expected = ConstraintViolationException.class)
    public void add_invalid_person() throws Exception {
        Person newPerson = PersonTests.getPerson().setName(null);

        Person person = resources.client().resource("/person")
                .type(MediaType.APPLICATION_JSON)
                .post(Person.class, newPerson);
    }

    @Test()
    public void delete() throws Exception {
        resources.client().resource("/person/1").delete();
    }
}
