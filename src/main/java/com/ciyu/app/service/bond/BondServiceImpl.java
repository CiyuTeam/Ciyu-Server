package com.ciyu.app.service.bond;

import com.ciyu.app.pojo.Bond;
import com.ciyu.app.pojo.Meet;
import com.ciyu.app.pojo.User;
import com.ciyu.app.pojo.Word;
import com.ciyu.app.repository.BondRepo;
import com.ciyu.app.repository.MeetRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service @RequiredArgsConstructor @Slf4j
public class BondServiceImpl implements BondService {
    private final BondRepo bondRepo;
    private final MeetRepo meetRepo;

    @Override
    public void saveBond(Bond bond) {
        try {
            Bond existedBond = findAnyBondByUserAndWord(bond.getUser(), bond.getWord());
            existedBond.setDeleted(false);
            bondRepo.save(existedBond);
            bond.setId(existedBond.getId());
        } catch (Exception exception) {
            bondRepo.save(bond);
        }
    }

    @Override
    public Bond findBondById(String id) {
        return bondRepo.findById(id).orElseThrow(() -> new RuntimeException("Bond Not Found"));
    }

    @Override
    public Bond findBondByUserAndWord(User user, Word word) {
        return bondRepo.findByUserAndWord(user, word).orElseThrow(() -> new RuntimeException("Bond Not Found"));
    }

    @Override
    public Bond findAnyBondByUserAndWord(User user, Word word) {
        return bondRepo.findAnyByUserAndWord(user.getId(), word.getId()).orElseThrow(() -> new RuntimeException("Bond Not Found"));
    }
    @Override
    public List<Bond> findAllBondsByUser(User user) {
        return bondRepo.findAllByUser(user);
    }

    @Override
    public Page<Bond> findBondPageByUser(User user, Pageable pageable) {
        return bondRepo.findAllByUser(user, pageable);
    }

    @Override
    public double calculateStrengthByBond(Bond bond) {
        List<Meet> meets = meetRepo.findTop2ByBondOrderByCreatedTimeDesc(bond);
        if (meets.size() == 0)
            throw new RuntimeException("Meet Not Found");

        double base = 0.5;

        LocalDateTime lastMeetTime = meets.get(0).getCreatedTime();
        if (meets.size() >= 2) {
            LocalDateTime baseMeetTime = meets.get(1).getCreatedTime();
            double quality = meets.get(0).getQuality();
            long durationInSecond = lastMeetTime.toEpochSecond(ZoneOffset.UTC) - baseMeetTime.toEpochSecond(ZoneOffset.UTC);
            double durationInDay = durationInSecond / 86400.0;
            log.info(meets.get(0).getQuality().toString());
            log.info(meets.get(1).getQuality().toString());
            base = Math.pow(quality, 1.0 / 2 * durationInDay);
        }

        long elapsedInSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - lastMeetTime.toEpochSecond(ZoneOffset.UTC);
        double elapsedInDay = elapsedInSecond / 86400.0;
        return Math.pow(base, elapsedInDay);
    }

    @Override
    public void removeBondByUserAndWord(User user, Word word) {
        Bond bond = findBondByUserAndWord(user, word);
        bondRepo.delete(bond);
    }
}
