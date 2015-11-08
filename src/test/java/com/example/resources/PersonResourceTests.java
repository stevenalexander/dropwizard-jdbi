package com.example.resources;

import com.example.core.Person;
import com.example.core.PersonTests;
import com.example.dao.PersonDAO;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * Tests {@link io.dropwizard.testing.junit.ResourceTestRule}
 */
public class PersonResourceTests {

    private static final PersonDAO personDAO = mock(PersonDAO.class);

    static {
        Logger.getLogger("com.sun.jersey").setLevel(Level.OFF);
    }

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new PersonResource(personDAO))
            .build();

    @Test
    public void getAll() throws Exception {
        List<Person> persons = new ArrayList<>();
        persons.add(new Person(1, "person1"));
        persons.add(new Person(2, "person2"));
        when(personDAO.getAll()).thenReturn(persons);

        List<Person> result = resources.client().target("/person").request().get(new GenericType<List<Person>>() {});

        assertEquals(2, result.size());
        assertEquals("person1", result.get(0).getName());
    }

    @Test
    public void get() throws Exception {
        when(personDAO.findById(1)).thenReturn(
                new Person(1, "person1")
        );

        Person person = resources.client().target("/person/1").request().get(new GenericType<Person>() {});

        assertEquals("person1", person.getName());
    }

    @Test
    public void update() throws Exception {
        Person person = PersonTests.getPerson();

        Person updatedPerson = resources.client().target("/person/10")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(person), Person.class);

        assertEquals(person.getId(), updatedPerson.getId());
        assertEquals(person.getName(), updatedPerson.getName());
        verify(personDAO, times(1)).update(person);
    }

    @Test
    public void update_invalid_person() throws Exception {
        Person person = PersonTests.getPerson();
        person.setName(null);

        try {
            Person updatedPerson = resources.client().target("/person/10")
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.json(person), Person.class);
        } catch (ProcessingException ex) {
            if (!ex.getCause().toString().contains("javax.validation.ConstraintViolationException")) {
                fail("Should have thrown validation error");
            }
        }
    }

    @Test()
    public void add() throws Exception {
        Person newPerson = PersonTests.getPerson();

        Person person = resources.client().target("/person")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(newPerson), Person.class);

        assertEquals(newPerson.getName(), person.getName());
        verify(personDAO, times(1)).insert(any(Person.class));
    }

    @Test
    public void add_invalid_person() throws Exception {
        Person newPerson = PersonTests.getPerson();
        newPerson.setName(null);

        try {
            Person person = resources.client().target("/person")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(newPerson), Person.class);
        } catch (ProcessingException ex) {
            if (!ex.getCause().toString().contains("javax.validation.ConstraintViolationException")) {
                fail("Should have thrown validation error");
            }
        }
    }

    @Test()
    public void delete() throws Exception {
        resources.client().target("/person/1").request().delete();
        verify(personDAO, times(1)).deleteById(1);
    }
}
