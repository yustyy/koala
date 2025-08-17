package com.exskylab.koala.core.dtos.images.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Getter
@Service
@AllArgsConstructor
@NoArgsConstructor
public class UploadImageResponseDto {

    private UUID id;

    private String fileName;

    private String fileType;

    private String fileUrl;

    private Long fileSize;

}
