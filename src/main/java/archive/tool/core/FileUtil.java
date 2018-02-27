package archive.tool.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {

    public static void write(InputStream inputStream, OutputStream outputStream) throws IOException {
        int len;
        byte[] buffer = new byte[1024];
        while ((len = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, len);
        }
    }
}
