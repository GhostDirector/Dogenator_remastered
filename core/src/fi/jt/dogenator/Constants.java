package fi.jt.dogenator;

public class Constants {

    // Background asset Strings (Loading)
    public static final String BACKGROUND_LOADING = "backgrounds/loading_background.jpg";

    // Background asset Strings (Main menu)
    public static final String BACKGROUND_MAIN = "backgrounds/main_menu_background.png";

    // Background asset Strings (Level selection menu)
    public static final String BACKGROUND_LEVEL_SELECTION = "backgrounds/level_selection_background.png";

    // Button asset Strings
    public static final String BUTTON_START = "buttons/button_start.png";
    public static final String BUTTON_START_PRESSED = "buttons/button_start_pressed.png";

    public static final String BUTTON_CREDITS = "buttons/button_credits.png";
    public static final String BUTTON_CREDITS_PRESSED = "buttons/button_credits_pressed.png";

    public static final String BUTTON_QUIT = "buttons/button_quit.png";
    public static final String BUTTON_QUIT_PRESSED = "buttons/button_quit_pressed.png";

    public static final String BUTTON_SOUND = "buttons/button_sound.png";
    public static final String BUTTON_MUTE = "buttons/button_mute.png";

    public static final String BUTTON_ACCEPT = "buttons/button_accept.png";
    public static final String BUTTON_ACCEPT_PRESSED = "buttons/button_accept_pressed.png";

    public static final String BUTTON_NEXT = "buttons/button_next.png";
    public static final String BUTTON_NEXT_PRESSED = "buttons/button_next_pressed.png";

    public static final String BUTTON_PREVIOUS = "buttons/button_previous.png";
    public static final String BUTTON_PREVIOUS_PRESSED = "buttons/button_previous_pressed.png";

    public static final String BUTTON_RETURN = "buttons/button_return.png";
    public static final String BUTTON_RETURN_PRESSED = "buttons/button_return_pressed.png";

    // HUD
    public static final String BUTTON_RETURN_HUD = "buttons/button_hud_return.png";
    public static final String BUTTON_RETURN_PRESSED_HUD = "buttons/button_hud_return_pressed.png";

    // Entities
    public static final String ENTITY_DOGE = "entities/doge.png";

    // Player skins
    public static final String PLAYER_DOGE_DEFAULT = ENTITY_DOGE;
    public static final String PLAYER_DOGE_NUKEM = "entities/doge_nukem.png";

    // Music
    public static final String MUSIC_DEFAULT = "music/background_music_default.mp3";
    public static final String MUSIC_DOOM = "music/background_music_doom.mp3";
    public static final String MUSIC_GABEN = "music/background_music_gaben.mp3";

    // Fonts
    public static final String FONT_COMIC_SANS = "fonts/comic.ttf";

    // Layers
    public static final String LAYER_WORLD_WALL_RECT = "world-wall-rectangles";
    public static final String LAYER_GROUND_RECT = "ground-rectangles";
    public static final String LAYER_BOUNCE_RECT = "bounce-rectangles";
    public static final String LAYER_POLYGON = "polygon-layer";
    public static final String LAYER_PLAYER = "player-layer";
    public static final String LAYER_DOGE = "doge-layer";
    public static final String LAYER_LIGHT = "light-layer";

    // LEVEL COUNT
    public static final int LEVEL_COUNT = 5;

    // LEVEL ID
    public static final int LEVEL_1_ID = 1;
    public static final int LEVEL_2_ID = 2;
    public static final int LEVEL_3_ID = 3;
    public static final int LEVEL_4_ID = 4;
    public static final int LEVEL_5_ID = 5;

    // Button asset Strings (Level selection menu)
    public static final float WORLD_WIDTH = 800f;
    public static final float WORLD_HEIGHT = 480f;

    public enum DogeScreen {

        LOADING,
        MAINMENU,
        LEVEL_SELECTION
    }
}
