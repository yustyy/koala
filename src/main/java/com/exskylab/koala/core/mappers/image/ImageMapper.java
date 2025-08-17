package com.exskylab.koala.core.mappers.image;

import com.exskylab.koala.core.dtos.images.response.UploadImageResponseDto;
import com.exskylab.koala.core.properties.R2Properties;
import com.exskylab.koala.entities.Image;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ImageMapper {

    private final R2Properties r2Properties;

    public ImageMapper(R2Properties r2Properties) {
        this.r2Properties = r2Properties;
    }

    public UploadImageResponseDto toUploadImageResponseDto(Image image) {
        return new UploadImageResponseDto(
                image.getId(),
                image.getFileName(),
                image.getFileType(),
                r2Properties.getPublicUrl()+ "/" + image.getFileUrl(),
                image.getFileSize()
        );
    }


}
