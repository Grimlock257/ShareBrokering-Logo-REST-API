package io.grimlock257.sccc.logoapi.paths;

import com.google.gson.Gson;
import com.sun.xml.wss.util.DateUtils;
import io.grimlock257.sccc.logoapi.model.DomainsApiResponse;
import io.grimlock257.sccc.logoapi.model.LogoResponse;
import io.grimlock257.sccc.logoapi.model.LogoStorageModel;
import io.grimlock257.sccc.logoapi.util.FileUtil;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
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

    private final int LOGO_URL_UPDATE_THRESHOLD = 60 * 60 * 1000;

    private final Gson gson = new Gson();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(
            @QueryParam("name") String name
    ) {
        // Attempt to read local
        LogoStorageModel logoStorageModel = FileUtil.loadLogoForName(name);

        if (logoStorageModel != null) {
            long timeDifference = new Date().getTime() - logoStorageModel.getDate().getTime();

            // If previous cache value is over an hour old, request an update, else use cached value
            if (timeDifference > LOGO_URL_UPDATE_THRESHOLD) {
                String logoUrl = requestLogo(name);

                if (logoUrl != null) {
                    return gson.toJson(new LogoResponse(logoUrl));
                } else {
                    return gson.toJson(new LogoResponse(logoStorageModel.getLogoUrl()));
                }
            } else {
                return gson.toJson(new LogoResponse(logoStorageModel.getLogoUrl()));
            }
        }

        // No local cache, retrieve logo a fresh
        String logoUrl = requestLogo(name);

        if (logoUrl != null) {
            return gson.toJson(new LogoResponse(logoUrl));
        } else {
            return gson.toJson(new LogoResponse());
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
                throw new IOException("Connection response code was " + conn.getResponseCode() + ". " + conn.getResponseMessage());
            }

            // Deserialise the JSON response and extract the "logo" field for return
            DomainsApiResponse domainsApiResponse = gson.fromJson(new InputStreamReader(conn.getInputStream()), DomainsApiResponse.class);

            FileUtil.saveLogoForName(name, new LogoStorageModel(domainsApiResponse.getLogo()));

            return domainsApiResponse.getLogo();
        } catch (MalformedURLException e) {
            System.err.println("Malformed URL: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        } catch (NullPointerException e) {
            System.err.println("NPE: " + e.getMessage());
        } catch (ClassCastException e) {
            System.err.println("ClassCastException (Logo is likely null): " + e.getMessage());
        }

        return null;
    }
}
