package com.ecommerce.representation;

import com.ecommerce.representation.design.IModel;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonIgnoreProperties("aliasName")
public class Product implements IModel {

	private long id;
	private String name;
	private String description;
	private long availableQuantity;
	private float maxRetailPrice;
	private float sellingPrice;
	private long manufactureDate;
	private int warranty;
	private long createdAt;
	private long updatedAt;

    public Product() {
        this.id = -1;
    }

	@Override
	public long getId() {
		return this.id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the available quantity.
	 *
	 * @return the available quantity
	 */
	public long getAvailableQuantity() {
		return this.availableQuantity;
	}

	/**
	 * Sets the available quantity.
	 *
	 * @param availableQuantity the new available quantity
	 */
	public void setAvailableQuantity(long availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	/**
	 * Gets the max retail price.
	 *
	 * @return the max retail price
	 */
	public float getMaxRetailPrice() {
		return this.maxRetailPrice;
	}

	/**
	 * Sets the max retail price.
	 *
	 * @param maxRetailPrice the new max retail price
	 */
	public void setMaxRetailPrice(float maxRetailPrice) {
		this.maxRetailPrice = maxRetailPrice;
	}

	/**
	 * Gets the selling price.
	 *
	 * @return the selling price
	 */
	public float getSellingPrice() {
		return this.sellingPrice;
	}

	/**
	 * Sets the selling price.
	 *
	 * @param sellingPrice the new selling price
	 */
	public void setSellingPrice(float sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	/**
	 * Gets the manufacture date.
	 *
	 * @return the manufacture date
	 */
	public long getManufactureDate() {
		return this.manufactureDate;
	}

	/**
	 * Sets the manufacture date.
	 *
	 * @param manufactureDate the new manufacture date
	 */
	public void setManufactureDate(long manufactureDate) {
		this.manufactureDate = manufactureDate;
	}

	/**
	 * Gets the warranty.
	 *
	 * @return the warranty
	 */
	public int getWarranty() {
		return this.warranty;
	}

	/**
	 * Sets the warranty.
	 *
	 * @param warranty the new warranty
	 */
	public void setWarranty(int warranty) {
		this.warranty = warranty;
	}

	/**
	 * Gets the created at.
	 *
	 * @return the created at
	 */
	public long getCreatedAt() {
		return this.createdAt;
	}

	/**
	 * Sets the created at.
	 *
	 * @param createdAt the new created at
	 */
	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Gets the updated at.
	 *
	 * @return the updated at
	 */
	public long getUpdatedAt() {
		return this.updatedAt;
	}

	/**
	 * Sets the updated at.
	 *
	 * @param updatedAt the new updated at
	 */
	public void setUpdatedAt(long updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
    @JsonIgnore
	public String getAliasName(){
		return "product";
	}

    @Override
    public String toString() {
        StringBuilder sbuild = new StringBuilder();
        sbuild.append("Product\n[\n");
        sbuild.append(" id = ").append(id);
        sbuild.append(",\n name = ").append(name);
        sbuild.append(",\n description = ").append(description);
        sbuild.append(",\n availableQuantity = ").append(availableQuantity);
        sbuild.append(",\n maxRetailPrice = ").append(maxRetailPrice);
        sbuild.append(",\n sellingPrice = ").append(sellingPrice);
        sbuild.append(",\n manufactureDate = ").append(manufactureDate);
        sbuild.append(",\n warranty = ").append(warranty);
        sbuild.append(",\n createdAt = ").append(createdAt);
        sbuild.append(",\n updatedAt = ").append(updatedAt);
        sbuild.append("\n]");
        return sbuild.toString();
    }
}