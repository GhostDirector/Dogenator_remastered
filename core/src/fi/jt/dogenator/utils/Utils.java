package fi.jt.dogenator.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import org.json.JSONObject;

import static fi.jt.dogenator.Constants.BACKGROUND_LEVEL_SELECTION;
import static fi.jt.dogenator.Constants.BUTTON_ACCEPT;
import static fi.jt.dogenator.Constants.BUTTON_ACCEPT_PRESSED;
import static fi.jt.dogenator.Constants.BUTTON_CREDITS;
import static fi.jt.dogenator.Constants.BUTTON_CREDITS_PRESSED;
import static fi.jt.dogenator.Constants.BUTTON_MUTE;
import static fi.jt.dogenator.Constants.BUTTON_NEXT;
import static fi.jt.dogenator.Constants.BUTTON_NEXT_PRESSED;
import static fi.jt.dogenator.Constants.BUTTON_PREVIOUS;
import static fi.jt.dogenator.Constants.BUTTON_PREVIOUS_PRESSED;
import static fi.jt.dogenator.Constants.BUTTON_QUIT;
import static fi.jt.dogenator.Constants.BUTTON_QUIT_PRESSED;
import static fi.jt.dogenator.Constants.BUTTON_RETURN;
import static fi.jt.dogenator.Constants.BUTTON_RETURN_HUD;
import static fi.jt.dogenator.Constants.BUTTON_RETURN_PRESSED;
import static fi.jt.dogenator.Constants.BUTTON_RETURN_PRESSED_HUD;
import static fi.jt.dogenator.Constants.BUTTON_SOUND;
import static fi.jt.dogenator.Constants.BUTTON_START;
import static fi.jt.dogenator.Constants.BUTTON_START_PRESSED;
import static fi.jt.dogenator.Constants.ENTITY_DOGE;
import static fi.jt.dogenator.Constants.BACKGROUND_MAIN;
import static fi.jt.dogenator.Constants.LEVEL_COUNT;
import static fi.jt.dogenator.Constants.MUSIC_DEFAULT;
import static fi.jt.dogenator.Constants.MUSIC_DOOM;
import static fi.jt.dogenator.Constants.MUSIC_GABEN;
import static fi.jt.dogenator.Constants.PLAYER_DOGE_NUKEM;

public class Utils {

    public static void loadAssets(AssetManager assetManager) {

        // Main menu resources
        assetManager.load(BACKGROUND_MAIN, Texture.class);
        assetManager.load(BUTTON_CREDITS, Texture.class);
        assetManager.load(BUTTON_CREDITS_PRESSED, Texture.class);
        assetManager.load(BUTTON_MUTE, Texture.class);
        assetManager.load(BUTTON_QUIT, Texture.class);
        assetManager.load(BUTTON_QUIT_PRESSED, Texture.class);
        assetManager.load(BUTTON_SOUND, Texture.class);
        assetManager.load(BUTTON_START, Texture.class);
        assetManager.load(BUTTON_START_PRESSED, Texture.class);

        assetManager.load(MUSIC_DEFAULT, Music.class);
        assetManager.load(MUSIC_DOOM, Music.class);
        assetManager.load(MUSIC_GABEN, Music.class);

        // level select resources
        assetManager.load(BACKGROUND_LEVEL_SELECTION, Texture.class);
        assetManager.load(BUTTON_ACCEPT, Texture.class);
        assetManager.load(BUTTON_ACCEPT_PRESSED, Texture.class);
        assetManager.load(BUTTON_NEXT, Texture.class);
        assetManager.load(BUTTON_NEXT_PRESSED, Texture.class);
        assetManager.load(BUTTON_PREVIOUS, Texture.class);
        assetManager.load(BUTTON_PREVIOUS_PRESSED, Texture.class);
        assetManager.load(BUTTON_RETURN, Texture.class);
        assetManager.load(BUTTON_RETURN_PRESSED, Texture.class);

        // HUD resources
        assetManager.load(BUTTON_RETURN_HUD, Texture.class);
        assetManager.load(BUTTON_RETURN_PRESSED_HUD, Texture.class);

        // Entities
        assetManager.load(ENTITY_DOGE, Texture.class);
        assetManager.load(PLAYER_DOGE_NUKEM, Texture.class);

        // Levels
        loadMaps(assetManager);
    }

    public static void loadMaps(AssetManager assetManager) {
        for(int i = 1; i <= LEVEL_COUNT; i++) {
            String path = "level" + i + "/info.json";
            JSONObject json = loadJson(path);
            assetManager.load(json.getString("image"), Texture.class);
        }
    }

    public static BitmapFont generateFont(String font, int size, Color color, int borderWidth) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(font));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = size;
        param.color = color;
        param.borderWidth = borderWidth;

        return generator.generateFont(param);
    }

    public static JSONObject loadJson(String path){
        FileHandle handle = Gdx.files.internal(path);
        String fileContent = handle.readString();
        return new JSONObject(fileContent);
    }
}
