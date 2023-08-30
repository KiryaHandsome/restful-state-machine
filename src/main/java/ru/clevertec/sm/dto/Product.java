package ru.clevertec.sm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Integer id;
    private String title;
    private String description;
    private Integer price;
    private Double discountPercentage;
    private Float rating;
    private Integer stock;
    private String brand;
    private String category;
}
