package com.ciyu.app.controller;

import com.ciyu.app.dto.bond.CreateBondDto;
import com.ciyu.app.dto.bond.GetBondsDto;
import com.ciyu.app.dto.meet.CreateMeetDto;
import com.ciyu.app.pojo.*;
import com.ciyu.app.security.CurrentUser;
import com.ciyu.app.service.bond.BondService;
import com.ciyu.app.service.meaning.MeaningService;
import com.ciyu.app.service.meet.MeetService;
import com.ciyu.app.service.word.WordService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.EntityUtils;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequiredArgsConstructor
@RequestMapping("/api/v1/bonds") @Slf4j
public class BondController {
    private final BondService bondService;
    private final MeetService meetService;
    private final MeaningService meaningService;
    private final WordService wordService;

    @PostMapping
    public void createBond(@CurrentUser User user, @RequestBody CreateBondDto createBondDto) {
        Meaning meaning = meaningService.findMeaningById(createBondDto.getMeaningId());
        Word word = meaning.getWord();
        Bond bond = new Bond().setUser(user).setWord(word);
        bondService.saveBond(bond);
        meetService.saveMeet(new Meet().setBond(bond).setArticleId(createBondDto.getArticleId()).setMeaning(meaning).setQuality(createBondDto.getQuality()));
    }

//    @GetMapping
//    public Bond getBond(@CurrentUser User user, String wordId) {
//        Word word = wordService.findWordById(wordId);
//        return bondService.findBondByUserAndWord(user, word);
//    }

//    @GetMapping
//    public List<Bond> getBonds(@CurrentUser User user) {
//        return bondService.findAllBondsByUser(user);
//    }

    @GetMapping @PageableAsQueryParam
    public List<GetBondsDto> getBonds(@CurrentUser User user, @Parameter(hidden = true) Pageable pageable) {
        Page<Bond> bonds = bondService.findBondPageByUser(user, pageable);
        return bonds.stream().map(bond -> {
            GetBondsDto getBondsDto = new GetBondsDto();
            BeanUtils.copyProperties(bond, getBondsDto);
            getBondsDto.setStrength(bondService.calculateStrengthByBond(bond));
            return getBondsDto;
        }).toList();
    }

    @DeleteMapping
    public void removeBond(@CurrentUser User user, String wordId) {
        Word word = wordService.findWordById(wordId);
        bondService.removeBondByUserAndWord(user, word);
    }
}
