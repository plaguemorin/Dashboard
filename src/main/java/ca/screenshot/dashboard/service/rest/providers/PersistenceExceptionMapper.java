package ca.screenshot.dashboard.service.rest.providers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * User: plaguemorin
 * Date: 28/06/12
 * Time: 3:31 PM
 */
@Provider
public class PersistenceExceptionMapper implements ExceptionMapper<RuntimeException> {
	@Override
	public Response toResponse(RuntimeException exception) {
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN_TYPE).entity(exception.getMessage()).build();
	}
}
