package com.ecommerce.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ecommerce.constants.StatusConstants;
import com.ecommerce.data.*;
import com.ecommerce.representation.User;
import com.ecommerce.representation.Review;
import com.ecommerce.service.design.IUserService;

import javax.ws.rs.NotFoundException;

public class UserServiceImpl implements IUserService {

	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	private static final ReviewFileSystemImpl reviewDataService = new ReviewFileSystemImpl();
	private static final UserFileSystemImpl userDataService = new UserFileSystemImpl();
	private static final MaxIDFileSystemImpl maxIDDataService = new MaxIDFileSystemImpl();
	private static final LookUpFileSystemImpl lookUpDataService = new LookUpFileSystemImpl();
	private static final ReviewServiceImpl reviewService = new ReviewServiceImpl();
	private static final UpvoteFileSystemImpl upvoteDataService = new UpvoteFileSystemImpl();

	@Override
	public User add(User user) throws InterruptedException, IOException {
		// Get new user id
		long userId = maxIDDataService.get(StatusConstants.USER);
		
		long currTime = System.currentTimeMillis();
		user.setId(userId);
		user.setCreatedAt(currTime);
		user.setUpdatedAt(currTime);

        //Create User
		userDataService.create(user);
		logger.info("Added new userId " + userId + " successfully!");

		return user;
	}
	
	@Override
	public User get(User user) throws InterruptedException, IOException, NotFoundException {
		if (isPresent(user)) {
			return (User) userDataService.read(user);
		}
        throw new NotFoundException("user - "+user.getId());
	}
	
	@Override
	public List<User> getAll() throws InterruptedException, IOException, NotFoundException {
		User user = new User();
		List<User> users = userDataService.getAll(user);
        if(users.isEmpty()){
            throw new NotFoundException("users");
        }
        return users;
	}
	
	@Override
	public List<Review> getAllReviews(User user) throws InterruptedException, IOException, NotFoundException{
        List<Review> reviews = new ArrayList<Review>();
		if (isPresent(user)) {
			List<Long> listOfReviewIDs = lookUpDataService.fetchAllReviewIDs(user);
			if(!listOfReviewIDs.isEmpty()){
				for (Long reviewId : listOfReviewIDs) {
					Review review = new Review();
					review.setId(reviewId);
					reviews.add(reviewService.get(review));
				}
			}
		}
        if(reviews.isEmpty()) {
            throw new NotFoundException("reviews for user - "+user.getId());
        }
        return reviews;
	}

	@Override
	public User modify(User user) throws InterruptedException, IOException, NotFoundException {
		if (!isPresent(user)) {
			logger.error("Cannot modify User!");
            throw new NotFoundException("user - "+user.getId());
		}

		// Get old user data
		User fetchedUser = (User) userDataService.read(user);
		User updatedUser = overwriteUser(user, fetchedUser);

		// Update user data
		userDataService.update(updatedUser);

		logger.info("Modified userId " + user.getId() + " successfully!");
		return updatedUser;
	}

	@Override
	public int delete(User user) throws InterruptedException, IOException, NotFoundException {
		long userId = user.getId();

        //Check if User exists
        if (!isPresent(user)) {
            logger.error("Cannot modify User!");
            throw new NotFoundException("user - "+userId);
        }

		// Delete user
		if(userDataService.delete(user)) {
			logger.info("Deleted userId " + userId + " successfully!");
			
			// Delete all reviews for this user
			List<Long>listOfReviewIDs = lookUpDataService.fetchAllReviewIDs(user);
			if(!listOfReviewIDs.isEmpty()){
				for (Long reviewId : listOfReviewIDs) {
					Review review = new Review();
					review.setId(reviewId);
					review.setUserId(userId);
					reviewDataService.delete(review);
				}
			}
			lookUpDataService.delete(user);
			upvoteDataService.delete(user);
		}
		return StatusConstants.DELETE_SUCCESS;
	}
	
	@Override
	public boolean isPresent(User user) {
		long userId = user.getId();
		if (!userDataService.isPresent(user)) {
			logger.error("UserId " + userId + " does not exist!");
			return false;
		}
		logger.info("UserId " + userId + " exists!");
		return true;
	}
	
	private User overwriteUser(User user, User editUser) {

		// Set new data
		if (user.getName() != null) {
			editUser.setName(user.getName());
		}
		if (user.getEmail() != null) {
			editUser.setEmail(user.getEmail());
		}
		if (user.getPhone() != null) {
			editUser.setPhone(user.getPhone());
		}
		if (user.getAge() != 0) {
			editUser.setAge(user.getAge());
		}
		if (user.getGender() != null) {
			editUser.setGender(user.getGender());
		}
		if (user.getAddress() != null) {
			editUser.setAddress(user.getAddress());
		}
		editUser.setUpdatedAt(System.currentTimeMillis());
		return editUser;
	}
}
