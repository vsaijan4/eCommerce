package com.ecommerce.service.design;

import java.io.IOException;
import java.util.List;

import com.ecommerce.representation.Product;
import com.ecommerce.representation.Review;

/**
 * The Interface ProductService.
 */
public interface IProductService extends IService<Product> {

	public List<Review> getAllReviews(Product product) throws InterruptedException, IOException;

	public List<Product> getAll() throws InterruptedException, IOException;

}
