package com.ecommerce.controller;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.ecommerce.constants.StatusConstants;
import com.ecommerce.representation.Product;
import com.ecommerce.representation.Review;
import com.ecommerce.service.ProductServiceImpl;
import com.ecommerce.service.ResponseServiceImpl;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
public class ProductController {

	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	private static final ProductServiceImpl productService = new ProductServiceImpl();
	private static final ResponseServiceImpl responseService = new ResponseServiceImpl();
    private static Product getProduct;

	@GET
	@Path("{id}")
	public Response get(@PathParam("id") long productId) {
        logger.info("GET | Product | " + productId);
        Product product = new Product();
        product.setId(productId);
        try {
            getProduct = productService.get(product);
            return responseService.success(getProduct);
        } catch (NotFoundException e) {
            return responseService.failure(e);
        }  catch (IOException | InterruptedException e) {
            return responseService.failure(e);
        }
	}

	@GET
	public Response getAll() {
		logger.info("GET | All Products");
		try {
            List<Product> products = productService.getAll();
            return responseService.success(products);
        } catch (NotFoundException e) {
            return responseService.failure(e);
        } catch (IOException | InterruptedException e) {
            return responseService.failure(e);
        }
	}

	@GET
	@Path("/{id}/reviews")
	public Response getAllReviews(@PathParam("id") long productId) {
		logger.info("GET | Product - All Reviews | " + productId);
		Product product = new Product();
		product.setId(productId);
		try {
            List<Review> reviews = productService.getAllReviews(product);

            return responseService.success(reviews);
        } catch (NotFoundException e) {
            return responseService.failure(e);
        } catch (IOException | InterruptedException e) {
            return responseService.failure(e);
        }
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(Product product) {
		logger.info("POST | Product | " + product.toString());
        try {
            getProduct = productService.add(product);

            return responseService.success(getProduct, StatusConstants.ADD_SUCCESS);
        } catch (IllegalAccessException e1) {
            return responseService.failure(e1);
        } catch (IllegalArgumentException e1) {
            return responseService.failure(e1);
        } catch (NullPointerException e1) {
            return responseService.failure(e1);
        } catch (IOException | InterruptedException e1) {
            return responseService.failure(e1);
        }
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modify(Product product, @PathParam("id") long productId) {

		product.setId(productId);
        logger.info("PUT | Product | " + product.toString());

        try {
            getProduct = productService.modify(product);

            return responseService.success(getProduct);
        } catch (NotFoundException e) {
            return responseService.failure(e);
        } catch (IllegalAccessException e1) {
            return responseService.failure(e1);
        } catch (IllegalArgumentException e1) {
            return responseService.failure(e1);
        } catch (NullPointerException e1) {
            return responseService.failure(e1);
        } catch (IOException | InterruptedException e1) {
            return responseService.failure(e1);
        }
	}

	@DELETE
	@Path("{id}")
	public Response delete(@PathParam("id") long productId) {

		Product product = new Product();
		product.setId(productId);
        logger.info("DELETE | Product | " + product.toString());

        try {
            int status = productService.delete(product);
            if (status == StatusConstants.DELETE_SUCCESS) {
                return responseService.success(status);
            }
            return responseService.failure(status);
        } catch (NotFoundException e) {
            return responseService.failure(e);
        } catch (IOException | InterruptedException e1) {
            return responseService.failure(e1);
        }
	}
}
