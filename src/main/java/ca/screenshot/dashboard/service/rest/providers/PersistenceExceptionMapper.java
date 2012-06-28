package ca.screenshot.dashboard.service.rest.providers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN_TYPE;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

/**
 * User: plaguemorin
 * Date: 28/06/12
 * Time: 3:31 PM
 */
@Provider
public class PersistenceExceptionMapper implements ExceptionMapper<RuntimeException> {
	@Override
	public Response toResponse(RuntimeException exception) {
		return Response.status(INTERNAL_SERVER_ERROR).type(TEXT_PLAIN_TYPE).entity(exception.getMessage()).build();
	}
}
