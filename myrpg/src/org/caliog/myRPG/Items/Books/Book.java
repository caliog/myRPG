package org.caliog.myRPG.Items.Books;

import org.caliog.myRPG.Entities.myClass;
import org.caliog.myRPG.Items.CustomItem;

import org.bukkit.Material;

public abstract class Book extends CustomItem {
    protected myClass player;

    public Book(String name, myClass clazz) {
	super(Material.WRITTEN_BOOK, name, false);
	this.player = clazz;
	syncItemStack();
    }
}
