package com.sworddagger.ldjam.util;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Adam on 12/3/2017.
 */

public class ObstacleMap {
	private Array<Rectangle> obstacleRects = new Array<Rectangle>();
	private Map map;

	public ObstacleMap(Map map) {
		this.map = map;
		buildObstaclesRects();

	}

	private void buildObstaclesRects() {
		MapObjects objects = map.getLayers().get("obstacles").getObjects();

		for (MapObject object : objects) {
			if (object instanceof RectangleMapObject) {
				Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
				obstacleRects.add(rectangle);
			}
		}
	}

	// TODO use intersector class
	public float getObstructionValue(Vector2 start, Vector2 end) {
		float obstruction = 1;
		Vector2 direction_inv = new Vector2(start.cpy().sub(end));
		direction_inv.x = 1.0f / direction_inv.x;
		direction_inv.y = 1.0f / direction_inv.y;

		for (Rectangle rect : obstacleRects) {
			double tx1 = (rect.x - start.x) * direction_inv.x;
			double tx2 = ((rect.x + rect.width) - start.x) * direction_inv.x;

			double tmin = Math.min(tx1, tx2);
			double tmax = Math.max(tx1, tx2);

			double ty1 = (rect.y - start.y) * direction_inv.y;
			double ty2 = ((rect.y + rect.height) - start.y) * direction_inv.y;

			tmin = Math.max(tmin, Math.min(ty1, ty2));
			tmax = Math.min(tmax, Math.max(ty1, ty2));
			if (tmax >= tmin) {
				obstruction *= 0.5;
			}
		}

		return obstruction;
	}
}
