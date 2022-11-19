package com.ciyu.app.service.bond;

import com.ciyu.app.pojo.Bond;
import com.ciyu.app.pojo.User;
import com.ciyu.app.pojo.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/*
需要有以下功能：
1. (生词id) => 生词
2. () => 单词
 */
public interface BondService {
    void saveBond(Bond bond);
    Bond findBondById(String id);
    Bond findBondByUserAndWord(User user, Word word);

    Bond findAnyBondByUserAndWord(User user, Word word);

    List<Bond> findAllBondsByUser(User user);

    Page<Bond> findBondPageByUser(User user, Pageable pageable);

    double calculateStrengthByBond(Bond bond);
    void removeBondByUserAndWord(User user, Word word);
}
