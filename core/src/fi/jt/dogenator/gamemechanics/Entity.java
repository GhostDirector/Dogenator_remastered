package fi.jt.dogenator.gamemechanics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Entity extends BodyDef {

    private Texture mEntityTexture;
    private Body mEntityBody;
    private FixtureDef mEntityFixtureDef;
    private CircleShape mCircleshape;
    private boolean mIsDead;
    private float mPosX, mPosY, mRadius;
    private World mWorld;

    public Entity(Texture texture, float x, float y, World world, String userdata, float density, float restitution, float friction, float radius){
        mEntityTexture = texture;
        type = BodyDef.BodyType.DynamicBody;
        mPosX = x;
        mPosY = y;
        mRadius = radius;
        mWorld = world;
        mIsDead = false;

        position.set(mPosX, mPosY); //1f, 7f

        mEntityBody = this.mWorld.createBody(this);
        mEntityBody.setUserData(userdata);
        mEntityFixtureDef = new FixtureDef();

        mEntityFixtureDef.density     = density; //4.5f
        mEntityFixtureDef.restitution = restitution; //0.7f
        mEntityFixtureDef.friction    = friction; //0.7f

        mCircleshape = new CircleShape();
        mCircleshape.setRadius(radius);

        mEntityFixtureDef.shape = mCircleshape;
        mEntityBody.createFixture(mEntityFixtureDef);
    }

    public Body getEntityBody() {
        return mEntityBody;
    }

    public Texture getEntityTexture() {
        return mEntityTexture;
    }

    public FixtureDef getEntityFixtureDef(){
        return mEntityFixtureDef;
    }

    public CircleShape getCircleshape() {
        return mCircleshape;
    }

    public void setDead(){
        mIsDead = true;
    }

    public boolean getIsDead(){
        return mIsDead;
    }

    public float getPosX() {
        return mPosX;
    }

    public float getPosY() {
        return mPosY;
    }

    public float getRadius() {
        return mRadius;
    }
}
