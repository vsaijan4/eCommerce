package com.ecommerce.data.design;

import com.ecommerce.representation.design.IModel;

import java.io.IOException;
import java.util.List;

public interface IDataAccess<T extends IModel> {

    public <T extends IModel> void create(T model) throws IOException;

	public <T extends IModel> T read(T model) throws IOException, InterruptedException;

	public <T extends IModel> void update(T model) throws IOException, InterruptedException;

    public <T extends IModel> boolean delete(T model) throws InterruptedException;

	public <T extends IModel> boolean isPresent(T model);

    public <T extends IModel> String getFilePath(T model);
	
	public <T extends IModel> List<T> getAll(T model) throws IOException, InterruptedException;
}
