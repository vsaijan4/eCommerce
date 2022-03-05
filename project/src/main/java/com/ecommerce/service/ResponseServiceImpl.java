package com.ecommerce.service;

import java.util.List;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import com.ecommerce.constants.StatusConstants;
import com.ecommerce.representation.ErrorResponse;
import com.ecommerce.representation.Error;
import com.ecommerce.representation.ResponseBean;
import com.ecommerce.representation.design.IModel;
import com.ecommerce.service.design.IResponseService;
import org.apache.log4j.Logger;

public class ResponseServiceImpl implements IResponseService {
	
	private static ResponseBean response = new ResponseBean();
    private static ErrorResponse errResponse = new ErrorResponse();
    private static Error err = new Error();

    private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());

	@Override
	public Response success(Object object, int status) {
		return Response.status(status).entity(object).build();
	}

    @Override
    public Response success(Object object) {
        return Response.status(200).entity(object).build();
    }

	@Override
	public Response success(List<? extends IModel> objects) {
		return Response.status(200).entity(objects).build();
	}
	
	@Override
	public Response success(int status) {
		response.setStatusCode(200);
        if(status == StatusConstants.UPVOTE_SUCCESS) {
			response.setMessage("The request has been fulfilled. Up-voted resource!");
		} else if(status == StatusConstants.DOWNVOTE_SUCCESS) {
			response.setMessage("The request has been fulfilled. Down-voted resource!");
		}
		return Response.status(200).entity(response).build();
	}

    public Response failure(NullPointerException e) {
        logger.error(getStackTrace(e));
        errResponse.setDescription("Validation Failed");
        err.setMessage("Mandatory attribute is missing");
        err.setField(e.getMessage());
        err.setCode("missing_field");
        errResponse.setErrors(new Error[] { err } );
        return Response.status(400).entity(errResponse).build();
    }

    public Response failure(NotFoundException e) {
        logger.error(getStackTrace(e));
        errResponse.setDescription("Not Found");
        err.setMessage("Resource(s) not found on the server");
        err.setField(e.getMessage());
        err.setCode("not_found");
        errResponse.setErrors(new Error[] { err } );
        return Response.status(404).entity(errResponse).build();
    }

    public Response failure(IllegalArgumentException e) {
        logger.error(getStackTrace(e));
        errResponse.setDescription("Validation Failed");
        err.setMessage("Inappropriate attribute value");
        err.setField(e.getMessage());
        err.setCode("invalid_value");
        errResponse.setErrors(new Error[] { err } );
        return Response.status(400).entity(errResponse).build();
    }

    public Response failure(IllegalAccessException e ) {
        logger.error(getStackTrace(e));
        errResponse.setDescription("Validation Failed");
        err.setMessage("Unexpected attribute present");
        err.setField(e.getMessage());
        err.setCode("invalid_field");
        errResponse.setErrors(new Error[] { err } );
        return Response.status(400).entity(errResponse).build();
    }

    public Response failure(Exception e) {
        logger.error(getStackTrace(e));
        errResponse.setDescription("Operation Failed");
        err.setMessage("Unexpected Server Error");
        err.setField("Not applicable");
        err.setCode("server_error");
        errResponse.setErrors(new Error[] { err } );
        return Response.status(500).entity(errResponse).build();
    }

	@Override
	public Response failure(int status) {
        errResponse.setDescription("Invalid Operation");
        err.setCode("invalid_operation");
        if (status == StatusConstants.SAME_USER_VOTE) {
            err.setMessage("Review author cannot upvote(or downvote) his(or her) own review");
            err.setField("upvote(or downvote)");
            err.setCode("access_denied");
		} else if (status == StatusConstants.ALREADY_UPVOTED) {
            err.setMessage("User already upvoted the review");
            err.setField("upvote");
		} else if (status == StatusConstants.ALREADY_DOWNVOTED) {
            err.setMessage("User already downvoted the review");
            err.setField("downvote");
		}  else if (status == StatusConstants.DIFF_PRODUCT) {
            err.setMessage("Review does not belong to the product");
            err.setField("--");
		} else if (status == StatusConstants.DIFF_USER) {
            err.setMessage("Review does not belong to the user");
            err.setField("--");
		} else {
            errResponse.setDescription("Operation Failed");
            err.setMessage("Unexpected Server Error");
            err.setField("--");
            err.setCode("server_error");
		}
        errResponse.setErrors(new Error[] { err } );
        return Response.status(400).entity(errResponse).build();
	}

    private static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
