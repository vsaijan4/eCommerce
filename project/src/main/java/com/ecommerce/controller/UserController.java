package com.ecommerce.controller;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.ecommerce.constants.StatusConstants;
import com.ecommerce.representation.Review;
import com.ecommerce.representation.User;
import com.ecommerce.service.ResponseServiceImpl;
import com.ecommerce.service.ReviewServiceImpl;
import com.ecommerce.service.UserServiceImpl;
import com.ecommerce.validation.UserValidation;

@Path("/users")
public class UserController {

	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());

	private static final UserServiceImpl userService = new UserServiceImpl();
	private static final ResponseServiceImpl responseService = new ResponseServiceImpl();
	private static final UserValidation userValidateService = new UserValidation();
	private static final ReviewServiceImpl reviewService = new ReviewServiceImpl();
	private static User getUser = null;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") long userId) {
		logger.info("GET | User | " + userId);
		User user = new User();
		user.setId(userId);
		try {
            getUser = userService.get(user);

            return responseService.success(getUser);
        } catch (NotFoundException e) {
            return responseService.failure(e);
		} catch (InterruptedException | IOException e) {
            return responseService.failure(e);
        }
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		logger.info("GET | All Users");
        try {
            List<User> users = userService.getAll();
            return responseService.success(users);
        } catch (NotFoundException e) {
            return responseService.failure(e);
        } catch (InterruptedException | IOException e) {
            return responseService.failure(e);
        }
	}

	@GET
	@Path("{id}/reviews/{rid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") long userId, @PathParam("rid") long reviewId) {
		logger.info("userId: " + userId + ", reviewId: " + reviewId);

		User user = new User();
		user.setId(userId);
        try {
            if(!userService.isPresent(user)) {
                throw new NotFoundException("user - "+user.getId());
            }
            Review review = new Review();
            review.setUserId(userId);
            review.setId(reviewId);

            Review getReview = reviewService.get(review);
            if (getReview.getUserId() != userId) {
                return responseService.failure(StatusConstants.DIFF_USER);
            }

            return responseService.success(getReview);
        } catch (NotFoundException e) {
            return responseService.failure(e);
        } catch (InterruptedException | IOException e) {
            return responseService.failure(e);
        }
	}

	@GET
	@Path("/{id}/reviews")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllReviews(@PathParam("id") long userId) {
		logger.info("GET | User - All Reviews | " + userId);
		User user = new User();
		user.setId(userId);

        try {
            if(!userService.isPresent(user)) {
                throw new NotFoundException("user - "+userId);
            }
            List<Review> reviews = userService.getAllReviews(user);

            return responseService.success(reviews);
        } catch (NotFoundException e) {
            return responseService.failure(e);
        } catch (InterruptedException | IOException e) {
            return responseService.failure(e);
        }
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(User user) {
		logger.info("POST | User");

        try {
            userValidateService.validateAdd(user);

            getUser = userService.add(user);

            return responseService.success(getUser, StatusConstants.ADD_SUCCESS);
        } catch (IllegalAccessException e) {
            return responseService.failure(e);
        } catch (IllegalArgumentException e) {
            return responseService.failure(e);
        }  catch (NullPointerException e) {
            return responseService.failure(e);
        } catch (InterruptedException | IOException e) {
            return responseService.failure(e);
        }
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modify(User user, @PathParam("id") long userId) {
		logger.info("PUT | User | " + userId);
		try {
            userValidateService.validateModify(user);

            user.setId(userId);
            getUser = userService.modify(user);

            return responseService.success(getUser);
        } catch (NotFoundException e) {
            return responseService.failure(e);
        } catch(IllegalAccessException e) {
            return responseService.failure(e);
        } catch(IllegalArgumentException e) {
            return responseService.failure(e);
        } catch (InterruptedException | IOException e) {
            return responseService.failure(e);
        }
	}

	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") long userId) {
		logger.info("DELETE | User | " + userId);
		User user = new User();
		user.setId(userId);
        try {
            int status = userService.delete(user);
            if (status == StatusConstants.DELETE_SUCCESS) {
                return responseService.success(status);
            }
            return responseService.failure(status);
        } catch (NotFoundException e) {
            return responseService.failure(e);
        } catch (IOException | InterruptedException e) {
            return responseService.failure(e);
        }
	}
}
