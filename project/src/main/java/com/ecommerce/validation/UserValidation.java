package com.ecommerce.validation;

import org.apache.log4j.Logger;

import com.ecommerce.constants.StatusConstants;
import com.ecommerce.representation.User;

public class UserValidation {

	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	/**
	 * Validate user on add/modify.
	 *
	 * @param user the user
	 */
	
	public void commonValidation(User user) throws IllegalAccessException, IllegalArgumentException {

        long userId = user.getId();
		if (userId != 0) {
            throw new IllegalAccessException("id");
		}

        int age = user.getAge();
        if (age <= 0 || age > 121) {
            throw new IllegalArgumentException("age");
        }

        String gender = user.getGender();
        if (gender != null && !gender.equals("male") && !gender.equals("female")) {
            throw new IllegalArgumentException("gender");
        }

		long createdAt = user.getCreatedAt();
		if (createdAt != 0) {
            throw new IllegalAccessException("createdAt");
		}

		long updatedAt = user.getUpdatedAt();
		if (updatedAt != 0) {
            throw new IllegalAccessException("updatedAt");
		}
	}
	
	public void validateAdd(User user) throws IllegalAccessException, IllegalArgumentException {

        String name = user.getName();
        if (name == null) {
            throw new NullPointerException("name");
        } else if(name.equals("")) {
            throw new IllegalArgumentException("name");
        }

        String email = user.getEmail();
        if (email == null) {
            throw new NullPointerException("email");
        } else if(email.equals("")) {
            throw new IllegalArgumentException("email");
        }

        String gender = user.getGender();
        if (gender == null) {
            throw new NullPointerException("gender");
        } else if(!gender.equals("male") && !gender.equals("female")) {
            throw new IllegalArgumentException("gender");
        }

        String phone = user.getPhone();
        if (phone == null) {
            throw new NullPointerException("phone");
        } else if(phone.equals("")) {
            throw new IllegalArgumentException("phone");
        }

		String address = user.getAddress();
        if (address == null) {
            throw new NullPointerException("address");
        } else if(address.equals("")) {
            throw new IllegalArgumentException("address");
        }

		commonValidation(user);
	}
	
	public void validateModify(User user) throws IllegalAccessException, IllegalArgumentException {

		commonValidation(user);
	}
}
