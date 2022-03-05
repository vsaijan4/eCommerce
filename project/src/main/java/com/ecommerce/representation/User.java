package com.ecommerce.representation;

import com.ecommerce.representation.design.IModel;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonIgnoreProperties("aliasName")
public class User implements IModel{
	private long id;
	private String name;
	private String email;
	private String phone;
	private int age;
	private String gender;
	private String address;
	private long createdAt;
	private long updatedAt;

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
	public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return this.phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getAge() {
		return this.age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getGender() {
		return this.gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAddress() {
		return this.address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public long getCreatedAt() {
		return this.createdAt;
	}
	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}
	public long getUpdatedAt() {
		return this.updatedAt;
	}
	public void setUpdatedAt(long updatedAt) {
		this.updatedAt = updatedAt;
	}

    @Override
	@JsonIgnore
    public String getAliasName(){
        return "user";
    }

    @Override
    public String toString() {
        StringBuilder sbuild = new StringBuilder();
        sbuild.append("User\n[\n");
        sbuild.append(" id = ").append(id);
        sbuild.append(",\n name = ").append(name);
        sbuild.append(",\n email = ").append(email);
        sbuild.append(",\n phone = ").append(phone);
        sbuild.append(",\n age = ").append(age);
        sbuild.append(",\n gender = ").append(gender);
        sbuild.append(",\n address = ").append(address);
        sbuild.append(",\n createdAt = ").append(createdAt);
        sbuild.append(",\n updatedAt = ").append(updatedAt);
        sbuild.append("\n]");
        return sbuild.toString();
    }
}
