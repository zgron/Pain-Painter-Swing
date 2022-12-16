import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;


public class Utils {

    public static <ZipInputStream> String[] listFilesInFolder(String path) throws URISyntaxException,  IOException {
        URI uri = Utils.class.getResource(path).toURI();
        Path myPath;
        if (uri.getScheme().equals("jar")) {
            FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
            myPath = fileSystem.getPath(path);
        } else {
            myPath = Paths.get(uri);
        }
        Stream<Path> walk = Files.walk(myPath, 1);
        Iterator<Path> it = walk.iterator();
        it.next(); // Skip folder itself.
        ArrayList<String> files = new ArrayList<String>();
        while ( it.hasNext() ) {
            String next = it.next().toString();
            String relative = path + '/' + next.substring(next.lastIndexOf('\\')+1).substring(next.lastIndexOf('/') + 1); // make compatible regardless of / or \
            files.add(relative);
        }
        return files.toArray(new String[0]); // Wonkiest but correct syntax
    }
}
