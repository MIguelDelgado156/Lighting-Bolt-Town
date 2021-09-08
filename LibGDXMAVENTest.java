import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

public class LibGDXMAVENTest extends Game {
    public SpriteBatch batch;
    private Texture charImg;
    private Texture backgroundImg;
    private Music music;
    private OrthographicCamera camera;
    private Rectangle character;

    @Override
    public void create () {
        batch = new SpriteBatch();

        charImg = new Texture("Knight2.jpg");

        backgroundImg = new Texture("background.jpg");

        FileHandle dst = Gdx.files.internal("ScapeMain.ogg");
        music = Gdx.audio.newMusic(dst);


        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        character = new Rectangle();
        character.x = 200;
        character.y = 1;
//        character.width = 100;
//        character.height = 100;

        music.setLooping(true);
        music.setVolume(10);
        music.play();
    }

    @Override
    public void render () {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(backgroundImg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(charImg, character.x, character.y);

        batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.A)) character.x -= 350 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.D)) character.x += 350 * Gdx.graphics.getDeltaTime();

        if (character.x < 0) character.x = 0;
        if (character.x > 800 - 120) character.x = 800 - 120;
    }

    @Override
    public void dispose () {
        charImg.dispose();
        backgroundImg.dispose();
        music.dispose();
        batch.dispose();
    }
}
