package com.sworddagger.ldjam.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.sworddagger.ldjam.Level;

import static com.sworddagger.ldjam.LDJamGame.SCREEN_HEIGHT;
import static com.sworddagger.ldjam.LDJamGame.SCREEN_WIDTH;
import static com.sworddagger.ldjam.LDJamGame.TILE_SIZE;

/**
 * Created by Adam on 12/2/2017.
 */

public class Animator extends Image {
	private float interpolationAccumulator = 0;
	private float TILES_PRE_SECOND = 0.5f;
	private Vector2 target = new Vector2();
	private Vector2 start = new Vector2();

	// Constant rows and columns of the sprite sheet
	private int FRAME_COLS;
	private int FRAME_ROWS;

	private Animation<TextureRegion> walkLeftAnimation;
	private Animation<TextureRegion> walkRightAnimation;
	private Animation<TextureRegion> walkUpAnimation;
	private Animation<TextureRegion> walkDownAnimation;
	private boolean walkUp = false;
	private boolean walkDown = false;
	private boolean walkLeft = false;
	private boolean walkRight = false;
	private DIRECTION facingDirection = DIRECTION.DOWN;
	private Level level;

	public void startWalking(DIRECTION direction) {
		switch (direction) {
			case UP:
				walkUp = true;
				break;
			case DOWN:
				walkDown = true;
				break;
			case LEFT:
				walkLeft = true;
				break;
			case RIGHT:
				walkRight = true;
				break;
		}
	}

	public void stopWalking(DIRECTION direction) {
		switch (direction) {
			case UP:
				walkUp = false;
				break;
			case DOWN:
				walkDown = false;
				break;
			case LEFT:
				walkLeft = false;
				break;
			case RIGHT:
				walkRight = false;
				break;
		}
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public enum DIRECTION {UP, DOWN, LEFT, RIGHT}

	;

	private Texture spriteSheet;

	private float stateTime = 0f;

	public Animator(String filename, int cols, int rows) {
		super(new TextureRegionDrawable());
		FRAME_COLS = cols;
		FRAME_ROWS = rows;

		spriteSheet = new Texture(Gdx.files.internal(filename));
		setSize(spriteSheet.getWidth() / FRAME_COLS, spriteSheet.getHeight() / FRAME_ROWS);
	}

	public void setWalkFrames(int start, int end, DIRECTION direction) {
		TextureRegion[][] tmp = TextureRegion.split(spriteSheet,
													spriteSheet.getWidth() / FRAME_COLS,
													spriteSheet.getHeight() / FRAME_ROWS);

		TextureRegion[] walkFrames = new TextureRegion[end - start + 1];
		int index = 0;
		for (int i = start / FRAME_COLS; i < end / FRAME_COLS + 1; i++) {
			for (int j = start % FRAME_COLS; j < end % FRAME_COLS + 1; j++) {
				walkFrames[index++] = tmp[i][j];
			}
		}

		Animation animation = new Animation<TextureRegion>(0.25f, walkFrames);
		switch (direction) {
			case UP:
				walkUpAnimation = animation;
				break;
			case DOWN:
				walkDownAnimation = animation;
				break;
			case LEFT:
				walkLeftAnimation = animation;
				break;
			case RIGHT:
				walkRightAnimation = animation;
				break;
		}
	}

	@Override
	public boolean remove() {
		spriteSheet.dispose();
		return super.remove();
	}

	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		start.set(getX(), getY());
		target.set(start);
	}

	static private Interpolation interpolation = Interpolation.linear;

	@Override
	public void act(float delta) {
		if (interpolationAccumulator > TILES_PRE_SECOND) {
			interpolationAccumulator = 0;
			start.set(target);
		}
		boolean walking = walkUp || walkDown || walkLeft || walkRight;
		if (walking || interpolationAccumulator > 0) {
			if (interpolationAccumulator == 0) {
				if (walkUp) {
					if (target.y + TILE_SIZE < SCREEN_HEIGHT) {
						target.y += TILE_SIZE;
					}
					facingDirection = DIRECTION.UP;
				}
				else if (walkDown) {
					if (target.y - TILE_SIZE >= 0) {
						target.y -= TILE_SIZE;
					}
					facingDirection = DIRECTION.DOWN;
				}
				else if (walkLeft) {
					if (target.x + TILE_SIZE >= 0) {
						target.x -= TILE_SIZE;
					}
					facingDirection = DIRECTION.LEFT;
				}
				else if (walkRight) {
					if (target.x + TILE_SIZE < SCREEN_WIDTH) {
						target.x += TILE_SIZE;
					}
					facingDirection = DIRECTION.RIGHT;
				}
				if (!level.canWalk(start, target)) {
					target.set(start);
				}
			}
			interpolationAccumulator += delta;
			Vector2 result = new Vector2(start);
			result.interpolate(target, interpolationAccumulator * 2f, interpolation);
			setX(result.x);
			setY(result.y);
		}
		stateTime += delta;

		Animation<TextureRegion> animation = walkDownAnimation;
		switch (facingDirection) {
			case UP:
				animation = walkUpAnimation;
				break;
			case LEFT:
				animation = walkLeftAnimation;
				break;
			case RIGHT:
				animation = walkRightAnimation;
				break;
		}
		((TextureRegionDrawable) getDrawable()).setRegion(
				animation.getKeyFrame(walking ? stateTime += delta : 0, true));
		super.act(delta);
	}
}
