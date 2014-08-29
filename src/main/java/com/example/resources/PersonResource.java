package com.example.resources;

import com.example.core.Person;
import com.example.dao.PersonDAO;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;
import java.util.List;

@Path("/person")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class PersonResource {

    PersonDAO personDAO;

    public PersonResource(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GET
    public List<Person> getAll(){

        List<Person> persons = new LinkedList<>();
        persons.add(
            new Person()
                .setId(1)
                .setName("person1")
        );
        persons.add(
            new Person()
                .setId(2)
                .setName("person2")
        );

        return persons;
    }

    @GET
    @Path("/{id}")
    public Person get(@PathParam("id") Integer id){
        String name = personDAO.findNameById(1);

        return new Person()
            .setId(id)
            .setName(name);
    }

    @POST
    public Person add(@Valid Person person) {
        return person;
    }

    @PUT
    @Path("/{id}")
    public Person update(@PathParam("id") Integer id, @Valid Person person) {
        return person;
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Integer id) {
    }
}
