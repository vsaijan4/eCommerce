package com.ecommerce.service.design;

import java.io.IOException;
import java.util.List;

import com.ecommerce.representation.Review;
import com.ecommerce.representation.User;

public interface IUserService extends IService<User> {

	public List<Review> getAllReviews(User user) throws InterruptedException, IOException;

	public List<User> getAll() throws InterruptedException, IOException;
}