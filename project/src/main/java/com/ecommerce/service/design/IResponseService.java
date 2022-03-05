package com.ecommerce.service.design;

import com.ecommerce.representation.design.IModel;

import java.util.List;

import javax.ws.rs.core.Response;

public interface IResponseService {

    Response success(Object object, int status);

    Response success(Object object);

    Response success(List<? extends IModel> objects);

    Response success(int status);

    Response failure(int status);

}
