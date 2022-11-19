package com.ciyu.app.repository;

import com.ciyu.app.pojo.Bond;
import com.ciyu.app.pojo.Meet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MeetRepo extends CrudRepository<Meet, UUID> {
    Optional<Meet> findTopByOrderByCreatedTimeDesc();
    List<Meet> findTop2ByBondOrderByCreatedTimeDesc(Bond bond);
}