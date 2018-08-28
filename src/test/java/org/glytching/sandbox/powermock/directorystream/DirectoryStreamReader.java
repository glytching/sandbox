package org.glytching.sandbox.powermock.directorystream;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class DirectoryStreamReader {

    public Map<String, String> read(Path directory) {

        Map<String, String> fileContentsByName = new HashMap<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory)) {
            for (Path path : directoryStream) {
                if (Files.isRegularFile(path)) {
                    fileContentsByName.put(path.getFileName().toString(), new String(Files.readAllBytes(path)));
                }
            }
        } catch (IOException e) {
        }

        return fileContentsByName;
    }
}