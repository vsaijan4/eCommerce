package com.ecommerce.representation.design;

public interface IModel<T> {

    public String getAliasName();

    public long getId();

    public void setId(long id);
}
