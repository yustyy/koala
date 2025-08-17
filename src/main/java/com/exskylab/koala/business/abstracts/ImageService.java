package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.entities.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ImageService {
    Image uploadImage(MultipartFile image);

    void deleteImage(UUID imageId);

}
