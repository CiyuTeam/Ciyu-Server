package com.ciyu.app.repository;

import com.ciyu.app.pojo.Meaning;
import com.ciyu.app.pojo.Phonetic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository @Transactional
public interface PhoneticRepo extends CrudRepository<Phonetic, String> {
}