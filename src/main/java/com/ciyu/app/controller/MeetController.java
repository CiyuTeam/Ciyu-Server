package com.ciyu.app.controller;

import com.ciyu.app.dto.meet.CreateMeetsDto;
import com.ciyu.app.dto.meet.CreateMeetDto;
import com.ciyu.app.pojo.*;
import com.ciyu.app.security.CurrentUser;
import com.ciyu.app.service.bond.BondService;
import com.ciyu.app.service.meaning.MeaningService;
import com.ciyu.app.service.meet.MeetService;
import com.ciyu.app.service.word.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/meets")
public class MeetController {
    private final MeetService meetService;
    private final BondService bondService;
    private final MeaningService meaningService;
    private final WordService wordService;

    @PostMapping
    public void createMeet(@CurrentUser User user, @RequestBody CreateMeetDto createMeetDto) {
        Meaning meaning = meaningService.findMeaningById(createMeetDto.getMeaningId());
        Word word = meaning.getWord();
        Bond bond = bondService.findBondByUserAndWord(user, word);
        meetService.saveMeet(new Meet().setBond(bond).setMeaning(meaning).setArticleId(createMeetDto.getArticleId()).setQuality(createMeetDto.getQuality()));
    }

    @PostMapping("/batch")
    public void createMeets(@CurrentUser User user, @RequestBody CreateMeetsDto createMeetsDto) {
        createMeetsDto.getMeaningIds().forEach(meaningId -> {
            Meaning meaning = meaningService.findMeaningById(meaningId);
            Word word = meaning.getWord();
            Bond bond = bondService.findBondByUserAndWord(user, word);
            meetService.saveMeet(new Meet().setBond(bond).setMeaning(meaning).setArticleId(createMeetsDto.getArticleId()).setQuality(1.0));
        });
    }

    @GetMapping
    public List<Meet> getMeets(@CurrentUser User user, String wordId) {
        Word word = wordService.findWordById(wordId);
        return bondService.findBondByUserAndWord(user, word).getMeets();
    }
}
