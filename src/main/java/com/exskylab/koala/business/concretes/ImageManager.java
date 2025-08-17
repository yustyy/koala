package com.exskylab.koala.business.concretes;

import com.exskylab.koala.business.abstracts.ImageService;
import com.exskylab.koala.business.abstracts.UserService;
import com.exskylab.koala.core.configs.r2.FolderType;
import com.exskylab.koala.core.constants.ImageMessages;
import com.exskylab.koala.core.exceptions.ImageUploadError;
import com.exskylab.koala.core.utilities.storage.R2StorageService;
import com.exskylab.koala.dataAccess.ImageDao;
import com.exskylab.koala.entities.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ImageManager implements ImageService {

    private final ImageDao imageDao;
    private final R2StorageService r2StorageService;
    private final static Logger logger = LoggerFactory.getLogger(ImageManager.class);


    public ImageManager(ImageDao imageDao, R2StorageService r2StorageService) {
        this.imageDao = imageDao;
        this.r2StorageService = r2StorageService;
    }


    @Override
    public Image uploadImage(MultipartFile image) {
        logger.info("Uploading image file: {}", image.getOriginalFilename());
        if (image.isEmpty()){
            logger.error("Image upload failed: Image file is empty.");
            throw new ImageUploadError(ImageMessages.IMAGE_EMPTY_ERROR);
        }

        if (image.getSize() > 10 * 1024 * 1024) {
            logger.error("Image upload failed: Image file size exceeds the limit of 10 MB.");
            throw new ImageUploadError(ImageMessages.IMAGE_SIZE_ERROR);
        }

        try {
            byte[] cleanImageBytes = removeMetadata(image);

            String imageUrl = r2StorageService.uploadFile(
                    cleanImageBytes,
                    image.getOriginalFilename(),
                    image.getContentType(),
                    FolderType.IMAGE);


            Image newImage = new Image();
            newImage.setFileName(image.getOriginalFilename());
            newImage.setFileType(image.getContentType());
            newImage.setFileUrl(imageUrl);
            newImage.setFileSize((long) cleanImageBytes.length);

            var savedImage = imageDao.save(newImage);
            logger.info("Image uploaded successfully: {}", savedImage.getFileName());
            return savedImage;
        } catch (Exception e) {
            logger.error("Image upload failed fileName: {}, errorMessage: {}", image.getOriginalFilename(), e.getMessage());
            throw new ImageUploadError(ImageMessages.IMAGE_UPLOAD_ERROR);
        }
    }


    private byte[] removeMetadata(MultipartFile image) throws IOException {
        BufferedImage originalImage = ImageIO.read(image.getInputStream());

        if (originalImage == null){
            throw new IOException("Image file is empty.");
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        String formatName = getFileExtension(image.getOriginalFilename()).substring(1);


        ImageIO.write(originalImage, formatName, outputStream);

        return outputStream.toByteArray();
    }

    private String getFileExtension(String fileName) {

        if (fileName == null || !fileName.contains(".")) {
            throw new IllegalArgumentException("File has no extension: " + fileName);
        }

        return fileName.substring(fileName.lastIndexOf("."));

    }
}
