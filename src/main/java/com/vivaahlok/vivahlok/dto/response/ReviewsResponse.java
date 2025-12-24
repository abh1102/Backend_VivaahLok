package com.vivaahlok.vivahlok.dto.response;

import com.vivaahlok.vivahlok.dto.ReviewDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewsResponse {
    private List<ReviewDTO> reviews;
    private Double avgRating;
    private long total;
    private int page;
    private int pages;
}
