package com.ecommerce.validation;

import com.ecommerce.representation.Product;

public class ProductValidation {

	public void commonValidation(Product product) throws IllegalAccessException, IllegalArgumentException {

        float mrp = product.getMaxRetailPrice();
        if (mrp <= 0) {
            throw new IllegalArgumentException("maxRetailPrice");
        }
        float sellingPrice = product.getSellingPrice();
        if (sellingPrice <= 0) {
            throw new IllegalArgumentException("sellingPrice");
        }
        long manufactureDate = product.getManufactureDate();
        if (manufactureDate <= 0 || manufactureDate > System.currentTimeMillis()) {
            throw new IllegalArgumentException("manufactureDate");
        }
        long warranty = product.getWarranty();
        if (warranty <= 0) {
            throw new IllegalArgumentException("warranty");
        }

        long createdAt = product.getCreatedAt();
        if (createdAt != 0) {
            throw new IllegalAccessException("createdAt");
        }
        long updatedAt = product.getUpdatedAt();
        if (updatedAt != 0) {
            throw new IllegalAccessException("updatedAt");
        }
	}
	
	public void validateAdd(Product product) throws IllegalAccessException, IllegalArgumentException, NullPointerException {

        long productId = product.getId();
        if (productId != -1) {
            throw new IllegalAccessException("id");
        }

        String name = product.getName();
        if (name == null) {
            throw new NullPointerException("name");
        } else if(name.equals("")) {
            throw new IllegalArgumentException("name");
        }

        String description = product.getDescription();
        if (description == null) {
            throw new NullPointerException("description");
        } else if(description.equals("")) {
            throw new IllegalArgumentException("description");
        }

        long quantity = product.getAvailableQuantity();
        if (quantity <= 0) {
            throw new IllegalArgumentException("availableQuantity");
        }

		commonValidation(product);
	}
	
	public void validateModify(Product product) throws IllegalAccessException, IllegalArgumentException {

        String name = product.getName();
        if(name != null && name.equals("")) {
            throw new IllegalArgumentException("name");
        }

        String description = product.getDescription();
        if(description != null && description.equals("")) {
            throw new IllegalArgumentException("description");
        }

        long quantity = product.getAvailableQuantity();
        if (quantity < 0) {
            throw new IllegalArgumentException("availableQuantity");
        }

        commonValidation(product);
	}
}
