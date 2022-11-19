package com.ciyu.app.dto.bond;

import com.ciyu.app.pojo.Word;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the {@link com.ciyu.app.pojo.Bond} entity
 */
@Data
public class GetBondsDto {
    private String id;
    private Word word;
    private Double strength;
}