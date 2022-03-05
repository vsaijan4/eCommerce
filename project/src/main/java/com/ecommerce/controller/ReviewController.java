package com.ecommerce.controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.ecommerce.constants.StatusConstants;
import com.ecommerce.representation.Product;
import com.ecommerce.representation.Review;
import com.ecommerce.service.ProductServiceImpl;
import com.ecommerce.service.ResponseServiceImpl;
import com.ecommerce.service.ReviewServiceImpl;
import com.ecommerce.validation.ReviewValidation;

import java.io.IOException;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
public class ReviewController {

	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	private static final ReviewServiceImpl reviewService = new ReviewServiceImpl();
	private static final ProductServiceImpl productService = new ProductServiceImpl();
	private static final ResponseServiceImpl responseService = new ResponseServiceImpl();
	private static final ReviewValidation reviewValidateService = new ReviewValidation();
    private static Review getReview = null;

	@GET
	@Path("{id}/reviews/{rid}")
	public Response get(@PathParam("id") long productId, @PathParam("rid") long reviewId) {
		logger.info("GET | productId - " + productId + " | reviewId - " + reviewId);

        try {
            Product product = new Product();
            product.setId(productId);
            if(!productService.isPresent(product)) {
                throw new NotFoundException("product - "+productId);
            }

            Review review = new Review();
            review.setProductId(productId);
            review.setId(reviewId);

            getReview = reviewService.get(review);
            if (getReview.getProductId() != productId) {
                return responseService.failure(StatusConstants.DIFF_PRODUCT);
            }

            return responseService.success(getReview);
        } catch (NotFoundException e) {
            return responseService.failure(e);
        } catch(InterruptedException | IOException e) {
            return responseService.failure(e);
        }
	}
	
	@POST
	@Path("{id}/reviews")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(Review review, @PathParam("id") long productId) {
		logger.info("POST | REVIEW | productId - " + productId + " | Review - " + review.toString());

        try {
            reviewValidateService.validateOnAdd(review);

            review.setProductId(productId);
            getReview = reviewService.add(review);

            return responseService.success(getReview);
        } catch (NotFoundException e) {
            return responseService.failure(e);
        } catch (IllegalAccessException e) {
            return responseService.failure(e);
        } catch (IllegalArgumentException e) {
            return responseService.failure(e);
        } catch (NullPointerException e) {
            return responseService.failure(e);
        } catch (InterruptedException | IOException e) {
            return responseService.failure(e);
        }
	}

	@PUT
	@Path("{id}/reviews/{rid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modify(Review review, @PathParam("id") long productId, @PathParam("rid") long reviewId) {
		logger.info("PUT | REVIEW | productId - " + productId + "| reviewId - " + reviewId + " | Review - " + review.toString());

        try {
            reviewValidateService.validateOnModify(review);

            review.setId(reviewId);
            review.setProductId(productId);
            getReview = reviewService.modify(review);

            return responseService.success(getReview);
        } catch (NotFoundException e) {
            return responseService.failure(e);
        } catch (IllegalAccessException e) {
            return responseService.failure(e);
        } catch (IllegalArgumentException e) {
            return responseService.failure(e);
        } catch (InterruptedException | IOException e) {
            return responseService.failure(e);
        }
	}

	@PUT
	@Path("{id}/reviews/{rid}/upvote")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response upvote(Review review, @PathParam("id") long productId, @PathParam("rid") long reviewId) {
		logger.info("PUT | UPVOTE REVIEW | productId - " + productId + " | reviewId - " + reviewId);

        try {
            reviewValidateService.validateOnVote(review);

            review.setId(reviewId);
            review.setProductId(productId);
            long userId = review.getUserId();
            int value = reviewService.vote(review, userId, StatusConstants.UPVOTE);
            if (value == StatusConstants.UPVOTE_SUCCESS) {
                return responseService.success(value);
            }

            return responseService.failure(value);
        } catch (NotFoundException e) {
            return responseService.failure(e);
        } catch (IllegalAccessException e) {
            return responseService.failure(e);
        } catch (IllegalArgumentException e) {
            return responseService.failure(e);
        } catch (InterruptedException | IOException e) {
            return responseService.failure(e);
        }
	}

	@PUT
	@Path("{id}/reviews/{rid}/downvote")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response downvote(Review review, @PathParam("id") long productId, @PathParam("rid") long reviewId) {
        logger.info("PUT | DOWNVOTE REVIEW | productId - " + productId + " | reviewId - " + reviewId);

        try {
            reviewValidateService.validateOnVote(review);

            review.setId(reviewId);
            review.setProductId(productId);
            long userId = review.getUserId();
            int value = reviewService.vote(review, userId, StatusConstants.DOWNVOTE);
            if (value == StatusConstants.DOWNVOTE_SUCCESS) {
                return responseService.success(value);
            }

            return responseService.failure(value);
        } catch (NotFoundException e) {
            return responseService.failure(e);
        } catch (IllegalAccessException e) {
            return responseService.failure(e);
        } catch (IllegalArgumentException e) {
            return responseService.failure(e);
        } catch (InterruptedException | IOException e) {
            return responseService.failure(e);
        }
	}

	@DELETE
	@Path("{id}/reviews/{rid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response delete(Review review, @PathParam("id") long productId, @PathParam("rid") long reviewId) {
		logger.info("DELETE | productId - " + productId + " | reviewId - " + reviewId);

        try {
            reviewValidateService.validateOnDelete(review);

            review.setId(reviewId);
            review.setProductId(productId);
            int status = reviewService.delete(review);
            if (status == StatusConstants.DELETE_SUCCESS) {
                return responseService.success(status);
            }

            return responseService.failure(status);
        } catch (NotFoundException e) {
            return responseService.failure(e);
        } catch(IllegalAccessException e1) {
            return responseService.failure(e1);
        } catch(IllegalArgumentException e1) {
            return responseService.failure(e1);
        } catch (InterruptedException | IOException e) {
            return responseService.failure(e);
        }
	}
}
