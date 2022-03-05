package com.ecommerce.data.design;

import java.io.IOException;
import java.util.List;

import com.ecommerce.representation.*;

public interface ILookUpDataAccess<T> {

	public boolean create(T model) throws InterruptedException, IOException;
	
	public long read(T model) throws InterruptedException, IOException;

	public boolean delete(User user) throws InterruptedException, IOException;

	public boolean delete(Product product) throws InterruptedException, IOException;

	public boolean delete(Review review) throws InterruptedException, IOException;

	public List<Long> fetchAllReviewIDs(Product product) throws InterruptedException, IOException;

	public List<Long> fetchAllReviewIDs(User user) throws InterruptedException, IOException;

}
