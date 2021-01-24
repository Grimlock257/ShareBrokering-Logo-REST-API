package io.grimlock257.sccc.logoapi.model;

import java.util.Date;

/**
 * LogoResponse
 *
 * Represents a logo storage model that stores the logo URL and date of last update
 *
 * @author Adam Watson
 */
public class LogoStorageModel {

    private final Date date;
    private final String logoUrl;

    /**
     * Create a LogoStorageModel with the current date and provided logo URL
     *
     * @param logoUrl The logo URL as a String
     */
    public LogoStorageModel(String logoUrl) {
        this.date = new Date();
        this.logoUrl = logoUrl;
    }

    public Date getDate() {
        return date;
    }

    public String getLogoUrl() {
        return logoUrl;
    }
}
