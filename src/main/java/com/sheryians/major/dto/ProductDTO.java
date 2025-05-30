package com.sheryians.major.dto;

import com.sheryians.major.model.Category;
import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private int categoryId;
    private double price;
    private double weight;
    private String description;

    // Field for image name if storing the file
    private String imageName;

    // New field for image URL
    private String imageUrl;
}
