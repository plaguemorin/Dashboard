package dashboard.service.rest;

import org.springframework.stereotype.Service;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * User: plaguemorin
 * Date: 27/06/12
 * Time: 5:41 PM
 */
@Path("/participants/")
@Service
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class ParticipantRestService {
}
