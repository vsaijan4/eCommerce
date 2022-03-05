package com.ecommerce.data.design;

import java.io.IOException;

public interface IMaxIDDataAccess {

	public long get(int modelType) throws InterruptedException, IOException;

	public boolean update(int modelType, long id) throws InterruptedException, IOException;

}
