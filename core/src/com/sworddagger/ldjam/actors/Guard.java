package com.sworddagger.ldjam.actors;

import com.badlogic.gdx.Gdx;
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
		if (path != null) {
			path.remove(0);
		}
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
			Gdx.app.log("pathing", "started at " + Math.rint(getX() / TILE_SIZE) + ", " + Math.rint(
					getY() / TILE_SIZE));
			Gdx.app.log("pathing", "moving to " + node.toString());
			if (Math.rint(getX() / TILE_SIZE) < node.getX()) {
				startWalking(DIRECTION.RIGHT);
				Gdx.app.log("pathing", "moved right");
			}
			else if (Math.rint(getX() / TILE_SIZE) > node.getX()) {
				startWalking(DIRECTION.LEFT);
				Gdx.app.log("pathing", "moved left");
			}
			else if (Math.rint(getY() / TILE_SIZE) > node.getY()) {
				startWalking(DIRECTION.DOWN);
				Gdx.app.log("pathing", "moved down");
			}
			else if (Math.rint(getY() / TILE_SIZE) < node.getY()) {
				startWalking(DIRECTION.UP);
				Gdx.app.log("pathing", "moved up");
			}
			path.remove(0);
			if (path.size() == 0) {
				path = null;
			}
		}
		super.act(delta);
	}
}
