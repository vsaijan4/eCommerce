package com.ecommerce.representation;

public class Error
{
    private String field;

    private String message;

    private String code;

    public String getField () {
        return field;
    }

    public void setField (String field) {
        this.field = field;
    }

    public String getMessage () {
        return message;
    }

    public void setMessage (String message) {
        this.message = message;
    }

    public String getCode () {
        return code;
    }

    public void setCode (String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        StringBuilder sbuild = new StringBuilder();
        sbuild.append("Error\n[\n");
        sbuild.append(",\n field = ").append(field);
        sbuild.append(",\n message = ").append(message);
        sbuild.append(",\n code = ").append(code);
        sbuild.append("\n]");
        return sbuild.toString();
    }
}