package org.caliog.myRPG.Resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class FileCreator {

    public void copyFile(String dest, String src) throws FileNotFoundException, IOException {
	File destination = new File(dest);
	URL url = getClass().getResource(src);
	if (destination == null || url == null || !destination.exists())
	    return;

	copyFile(url.openStream(), new FileOutputStream(dest));

    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {

	byte[] buffer = new byte[1024];
	int length;
	while ((length = in.read(buffer)) > 0) {
	    out.write(buffer, 0, length);
	}
	in.close();
	out.close();
    }

}
