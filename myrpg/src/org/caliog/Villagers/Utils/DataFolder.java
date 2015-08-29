package org.caliog.Villagers.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.caliog.myRPG.Utils.FilePath;

import org.apache.commons.io.FileUtils;

public class DataFolder {

    public static void backup() {
	int i = 0;
	for (String e : new File(FilePath.villagerBackup).list()) {
	    int a = Integer.parseInt(e.replace("data", ""));
	    if (a > i) {
		i = a;
	    }
	}
	i++;
	File srcFolder = new File(FilePath.villagerData);
	File destFolder = new File(FilePath.villagerBackup + "data" + i);

	//make sure source exists
	if (srcFolder.exists()) {
	    try {
		copyFolder(srcFolder, destFolder);
	    } catch (IOException e) {
		e.printStackTrace();
		return;
	    }
	    if (i > 20) {
		File fff = new File(FilePath.villagerBackup + "data" + (i - 20));
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

	    //if directory not exists, create it
	    if (!dest.exists()) {
		dest.mkdir();
	    }

	    //list all the directory contents
	    String files[] = src.list();

	    for (String file : files) {
		//construct the src and dest file structure
		File srcFile = new File(src, file);
		File destFile = new File(dest, file);
		//recursive copy
		copyFolder(srcFile, destFile);
	    }

	} else {
	    //if file, then copy it
	    //Use bytes stream to support all file types
	    InputStream in = new FileInputStream(src);
	    OutputStream out = new FileOutputStream(dest);

	    byte[] buffer = new byte[1024];

	    int length;
	    //copy the file content in bytes 
	    while ((length = in.read(buffer)) > 0) {
		out.write(buffer, 0, length);
	    }

	    in.close();
	    out.close();

	}
    }

}
