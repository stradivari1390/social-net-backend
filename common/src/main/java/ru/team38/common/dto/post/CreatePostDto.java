package ru.team38.common.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostDto {
    private Long id;
    private String imagePath;
    private String postText;
    private String publishDate;
    private List<TagDto> tags;
    private String title;
}
