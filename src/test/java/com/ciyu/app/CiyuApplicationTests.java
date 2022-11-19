package com.ciyu.app;

import com.ciyu.app.pojo.Meaning;
import com.ciyu.app.pojo.Phonetic;
import com.ciyu.app.pojo.Sentence;
import com.ciyu.app.pojo.Word;
import com.ciyu.app.repository.SentenceRepo;
import com.ciyu.app.repository.WordRepo;
import com.ciyu.app.service.word.WordService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootTest
class CiyuApplicationTests {
    @Autowired
    private WordRepo wordRepo;
    @Autowired
    private SentenceRepo sentenceRepo;

    @Test
    void addSentencesForAllWords() {
        wordRepo.findAll().forEach(word -> {
            String url = "https://youdao.com/result?lang=en&word=lj:" + word.getId();
            String body = new RestTemplate().getForObject(url, String.class);
            if (body == null) throw new RuntimeException("Body is Null");
            Document doc = Jsoup.parse(body);
            List<Sentence> sentences = doc.select(".col2").stream().map(element ->
                    new Sentence()
                            .setContent(element.select(".sen-eng").text())
                            .setTranslation(element.select(".sen-ch").text())
                            .setSource(element.select(".secondary").text())
                            .setWord(word)).toList();
            word.setSentences(sentences);
            sentenceRepo.saveAll(sentences);
//            word.setSentences(sentences).getSentences().stream().map(Sentence::getContent).forEach(System.out::println);
//            word.setSentences(sentences).getSentences().stream().map(Sentence::getTranslation).forEach(System.out::println);
//            word.setSentences(sentences).getSentences().stream().map(Sentence::getSource).forEach(System.out::println);
        });
    }

    @Test
    void requestWord() {
        wordRepo.findAll().forEach(word -> {
            String url = "https://youdao.com/result?lang=en&word=" + word.getId();
            String body = new RestTemplate().getForObject(url, String.class);
            if (body == null) throw new RuntimeException("Body is Null");
            Document doc = Jsoup.parse(body);
            List<Phonetic> phonetics = doc.select(".per-phone").stream().map(element ->
                    new Phonetic()
                            .setLocale(element.select("span:first-child").text())
                            .setSymbol(element.select(".phonetic").text())
                            .setWord(word)
                            .setAudio(String.format("https://dict.youdao.com/dictvoice?audio=%s&type=%d", word.getId(), element.ownText().equals("英") ? 1 : 2))).toList();
            List<String> definitions = doc.select(".basic>.word-exp").eachText();
            List<Meaning> meanings = definitions.stream().map(definition -> new Meaning().setDefinition(definition).setWord(word)).toList();

            meanings.stream().map(Meaning::getDefinition).forEach(System.out::println);
        });
    }
}
