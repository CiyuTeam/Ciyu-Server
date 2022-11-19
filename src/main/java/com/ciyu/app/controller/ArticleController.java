package com.ciyu.app.controller;

import com.ciyu.app.pojo.Article;
import com.ciyu.app.pojo.User;
import com.ciyu.app.security.CurrentUser;
import com.ciyu.app.service.article.ArticleService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ArticleController {
    private final ArticleService articleService;

//    @GetMapping
//    public SearchHits<Article> getBestArticles(@CurrentUser User user, Pageable pageable, List<String> excludedArticleIds) {
//        return articleService.searchBestArticlesByUser(user, pageable, excludedArticleIds);
//    }

    @GetMapping @PageableAsQueryParam
    public SearchHits<Article> getBestArticles(@CurrentUser User user, @Parameter(hidden = true) Pageable pageable, @RequestParam List<String> excludedArticleIds) {
        return articleService.searchBestArticlesByUser(user, pageable, excludedArticleIds);
    }

    @GetMapping("/word") @PageableAsQueryParam
    public SearchHits<Article> searchByWordId(@CurrentUser User user, String wordId, @Parameter(hidden = true) Pageable pageable) {
        return articleService.searchArticlesByUserAndWordId(user, wordId, pageable);
    }
}
