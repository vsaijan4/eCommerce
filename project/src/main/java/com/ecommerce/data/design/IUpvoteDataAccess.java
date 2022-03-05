package com.ecommerce.data.design;

import com.ecommerce.representation.*;

public interface IUpvoteDataAccess {
	
	public boolean create(Review review, int vote);

	public int read(Review review);

	public boolean delete(User user);

	public boolean delete(Review review);

	public boolean update(Review review, long vote);
	
}
