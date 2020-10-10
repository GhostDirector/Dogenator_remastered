package fi.jt.dogenator.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import fi.jt.dogenator.Dogenator;
import fi.jt.dogenator.logger.Log;
import fi.jt.dogenator.utils.MusicPlayer;

import static fi.jt.dogenator.Constants.DogeScreen.LEVEL_SELECTION;
import static fi.jt.dogenator.Constants.BUTTON_CREDITS;
import static fi.jt.dogenator.Constants.BUTTON_CREDITS_PRESSED;
import static fi.jt.dogenator.Constants.BUTTON_MUTE;
import static fi.jt.dogenator.Constants.BUTTON_QUIT;
import static fi.jt.dogenator.Constants.BUTTON_QUIT_PRESSED;
import static fi.jt.dogenator.Constants.BUTTON_SOUND;
import static fi.jt.dogenator.Constants.BUTTON_START;
import static fi.jt.dogenator.Constants.BUTTON_START_PRESSED;
import static fi.jt.dogenator.Constants.BACKGROUND_MAIN;
import static fi.jt.dogenator.Constants.WORLD_HEIGHT;
import static fi.jt.dogenator.Constants.WORLD_WIDTH;

public class MainMenuScreen implements Screen {
    // Game instance
    private Dogenator mDogenator;

    // SpriteBatch
    private SpriteBatch mSpiteBatch;

    // Background
    private Texture mBackground;

    // Stage
    private Stage mMainStage;

    // Main menu buttons
    private StartButton startButton;
    private CreditsButton creditsButton;
    private QuitButton quitButton;
    private SoundButton soundButton;
    private FitViewport fitViewport;

    // Game state
    private boolean mGameIsRunning;

    public MainMenuScreen(Dogenator dogenator) {
        mGameIsRunning = true;
        mDogenator = dogenator;

        mSpiteBatch = mDogenator.getSpriteBatch();
        mBackground = mDogenator.getAssetManager().get(BACKGROUND_MAIN, Texture.class);
        fitViewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
        mMainStage = new Stage(fitViewport, mSpiteBatch);

        addActors();

        if(!MusicPlayer.getInstance().isMuted())
        MusicPlayer.getInstance().playMusic();

        Gdx.input.setInputProcessor(mMainStage);
    }

    // Screens
    private void addActors() {
        startButton = new StartButton();
        mMainStage.addActor(startButton);
        creditsButton = new CreditsButton();
        mMainStage.addActor(creditsButton);
        quitButton = new QuitButton();
        mMainStage.addActor(quitButton);
        soundButton = new SoundButton();
        mMainStage.addActor(soundButton);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        if(mGameIsRunning){
            Gdx.gl.glClearColor(0,0,0,0);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            mSpiteBatch.begin();
            mSpiteBatch.draw(mBackground, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
            mSpiteBatch.end();

            mMainStage.act(Gdx.graphics.getDeltaTime());

            mMainStage.draw();
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

    public class StartButton extends Actor {

        private Texture sButtonTexture;

        public StartButton(){
            sButtonTexture = mDogenator.getAssetManager().get(BUTTON_START);
            setWidth(sButtonTexture.getWidth());
            setHeight(sButtonTexture.getHeight());
            setBounds(WORLD_WIDTH * 0.59f, WORLD_HEIGHT * 0.7f, getWidth(), getHeight());

            addListener(new StartButtonListener());
        }

        public void draw(Batch batch, float delta){
            batch.draw(sButtonTexture, getX(), getY(), getWidth(), getHeight());
        }

        public void act(float delta){
            super.act(delta);
        }

        public void pressedTexture(){
            sButtonTexture = mDogenator.getAssetManager().get(BUTTON_START_PRESSED);
        }

        public void releasedTexture(){
            sButtonTexture = mDogenator.getAssetManager().get(BUTTON_START);
        }

        class StartButtonListener extends InputListener {

            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                pressedTexture();
                Log.d("touch started at (" + x + ", " + y + ")");
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                releasedTexture();
                mDogenator.switchScreen(LEVEL_SELECTION);
            }
        }
    }

    public class CreditsButton extends Actor {

        private Texture cButtonTexture;

        public CreditsButton(){
            cButtonTexture = mDogenator.getAssetManager().get(BUTTON_CREDITS);
            setWidth(cButtonTexture.getWidth());
            setHeight(cButtonTexture.getHeight());
            setBounds(WORLD_WIDTH * 0.59f, WORLD_HEIGHT * 0.5f, getWidth(), getHeight());

            addListener(new CreditsButtonListener());
        }

        public void draw(Batch batch, float delta){
            batch.draw(cButtonTexture, getX(), getY(), getWidth(), getHeight());
        }

        public void pressedTexture(){
            cButtonTexture = mDogenator.getAssetManager().get(BUTTON_CREDITS_PRESSED);
        }

        public void releasedTexture(){
            cButtonTexture = mDogenator.getAssetManager().get(BUTTON_CREDITS);
        }

        public void act(float delta){
            super.act(delta);
        }

        class CreditsButtonListener extends InputListener{

            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                pressedTexture();
                Log.d("touch started at (" + x + ", " + y + ")");
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                releasedTexture();
                // TODO: implement
                //mDogenator.switchScreen();
            }
        }
    }

    public class QuitButton extends Actor{

        private Texture qButtonTexture;

        public QuitButton(){
            qButtonTexture = mDogenator.getAssetManager().get(BUTTON_QUIT);
            setWidth(qButtonTexture.getWidth());
            setHeight(qButtonTexture.getHeight());
            setBounds(WORLD_WIDTH * 0.59f, WORLD_HEIGHT * 0.3f, getWidth(), getHeight());

            addListener(new QuitButtonListener());
        }

        public void draw(Batch batch, float delta){
            batch.draw(qButtonTexture, getX(), getY(), getWidth(), getHeight());
        }

        public void pressedTexture(){
            qButtonTexture = mDogenator.getAssetManager().get(BUTTON_QUIT_PRESSED);
        }

        public void releasedTexture(){
            qButtonTexture = mDogenator.getAssetManager().get(BUTTON_QUIT);
        }

        public void act(float delta){
            super.act(delta);
        }

        class QuitButtonListener extends InputListener{

            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                pressedTexture();
                Log.d("touch started at (" + x + ", " + y + ")");
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                releasedTexture();
                System.exit(0);
            }
        }
    }

    public class SoundButton extends Actor{

        private Texture soundButtonTexture;

        public SoundButton(){
            pressedTexture();
            setWidth(soundButtonTexture.getWidth());
            setHeight(soundButtonTexture.getHeight());
            setBounds(WORLD_WIDTH * 0.87f, WORLD_HEIGHT * 0.1f, getWidth(), getHeight());

            addListener(new SoundButtonListener());
        }

        public void draw(Batch batch, float delta){
            batch.draw(soundButtonTexture, getX(), getY(), getWidth(), getHeight());
        }

        public void pressedTexture(){
            if(!MusicPlayer.getInstance().isMuted()){
                soundButtonTexture = mDogenator.getAssetManager().get(BUTTON_SOUND);
            }
            if(MusicPlayer.getInstance().isMuted()){
                soundButtonTexture = mDogenator.getAssetManager().get(BUTTON_MUTE);
            }
        }

        public void act(float delta){
            super.act(delta);
        }

        class SoundButtonListener extends InputListener{

            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                // TODO: implement music playing
                if(!MusicPlayer.getInstance().isMuted()){
                    MusicPlayer.getInstance().setMuted(true);
                    pressedTexture();
                } else {
                    MusicPlayer.getInstance().setMuted(false);
                    pressedTexture();
                }

                Log.d("touch started at (" + x + ", " + y + ")");
                return true;
            }
        }
    }
}
