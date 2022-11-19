package com.ciyu.app.repository;

import com.ciyu.app.pojo.Bond;
import com.ciyu.app.pojo.User;
import com.ciyu.app.pojo.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BondRepo extends CrudRepository<Bond, String> {
    boolean existsByUserAndWord(User user, @NotNull Word word);
    Optional<Bond> findByUserAndWord(User user, @NotNull Word word);

    @Query(value="SELECT * FROM BOND WHERE USER_ID = :userId AND WORD_ID = :wordId", nativeQuery=true)
    Optional<Bond> findAnyByUserAndWord(String userId, @NotNull String wordId);

    List<Bond> findAllByUser(User user);
    Page<Bond> findAllByUser(User user, Pageable pageable);
}