package com.exskylab.koala.core.mappers.image;

import com.exskylab.koala.core.dtos.images.response.UploadImageResponseDto;
import com.exskylab.koala.entities.Image;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ImageMapper {

    public UploadImageResponseDto toUploadImageResponseDto(Image image) {
        return new UploadImageResponseDto(
                image.getId(),
                image.getFileName(),
                image.getFileType(),
                image.getFileUrl(),
                image.getFileSize()
        );
    }


}
