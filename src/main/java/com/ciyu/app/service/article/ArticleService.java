package com.ciyu.app.service.article;

import com.ciyu.app.pojo.Article;
import com.ciyu.app.pojo.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;

public interface ArticleService {
    SearchHits<Article> searchBestArticlesByUser(User user, Pageable pageable, List<String> excludedArticleIds);

    SearchHits<Article> searchArticlesByUserAndWordId(User user, String wordId, Pageable pageable);
}
