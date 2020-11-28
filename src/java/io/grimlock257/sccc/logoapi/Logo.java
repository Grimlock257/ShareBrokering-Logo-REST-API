package io.grimlock257.sccc.logoapi;

import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/**
 * @author AdamW
 */
@Path("logo")
public class Logo {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        return "{ \"success\": true }";
    }
}
