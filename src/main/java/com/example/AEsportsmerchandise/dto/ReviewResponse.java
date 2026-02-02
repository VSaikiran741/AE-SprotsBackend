package com.example.AEsportsmerchandise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReviewResponse {

    private Long id;
    private int rating;
    private String comment;
    private String userEmail;
    private boolean verifiedPurchase;
    private LocalDateTime createdAt;
}
