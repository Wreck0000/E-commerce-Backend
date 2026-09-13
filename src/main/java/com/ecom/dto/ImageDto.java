package com.ecom.dto;

import com.ecom.Model.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
public class ImageDto {
    private Long id;
    private String filename;
    private String filetype;
    private String url;

    public ImageDto(Image image) {
        this.id = image.getId();
        this.filename = image.getFilename();
        this.filetype = image.getFiletype();
        this.url = image.getUrl();
    }
}
