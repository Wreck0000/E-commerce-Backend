package com.ecom.service.image;

import com.ecom.Model.Image;
import com.ecom.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IimageService {
    Image getImageById(Long id);
    void  deleteImageById(Long id);
    List<ImageDto> saveImage(List<MultipartFile> files, Long productId);
    void updateImage(MultipartFile file, Long imageId);

















}
