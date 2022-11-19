package com.ciyu.app.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Document(indexName = "ciyu")
@Data
public class Article {
    @Id
    @Field("id")
    String id;
    @Field("content")
    String content;
    @Field("title")
    String title;
    @Field("thumbnail")
    String thumbnail;
}
