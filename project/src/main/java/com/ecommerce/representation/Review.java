package com.ecommerce.representation;

import com.ecommerce.representation.design.IModel;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonIgnoreProperties("aliasName")
public class Review implements IModel {

	private long id;
	private long productId;
	private long userId;
	private String title;
	private String description;
	private float rating;
	private long upvote;
	private long downvote;
	private long createdAt;
	private long updatedAt;
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	@Override
	public long getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 */

	@Override
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the product id.
	 *
	 * @return the product id
	 */
	public long getProductId() {
		return this.productId;
	}

	/**
	 * Sets the product id.
	 *
	 * @param productId the new product id
	 */
	public void setProductId(long productId) {
		this.productId = productId;
	}

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public long getUserId() {
		return this.userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId the new user id
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
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
	 * Gets the rating.
	 *
	 * @return the rating
	 */
	public float getRating() {
		return this.rating;
	}

	/**
	 * Sets the rating.
	 *
	 * @param rating the new rating
	 */
	public void setRating(float rating) {
		this.rating = rating;
	}

	/**
	 * Gets the upvote.
	 *
	 * @return the upvote
	 */
	public long getUpvote() {
		return this.upvote;
	}

	/**
	 * Sets the upvote.
	 *
	 * @param upvote the new upvote
	 */
	public void setUpvote(long upvote) {
		this.upvote = upvote;
	}

	/**
	 * Gets the downvote.
	 *
	 * @return the downvote
	 */
	public long getDownvote() {
		return this.downvote;
	}

	/**
	 * Sets the downvote.
	 *
	 * @param downvote the new downvote
	 */
	public void setDownvote(long downvote) {
		this.downvote = downvote;
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
		return "review";
	}

    @Override
    public String toString() {
        StringBuilder sbuild = new StringBuilder();
        sbuild.append("Review\n[\n");
        sbuild.append(" id = ").append(id);
        sbuild.append(",\n productId = ").append(productId);
        sbuild.append(",\n userId = ").append(userId);
        sbuild.append(",\n title = ").append(title);
        sbuild.append(",\n description = ").append(description);
        sbuild.append(",\n rating = ").append(rating);
        sbuild.append(",\n upvote = ").append(upvote);
        sbuild.append(",\n downvote = ").append(downvote);
        sbuild.append(",\n createdAt = ").append(createdAt);
        sbuild.append(",\n updatedAt = ").append(updatedAt);
        sbuild.append("\n]");
        return sbuild.toString();
    }
}