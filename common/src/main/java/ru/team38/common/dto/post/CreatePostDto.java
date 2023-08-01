package ru.team38.common.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostDto {
    private UUID id;
    private String imagePath;
    private String postText;
    private String publishDate;
    private List<TagDto> tags;
    private String title;
}
