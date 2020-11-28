package io.grimlock257.sccc.logoapi.model;

/**
 * LogoResponse
 *
 * Represents a response that this web service can provide
 *
 * @author AdamW
 */
public class LogoResponse {

    private boolean success;
    private String logoUrl;

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

    /**
     * Convert this object to JSON format
     *
     * @return
     */
    public String toJson() {
        StringBuilder builder = new StringBuilder();

        builder.append("{");
        builder.append("\"success\": ").append(this.success);

        if (this.success) {
            builder.append(",\"logoUrl\": \"").append(this.logoUrl).append("\"");
        }

        builder.append("}");

        return builder.toString();
    }
}
