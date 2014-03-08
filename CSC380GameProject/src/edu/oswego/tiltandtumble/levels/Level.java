package edu.oswego.tiltandtumble.levels;

import java.text.DecimalFormat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;

import edu.oswego.tiltandtumble.Settings;
import edu.oswego.tiltandtumble.collisionListener.OurCollisionListener;
import edu.oswego.tiltandtumble.worldObjects.Ball;

public class Level implements Disposable {

    private final Settings settings;

    private final OrthographicCamera camera;

    private final Ball ball;

    private final World world = new World(new Vector2(0, 0), true);
    private final ContactListener contactListener;

    private int fps = 0;
    private long startTime;

    private final TiledMap map;
    private final OrthogonalTiledMapRenderer mapRenderer;

    private float tiltX;
    private float tiltY;

    private final UnitScale scale = new UnitScale(1f/64f);

    private final Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    private final DecimalFormat decimalFormatter = new DecimalFormat("######.##");

    private final int level;

    public Level(int level, Settings settings) {
        this.level = level;
        this.settings = settings;
        debugRenderer.setDrawVelocities(true);
        debugRenderer.setDrawContacts(true);
        debugRenderer.setDrawJoints(true);

        camera = new OrthographicCamera();
        map = loadMap(level);
        // NOTE: if we set the scaling based on the texture size then
        //       we can use tile counts, instead of pixels, for the
        //       camera.setToOrtho call below.
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1);
        mapRenderer.setView(camera);

        // TODO: figure out how to scale this to different screen sizes
        camera.setToOrtho(false, 480, 320); //game.width, game.height);

        // TODO: move the world populator up to the GameScreen and pass it in here
        ball = new WorldPopulator().populateWorld(map, world, scale);

        if (settings.isUseDpad()) {
            addKeyInputProcessor();
        }
        contactListener = new OurCollisionListener();
        world.setContactListener(contactListener);
        startTime = TimeUtils.nanoTime();
    }

    private TiledMap loadMap(int level) {
        return new TmxMapLoader().load("data/level" + level + ".tmx");
    }

    private void addKeyInputProcessor() {
        Gdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean scrolled(int amount) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                case Keys.LEFT:
                    tiltX -= 0.001f;
                    break;
                case Keys.RIGHT:
                    tiltX += 0.001f;
                    break;
                case Keys.UP:
                    tiltY += 0.001f;
                    break;
                case Keys.DOWN:
                    tiltY -= 0.001f;
                    break;
                case Keys.CENTER:
                    tiltX = 0f;
                    tiltY = 0f;
                    break;

                default:
                    break;
                }
                Gdx.app.log("keydown", tiltX + " " + tiltY);
                return false;
            }
        });
    }

    public int getLevelNumber() {
        return level;
    }

    public void render(SpriteBatch batch, BitmapFont font) {
        // clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        camera.position.set(
                scale.metersToPixels(ball.getBody().getPosition().x),
                scale.metersToPixels(ball.getBody().getPosition().y),
                camera.position.z);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        if (settings.isDebugRender()) {
            debugRenderer.render(world, camera.combined.scl(1f / scale.getScale()));
        }
        else {
            mapRenderer.setView(camera);
            mapRenderer.render();
        }

        batch.begin();
        if (settings.isDebugRender()) {
            renderTextInCameraView(batch, font, "FPS: " + fps, 10, 15);
            renderTextInCameraView(batch, font, "X: " + decimalFormatter.format(tiltX), 10, 30);
            renderTextInCameraView(batch, font, "Y: " + decimalFormatter.format(tiltY), 10, 45);
            renderTextInCameraView(batch, font, "BVel X: " + decimalFormatter.format(ball.getBody().getLinearVelocity().x), 10, 60);
            renderTextInCameraView(batch, font, "BVel Y: " + decimalFormatter.format(ball.getBody().getLinearVelocity().y), 10, 75);
            renderTextInCameraView(batch, font, "BVel A: " + decimalFormatter.format(ball.getBody().getAngularVelocity()), 10, 90);
        }
        else {
            ball.render(batch);
        }
        batch.end();


        // update every second
        if (TimeUtils.nanoTime() - startTime > 1000000000)  {
            fps = Gdx.graphics.getFramesPerSecond();
            startTime = TimeUtils.nanoTime();
        }

        if (!settings.isUseDpad()) {
            if (Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)) {
                // accelerometer is reversed from screen coordinates, we are in landscape mode
                tiltX = Gdx.input.getAccelerometerY() * 0.001f;
                tiltY = Gdx.input.getAccelerometerX() * -0.001f;
            }
        }

        ball.applyLinearImpulse(tiltX, tiltY);

        //world.step(1/60f, 6, 2);
        world.step(1/45f, 10, 8);
    }

    private void renderTextInCameraView(SpriteBatch batch, BitmapFont font, String text, float x, float y) {
        font.draw(batch, text,
            camera.position.x - (camera.viewportWidth / 2f) + x,
            camera.position.y - (camera.viewportHeight / 2f) + y
        );
    }

    public void pause() {
        // TODO: implement
    }

    public void resume() {
        // TODO: implement
    }

    @Override
    public void dispose() {
        mapRenderer.dispose();
        world.dispose();
    }
}
