package fi.jt.dogenator.uicomponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import fi.jt.dogenator.Dogenator;
import fi.jt.dogenator.logger.Log;
import fi.jt.dogenator.utils.MusicPlayer;

import static fi.jt.dogenator.Constants.BUTTON_RETURN_HUD;
import static fi.jt.dogenator.Constants.BUTTON_RETURN_PRESSED_HUD;
import static fi.jt.dogenator.Constants.DogeScreen.LEVEL_SELECTION;
import static fi.jt.dogenator.Constants.MUSIC_DEFAULT;
import static fi.jt.dogenator.Constants.WORLD_HEIGHT;
import static fi.jt.dogenator.Constants.WORLD_WIDTH;

public class HUD implements Disposable {
    // Game instance
    private Dogenator mDogenator;

    // SpriteBatch
    private SpriteBatch mSpriteBatch;

    // Stage
    public Stage mHUDStage;

    // Viewport
    private Viewport mHUDViewPort;

    // Current score
    private Integer mScore;

    // Label for objective
    private Label mObjectiveLabel;

    // Label for link
    private Label mLinkLabel;

    // Label for score
    private static Label mScoreLabel;

    public HUD(Dogenator dogenator, boolean lightFont) {
        mDogenator = dogenator;
        mSpriteBatch = mDogenator.getSpriteBatch();

        mHUDViewPort = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, new OrthographicCamera());
        mHUDStage = new Stage(mHUDViewPort, mSpriteBatch);

        mScore = 0;

        Color fontColor;
        if(lightFont) {
            fontColor = Color.WHITE;
        } else {
            fontColor = Color.BLACK;
        }

        mScoreLabel = new Label(String.format("%02d", mScore), new Label.LabelStyle(new BitmapFont(), fontColor));
        mObjectiveLabel = new Label("Objective: push doges into the hole", new Label.LabelStyle(new BitmapFont(), fontColor));
        mLinkLabel = new Label("DOGES LEFT", new Label.LabelStyle(new BitmapFont(), fontColor));

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        table.add(mLinkLabel).expandX().padTop(10);
        table.add(mObjectiveLabel).expandX().padTop(10);
        table.row();
        table.add(mScoreLabel).expandX();

        ReturnButton returnButton = new ReturnButton();
        mHUDStage.addActor(table);
        mHUDStage.addActor(returnButton);

        Gdx.input.setInputProcessor(mHUDStage);

    }

    public void setScore(int value) {
        mScoreLabel.setText(String.format("%02d", value));
    }

    // TODO: Add support to change objective
    public void setObjectiveLabel(){
        mObjectiveLabel.setText("Level Completed. Press ---->");
    }

    @Override
    public void dispose() {
        mHUDStage.dispose();
    }

    public class ReturnButton extends Actor {

        private Texture rButtonTexture;

        public ReturnButton(){
            rButtonTexture = mDogenator.getAssetManager().get(BUTTON_RETURN_HUD);
            setWidth(rButtonTexture.getWidth());
            setHeight(rButtonTexture.getHeight());
            setBounds(700, 420, getWidth() * 0.7f, getHeight() * 0.7f);

            addListener(new ReturnButtonListener());
        }

        public void draw(Batch batch, float delta){
            batch.draw(rButtonTexture, getX(), getY(), getWidth(), getHeight());
        }

        public void act(float delta){
            super.act(delta);
        }

        public void pressedTexture(){
            rButtonTexture = mDogenator.getAssetManager().get(BUTTON_RETURN_PRESSED_HUD);
        }

        public void releasedTexture(){
            rButtonTexture = mDogenator.getAssetManager().get(BUTTON_RETURN_HUD);
        }

        class ReturnButtonListener extends InputListener {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                pressedTexture();
                Log.d("touch started at (" + x + ", " + y + ")");
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                releasedTexture();
                MusicPlayer.getInstance().setTrack(MUSIC_DEFAULT);
                if(!MusicPlayer.getInstance().isMuted()) {
                    MusicPlayer.getInstance().setVolume(0.1f);
                    MusicPlayer.getInstance().playMusic();
                }
                mDogenator.switchScreen(LEVEL_SELECTION);
            }
        }
    }
}