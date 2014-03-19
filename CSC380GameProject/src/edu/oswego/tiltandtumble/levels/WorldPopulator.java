package edu.oswego.tiltandtumble.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import edu.oswego.tiltandtumble.worldObjects.Ball;
import edu.oswego.tiltandtumble.worldObjects.FinishLine;
import edu.oswego.tiltandtumble.worldObjects.Hole;
import edu.oswego.tiltandtumble.worldObjects.PushBumper;
import edu.oswego.tiltandtumble.worldObjects.StaticWall;

public final class WorldPopulator {
	private final BodyDefBuilder bodyDef = new BodyDefBuilder();
	private final FixtureDefBuilder fixtureDef = new FixtureDefBuilder();

	public Ball populateWorldFromMap(Level level, TiledMap map, World world,
			UnitScale scale) {
		// TODO: probably need to return an data structure of all the objects
		// some may need to be disposed once the world goes away...
		Ball ball = null;
		MapLayer layer = map.getLayers().get("collision");
		for (MapObject obj : layer.getObjects()) {
			if(obj.getName() != null){
			if (obj.getName().equals("StaticWall")) {
				createStaticWall(obj, world, scale);
			} else if (obj.getName().equals("PushBumper")) {
				createPushBumper(obj, world, scale);
			} else if (obj.getName().equals("FinishLine")) {
				createFinishLine(obj, level, world, scale);
			} else if (obj.getName().equals("Hole")) {
				createHole(obj, level, world, scale);
			} else if (obj.getName().equals("Ball")) {
				ball = createBall(obj, world, scale);
			}
		}
		}
		return ball;
		
	}

	public StaticWall createStaticWall(MapObject obj, World world, UnitScale scale) {
		Shape shape = createShape(obj, scale);

		Body body = world.createBody(bodyDef.reset().type(StaticWall.BODY_TYPE)
				.build());
		body.createFixture(fixtureDef.reset().shape(shape)
				.friction(getFloatProperty(obj, "friction", StaticWall.FRICTION))
				.density(getFloatProperty(obj, "density", StaticWall.DENSITY))
				.restitution(getFloatProperty(obj, "restitution", StaticWall.RESTITUTION))
				.build());

		// dispose after creating fixture
		shape.dispose();

		if (obj instanceof EllipseMapObject) {
			transformCircleBody((EllipseMapObject)obj, body, scale);
		}
		return new StaticWall(body);
	}

	public PushBumper createPushBumper(MapObject obj, World world,
			UnitScale scale) {
		Shape shape = createShape(obj, scale);

		Body body = world.createBody(bodyDef.reset().type(PushBumper.BODY_TYPE)
				.build());
		body.createFixture(fixtureDef.reset().shape(shape)
				.friction(getFloatProperty(obj, "friction", PushBumper.FRICTION))
				.density(getFloatProperty(obj, "density", PushBumper.DENSITY))
				.restitution(getFloatProperty(obj, "restitution", PushBumper.RESTITUTION))
				.build());

		// dispose after creating fixture
		shape.dispose();

		if (obj instanceof EllipseMapObject) {
			transformCircleBody((EllipseMapObject)obj, body, scale);
		}
		return new PushBumper(body,
				getFloatProperty(obj, "speed", PushBumper.DEFAULT_SPEED));
	}

	public Ball createBall(MapObject obj, World world, UnitScale scale) {
		if (!(obj instanceof EllipseMapObject)) {
			throw new IllegalArgumentException(obj.getName()
					+ " Unsupported MapObject: "
					+ obj.getClass().getName());
		}

		Shape shape = createShape((EllipseMapObject)obj, scale);
		Gdx.app.log("populating map", "adding " + obj.getName()
				+ " - " + obj.getClass().getSimpleName()
				+ " > "+ shape.getClass().getSimpleName());

		Body body = world.createBody(bodyDef
				.reset()
				.type(Ball.BODY_TYPE)
				.type(Ball.BODY_TYPE)		
				.angularDampening(getFloatProperty(obj, "angular dampening", Ball.ANGULAR_DAMPENING))
				.linearDamping(getFloatProperty(obj, "linear dampening", Ball.LINEAR_DAMPENING))
				.build());

		body.createFixture(fixtureDef.reset().shape(shape)
				.friction(getFloatProperty(obj, "friction", Ball.FRICTION))
				.density(getFloatProperty(obj, "density", Ball.DENSITY))
				.restitution(getFloatProperty(obj, "restitution", Ball.RESTITUTION))
				.build());

		float diameter = scale.metersToPixels(shape.getRadius()) * 2;

		// dispose after creating fixture
		shape.dispose();

		transformCircleBody((EllipseMapObject)obj, body, scale);
		return new Ball(body, diameter, scale);
	}

	public FinishLine createFinishLine(MapObject obj, Level level,
			World world, UnitScale scale) {
		Shape shape = createShape(obj, scale);

		Body body = world.createBody(bodyDef.reset().type(FinishLine.BODY_TYPE)
				.build());
		body.createFixture(fixtureDef.reset().shape(shape)
				.isSensor(FinishLine.IS_SENSOR).build());

		// dispose after creating fixture
		shape.dispose();

		if (obj instanceof EllipseMapObject) {
			transformCircleBody((EllipseMapObject)obj, body, scale);
		}
		return new FinishLine(body, level);
	}

	public Hole createHole(MapObject obj, Level level, World world,
			UnitScale scale) {
		Shape shape = createShape(obj, scale);

		Body body = world.createBody(bodyDef.reset().type(Hole.BODY_TYPE)
				.build());
		body.createFixture(fixtureDef.reset().shape(shape)
				.isSensor(Hole.IS_SENSOR).build());

		// dispose after creating fixture
		shape.dispose();

		if (obj instanceof EllipseMapObject) {
			transformCircleBody((EllipseMapObject)obj, body, scale);
		}
		return new Hole(body, level);
	}

	private Shape createShape(MapObject object, UnitScale scale) {
		Shape shape;
		if (object instanceof PolygonMapObject) {
			shape = createShape((PolygonMapObject)object, scale);
		} else if (object instanceof RectangleMapObject) {
			shape = createShape((RectangleMapObject)object, scale);
		} else if (object instanceof EllipseMapObject) {
			shape = createShape((EllipseMapObject)object, scale);
		} else if (object instanceof PolylineMapObject) {
			shape = createShape((PolylineMapObject)object, scale);
		} else {
			throw new IllegalArgumentException(object.getName()
					+ " Unsupported MapObject: "
					+ object.getClass().getName());
		}
		Gdx.app.log("populating map", "adding " + object.getName()
				+ " - " + object.getClass().getSimpleName()
				+ " > "+ shape.getClass().getSimpleName());
		return shape;
	}

	private Shape createShape(PolylineMapObject object, UnitScale scale) {
		ChainShape shape = new ChainShape();
		float[] vertices = object.getPolyline().getTransformedVertices();
		Vector2[] worldVertices = new Vector2[vertices.length / 2];
		for (int i = 0; i < worldVertices.length; ++i) {
			worldVertices[i] = new Vector2(
				scale.pixelsToMeters(vertices[i * 2]),
				scale.pixelsToMeters(vertices[(i * 2) + 1])
			);
		}
		if (Boolean.valueOf(object.getProperties().get("loop", "false", String.class))) {
			shape.createLoop(worldVertices);
		}
		else {
			shape.createChain(worldVertices);
		}
		return shape;
	}

	private Shape createShape(PolygonMapObject object, UnitScale scale) {
		// NOTE: when creating the map objects the polygons must have no
		// more than 8 vertices and must not be concave. this is a
		// limitation of the physics engine. so complex shapes need to be
		// composed of multiple adjacent polygons.
		PolygonShape shape = new PolygonShape();
		float[] vertices = object.getPolygon().getTransformedVertices();
		float[] worldVertices = new float[vertices.length];
		for (int i = 0; i < vertices.length; ++i) {
			worldVertices[i] = scale.pixelsToMeters(vertices[i]);
		}
		shape.set(worldVertices);
		return shape;
	}

	private Shape createShape(RectangleMapObject object, UnitScale scale) {
		PolygonShape shape = new PolygonShape();
		Rectangle rectangle = object.getRectangle();
		Vector2 center = new Vector2(scale.pixelsToMeters(rectangle.x
				+ (rectangle.width * 0.5f)), scale.pixelsToMeters(rectangle.y
				+ (rectangle.height * 0.5f)));
		shape.setAsBox(scale.pixelsToMeters(rectangle.width * 0.5f),
				scale.pixelsToMeters(rectangle.height * 0.5f), center, 0.0f);
		return shape;
	}

	private Shape createShape(EllipseMapObject object, UnitScale scale) {
		Gdx.app.log("warning", "Converting ellipse to a circle");
		// NOTE: there are no ellipse shapes so just convert it to a circle
		Ellipse ellipse = object.getEllipse();
		CircleShape shape = new CircleShape();
		shape.setRadius(scale
				.pixelsToMeters(((ellipse.width * 0.5f) + (ellipse.height * 0.5f)) * 0.5f));
		// NOTE: setting position here seems to cause really weird things to happen.
		//       seems like a bug somewhere with libgdx or box2d.
		return shape;
	}

	private void transformCircleBody(EllipseMapObject obj, Body body,
			UnitScale scale) {
		// NOTE: there seems to be some sort of bug with setting the position
		//       on the shape and then having it translate to the body.
		//       This works fine for everything but CircleShape. So we need
		//       to transform the body instead.
		Ellipse ellipse = obj.getEllipse();
		body.setTransform(
				scale.pixelsToMeters(ellipse.x
						+ (ellipse.width * 0.5f)),
				scale.pixelsToMeters(ellipse.y
						+ (ellipse.height * 0.5f)),
				body.getAngle());
	}

	private float getFloatProperty(MapObject object, String key, float def) {
		String prop = object.getProperties().get(key, null, String.class);
		if (prop == null) {
			return def;
		}
		return Float.valueOf(prop);
	}

	static final class BodyDefBuilder {
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
		 * are not affected by forces in the world, dynamic bodies move and are
		 * affected by the world.
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

	static final class FixtureDefBuilder {
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
		 * sensors do not generate a collision response, but do generate
		 * collision events
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
