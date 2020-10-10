package fi.jt.dogenator;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fi.jt.dogenator.screens.LevelSelectScreen;
import fi.jt.dogenator.screens.LoadingScreen;
import fi.jt.dogenator.screens.MainMenuScreen;
import fi.jt.dogenator.utils.Utils;

import static fi.jt.dogenator.Constants.DogeScreen.LOADING;
import static fi.jt.dogenator.Constants.WORLD_HEIGHT;
import static fi.jt.dogenator.Constants.WORLD_WIDTH;

public class Dogenator extends Game {
	// SpriteBatch
	private SpriteBatch mSpriteBatch;

	// Camera
	private OrthographicCamera mCamera;

	// Asset manager
	private static AssetManager mAssetManager;

	// Screens
    private LoadingScreen mLoadingScreen;
	private MainMenuScreen mMainMenuScreen;
	private LevelSelectScreen mLevelSelectScreen;

	@Override
	public void create () {
		mAssetManager = new AssetManager();
		mSpriteBatch = new SpriteBatch();

		initCamera();

		mLoadingScreen = new LoadingScreen(this);
		Utils.loadAssets(mAssetManager);

		switchScreen(LOADING);
	}

	@Override
	public void render() {
		super.render();
		mSpriteBatch.setProjectionMatrix(mCamera.combined);
	}
	
	@Override
	public void dispose () {
		mSpriteBatch.dispose();
	}

	public void initCamera() {
		mCamera = new OrthographicCamera();
		mCamera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
	}

	public SpriteBatch getSpriteBatch() {
		return mSpriteBatch;
	}

	public AssetManager getAssetManager() {
		return mAssetManager;
	}

	public void switchScreen(Constants.DogeScreen screen) {
		switch(screen) {
			case LOADING:
				mLoadingScreen = new LoadingScreen(this);
				setScreen(mLoadingScreen);
				break;
			case MAINMENU:
				mMainMenuScreen = new MainMenuScreen(this);
				setScreen(mMainMenuScreen);
				break;
			case LEVEL_SELECTION:
				mLevelSelectScreen = new LevelSelectScreen(this);
				setScreen(mLevelSelectScreen);
				break;

		}
	}
}
