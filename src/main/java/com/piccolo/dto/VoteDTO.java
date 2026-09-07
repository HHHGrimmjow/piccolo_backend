package com.piccolo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VoteDTO {

    @NotNull(message = "请选择一个选项")
    private Long optionId;
}
