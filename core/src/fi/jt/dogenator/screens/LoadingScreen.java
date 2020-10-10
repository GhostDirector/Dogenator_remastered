package fi.jt.dogenator.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import fi.jt.dogenator.Dogenator;
import fi.jt.dogenator.utils.Utils;

import static fi.jt.dogenator.Constants.DogeScreen.MAINMENU;
import static fi.jt.dogenator.Constants.BACKGROUND_LOADING;
import static fi.jt.dogenator.Constants.FONT_COMIC_SANS;
import static fi.jt.dogenator.Constants.WORLD_HEIGHT;
import static fi.jt.dogenator.Constants.WORLD_WIDTH;

public class LoadingScreen implements Screen {
    // Game instance
    private Dogenator mDogenator;

    // SpriteBatch
    private SpriteBatch mSpriteBatch;

    // Stage
    private Stage mLoadingStage;

    // Background
    private Texture mBackground;

    // Company logo (Because why the fuck not)
    //private Texture mLogo;

    // Font for the UI
    private BitmapFont mFont;

    public LoadingScreen(Dogenator dogenator){
        mDogenator = dogenator;
        mSpriteBatch = mDogenator.getSpriteBatch();

        setup();
    }

    private void setup() {
        mLoadingStage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT), mSpriteBatch);
        mBackground = new Texture(Gdx.files.internal(BACKGROUND_LOADING));

        // TODO: implement logo
        //mLogo = new Texture("logos/tpg.png");

        mFont = Utils.generateFont(FONT_COMIC_SANS, 32, Color.ORANGE, 2);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mLoadingStage.getBatch().begin();
        mLoadingStage.getBatch().draw(mBackground, 0, 0, WORLD_WIDTH,  WORLD_HEIGHT);
        //mLoadingStage.getBatch().draw(mLogo, 280, 180, mLogo.getWidth() * 0.53f,  mLogo.getHeight() * 0.53f);

        if (mDogenator.getAssetManager().update()){
            mDogenator.switchScreen(MAINMENU);
        }
        else {
            mFont.draw(mSpriteBatch, "Loading Resources", WORLD_WIDTH * 0.35f, WORLD_HEIGHT * 0.6f);
            mFont.draw(mSpriteBatch, "Progress: " + (int)(mDogenator.getAssetManager().getProgress() * 100) + "%", WORLD_WIDTH * 0.35f, WORLD_HEIGHT * 0.5f);
        }
        mLoadingStage.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}
}
