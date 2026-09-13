package com.ecom.service.image;

import com.ecom.Model.Image;
import com.ecom.Model.Product;
import com.ecom.dto.ImageDto;
import com.ecom.exceptions.ResourceNotFoundException;
import com.ecom.repository.ImageRepository;
import com.ecom.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IimageService {

    private final IProductService productService;
    private final ImageRepository imageRepository;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Image not found with id: " + id));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(
                imageRepository::delete,
                () -> { throw new ResourceNotFoundException("Image Not Found with id: " + id); }
        );
    }

    @Override
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
