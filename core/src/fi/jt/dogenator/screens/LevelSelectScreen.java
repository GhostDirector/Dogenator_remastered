package fi.jt.dogenator.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

import fi.jt.dogenator.Dogenator;
import fi.jt.dogenator.level.LevelInformation;
import fi.jt.dogenator.logger.Log;
import fi.jt.dogenator.utils.Utils;

import static fi.jt.dogenator.Constants.BACKGROUND_LEVEL_SELECTION;
import static fi.jt.dogenator.Constants.BUTTON_ACCEPT;
import static fi.jt.dogenator.Constants.BUTTON_ACCEPT_PRESSED;
import static fi.jt.dogenator.Constants.BUTTON_NEXT;
import static fi.jt.dogenator.Constants.BUTTON_NEXT_PRESSED;
import static fi.jt.dogenator.Constants.BUTTON_PREVIOUS;
import static fi.jt.dogenator.Constants.BUTTON_PREVIOUS_PRESSED;
import static fi.jt.dogenator.Constants.BUTTON_RETURN;
import static fi.jt.dogenator.Constants.BUTTON_RETURN_PRESSED;
import static fi.jt.dogenator.Constants.DogeScreen.MAINMENU;
import static fi.jt.dogenator.Constants.FONT_COMIC_SANS;
import static fi.jt.dogenator.Constants.LEVEL_1_ID;
import static fi.jt.dogenator.Constants.LEVEL_2_ID;
import static fi.jt.dogenator.Constants.LEVEL_3_ID;
import static fi.jt.dogenator.Constants.LEVEL_4_ID;
import static fi.jt.dogenator.Constants.LEVEL_5_ID;
import static fi.jt.dogenator.Constants.WORLD_HEIGHT;
import static fi.jt.dogenator.Constants.WORLD_WIDTH;

public class LevelSelectScreen implements Screen {
    // Game instance
    private Dogenator mDogenator;

    // SpriteBatch
    private SpriteBatch mSpriteBatch;

    // Background
    private Texture mBackground;

    // Font for UI
    private BitmapFont mFont;

    // Stage
    private Stage mLevelSelectionStage;

    // Texture for currently selected level
    private Texture mSelectedLevel;

    // Index for currently selected level
    private int mCurrentLevel;

    // Array containing all level info items
    private ArrayList<LevelInformation> mLevelArray;

    // Game state
    private boolean mGameIsRunning;

    public LevelSelectScreen(Dogenator dogenator) {
        mGameIsRunning = true;

        mDogenator = dogenator;
        mSpriteBatch = mDogenator.getSpriteBatch();

        setup();
        addActors();

        Gdx.input.setInputProcessor(mLevelSelectionStage);
    }

    private void setup() {
        mFont = Utils.generateFont(FONT_COMIC_SANS, 26, Color.CORAL, 3);

        mCurrentLevel = 0;
        mLevelArray = new ArrayList<LevelInformation>();
        loadLevels();
        mSelectedLevel = getTexture(mLevelArray.get(0).getPicture());

        mLevelSelectionStage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT), mSpriteBatch);

        mBackground = mDogenator.getAssetManager().get(BACKGROUND_LEVEL_SELECTION);
    }

    private void addActors() {
        PreviousButton previousButton = new PreviousButton();
        mLevelSelectionStage.addActor(previousButton);
        NextButton nextButton = new NextButton();
        mLevelSelectionStage.addActor(nextButton);
        ReturnButton returnButton = new ReturnButton();
        mLevelSelectionStage.addActor(returnButton);
        PlayButton playButton = new PlayButton();
        mLevelSelectionStage.addActor(playButton);
    }


    private void setLevel(int l){
        int tmp = mCurrentLevel + l;
        if(tmp >= 0 && tmp <= mLevelArray.size() - 1){
            mCurrentLevel = mCurrentLevel + l;
            mSelectedLevel = getTexture(mLevelArray.get(mCurrentLevel).getPicture());
            Log.d("Selected level: " + mLevelArray.get(mCurrentLevel).toString());
        }
    }

    private void loadLevels(){
        mLevelArray.add(new LevelInformation(LEVEL_1_ID));
        mLevelArray.add(new LevelInformation(LEVEL_2_ID));
        mLevelArray.add(new LevelInformation(LEVEL_3_ID));
        mLevelArray.add(new LevelInformation(LEVEL_4_ID));
        mLevelArray.add(new LevelInformation(LEVEL_5_ID));
    }

    private Texture getTexture(String texture) {
        return mDogenator.getAssetManager().get(texture);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        if(mGameIsRunning){
            Gdx.gl.glClearColor(0,0,0,0);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            mSpriteBatch.begin();

            mSpriteBatch.draw(mBackground, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);

            // TODO: set correct width and height
            mSpriteBatch.draw(mSelectedLevel, 220, 100, mSelectedLevel.getWidth() * 1.10f, mSelectedLevel.getHeight()* 1.10f);
            mFont.draw(mSpriteBatch, "Select level: " + mLevelArray.get(mCurrentLevel).getId() + " / " + mLevelArray.size(), 260, 400);
            mFont.draw(mSpriteBatch, "Level name: " + mLevelArray.get(mCurrentLevel).getLevelName(), 200, 70);
            mSpriteBatch.end();

            mLevelSelectionStage.act(Gdx.graphics.getDeltaTime());

            mLevelSelectionStage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {
        mGameIsRunning = false;
    }

    @Override
    public void resume() {
        mGameIsRunning = true;
    }

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        mGameIsRunning = false;
    }

    public class PreviousButton extends Actor {

        private Texture pButtonTexture;

        public PreviousButton(){
            pButtonTexture = mDogenator.getAssetManager().get(BUTTON_PREVIOUS);
            setWidth(pButtonTexture.getWidth());
            setHeight(pButtonTexture.getHeight());
            setBounds(30, 200, getWidth(), getHeight());

            addListener(new PreviousButtonListener());
        }

        public void draw(Batch batch, float delta){
            batch.draw(pButtonTexture, getX(), getY(), getWidth(), getHeight());
        }

        public void act(float delta){
            super.act(delta);
        }

        public void pressedTexture(){
            pButtonTexture = mDogenator.getAssetManager().get(BUTTON_PREVIOUS_PRESSED);
        }

        public void releasedTexture(){
            pButtonTexture = mDogenator.getAssetManager().get(BUTTON_PREVIOUS);
        }

        class PreviousButtonListener extends InputListener {

            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                pressedTexture();
                Log.d("touch started at (" + x + ", " + y + ")");
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                setLevel(-1);
                releasedTexture();
            }
        }
    }

    public class NextButton extends Actor{

        private Texture nButtonTexture;

        public NextButton(){
            nButtonTexture = mDogenator.getAssetManager().get(BUTTON_NEXT);
            setWidth(nButtonTexture.getWidth());
            setHeight(nButtonTexture.getHeight());
            setBounds(680, 200, getWidth(), getHeight());

            addListener(new NextButtonListener());
        }

        public void draw(Batch batch, float delta){
            batch.draw(nButtonTexture, getX(), getY(), getWidth(), getHeight());
        }

        public void pressedTexture(){
            nButtonTexture = mDogenator.getAssetManager().get(BUTTON_NEXT_PRESSED);
        }

        public void releasedTexture(){
            nButtonTexture = mDogenator.getAssetManager().get(BUTTON_NEXT);
        }

        public void act(float delta){
            super.act(delta);
        }

        class NextButtonListener extends InputListener{

            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                pressedTexture();
                Log.d("touch started at (" + x + ", " + y + ")");
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                setLevel(1);
                releasedTexture();
            }
        }
    }

    public class ReturnButton extends Actor{

        private Texture rButtonTexture;

        public ReturnButton(){
            rButtonTexture = mDogenator.getAssetManager().get(BUTTON_RETURN);
            setWidth(rButtonTexture.getWidth());
            setHeight(rButtonTexture.getHeight());
            setBounds(680, 370, getWidth(), getHeight());

            addListener(new ReturnButtonListener());
        }

        public void draw(Batch batch, float delta){
            batch.draw(rButtonTexture, getX(), getY(), getWidth(), getHeight());
        }

        public void pressedTexture(){
            rButtonTexture = mDogenator.getAssetManager().get(BUTTON_RETURN_PRESSED);
        }

        public void releasedTexture(){
            rButtonTexture = mDogenator.getAssetManager().get(BUTTON_RETURN);
        }

        public void act(float delta){
            super.act(delta);
        }

        class ReturnButtonListener extends InputListener{

            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                pressedTexture();
                Log.d("touch started at (" + x + ", " + y + ")");
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                releasedTexture();
                mDogenator.switchScreen(MAINMENU);
            }
        }
    }

    public class PlayButton extends Actor{

        private Texture plButtonTexture;

        public PlayButton(){
            plButtonTexture = mDogenator.getAssetManager().get(BUTTON_ACCEPT);
            setWidth(plButtonTexture.getWidth());
            setHeight(plButtonTexture.getHeight());
            setBounds(680, 30, getWidth(), getHeight());

            addListener(new PlayButtonListener());
        }

        public void draw(Batch batch, float delta){
            batch.draw(plButtonTexture, getX(), getY(), getWidth(), getHeight());
        }

        public void pressedTexture(){
            plButtonTexture = mDogenator.getAssetManager().get(BUTTON_ACCEPT_PRESSED);
        }

        public void releasedTexture(){
            plButtonTexture = mDogenator.getAssetManager().get(BUTTON_ACCEPT);
        }

        public void act(float delta){
            super.act(delta);
        }

        class PlayButtonListener extends InputListener{

            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                pressedTexture();
                Log.d("touch started at (" + x + ", " + y + ")");
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                releasedTexture();
                mDogenator.setScreen(new GameScreen(mDogenator, mLevelArray.get(mCurrentLevel)));
            }
        }
    }
}
