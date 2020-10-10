package fi.jt.dogenator.gamemechanics;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Iterator;

import fi.jt.dogenator.Dogenator;

public class MapBodyManager {

    private Dogenator mDogenator;

    private World mWorld;
    private float mUnits;
    private Array<Body> mBodies;
    private ObjectMap<String, FixtureDef> mMaterials;


    public MapBodyManager(Dogenator dogenator, World world, float unitsPerPixel, FileHandle materialsFile) {
        mDogenator = dogenator;

        mWorld = world;
        mUnits = unitsPerPixel;

        FixtureDef defaultFixture = new FixtureDef();
        defaultFixture.density = 1.0f;
        defaultFixture.friction = 0.8f;
        defaultFixture.restitution = 0.0f;

        mMaterials.put("default", defaultFixture);

        if(materialsFile != null) {
            loadMaterialsFile(materialsFile);
        }
    }

    private void loadMaterialsFile(FileHandle materialsFile) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 1.0f;
        fixtureDef.restitution = 0.0f;
        mMaterials.put("default", fixtureDef);

        try {
            JsonReader reader = new JsonReader();
            JsonValue root = reader.parse(materialsFile);
            JsonValue.JsonIterator materialIt = root.iterator();

            while (materialIt.hasNext()) {
                JsonValue materialValue = materialIt.next();

                if (!materialValue.has("name")) {
                    //logger.error("material without name");
                    continue;
                }

                String name = materialValue.getString("name");

                fixtureDef = new FixtureDef();
                fixtureDef.density = materialValue.getFloat("density", 1.0f);
                fixtureDef.friction = materialValue.getFloat("friction", 1.0f);
                fixtureDef.restitution = materialValue.getFloat("restitution", 0.0f);
                //logger.info("adding material " + name);
                mMaterials.put(name, fixtureDef);
            }

        } catch (Exception e) {
            //logger.error("error loading " + materialsFile.name() + " " + e.getMessage());
        }
    }

    public void destroyPhysics() {
        for (Body body : mBodies) {
            mWorld.destroyBody(body);
        }

        mBodies.clear();
    }

    public void createPhysics(Map map) {
        createPhysics(map, "physics");
    }

    public void createPhysics(Map map, String layerName) {
        MapLayer layer = map.getLayers().get(layerName);

        if (layer == null) {
            //logger.error("layer " + layerName + " does not exist");
            return;
        }

        MapObjects objects = layer.getObjects();
        Iterator<MapObject> objectIt = objects.iterator();

        while(objectIt.hasNext()) {
            MapObject object = objectIt.next();

            if (object instanceof TextureMapObject){
                continue;
            }

            Shape shape;
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;

            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectangle = (RectangleMapObject)object;
                shape = getRectangle(rectangle);
            }
            else if (object instanceof PolygonMapObject) {
                shape = getPolygon((PolygonMapObject)object);
            }
            else if (object instanceof PolylineMapObject) {
                shape = getPolyline((PolylineMapObject)object);
            }
            else if (object instanceof CircleMapObject) {
                shape = getCircle((CircleMapObject)object);
            }
            else {
                //logger.error("non suported shape " + object);
                continue;
            }

            MapProperties properties = object.getProperties();
            String material = properties.get("material", "default", String.class);
            FixtureDef fixtureDef = mMaterials.get(material);

            if (fixtureDef == null) {
                //logger.error("material does not exist " + material + " using default");
                fixtureDef = mMaterials.get("default");
            }

            fixtureDef.shape = shape;

            // TODO: maybe some implementation
            //fixtureDef.filter.categoryBits = mDogenator.getCategoryBitsManager().getCategoryBits("level");

            Body body = mWorld.createBody(bodyDef);
            body.createFixture(fixtureDef);

            mBodies.add(body);

            fixtureDef.shape = null;
            shape.dispose();
        }
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
}
