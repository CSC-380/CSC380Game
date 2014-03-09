package edu.oswego.tiltandtumble.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import edu.oswego.tiltandtumble.worldObjects.Ball;
import edu.oswego.tiltandtumble.worldObjects.PushBumper;
import edu.oswego.tiltandtumble.worldObjects.StaticWall;


public final class WorldPopulator {
    private final BodyDefBuilder bodyDef = new BodyDefBuilder();
    private final FixtureDefBuilder fixtureDef = new FixtureDefBuilder();


    public Ball populateWorldFromMap(TiledMap map, World world, UnitScale scale) {
        // TODO: probably need to return an data structure of all the objects
        //       some may need to be disposed once the world goes away...
        Ball ball = null;
        MapLayer layer = map.getLayers().get("collision");
        for (MapObject obj : layer.getObjects()) {
            // NOTE: when creating the map objects the polygons must have no more
            //       than 8 vertices and must not be concave. this is a limitation
            //       of the physics engine. so complex shapes need to be composed
            //       of multiple adjacent polygons.
            if (obj.getName().equals("StaticWall")) {
                if (obj instanceof PolygonMapObject) {
                    Gdx.app.log("populating map", "adding wall");
                    createStaticWall((PolygonMapObject)obj, world, scale);
                }
                else if (obj instanceof RectangleMapObject) {
                    Gdx.app.log("populating map", "adding wall");
                    createStaticWall((RectangleMapObject)obj, world, scale);
                }
            }
            else if (obj.getName().equals("PushBumper")) {
                if (obj instanceof PolygonMapObject) {
                    Gdx.app.log("populating map", "adding bumper");
                    createPushBumper((PolygonMapObject)obj, world, scale);
                }
                else if (obj instanceof RectangleMapObject) {
                    Gdx.app.log("populating map", "adding bumper");
                    createPushBumper((RectangleMapObject)obj, world, scale);
                }
            }
            else if (obj.getName().equals("Ball")) {
                if (obj instanceof EllipseMapObject) {
                    Gdx.app.log("populating map", "adding ball");
                    ball = createBall((EllipseMapObject)obj, world, scale);
                }
            }
        }
        return ball;
    }

    public StaticWall createStaticWall(PolygonMapObject obj, World world, UnitScale scale) {
        Polygon polygon = obj.getPolygon();
        polygon.scale(scale.getScale());
        PolygonShape shape = createPolygonShape(polygon);

        // TODO: look into allowing these values to be customized using the map object properties
        Body body = world.createBody(bodyDef.reset()
                .type(StaticWall.BODY_TYPE)
                .position(polygon.getX(), polygon.getY()).build());
        body.createFixture(fixtureDef.reset()
                .shape(shape)
                .friction(StaticWall.FRICTION)
                .density(StaticWall.DENSITY)
                .restitution(StaticWall.RESTITUTION).build());

        // dispose after creating fixture
        shape.dispose();
        return new StaticWall(body);
    }

    public StaticWall createStaticWall(RectangleMapObject obj, World world, UnitScale scale) {
        Rectangle rectangle = obj.getRectangle();
        PolygonShape shape = createPolygonShape(rectangle, scale);

        // TODO: look into allowing these values to be customized using the map object properties
        Body body = world.createBody(
                bodyDef.reset().type(StaticWall.BODY_TYPE).build());
        body.createFixture(fixtureDef.reset()
                .shape(shape)
                .friction(StaticWall.FRICTION)
                .density(StaticWall.DENSITY)
                .restitution(StaticWall.RESTITUTION).build());

        // dispose after creating fixture
        shape.dispose();
        return new StaticWall(body);
    }

    public PushBumper createPushBumper(PolygonMapObject obj, World world, UnitScale scale) {
        Polygon polygon = obj.getPolygon();
        polygon.scale(scale.getScale());
        PolygonShape shape = createPolygonShape(polygon);

        // TODO: look into allowing these values to be customized using the map object properties
        Body body = world.createBody(bodyDef.reset()
                .type(PushBumper.BODY_TYPE)
                .position(polygon.getX(), polygon.getY()).build());
        body.createFixture(fixtureDef.reset()
                .shape(shape)
                .friction(PushBumper.FRICTION)
                .density(PushBumper.DENSITY)
                .restitution(PushBumper.RESTITUTION).build());

        // dispose after creating fixture
        shape.dispose();
        return new PushBumper(body, scale);
    }

    public PushBumper createPushBumper(RectangleMapObject obj, World world, UnitScale scale) {
        Rectangle rectangle = obj.getRectangle();
        PolygonShape shape = createPolygonShape(rectangle, scale);

        // TODO: look into allowing these values to be customized using the map object properties
        Body body = world.createBody(
                bodyDef.reset().type(PushBumper.BODY_TYPE).build());
        body.createFixture(fixtureDef.reset()
                .shape(shape)
                .friction(PushBumper.FRICTION)
                .density(PushBumper.DENSITY)
                .restitution(PushBumper.RESTITUTION).build());

        // dispose after creating fixture
        shape.dispose();
        return new PushBumper(body, scale);
    }

    public Ball createBall(EllipseMapObject obj, World world, UnitScale scale) {
        Ellipse ellipse = obj.getEllipse();
        CircleShape shape = createCircleShape(ellipse, scale);

        // TODO: look into allowing these values to be customized using the map object properties
        Body body = world.createBody(bodyDef.reset()
                .type(Ball.BODY_TYPE)
                .angularDampening(Ball.ANGULAR_DAMPENING)
                .linearDamping(Ball.LINEAR_DAMPENING)
                .position(
                        scale.pixelsToMeters(ellipse.x + (ellipse.width * 0.5f)),
                        scale.pixelsToMeters(ellipse.y + (ellipse.height * 0.5f)))
                .build());
        body.createFixture(fixtureDef.reset()
                .shape(shape)
                .friction(Ball.FRICTION)
                .density(Ball.DENSITY)
                .restitution(Ball.RESTITUTION).build());

        // dispose after creating fixture
        shape.dispose();
        return new Ball(body, scale);
    }

    public PolygonShape createPolygonShape(Polygon polygon) {
        PolygonShape shape = new PolygonShape();
        shape.set(polygon.getVertices());
        return shape;
    }

    public PolygonShape createPolygonShape(Rectangle rectangle, UnitScale scale) {
        PolygonShape shape = new PolygonShape();
        Vector2 center = new Vector2(
                scale.pixelsToMeters(rectangle.x + (rectangle.width * 0.5f)),
                scale.pixelsToMeters(rectangle.y + (rectangle.height * 0.5f)));
        shape.setAsBox(
                scale.pixelsToMeters(rectangle.width * 0.5f),
                scale.pixelsToMeters(rectangle.height * 0.5f),
                center,
                0.0f);
        return shape;
    }

    public CircleShape createCircleShape(Circle circle, UnitScale scale) {
        CircleShape shape = new CircleShape();
        shape.setRadius(scale.pixelsToMeters(circle.radius));
        return shape;
    }

    public CircleShape createCircleShape(Ellipse circle, UnitScale scale) {
        Gdx.app.log("warning", "Converting ellipse to a circle");
        // NOTE: there are no ellipse shapes so just convert it to a circle
        CircleShape shape = new CircleShape();
        shape.setRadius(scale.pixelsToMeters(((circle.width / 2f) + (circle.height / 2f)) / 2f));
        return shape;
    }

    // TODO: convert chain/edge objects to a polyline shape if we make use of them

    public static class BodyDefBuilder {
        private final BodyDef def = new BodyDef();

        public BodyDef build() {
            return def;
        }

        /**
         * how quickly spin degrades over time, range between 0.0 and 1.0
         *
         * @param val
         * @return
         */
        public BodyDefBuilder angularDampening(float val) {
            def.angularDamping = val;
            return this;
        }

        /**
         * how quickly speed degrades over time, range between 0.0 and 1.0
         *
         * @param val
         * @return
         */
        public BodyDefBuilder linearDamping(float val) {
            def.linearDamping = val;
            return this;
        }

        /**
         * position in the world in meters
         *
         * @param x
         * @param y
         * @return
         */
        public BodyDefBuilder position(float x, float y) {
            def.position.set(x, y);
            return this;
        }

        /**
         * the body type, static bodies do not move, kinematic bodies move but
         * are not affected by forces in the world, dynamic bodies move and
         * are affected by the world.
         *
         * @param val
         * @return
         */
        public BodyDefBuilder type(BodyType val) {
            def.type = val;
            return this;
        }

        /**
         * prevent spin and angular velocity
         *
         * @param val
         * @return
         */
        public BodyDefBuilder fixedRotation(boolean val) {
            def.fixedRotation = val;
            return this;
        }

        public BodyDefBuilder reset() {
            def.angularDamping = 0;
            def.linearDamping = 0;
            def.position.set(0, 0);
            def.type = BodyType.StaticBody;
            def.fixedRotation = false;
            return this;
        }
    }

    public static class FixtureDefBuilder {
        private final FixtureDef def = new FixtureDef();

        public FixtureDef build() {
            return def;
        }

        /**
         * the shape of the fixture
         *
         * @param val
         * @return
         */
        public FixtureDefBuilder shape(Shape val) {
            def.shape = val;
            return this;
        }

        /**
         * the friction used when the fixture collides with another fixture
         * range between 0.0 and 1.0
         *
         * @param val
         * @return
         */
        public FixtureDefBuilder friction(float val) {
            def.friction = val;
            return this;
        }

        /**
         * the density, kg/m^2
         *
         * @param val
         * @return
         */
        public FixtureDefBuilder density(float val) {
            def.density = val;
            return this;
        }

        /**
         * the bouncyness, range between 0.0 and 1.0
         *
         * @param val
         * @return
         */
        public FixtureDefBuilder restitution(float val) {
            def.restitution = val;
            return this;
        }

        /**
         * sensors do not generate a collision response, but do generate collision events
         *
         * @param val
         * @return
         */
        public FixtureDefBuilder isSensor(boolean val) {
            def.isSensor = val;
            return this;
        }

        public FixtureDefBuilder reset() {
            def.shape = null;
            def.friction = 0;
            def.density = 1.0f;
            def.restitution = 0;
            def.isSensor = false;
            return this;
        }
    }
}
