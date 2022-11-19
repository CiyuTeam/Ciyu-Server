package com.ciyu.app.dto.word;

import com.ciyu.app.pojo.Meaning;
import com.ciyu.app.pojo.Word;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data @Accessors(chain = true)
public class WordDto extends Word {
    Boolean hasBond;
}
