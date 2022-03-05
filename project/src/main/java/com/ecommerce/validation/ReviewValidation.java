package com.ecommerce.validation;

import org.apache.log4j.Logger;

import com.ecommerce.representation.Review;

public class ReviewValidation {

	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	/**
	 * Validate review on add.
	 *
	 * @param review the review
	 */
	public void validateOnAdd(Review review) throws IllegalAccessException, IllegalArgumentException, NullPointerException {

        long userID = review.getUserId();
        if (userID == 0) {
            throw new IllegalArgumentException("userId");
        }
        String title = review.getTitle();
        if (title == null) {
            throw new NullPointerException("title");
        } else if(title.equals("")) {
            throw new IllegalArgumentException("title");
        }
        String description = review.getDescription();
        if (description == null) {
            throw new NullPointerException("description");
        } else if(description.equals("")) {
            throw new IllegalArgumentException("description");
        }
        float rating = review.getRating();
        if (rating <= 0 || rating > 5) {
            throw new IllegalArgumentException("rating");
        }
		commonValidation(review);
	}

	/**
	 * Validate review on modify.
	 *
	 * @param review the review
	 */
	public void validateOnModify(Review review) throws IllegalAccessException, IllegalArgumentException {

        long userId = review.getUserId();
        if (userId == 0) {
            throw new IllegalArgumentException("userId");
        }
		commonValidation(review);
	}
	
	public void validateOnDelete(Review review)  throws IllegalAccessException, IllegalArgumentException {

        long userId = review.getUserId();
        if (userId == 0) {
            throw new IllegalArgumentException("userId");
        }
        commonValidation(review);
	}
	
	/**
	 * Validate user id on upvote/downvote.
	 *
	 * @param review the review
	 */
	public void validateOnVote(Review review) throws IllegalAccessException, IllegalArgumentException {

        long userId = review.getUserId();
		if (userId == 0) {
			throw new IllegalArgumentException("userId");
		}
		String title = review.getTitle();
		if (title != null) {
			throw new IllegalAccessException("title");
		}
		String description = review.getDescription();
		if (description != null) {
			throw new IllegalAccessException("description");
		}
		float rating = review.getRating();
		if (rating != 0) {
			throw new IllegalAccessException("rating");
		}
		commonValidation(review);
	}
	
	public void commonValidation(Review review) throws IllegalAccessException {

		long reviewId = review.getId();
		if (reviewId != 0) {
			throw new IllegalAccessException("id");
		}
		long productId = review.getProductId();
		if (productId != 0) {
			throw new IllegalAccessException("productId");
		}
		long upvote = review.getUpvote();
		if (upvote != 0) {
			throw new IllegalAccessException("upvote");
		}
		long downvote = review.getDownvote();
		if (downvote != 0) {
			throw new IllegalAccessException("downvote");
		}
		long createdAt = review.getCreatedAt();
		if (createdAt != 0) {
			throw new IllegalAccessException("createdAt");
		}
		long updatedAt = review.getUpdatedAt();
		if (updatedAt != 0) {
			throw new IllegalAccessException("updatedAt");
		}
	}
}
