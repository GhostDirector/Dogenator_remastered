package fi.jt.dogenator.level;

import com.badlogic.gdx.graphics.Color;

import org.json.JSONObject;

import fi.jt.dogenator.utils.Utils;

public class LevelInformation {

    private int mID;
    private String mLevelName;
    private String mPicture;
    private String mMap;
    private boolean mHasLight;
    private Color mLightColor;
    private float mLightAmbient;
    private int mLightRays;
    private int mLightDistance;
    private boolean mAllowPlayerLight;
    private boolean mIsLightXRAY;
    private String mSoundTrack;

    public LevelInformation(int id){
        mID = id;
        loadFromJSON();
    }

    public void loadFromJSON() {
        String path = "level" + mID + "/info.json";
        JSONObject json = Utils.loadJson(path);

        mLevelName = json.getString("name");
        mHasLight = json.getBoolean("has_light");
        mPicture = json.getString("image");
        mMap = json.getString("map");
        mSoundTrack = json.getString("soundtrack");

        if(mHasLight) {
            mLightColor = mapColor(json.getInt("light_color"));
            mLightAmbient = json.getFloat("light_ambient");
            mLightRays = json.getInt("light_rays");
            mLightDistance = json.getInt("light_distance");
            mIsLightXRAY = json.getBoolean("light_xray");
            mAllowPlayerLight = json.getBoolean("allow_player_light");
        }
    }

    //
    // Colors
    // 1 = White
    // 2 = Red
    // 3 = Yellow
    // 4 = Purple
    // 5 = Green
    // 6 = Gaben
    //
    public Color mapColor(int color) {
        switch(color) {
            case 1:
                return Color.WHITE;
            case 2:
                return Color.RED;
            case 3:
                return Color.YELLOW;
            case 4:
                return Color.PURPLE;
            case 5:
                return Color.GREEN;
            case 6:
                return new Color(255, 215, 0, 0.8f);
            default:
                return Color.WHITE;
        }
    }

    public int getId(){
        return mID;
    }
    public String getLevelName(){
        return mLevelName;
    }
    public String getPicture(){
        return mPicture;
    }
    public String getMap(){
        return mMap;
    }
    public boolean hasLight() {
        return mHasLight;
    }
    public void hasLight(boolean mHasLight) {
        this.mHasLight = mHasLight;
    }
    public Color getLightColor() {
        return mLightColor;
    }
    public float getLightAmbient() {
        return mLightAmbient;
    }
    public void setLightAmbient(float lightAmbient) {
        this.mLightAmbient = lightAmbient;
    }
    public int getLightRays() {
        return mLightRays;
    }
    public void setLightRays(int lightRays) {
        this.mLightRays = lightRays;
    }
    public int getLightDistance() {
        return mLightDistance;
    }
    public void setLightDistance(int lightDistance) {
        this.mLightDistance = lightDistance;
    }
    public boolean isAllowPlayerLight() {
        return mAllowPlayerLight;
    }
    public void setAllowPlayerLight(boolean allowPlayerLight) {
        this.mAllowPlayerLight = allowPlayerLight;
    }
    public boolean isLightXRAY() {
        return mIsLightXRAY;
    }
    public void setIsLightXRAY(boolean isLightXRAY) {
        this.mIsLightXRAY = isLightXRAY;
    }
    public String getSoundTrack() {
        return mSoundTrack;
    }
    public void setSoundTrack(String soundTrack) {
        this.mSoundTrack = soundTrack;
    }

    @Override
    public String toString() {
        return "ID: " + getId() + " Level name: " + mLevelName;
    }
}
