package org.caliog.myRPG.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.caliog.myRPG.Resource.FileCreator;

public class DataFolder {
    public static void backup() {
	int i = 0;
	for (String e : new File(FilePath.backup).list()) {
	    int a = Integer.parseInt(e.replace("data", ""));
	    if (a > i) {
		i = a;
	    }
	}
	i++;
	File srcFolder = new File(FilePath.data);
	File destFolder = new File(FilePath.backup + "data" + i);
	if (srcFolder.exists()) {
	    try {
		copyFolder(srcFolder, destFolder);
	    } catch (IOException e) {
		e.printStackTrace();
		return;
	    }
	    if (i > 20) {
		File fff = new File(FilePath.backup + "data" + (i - 20));
		try {
		    FileUtils.deleteDirectory(fff);
		} catch (IOException e1) {
		    e1.printStackTrace();
		}
	    }
	}
    }

    private static void copyFolder(File src, File dest) throws IOException {
	if (src.isDirectory()) {
	    if (!dest.exists()) {
		dest.mkdir();
	    }
	    String[] files = src.list();
	    for (String file : files) {
		File srcFile = new File(src, file);
		File destFile = new File(dest, file);
		copyFolder(srcFile, destFile);
	    }
	} else {
	    copyFile(src, dest);
	}
    }

    public static void copyFile(File src, File dest) throws IOException {
	InputStream in = new FileInputStream(src);
	OutputStream out = new FileOutputStream(dest);
	FileCreator.copyFile(in, out);
    }

}
