package com.ecom.service.impl;

import com.ecom.dto.ImageDto;
import com.ecom.exception.ResourceNotFoundException;
import com.ecom.model.Image;
import com.ecom.model.Product;
import com.ecom.repository.ImageRepository;
import com.ecom.service.IImageService;
import com.ecom.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements IImageService {

    private final IProductService productService;
    private final ImageRepository imageRepository;

    @Override
    @Transactional(readOnly = true)
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Image not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getImageBytes(Long id) {
        Image image = getImageById(id);
        if (image.getImage() == null) {
            return new byte[0];
        }
        try {
            return image.getImage().getBytes(1, (int) image.getImage().length());
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to read image bytes for id: " + id, e);
        }
    }

    @Override
    @Transactional
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(
                imageRepository::delete,
                () -> { throw new ResourceNotFoundException("Image Not Found with id: " + id); }
        );
    }

    @Override
    @Transactional
    public List<ImageDto> saveImage(List<MultipartFile> files, Long productId) {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("At least one image file is required");
        }

        Product product = productService.getProductById(productId);
        List<Image> savedImageDtos = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("Image file cannot be empty");
            }
            try {
                Image image = new Image();
                image.setFilename(file.getOriginalFilename());
                image.setFiletype(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buildDownloadUrl = "/api/v1/images/image/download/";
                Image savedImage = imageRepository.save(image);
                savedImage.setUrl(buildDownloadUrl + savedImage.getId());
                savedImageDtos.add(imageRepository.save(savedImage));
            } catch (IOException | SQLException exception) {
                throw new IllegalStateException("Unable to save image", exception);
            }
        }

        return savedImageDtos.stream()
                .map(ImageDto::new)
                .toList();
    }

    @Override
    @Transactional
    public void updateImage(MultipartFile file, Long imageId) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Image file cannot be empty");
        }

        Image image = getImageById(imageId);
        try {
            image.setFilename(file.getOriginalFilename());
            image.setFiletype(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (IOException | SQLException exception) {
            throw new IllegalStateException("Unable to update image", exception);
        }
    }
}
