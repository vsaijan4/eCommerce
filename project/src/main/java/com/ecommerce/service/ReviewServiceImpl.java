package com.ecommerce.service;

import org.apache.log4j.Logger;

import com.ecommerce.constants.StatusConstants;
import com.ecommerce.data.LookUpFileSystemImpl;
import com.ecommerce.data.MaxIDFileSystemImpl;
import com.ecommerce.data.ReviewFileSystemImpl;
import com.ecommerce.data.UpvoteFileSystemImpl;
import com.ecommerce.representation.*;
import com.ecommerce.service.design.IReviewService;

import javax.ws.rs.NotFoundException;
import java.io.IOException;

public class ReviewServiceImpl implements IReviewService {
	
	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());	
	private static final ReviewFileSystemImpl reviewDataService = new ReviewFileSystemImpl();
	private static final ProductServiceImpl productService = new ProductServiceImpl();
	private static final UserServiceImpl userService = new UserServiceImpl();
	private static final LookUpFileSystemImpl lookUpDataService = new LookUpFileSystemImpl();
	private static final UpvoteFileSystemImpl upvoteDataService = new UpvoteFileSystemImpl();
	private static final MaxIDFileSystemImpl maxIDDataService = new MaxIDFileSystemImpl();

	@Override
	public Review add(Review review) throws InterruptedException, IOException, NotFoundException {
		long productId = review.getProductId();
		Product product = new Product();
		product.setId(productId);
		
		// Check if the product exists
		if (!productService.isPresent(product)) {
			logger.error("Cannot add review!");
			throw new NotFoundException("product - "+productId);
		}

		// Check if the user exists
		long userId = review.getUserId();
		User user = new User();
		user.setId(userId);
		if (!userService.isPresent(user)) {
			logger.error("Cannot add review!");
            throw new NotFoundException("user - " + userId);
        }

		// Get new review id
		long reviewId = maxIDDataService.get(StatusConstants.REVIEW);		
		long currTime = System.currentTimeMillis();

		review.setId(reviewId);
		review.setCreatedAt(currTime);
		review.setUpdatedAt(currTime);
		
		reviewDataService.create(review);
        logger.info("Added review " + reviewId + " for productId " + productId + " successfully!");

		// Adds Entry in Look-Up
		lookUpDataService.create(review);
		return review;
	}

	@Override
	public Review get(Review review) throws InterruptedException, IOException, NotFoundException {
        if(!isPresent(review)) {
            throw new NotFoundException("review - "+review.getId());
        }
        return (Review) reviewDataService.read(review);
	}

	@Override
	public Review modify(Review review) throws InterruptedException, IOException, NotFoundException {
		
		// Check if the product exists
		Product product = new Product();
        long productId = product.getId();
		product.setId(productId);
		if (!productService.isPresent(product)) {
			logger.error("Cannot add review!");
            throw new NotFoundException("product - "+productId);
		}
		
		// Check if the review exists
		Review fetchedReview = get(review);
		if(fetchedReview == null){
            throw new NotFoundException("review - "+review.getId());
		}
		
		long reviewId = review.getId();
		
		// Check if review belongs to the product
		long fetchedProductId = fetchedReview.getProductId();
		if(fetchedProductId != productId) {
			logger.error("ReviewId " + fetchedProductId + " does not belong to the specified productId " + productId + " !");
			return null;
		}
		
		// Check if review belongs to the user
		long userId = fetchedReview.getUserId();
		if(userId != review.getUserId()) {
			logger.error("ReviewId " + reviewId + " does not belong to the specified userId " + review.getUserId() + " !");
			return null;
		}

		// Set new data
		if (review.getTitle() != null) {
			fetchedReview.setTitle(review.getTitle());
		}
		if (review.getDescription() != null) {
			fetchedReview.setDescription(review.getDescription());
		}
		if (review.getRating() != 0.0) {
			fetchedReview.setRating(review.getRating());
		}
		
		fetchedReview.setUpdatedAt(System.currentTimeMillis());

		// Update new review data
		reviewDataService.update(fetchedReview);

		logger.info("Modified reviewId " + fetchedReview.getId() + " for productId " + fetchedReview.getProductId() + " successfully!");
		return fetchedReview;
	}
	
	@Override
	public int vote(Review review, long userId, int vote) throws InterruptedException, IOException, NotFoundException {

		// Check if the product exists
		Product product = new Product();
		product.setId(review.getProductId());
		if (!productService.isPresent(product)) {
			logger.error("Cannot add review!");
            throw new NotFoundException("product - "+product.getId());
		}
		
		// Check if the review exists
		Review fetchedReview = get(review);
		if(fetchedReview == null){
            throw new NotFoundException("review - "+review.getId());
		}
		
		// Check if review belongs to the product
		if(review.getProductId() != fetchedReview.getProductId()) {
			logger.error("ReviewId " + review.getId() + " does not belong to the specified productId " + review.getProductId() + " !");
			return StatusConstants.DIFF_PRODUCT;
		}
		
		// Check if the author(user) votes their own review
		if(userId == fetchedReview.getUserId()) {
			return StatusConstants.SAME_USER_VOTE;
		}
		
		// Check if the user exists
		User user = new User();
		user.setId(review.getUserId());
		if (!userService.isPresent(user)) {
			logger.error("User does not exist!");
            throw new NotFoundException("user - " + userId);
		}
		
		int voteVal = upvoteDataService.read(review);
		if (vote == StatusConstants.UPVOTE) {
			if(voteVal == StatusConstants.UPVOTE) {
				logger.error("User has already upvoted the review!");
				return StatusConstants.ALREADY_UPVOTED;
			}
			long upvoteCount = fetchedReview.getUpvote();
			if (voteVal == StatusConstants.DOWNVOTE) {
				long downvoteCount = fetchedReview.getDownvote();
				fetchedReview.setUpvote(++upvoteCount);
				fetchedReview.setDownvote(--downvoteCount);
				reviewDataService.update(fetchedReview);
				upvoteDataService.update(review, StatusConstants.UPVOTE);
				logger.info("Changed from Downvote to Upvote!");
			} else {
				fetchedReview.setUpvote(++upvoteCount);
				reviewDataService.update(fetchedReview);
				upvoteDataService.create(review, StatusConstants.UPVOTE);
				logger.info("Upvoted the review!");
			}
            return StatusConstants.UPVOTE_SUCCESS;
        } else {
			if(voteVal == StatusConstants.DOWNVOTE) {
				logger.error("User has already downvoted the review!");
				return StatusConstants.ALREADY_DOWNVOTED;
			}
			long downvoteCount = fetchedReview.getDownvote();
			if (voteVal == StatusConstants.UPVOTE) {
				long upvoteCount = fetchedReview.getUpvote();
				fetchedReview.setUpvote(--upvoteCount);
				fetchedReview.setDownvote(++downvoteCount);
				reviewDataService.update(fetchedReview);
				upvoteDataService.update(review, StatusConstants.DOWNVOTE);
				logger.info("Changed from Upvoted to Downvoted!");
			} else {
				fetchedReview.setDownvote(++downvoteCount);
				reviewDataService.update(fetchedReview);
				upvoteDataService.create(review, StatusConstants.DOWNVOTE);
				logger.info("Downvoted the review!");
			}
            return StatusConstants.DOWNVOTE_SUCCESS;
        }
	}

	@Override
	public int delete(Review review) throws InterruptedException, IOException, NotFoundException {

		// Check if the product exists
		Product product = new Product();
		product.setId(review.getProductId());
		if (!productService.isPresent(product)) {
			logger.error("Cannot add review!");
            throw new NotFoundException("product - " + product.getId());
		}
		
		// Check if the review exists
		Review fetchedReview = get(review);
		if(fetchedReview == null){
            throw new NotFoundException("review");
		}

		long reviewId = review.getId();

		// Check if review belongs to the product
		long productId = fetchedReview.getProductId();
		if(productId != review.getProductId()) {
			logger.error("ReviewId " + reviewId + " does not belong to the specified productId " + review.getProductId() + " !");
			return StatusConstants.DIFF_PRODUCT;
		}
		
		// Check if review belongs to the user
		long userId = fetchedReview.getUserId();
		if(review.getUserId() != 0 && userId != review.getUserId()) {
			logger.error("ReviewId " + reviewId + " does not belong to the specified userId " + review.getUserId() + " !");
			return StatusConstants.DIFF_USER;
		}
		
		if(reviewDataService.delete(review)) {
			// Delete reviewId, productId entry from LookUp, Upvote
			lookUpDataService.delete(review);
			upvoteDataService.delete(review);
			logger.info("Deleted reviewId " + reviewId + " successfully!");
		}
		return StatusConstants.DELETE_SUCCESS;
	}


	@Override
	public boolean isPresent(Review review) {
		long reviewId = review.getId();
		if (!reviewDataService.isPresent(review)) {
			logger.error("ReviewId: " + reviewId + " not present!");
			return false;
		}
		logger.info("ReviewId: " + reviewId + " is present!");
		return true;
	}
}