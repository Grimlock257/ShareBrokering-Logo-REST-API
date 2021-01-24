package io.grimlock257.sccc.logoapi.model;

/**
 * DomainsApiResponse
 *
 * Represents a response that is received from the external API
 *
 * @author AdamW
 */
public class DomainsApiResponse {

    private String domain;
    private String logo;
    private String name;

    public String getDomain() {
        return domain;
    }

    public String getLogo() {
        return logo;
    }

    public String getName() {
        return name;
    }
}
