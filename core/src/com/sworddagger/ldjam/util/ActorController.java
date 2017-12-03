package com.sworddagger.ldjam.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

/**
 * Created by Adam on 12/2/2017.
 */

public class ActorController extends InputAdapter {
	private Animator actor;

	public ActorController(Animator actor) {
		this.actor = actor;
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
			case Input.Keys.W:
				actor.startWalking(Animator.DIRECTION.UP);
				break;
			case Input.Keys.A:
				actor.startWalking(Animator.DIRECTION.LEFT);
				break;
			case Input.Keys.S:
				actor.startWalking(Animator.DIRECTION.DOWN);
				break;
			case Input.Keys.D:
				actor.startWalking(Animator.DIRECTION.RIGHT);
				break;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
			case Input.Keys.W:
				actor.stopWalking(Animator.DIRECTION.UP);
				break;
			case Input.Keys.A:
				actor.stopWalking(Animator.DIRECTION.LEFT);
				break;
			case Input.Keys.S:
				actor.stopWalking(Animator.DIRECTION.DOWN);
				break;
			case Input.Keys.D:
				actor.stopWalking(Animator.DIRECTION.RIGHT);
				break;
		}
		return false;
	}
}
