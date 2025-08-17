package com.exskylab.koala.webAPI;

import com.exskylab.koala.business.abstracts.ImageService;
import com.exskylab.koala.core.constants.ImageMessages;
import com.exskylab.koala.core.dtos.images.response.UploadImageResponseDto;
import com.exskylab.koala.core.mappers.image.ImageMapper;
import com.exskylab.koala.core.utilities.results.SuccessDataResult;
import com.exskylab.koala.entities.Image;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequestMapping("/api/images")
public class ImagesController {

    private final ImageService imageService;
    private final ImageMapper imageMapper;


    public ImagesController(ImageService imageService, ImageMapper imageMapper) {
        this.imageService = imageService;
        this.imageMapper = imageMapper;
    }



    @PostMapping("/uploadImage")
    public ResponseEntity<SuccessDataResult<UploadImageResponseDto>> uploadImage(@RequestParam("image") MultipartFile image, HttpServletRequest request){

        Image responseImage = imageService.uploadImage(image);

        var imageDto = imageMapper.toUploadImageResponseDto(responseImage);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SuccessDataResult<UploadImageResponseDto>(imageDto,
                        ImageMessages.UPLOAD_IMAGE_SUCCESS,
                        HttpStatus.CREATED,
                        request.getRequestURI())
        );

    }
}
