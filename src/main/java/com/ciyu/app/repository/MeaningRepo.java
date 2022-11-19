package com.ciyu.app.repository;

import com.ciyu.app.pojo.Meaning;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.UUID;

@Repository @Transactional
public interface MeaningRepo extends CrudRepository<Meaning, String> {
}