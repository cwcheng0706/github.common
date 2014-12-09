package com.zy.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.zy.mongodb.entity.Person;


@Repository
public interface PersonRepository extends MongoRepository<Person,String>{

}
