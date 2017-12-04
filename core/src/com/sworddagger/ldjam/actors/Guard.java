package com.sworddagger.ldjam.actors;

import com.sworddagger.ldjam.util.Animator;
import com.sworddagger.ldjam.util.pathfinding.Grid2D.MapNode;

import java.util.List;

import static com.sworddagger.ldjam.LDJamGame.TILE_SIZE;

/**
 * Created by Adam on 12/3/2017.
 */

public class Guard extends Animator {
	private float hearingCheck = 10;

	public void setPath(List<MapNode> path) {
		this.path = path;
		path.remove(0);
	}

	private List<MapNode> path;

	public float getHearingCheck() {
		return hearingCheck;
	}

	public void setHearingCheck(float level) {
		hearingCheck = level;
	}

	public Guard(String filename, int cols, int rows) {
		super(filename, cols, rows);
		npc = true;
	}

	public boolean check(float totalCheck) {
		return totalCheck >= hearingCheck;
	}

	@Override
	public void act(float delta) {
		boolean walking = walkUp || walkDown || walkLeft || walkRight;
		if (path != null && !walking) {
			MapNode node = path.get(0);
			if (Math.rint(getX()) < (node.getX() * TILE_SIZE)) {
				startWalking(DIRECTION.RIGHT);
			}
			else if (Math.rint(getX()) > (node.getX() * TILE_SIZE)) {
				startWalking(DIRECTION.LEFT);
			}
			else if (Math.rint(getY()) > ((node.getY()) * TILE_SIZE)) {
				startWalking(DIRECTION.DOWN);
			}
			else if (Math.rint(getY()) < ((node.getY()) * TILE_SIZE)) {
				startWalking(DIRECTION.UP);
			}
			path.remove(0);
			if (path.size() == 0) {
				path = null;
			}
		}
		super.act(delta);
	}
}
