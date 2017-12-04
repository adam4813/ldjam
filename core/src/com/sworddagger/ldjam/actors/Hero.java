package com.sworddagger.ldjam.actors;

import com.sworddagger.ldjam.Item;
import com.sworddagger.ldjam.util.Animator;

/**
 * Created by Adam on 12/3/2017.
 */

public class Hero extends Animator {
	private float weight = 10;
	private Item heldItem;

	public Hero(String filename, int cols, int rows) {
		super(filename, cols, rows);
		heldItem = new Item(10);
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public Item getHeldItem() {
		return heldItem;
	}

	public void setHeldItem(Item heldItem) {
		this.heldItem = heldItem;
	}
}
