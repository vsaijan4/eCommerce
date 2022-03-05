package com.ecommerce.service.design;

import com.ecommerce.representation.Review;

import java.io.IOException;

/**
 * The Interface IReviewService.
 */
public interface IReviewService extends IService<Review> {
	
	public int vote(Review review, long userId, int vote) throws InterruptedException, IOException;
}
