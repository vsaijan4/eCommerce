package com.ecommerce.representation;

public class ErrorResponse
{
    private String description;

    private Error[] error;

    public String getDescription () {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public Error[] getErrors () {
        return error;
    }

    public void setErrors (Error[] error) {
        this.error = error;
    }

    @Override
    public String toString() {
        StringBuilder sbuild = new StringBuilder();
        sbuild.append("ErrorResponse\n[\n");
        sbuild.append(",\n description = ").append(description);
        sbuild.append(",\n error = ").append(error);
        sbuild.append("\n]");
        return sbuild.toString();
    }
}