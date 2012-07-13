package ca.screenshot.dashboard.service.rest;

import ca.screenshot.dashboard.entity.RemoteReference;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * User: plaguemorin
 * Date: 13/07/12
 * Time: 5:49 PM
 */
@Service
@Path("/remote/{remoteResourceId}")
public class RemoteResourceRestResource {



	@GET
	public RemoteReference get(@PathParam("remoteResourceId") final String remoteResourceId) {
		return new RemoteReference();
	}
}
