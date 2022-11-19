package com.ciyu.app.service.word;

import com.ciyu.app.dto.word.WordDto;
import com.ciyu.app.pojo.Meaning;
import com.ciyu.app.pojo.Phonetic;
import com.ciyu.app.pojo.User;
import com.ciyu.app.pojo.Word;
import com.ciyu.app.repository.BondRepo;
import com.ciyu.app.repository.MeaningRepo;
import com.ciyu.app.repository.PhoneticRepo;
import com.ciyu.app.repository.WordRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OnlineWordService implements WordService {
    private final WordRepo wordRepo;
    private final MeaningRepo meaningRepo;
    private final PhoneticRepo phoneticRepo;
    private final BondRepo bondRepo;

    @Override
    public WordDto findWordDtoByIdAndUser(String id, User user) {
        return wordRepo.findById(id).map(word -> {
            WordDto wordDto = new WordDto();
            BeanUtils.copyProperties(word, wordDto);
            boolean hasBond = bondRepo.existsByUserAndWord(user, word);
            return wordDto.setHasBond(hasBond);
        }).orElseGet(() -> {
            WordDto wordDto = new WordDto();
            Word word = requestWordById(id);
            List<Meaning> meanings = word.getMeanings();
            List<Phonetic> phonetics = word.getPhonetics();
            wordRepo.save(word);
            meaningRepo.saveAll(meanings);
            phoneticRepo.saveAll(phonetics);
            BeanUtils.copyProperties(word, wordDto);
            return wordDto.setHasBond(false);
        });
    }

    @Override
    public Word findWordById(String id) {
        return wordRepo.findById(id).orElseThrow(() -> new RuntimeException("Word Not Found"));
    }

    private Word requestWordById(String id) {
        String url = "https://youdao.com/result?lang=en&word=" + id;
        String body = new RestTemplate().getForObject(url, String.class);
        if (body == null) throw new RuntimeException("Body is Null");
        Document doc = Jsoup.parse(body);
        // String id = doc.select(".word").text()
        Word word = new Word().setId(id);
        List<Phonetic> phonetics = doc.select(".per-phone").stream().map(element ->
                new Phonetic()
                        .setLocale(element.select("span:first-child").text())
                        .setSymbol(element.select(".phonetic").text())
                        .setWord(word)
                        .setAudio(String.format("https://dict.youdao.com/dictvoice?audio=%s&type=%d", id, element.ownText().equals("英") ? 1 : 2))).toList();
        List<String> definitions = doc.select(".basic>.word-exp").eachText();
        List<Meaning> meanings = definitions.stream().map(definition -> new Meaning().setDefinition(definition).setWord(word)).toList();
        return word.setMeanings(meanings).setPhonetics(phonetics);
    }
}
