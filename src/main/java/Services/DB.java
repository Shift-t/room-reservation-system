package Services;

import java.io.File;
import java.nio.file.Paths;

public class DB {
    // Resolves the db directory relative to wherever the JVM is launched from,
    // so the app works from any directory (IDE, Maven, packaged JAR, etc.)
    private static final String ROOT =
        Paths.get(System.getProperty("user.dir")).toString();

    public static String path(String filename) {
        String path1 = ROOT + "/src/db/" + filename;
        String path2 = ROOT + "/room-reservation-system-final/src/db/" + filename;
        if (new File(path1).exists() || !new File(path2).exists()) {
            return path1;
        }
        return path2;
    }
}
