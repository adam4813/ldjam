package com.sworddagger.ldjam.actors;

import com.sworddagger.ldjam.util.Animator;

/**
 * Created by Adam on 12/3/2017.
 */

public class Hero extends Animator {
	private float weight = 10;

	public Hero(String filename, int cols, int rows) {
		super(filename, cols, rows);
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}
}
