package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.entities.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image uploadImage(MultipartFile image);
}
