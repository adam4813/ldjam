package com.sworddagger.ldjam.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.sworddagger.ldjam.Item;
import com.sworddagger.ldjam.actors.Hero;
import com.sworddagger.ldjam.screens.GameScreen;

/**
 * Created by Adam on 12/2/2017.
 */

public class ActorController extends InputAdapter {
	private final GameScreen gameScreen;
	private final Hero hero;

	public ActorController(Hero hero, GameScreen gameScreen) {
		this.hero = hero;
		this.gameScreen = gameScreen;
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
			case Input.Keys.W:
				hero.startWalking(Animator.DIRECTION.UP);
				break;
			case Input.Keys.A:
				hero.startWalking(Animator.DIRECTION.LEFT);
				break;
			case Input.Keys.S:
				hero.startWalking(Animator.DIRECTION.DOWN);
				break;
			case Input.Keys.D:
				hero.startWalking(Animator.DIRECTION.RIGHT);
				break;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
			case Input.Keys.W:
				hero.stopWalking(Animator.DIRECTION.UP);
				break;
			case Input.Keys.A:
				hero.stopWalking(Animator.DIRECTION.LEFT);
				break;
			case Input.Keys.S:
				hero.stopWalking(Animator.DIRECTION.DOWN);
				break;
			case Input.Keys.D:
				hero.stopWalking(Animator.DIRECTION.RIGHT);
				break;
			case Input.Keys.PLUS:
				hero.setWeight(hero.getWeight() + 2);
				break;
			case Input.Keys.MINUS:
				hero.setWeight(hero.getWeight() - 2);
				break;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Item heldItem = hero.getHeldItem();

		gameScreen.dropItem(heldItem, screenX, screenY);

		return false;
	}
}
