package com.piccolo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TopicDTO {

    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题最长200个字符")
    private String title;

    private String description;

    private String imageUrl;

    private LocalDateTime deadline;

    @NotEmpty(message = "至少需要2个选项")
    @Size(min = 2, max = 10, message = "选项数量为2-10个")
    private List<OptionDTO> options;

    @Data
    public static class OptionDTO {
        @NotBlank(message = "选项内容不能为空")
        @Size(max = 200, message = "选项内容最长200个字符")
        private String optionText;

        private String optionImage;
    }
}
