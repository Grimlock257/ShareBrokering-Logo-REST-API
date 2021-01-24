package io.grimlock257.sccc.logoapi.util;

import com.google.gson.Gson;
import io.grimlock257.sccc.logoapi.model.LogoStorageModel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * FileUtil
 *
 * Utility methods for loading and reading logo files
 *
 * @author Adam Watson
 */
public class FileUtil {

    /**
     * Load logo URL for the given name from the local cache, if it exists
     *
     * @param name The company name whose logo cache to try and load
     * @return The LogoStorageModel object, or null if failure
     */
    public static LogoStorageModel loadLogoForName(String name) {
        Gson gson = new Gson();

        try {
            return gson.fromJson(new String(Files.readAllBytes(Paths.get("./logo/" + name.toLowerCase() + ".json"))), LogoStorageModel.class);
        } catch (IOException e) {
            System.err.println("[LogoAPI] IOException while trying to read logo from " + name.toLowerCase() + ".json: " + e.getMessage());
        }

        return null;
    }

    /**
     * Save the logo URL for the given name to the local cache
     *
     * @param name The company name whose logo URL cache to write
     * @param logoStorageModel The LogoStorageModel object to save to the file
     */
    public static void saveLogoForName(String name, LogoStorageModel logoStorageModel) {
        Gson gson = new Gson();

        // Create the folder if it doesn't already exist
        File file = new File("./logo/" + name.toLowerCase() + ".json");
        file.getParentFile().mkdirs();

        // Attempt to write the file
        try (Writer fileWriter = new FileWriter(file)) {
            String logoStorageModelJson = gson.toJson(logoStorageModel);

            fileWriter.write(logoStorageModelJson);

            System.out.println("[LogoAPI] Logo update for " + name + " successful");
        } catch (IOException e) {
            System.err.println("[LogoAPI] IO exception writing " + name.toLowerCase() + ".json: " + e.getMessage());
        }
    }
}
