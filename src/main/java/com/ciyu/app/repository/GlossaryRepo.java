package com.ciyu.app.repository;

import com.ciyu.app.pojo.Glossary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface GlossaryRepo extends CrudRepository<Glossary, String> {
}
