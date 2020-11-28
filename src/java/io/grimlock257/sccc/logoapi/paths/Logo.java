package io.grimlock257.sccc.logoapi.paths;

import io.grimlock257.sccc.logoapi.model.LogoResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Logo
 *
 * Logo path of the API, will request a logo from an external RestAPI via reverse lookup using company name
 *
 * @author AdamW
 */
@Path("logo")
public class Logo {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(
            @QueryParam("name") String name
    ) {
        String logoUrl = requestLogo(name);

        if (logoUrl != null) {
            return new LogoResponse(logoUrl).toJson();
        } else {
            return new LogoResponse().toJson();
        }
    }

    /**
     * Request a logo from the clearbit domain to name API which contains a company logo if found
     *
     * @param name The name of the company to find a logo for
     * @return The logo URL or null if error or not found
     */
    private String requestLogo(String name) {
        try {
            // Request components
            String baseUrl = "https://company.clearbit.com/v1/domains/find";
            String authHeader = "Authorization";
            String authToken = "Bearer your-auth-token-here";
            String nameQueryParam = "?name=" + URLEncoder.encode(name, "UTF-8");

            // Create URL object
            URL url = new URL(baseUrl + nameQueryParam);

            // Create HTTP connection, and attach auth header
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty(authHeader, authToken);

            // If the response was not a 200, throw an error
            if (conn.getResponseCode() != 200) {
                throw new IOException(conn.getResponseMessage());
            }

            // Deserialise the JSON response and extract the "logo" field for return
            JsonReader jsonReader = Json.createReader(conn.getInputStream());
            JsonObject jsonObject = jsonReader.readObject();

            return jsonObject.getString("logo");
        } catch (MalformedURLException e) {
            System.err.println("Malformed URL: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IOException connecting to URL: " + e.getMessage());
        } catch (NullPointerException e) {
            System.err.println("NPE: " + e.getMessage());
        } catch (ClassCastException e) {
            System.err.println("ClassCastException (Logo is likely null): " + e.getMessage());
        }

        return null;
    }
}
