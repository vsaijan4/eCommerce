package com.ecommerce.service.design;

import com.ecommerce.representation.design.IModel;

import javax.ws.rs.NotFoundException;
import java.io.IOException;

public interface IService<T extends IModel> {
	
	public T add(T model) throws IllegalAccessException, IllegalArgumentException, InterruptedException, IOException, NotFoundException, NullPointerException;
	
	public T get(T model) throws InterruptedException, IOException, NotFoundException;
	
	public T modify(T model) throws IllegalAccessException, InterruptedException, IOException, NotFoundException;

	public int delete(T model) throws InterruptedException, IOException, NotFoundException;

    public boolean isPresent(T model);

}
