package org.caliog.myRPG.Utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Utils {
    public static String cleanString(String str) {
	char[] abc = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
		't', 'u', 'v', 'w', 'x', 'y', 'z', ' ', 'ä', 'ö', 'ü' };

	String newString = "";
	for (int i = 0; i < str.length(); i++) {
	    char u = str.charAt(i);
	    boolean found = false;
	    for (int j = 0; j < abc.length; j++) {
		if (String.valueOf(u).toLowerCase().equals(String.valueOf(abc[j]))) {
		    found = true;
		}
	    }
	    if (found) {
		newString = newString + u;
	    }
	}
	return newString;
    }

    public static Player getPlayer(UUID id) {
	return Bukkit.getPlayer(id);
    }
}
