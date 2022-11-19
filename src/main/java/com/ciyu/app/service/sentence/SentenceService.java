package com.ciyu.app.service.sentence;

import com.ciyu.app.pojo.Sentence;
import com.ciyu.app.pojo.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SentenceService {
    Iterable<Sentence> searchBestSentencesByUser(User user, Pageable pageable, List<String> excludedSentenceIds);
}
