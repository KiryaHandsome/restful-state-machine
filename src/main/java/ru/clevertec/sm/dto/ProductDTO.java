package ru.clevertec.sm.dto;

import lombok.Data;

public record ProductDTO(Integer id,
                         String title,
                         String description,
                         Integer price,
                         Double discountPercentage,
                         Float rating,
                         Integer stock,
                         String brand,
                         String category) {
}
