package com.example.dao;

import com.example.core.Person;
import com.example.core.mapper.PersonMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(PersonMapper.class)
public interface PersonDAO {
    @SqlQuery("select ID, NAME from PERSON where ID = :id")
    Person findPersonById(@Bind("id") int id);
}
