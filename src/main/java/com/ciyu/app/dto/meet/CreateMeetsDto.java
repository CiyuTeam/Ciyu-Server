package com.ciyu.app.dto.meet;

import lombok.Data;

import java.util.List;

@Data
public class CreateMeetsDto {
    private String articleId;
    private List<String> meaningIds;
}
