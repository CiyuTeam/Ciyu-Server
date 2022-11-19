package com.ciyu.app.repository;

import com.ciyu.app.pojo.Article;
import net.minidev.json.JSONArray;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepo extends ElasticsearchRepository<Article, String> {
    @Query("""
            {
                "bool": {
                  "should": ?0
                }
            }""")
    Page<Article> search(JSONArray should, Pageable pageable);
}
