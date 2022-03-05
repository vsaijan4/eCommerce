package com.ecommerce.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ecommerce.validation.ProductValidation;
import org.apache.log4j.Logger;

import com.ecommerce.constants.StatusConstants;
import com.ecommerce.data.LookUpFileSystemImpl;
import com.ecommerce.data.MaxIDFileSystemImpl;
import com.ecommerce.data.ProductFileSystemImpl;
import com.ecommerce.data.ReviewFileSystemImpl;
import com.ecommerce.representation.Product;
import com.ecommerce.representation.Review;
import com.ecommerce.service.design.IProductService;

import javax.ws.rs.NotFoundException;

public class ProductServiceImpl implements IProductService {

	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	private static final ReviewFileSystemImpl reviewDataService = new ReviewFileSystemImpl();
	private static final ProductFileSystemImpl productDataService = new ProductFileSystemImpl();
	private static final ReviewServiceImpl reviewService = new ReviewServiceImpl();
	private static final MaxIDFileSystemImpl maxIDDataService = new MaxIDFileSystemImpl();
	private static final LookUpFileSystemImpl lookUpDataService = new LookUpFileSystemImpl();
    private static final ProductValidation productValidateService = new ProductValidation();

	@Override
	public Product add(Product product) throws IllegalAccessException, IllegalArgumentException, InterruptedException, IOException, NullPointerException {

        productValidateService.validateAdd(product);

		// Get new product id
		long productId = maxIDDataService.get(StatusConstants.PRODUCT);
		long currTime = System.currentTimeMillis();

		product.setId(productId);
		product.setCreatedAt(currTime);
		product.setUpdatedAt(currTime);

		productDataService.create(product);
        logger.info("Added new productId " + productId + " successfully!");

		return product;
	}

	@Override
	public Product get(Product product) throws IOException, InterruptedException, NotFoundException {
		if (isPresent(product)) {
			return (Product) productDataService.read(product);
		}
        throw new NotFoundException("product - "+product.getId());
	}

    @SuppressWarnings("unchecked")
	@Override
	public List<Product> getAll() throws IOException, InterruptedException, NotFoundException {
		Product product = new Product();
        List<Product> products = productDataService.getAll(product);
		if(products.isEmpty()) {
            throw new NotFoundException("products");
        }
        return products;
	}

	@Override
	public List<Review> getAllReviews(Product product) throws InterruptedException, IOException, NotFoundException {
		List<Review> reviews = new ArrayList<>();
		if (isPresent(product)) {
			List<Long> listOfReviewIDs = lookUpDataService.fetchAllReviewIDs(product);
			if (!listOfReviewIDs.isEmpty()) {
				for (Long reviewId : listOfReviewIDs) {
					Review review = new Review();
					review.setId(reviewId);
					reviews.add(reviewService.get(review));
				}
			}
		} else {
            logger.error("Cannot get all reviews");
            throw new NotFoundException("product - "+product.getId());
        }
		if(reviews.isEmpty()) {
            throw new NotFoundException("reviews for product - "+product.getId());
        }
        return reviews;
	}

	@Override
	public Product modify(Product product) throws IllegalAccessException, InterruptedException, IOException, NotFoundException {
        productValidateService.validateModify(product);

        //Check if Product exists
        if (!isPresent(product)) {
			logger.error("Cannot modify review for productId!");
            throw new NotFoundException("product - "+product.getId());
		}

		// Get old product data
		Product fetchedProduct = (Product) productDataService.read(product);
		Product updatedProduct = overwriteProduct(product, fetchedProduct);

		// Update new product data
		productDataService.update(updatedProduct);

		logger.info("Modified productId " + product.getId() + " successfully!");
		return updatedProduct;
	}

	@Override
	public int delete(Product product) throws InterruptedException, IOException, NotFoundException {
		long productId = product.getId();

        //Check if Product exists
        if (!isPresent(product)) {
            logger.error("Cannot modify review for productId!");
            throw new NotFoundException("product - "+productId);
        }

        // Delete product
		if (productDataService.delete(product)) {
			logger.info("Deleted productId " + productId + " successfully!");

			// Delete all reviews for this productId
			List<Long> listOfReviewIDs = lookUpDataService.fetchAllReviewIDs(product);
			if (!listOfReviewIDs.isEmpty()) {
				for (Long reviewId : listOfReviewIDs) {
					Review review = new Review();
					review.setId(reviewId);
					review.setProductId(productId);
					reviewDataService.delete(review);
				}
			}

			// Delete all productId entries from LookUp
			lookUpDataService.delete(product);
		}
		return StatusConstants.DELETE_SUCCESS;
	}

	@Override
	public boolean isPresent(Product product) {
		long productId = product.getId();
		if (!productDataService.isPresent(product)) {
			logger.info("ProductId " + productId + " does not exist!");
			return false;
		}
		logger.info("ProductId " + productId + " exists!");
		return true;
	}

	private Product overwriteProduct(Product product, Product editProduct) {
		// Set product data
		if (product.getName() != null && !product.getName().equals("")) {
			editProduct.setName(product.getName());
		}
		if (product.getDescription() != null && !product.getDescription().equals("")) {
			editProduct.setDescription(product.getDescription());
		}
		if (product.getAvailableQuantity() >= 0) {
			editProduct.setAvailableQuantity(product.getAvailableQuantity());
		}
		if (product.getMaxRetailPrice() > 0) {
			editProduct.setMaxRetailPrice(product.getMaxRetailPrice());
		}
		if (product.getSellingPrice() > 0) {
			editProduct.setSellingPrice(product.getSellingPrice());
		}
		if (product.getManufactureDate() >= 0 && product.getManufactureDate() <= System.currentTimeMillis()) {
			editProduct.setManufactureDate(product.getManufactureDate());
		}
		if (product.getWarranty() > 0) {
			editProduct.setWarranty(product.getWarranty());
		}
		editProduct.setUpdatedAt(System.currentTimeMillis());
		return editProduct;
	}
}
