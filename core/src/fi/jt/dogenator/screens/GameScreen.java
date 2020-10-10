package fi.jt.dogenator.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import fi.jt.dogenator.Configurations;
import fi.jt.dogenator.Dogenator;
import fi.jt.dogenator.gamemechanics.Entity;
import fi.jt.dogenator.level.LevelInformation;
import fi.jt.dogenator.logger.Log;
import fi.jt.dogenator.uicomponents.HUD;
import fi.jt.dogenator.utils.MusicPlayer;

import static fi.jt.dogenator.Constants.ENTITY_DOGE;
import static fi.jt.dogenator.Constants.LAYER_BOUNCE_RECT;
import static fi.jt.dogenator.Constants.LAYER_DOGE;
import static fi.jt.dogenator.Constants.LAYER_GROUND_RECT;
import static fi.jt.dogenator.Constants.LAYER_LIGHT;
import static fi.jt.dogenator.Constants.LAYER_PLAYER;
import static fi.jt.dogenator.Constants.LAYER_POLYGON;
import static fi.jt.dogenator.Constants.LAYER_WORLD_WALL_RECT;
import static fi.jt.dogenator.Constants.WORLD_HEIGHT;
import static fi.jt.dogenator.Constants.WORLD_WIDTH;

public class GameScreen implements Screen {
    // Game instance
    private Dogenator mDogenator;

    // SpriteBatch
    private SpriteBatch mSpriteBatch;

    // Info of the selected level
    private LevelInformation mLevelInfo;

    // Map of the selected level
    private TiledMap mTiledMap;

    // Renderer for map
    private TiledMapRenderer mTiledMapRenderer;

    // Tile units
    private float mUnits = 16;

    // Camera
    private OrthographicCamera mCamera;

    // HUD containing objective and other stuff
    private HUD mHud;

    // World
    private World mWorld;

    // Player entity
    private Entity mPlayerEntity;

    // Other entities in the map
    private ArrayList<Entity> mEntities;

    // Debug renderer for objects
    private Box2DDebugRenderer mDebugRenderer;

    // Bodies to be removed
    private Array<Body> mRemovalBodyArray;

    // Original count of doges in the map
    private int mDogeCount;

    // Rayhandler
    private RayHandler mRayHandler;

    // Player light
    private PointLight mPlayerlight;

    // Game state
    private boolean mGameIsRunning;

    // Player's starting point
    private float startX;
    private float startY;

    public GameScreen(Dogenator dogenator, LevelInformation levelInfo) {
        mGameIsRunning = true;

        mDogenator = dogenator;
        mSpriteBatch = mDogenator.getSpriteBatch();
        mLevelInfo = levelInfo;

        setupWorld();
        initTiledMap();
        setupCamera();
        moveCamera();

        if(mLevelInfo.hasLight()) {
            setupLight();
        }

        setupSoundtrack();

        mDebugRenderer = new Box2DDebugRenderer();
        mWorld.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {}

            @Override
            public void endContact(Contact contact) {}

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                String userDataA = (String) (contact.getFixtureA().getBody().getUserData());
                String userDataB = (String) (contact.getFixtureB().getBody().getUserData());

                if(userDataA.equals("")) {
                    contact.setEnabled(false);
                    mRemovalBodyArray.add(contact.getFixtureA().getBody());
                }
                if(userDataB.equals("")) {
                    contact.setEnabled(false);
                    mRemovalBodyArray.add(contact.getFixtureB().getBody());
                }
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {}
        });
    }

    private void setupSoundtrack() {
        MusicPlayer.getInstance().setTrack(mLevelInfo.getSoundTrack());
        if(!MusicPlayer.getInstance().isMuted()) {
            MusicPlayer.getInstance().setVolume(0.1f);
            MusicPlayer.getInstance().playMusic();
        }
    }

    private void setupWorld() {
        // TODO: check vector floats
        mWorld = new World(new Vector2(0f, -30.0f), true);

        mEntities = new ArrayList<Entity>();
        mRemovalBodyArray = new Array<Body>();

        mHud = new HUD(mDogenator, mLevelInfo.hasLight());
    }

    private void setupCamera() {
        mCamera = new OrthographicCamera();
        mCamera.setToOrtho(false,         // y points up
                WORLD_WIDTH / mUnits,            // width
                WORLD_HEIGHT / mUnits);          // height
    }

    private void initTiledMap() {
        mTiledMap = new TmxMapLoader().load(mLevelInfo.getMap());
        //mTiledMapRenderer = new OrthogonalTiledMapRenderer(mTiledMap, 6.2f / 100f);
        mTiledMapRenderer = new OrthogonalTiledMapRenderer(mTiledMap, 1f / mUnits);

        transformWallsToBodies(LAYER_WORLD_WALL_RECT, "wall");
        transformWallsToBodies(LAYER_GROUND_RECT, "wall");
        transformWallsToBodies(LAYER_BOUNCE_RECT, "bounce");
        if(mTiledMap.getLayers().get(LAYER_POLYGON) != null) {
            transformWallsToBodies(LAYER_POLYGON, "wall");
        }

        transformPlayer(LAYER_PLAYER);
        transformDogeLocations(LAYER_DOGE);
    }

    private void transformWallsToBodies(String layer, String userData) {
        MapLayer collisionObjectLayer = mTiledMap.getLayers().get(layer);
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        for(MapObject object : mapObjects) {
            if(object instanceof TextureMapObject) {
                continue;
            }

            Shape shape;
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;

            if(object instanceof RectangleMapObject) {
                RectangleMapObject rectangle = (RectangleMapObject) object;
                shape = getRectangle(rectangle);
            } else if(object instanceof PolygonMapObject) {
                shape = getPolygon((PolygonMapObject) object);
            } else if(object instanceof PolylineMapObject) {
                shape = getPolyline((PolylineMapObject) object);
            } else if(object instanceof CircleMapObject) {
                shape = getCircle((CircleMapObject) object);
            } else {
                Log.e("Shape type is not supported: " + object + " Will ignore.");
                continue;
            }

            FixtureDef fixtureDef = createFixtureDef();
            fixtureDef.shape = shape;

            Body body = mWorld.createBody(bodyDef);
            body.createFixture(fixtureDef);
            body.setUserData(userData);
            //mBodies.add(body);

            //fixtureDef.shape = null;
            //shape.dispose();
        }
    }

    private void transformPlayer(String layer) {
        MapLayer collisionObjectLayer = mTiledMap.getLayers().get(layer);
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        for(MapObject object : mapObjects) {
            if(object instanceof TextureMapObject) {
                continue;
            }

            if(object instanceof RectangleMapObject) {
                RectangleMapObject rectangle = (RectangleMapObject) object;
                Rectangle rect = rectangle.getRectangle();
                startX = (rect.x + (rect.width / 2)) / mUnits;
                startY = (rect.y + (rect.height / 2)) / mUnits;
                mPlayerEntity = new Entity(
                        (Texture) mDogenator.getAssetManager().get(Configurations.currentlySelectedPlayerSkin),
                        startX,
                        startY,
                        mWorld,
                        "player",
                        0.30f,
                        0.020f,
                        0.020f,
                        0.9f);
            }
        }
    }

    private void transformDogeLocations(String layer) {
        MapLayer collisionObjectLayer = mTiledMap.getLayers().get(layer);
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        for(MapObject object : mapObjects) {
            if(object instanceof TextureMapObject) {
                continue;
            }

            if(object instanceof RectangleMapObject) {
                RectangleMapObject rectangle = (RectangleMapObject) object;
                Rectangle rect = rectangle.getRectangle();
                float x = (rect.x + (rect.width / 2)) / mUnits;
                float y = (rect.y + (rect.height / 2)) / mUnits;
                mEntities.add(new Entity((Texture) mDogenator.getAssetManager().get(ENTITY_DOGE), x, y, mWorld, "doge1", 0.15f, 0.020f, 0.020f, 0.9f));
                mDogeCount++;
            }
        }
    }

    private void setupLight() {
        mRayHandler = new RayHandler(mWorld);
        mRayHandler.setAmbientLight(mLevelInfo.getLightAmbient());
        if(mLevelInfo.isAllowPlayerLight()) {
            mPlayerlight = new PointLight(mRayHandler, 10, Color.GRAY, 10, 0, 0);
            mPlayerlight.setSoftnessLength(0f);
            mPlayerlight.attachToBody(mPlayerEntity.getEntityBody());
            mPlayerlight.setXray(true);
        }
        mRayHandler.setShadows(true);

        transformLight(LAYER_LIGHT);
    }

    private void transformLight(String layer) {
        MapLayer collisionObjectLayer = mTiledMap.getLayers().get(layer);
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        for(MapObject object : mapObjects) {
            if(object instanceof TextureMapObject) {
                continue;
            }

            if(object instanceof RectangleMapObject) {
                RectangleMapObject rectangle = (RectangleMapObject) object;
                Rectangle rect = rectangle.getRectangle();
                float x = (rect.x + (rect.width / 2)) / mUnits;
                float y = (rect.y + (rect.height / 2)) / mUnits;
                PointLight l = new PointLight(mRayHandler, mLevelInfo.getLightRays(), mLevelInfo.getLightColor(), mLevelInfo.getLightDistance(), x, y);
                if(mLevelInfo.isLightXRAY()) {
                    l.setXray(true);
                }
            }
        }
    }

    private FixtureDef createFixtureDef() {
        FixtureDef fixtureDef = new FixtureDef();

        // TODO: change values
        fixtureDef.density = 0.0f;
        fixtureDef.friction = 1.0f;
        fixtureDef.restitution = 0.0f;
        return fixtureDef;
    }

    private Shape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / mUnits,
                (rectangle.y + rectangle.height * 0.5f ) / mUnits);
        polygon.setAsBox(rectangle.width * 0.5f / mUnits,
                rectangle.height * 0.5f / mUnits,
                size,
                0.0f);
        return polygon;
    }

    private Shape getCircle(CircleMapObject circleObject) {
        Circle circle = circleObject.getCircle();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.radius / mUnits);
        circleShape.setPosition(new Vector2(circle.x / mUnits, circle.y / mUnits));
        return circleShape;
    }

    private Shape getPolygon(PolygonMapObject polygonObject) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();

        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            worldVertices[i] = vertices[i] / mUnits;
        }

        polygon.set(worldVertices);
        return polygon;
    }

    private Shape getPolyline(PolylineMapObject polylineObject) {
        float[] vertices = polylineObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] / mUnits;
            worldVertices[i].y = vertices[i * 2 + 1] / mUnits;
        }

        ChainShape chain = new ChainShape();
        chain.createChain(worldVertices);
        return chain;
    }

    public void resurrectPlayer(){
        if(mPlayerEntity.getEntityBody().getPosition().y < -1 * mPlayerEntity.getRadius() * 2){
            mPlayerEntity.getEntityBody().setTransform(new Vector2(startX, startY + mPlayerEntity.getRadius()*2), 0);
        }
    }

    private void checkUserInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            mPlayerEntity.getEntityBody().applyForceToCenter(new Vector2(-30.5f, 0), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            mPlayerEntity.getEntityBody().applyForceToCenter(new Vector2(30.5f, 0), true);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            mPlayerEntity.getEntityBody().applyLinearImpulse(new Vector2(0, 20f),
                    mPlayerEntity.getEntityBody().getWorldCenter(), true);
        }
        if(Gdx.input.getAccelerometerY() < -1) {
            mPlayerEntity.getEntityBody().applyForceToCenter(new Vector2(-30.5f, 0), true);
        }
        if(Gdx.input.getAccelerometerY() > 1) {
            mPlayerEntity.getEntityBody().applyForceToCenter(new Vector2(30.5f, 0), true);
        }
        if (Gdx.input.isTouched()) {
            mPlayerEntity.getEntityBody().applyLinearImpulse(new Vector2(0, 0.20f),
                    mPlayerEntity.getEntityBody().getWorldCenter(), true);
        }
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void moveCamera() {
        mCamera.position.set(mPlayerEntity.getEntityBody().getPosition().x,
                mPlayerEntity.getEntityBody().getPosition().y,
                0);

        mCamera.update();
    }

    public void clearBodies() {
        for (int i = 0; i < mEntities.size(); i++){
            if(mEntities.get(i).getEntityBody().getPosition().y < -1 * mEntities.get(i).getRadius()*2) {
                mEntities.get(i).getEntityBody().setLinearVelocity(new Vector2(0,0));
                mEntities.get(i).setDead();
                mDogeCount--;
            }
        }

        for (int j = 0; j < mEntities.size(); j++){
            if (mEntities.get(j).getIsDead()){
                float yPos = mEntities.get(j).getEntityBody().getPosition().y;
                if (yPos < -1 * mEntities.get(j).getRadius() * 2) {

                    if(!mWorld.isLocked())
                        mEntities.get(j).getEntityBody().getWorld().destroyBody(mEntities.get(j).getEntityBody());
                    mEntities.remove(j);
                }
            }
        }
        mHud.setScore(mDogeCount);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        if(mGameIsRunning){
            mSpriteBatch.setProjectionMatrix(mCamera.combined);

            clearScreen();

            mTiledMapRenderer.setView(mCamera);
            mTiledMapRenderer.render();
            mDebugRenderer.render(mWorld, mCamera.combined);

            checkUserInput();
            moveCamera();

            Log.d("Player's position: " + mPlayerEntity.getEntityBody().getPosition());

            mSpriteBatch.begin();

            mSpriteBatch.draw(mPlayerEntity.getEntityTexture(),
                    mPlayerEntity.getEntityBody().getPosition().x - mPlayerEntity.getRadius(),
                    mPlayerEntity.getEntityBody().getPosition().y - mPlayerEntity.getRadius(),
                    mPlayerEntity.getRadius(),                 // originX
                    mPlayerEntity.getRadius(),                 // originY
                    mPlayerEntity.getRadius() * 2,             // width
                    mPlayerEntity.getRadius() * 2,             // height
                    1.0f,                          // scaleX
                    1.0f,                          // scaleY
                    mPlayerEntity.getEntityBody().getTransform().getRotation() * MathUtils.radiansToDegrees,
                    0,                             // Start drawing from x = 0
                    0,                             // Start drawing from y = 0
                    mPlayerEntity.getEntityTexture().getWidth(),      // End drawing x
                    mPlayerEntity.getEntityTexture().getHeight(),     // End drawing y
                    false,                         // flipX
                    false);                        // flipY

            resurrectPlayer();

            for (int i = 0; i < mEntities.size(); i++){
                if (!mEntities.get(i).getIsDead())
                    mSpriteBatch.draw(mEntities.get(i).getEntityTexture(),
                            mEntities.get(i).getEntityBody().getPosition().x - mEntities.get(i).getRadius(),
                            mEntities.get(i).getEntityBody().getPosition().y - mEntities.get(i).getRadius(),
                            mEntities.get(i).getRadius(),                 // originX
                            mEntities.get(i).getRadius(),                 // originY
                            mEntities.get(i).getRadius() * 2,             // width
                            mEntities.get(i).getRadius() * 2,             // height
                            1.0f,                          // scaleX
                            1.0f,                          // scaleY
                            mEntities.get(i).getEntityBody().getTransform().getRotation() * MathUtils.radiansToDegrees,
                            0,                             // Start drawing from x = 0
                            0,                             // Start drawing from y = 0
                            mEntities.get(i).getEntityTexture().getWidth(),      // End drawing x
                            mEntities.get(i).getEntityTexture().getHeight(),     // End drawing y
                            false,                         // flipX
                            false);                        // flipY
            }

            mSpriteBatch.end();

            if(mLevelInfo.hasLight()) {
                mRayHandler.setCombinedMatrix(mCamera.combined);
                mRayHandler.updateAndRender();
            }

            mSpriteBatch.setProjectionMatrix(mHud.mHUDStage.getCamera().combined);
            mHud.mHUDStage.draw();



            mWorld.step(1 / 60f, 50, 90);
            clearBodies();
            if(mDogeCount == 0){
                mHud.setObjectiveLabel();
            }
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
}
