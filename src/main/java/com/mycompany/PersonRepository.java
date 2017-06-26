package com.mycompany;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by rajaramkumar on 6/24/17.
 */
public interface PersonRepository extends CrudRepository<RestDocsDemoApplication.Person, Long> {


}