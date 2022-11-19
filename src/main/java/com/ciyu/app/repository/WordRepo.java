package com.ciyu.app.repository;

import com.ciyu.app.pojo.Word;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WordRepo extends CrudRepository<Word, String> {
}
