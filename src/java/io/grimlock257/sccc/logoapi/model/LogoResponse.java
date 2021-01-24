package io.grimlock257.sccc.logoapi.model;

/**
 * LogoResponse
 *
 * Represents a response that this web service can provide
 *
 * @author AdamW
 */
public class LogoResponse {

    private final boolean success;
    private final String logoUrl;

    /**
     * Create a LogoReponse with a success result of false
     */
    public LogoResponse() {
        this.success = false;
        this.logoUrl = null;
    }

    /**
     * Create a LogoResponse with a success result of true containing the logo URL
     *
     * @param logoUrl The logo URL as a String
     */
    public LogoResponse(String logoUrl) {
        this.success = true;
        this.logoUrl = logoUrl;
    }
}
