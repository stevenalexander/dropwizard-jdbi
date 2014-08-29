package com.example.dao;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

public interface PersonDAO {
    @SqlQuery("select NAME from PERSON where ID = :id")
    String findNameById(@Bind("id") int id);
}
