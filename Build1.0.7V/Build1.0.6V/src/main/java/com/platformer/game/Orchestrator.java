package com.platformer.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Random;




public class Orchestrator extends ApplicationAdapter implements InputProcessor {
	public static PerspectiveCamera camera;
	private ModelBatch modelBatch;
	private ModelBuilder modelBuilder;
	private Environment environment;
	private Renderer rendMan;
	private boolean forward;
	private boolean backwards;
	private boolean left;
	private boolean right;
	private boolean sprinting;
	private boolean jump = false;
	protected final Vector3 tmp = new Vector3();
	private double frameCounter = 0.0;
	private byte stamina = 60;
	private Array<ModelInstance> models = new Array<ModelInstance>();
	private Array<Character> cmans = new Array<Character>();


	@Override
	public void create () {

		camera = new PerspectiveCamera(90,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//Set camera position at origin and 3 units towards the camera
		camera.position.set(0f, 0f, 3f);
		//Point camera at origin
		camera.lookAt(0f,0f,0f);

		//View Distance
		camera.near = 0.1f;
		camera.far = 1000f;

		modelBatch = new ModelBatch();
		modelBuilder = new ModelBuilder();


		//Building Blocks
		Model Wall = modelBuilder.createBox(10f,10f,1f,
				new Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY)),
				VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal);

		Model Wall2 = modelBuilder.createBox(1f,10f,10f,
				new Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY)),
				VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal);

		Model HouseWall1 = modelBuilder.createBox(5f,5f,1f,
				new Material(ColorAttribute.createDiffuse(Color.BROWN)),
				VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal);

		Model HouseWall2 = modelBuilder.createBox(1f,5f,5f,
				new Material(ColorAttribute.createDiffuse(Color.BROWN)),
				VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal);

		Model Roof = modelBuilder.createCone(10f,2f,10f, 10,
				new Material(ColorAttribute.createDiffuse(Color.BLACK)),
				VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal);



		Model Player = modelBuilder.createBox(2f,2f,2f,
				new Material(ColorAttribute.createDiffuse(Color.GREEN)),
				VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal);

		Model Player1 = modelBuilder.createBox(2f,2f,2f,
				new Material(ColorAttribute.createDiffuse(Color.GREEN)),
				VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal);



		Model floor = modelBuilder.createBox(400f,0f,400f,
				new Material(ColorAttribute.createDiffuse(Color.FOREST)),
				VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal);


		//Level 1

		float min, max;
		min = -100f;
		max = 100f;



		//House
		for(int i = 0; i < 10; i++){

			float HouseCoordinatesX = (float) Math.floor(Math.random()*(max-min+1)+min);
			float HouseCoordinatesY = (float) Math.floor(Math.random()*(max-min+1)+min);



			models.add( new ModelInstance(HouseWall1,    HouseCoordinatesX  ,   0,HouseCoordinatesY + 2  ));
			models.add( new ModelInstance(HouseWall1,    HouseCoordinatesX  ,   0,HouseCoordinatesY - 2  ));
			models.add( new ModelInstance(HouseWall2, HouseCoordinatesX-2,   0,   HouseCoordinatesY  ));
			models.add( new ModelInstance(HouseWall2, HouseCoordinatesX+2,   0,   HouseCoordinatesY  ));
			models.add( new ModelInstance(Roof,          HouseCoordinatesX  ,   3,   HouseCoordinatesY  ));
		}


		//Spawn
		models.add( new ModelInstance(Wall,0,0,5));
		models.add( new ModelInstance(Wall2,5,0,0));
		models.add( new ModelInstance(Wall2,-5,0,0));



		//Floor
		models.add( new ModelInstance(floor, 0,-2,0));


		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight,0.9f,0.8f,0.8f,1f));

		Gdx.input.setInputProcessor(this);


		Random rand = new Random();
		byte int_random = (byte) rand.nextInt(4);


		Music music = Gdx.audio.newMusic(Gdx.files.internal("assets/untitled2.ogg")); // Will randomly play one out of the 4 songs for background music
		Music music1 = Gdx.audio.newMusic(Gdx.files.internal("assets/venice.ogg"));
		Music music2 = Gdx.audio.newMusic(Gdx.files.internal("assets/joyJaws.ogg"));
		Music music3 = Gdx.audio.newMusic(Gdx.files.internal("assets/ending.ogg"));
		// sets the volume to half the maximum volume
		switch(int_random) {
			case 0:
			music.setLooping(true);
			music.setVolume(0.2f);
			music.play();
			break;

			case 1:
			music1.setLooping(true);
			music1.setVolume(0.2f);
			music1.play();
			break;

			case 2:
			music2.setLooping(true);
			music2.setVolume(0.2f);
			music2.play();
			break;

			case 3:
			music3.setLooping(true);
			music3.setVolume(0.2f);
			music3.play();

		}

		//cman = new Character(Player, "Bob",camera,SPR.SKIN1,SPR.HAIR1_BLACK,SPR.EYES_RED,SPR.BLANK, 12f,0f,-16f);

		cmans.add(new Character(Player1, camera,"Bob",SPR.SKIN2,SPR.HAIR1_RED,SPR.EYES_RED,SPR.BLANK, 6f,0f,-16f) );
		cmans.add(new Character(Player, camera,"Bob",SPR.SKIN1,SPR.HAIR1_BLACK,SPR.EYES_RED,SPR.BLANK, 12f,0f,-16f));
		rendMan = new Renderer(camera,cmans);

		//models.add(cmans.get(0).c);
		//models.add(cmans.get(1).c);

	}








	@Override
	public void render () {
		Gdx.gl.glClearColor(135/255f, 206/255f,235/255f,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT|GL20.GL_DEPTH_BUFFER_BIT);

		camera.update();
		modelBatch.begin(camera);
		modelBatch.render(models);
		modelBatch.end();

		//cmans.get(0).move(0.05f,0,0);
		//render characters
		rendMan.render(Gdx.graphics.getDeltaTime());


		Gdx.input.setCursorCatched(true);
		if(forward && sprinting){  // sprinting
			tmp.set(camera.direction).nor().scl(0.15f);
			camera.position.add(tmp.x,0f, tmp.z);

		}


		if(forward) {
			tmp.set(camera.direction).nor().scl(0.05f);
			camera.position.add(tmp.x,0f, tmp.z);
		}

		/*if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){  // working on jump
			tmp.set(camera.direction).nor().scl(0.0005f);
			camera.position.add(tmp.x, 1f, tmp.z);
		}
		else if(camera.position.y > 0 ){
			tmp.set(camera.direction).nor().scl(0.005f);
			camera.position.add(tmp.x, -0.1f, tmp.z);
		}*/

		if(backwards && sprinting){
			tmp.set(camera.direction).nor().scl(-0.15f);
			camera.position.add(tmp.x,0f, tmp.z);
		}
		if(backwards) {
			tmp.set(camera.direction).nor().scl(-0.05f);
			camera.position.add(tmp.x,0f, tmp.z);
		}

		if(left && sprinting){  // sprinting
			tmp.set(camera.direction).crs(camera.up).nor().scl(-0.15f);

			camera.position.add(tmp);

		}
		if(left){
			tmp.set(camera.direction).crs(camera.up).nor().scl(-0.05f);
			camera.position.add(tmp);
		}

		if(right && sprinting){  // sprinting
			tmp.set(camera.direction).crs(camera.up).nor().scl(0.15f);
			camera.position.add(tmp);
		}
		if(right){
			tmp.set(camera.direction).crs(camera.up).nor().scl(0.05f);
			camera.position.add(tmp);
		}


		//System.out.println(camera.position.x + tmp.x);
		while(camera.position.x > 100){
			tmp.x = tmp.x - 1;
			camera.position.add(tmp);
		}

		while(camera.position.x < -100){
			tmp.x = tmp.x + 1;
			camera.position.add(tmp);
		}

		while(camera.position.z > 100 ){
			tmp.z = tmp.z - 1;
			camera.position.add(tmp);
		}

		while(camera.position.z < -100){
			tmp.z = tmp.z + 1;
			camera.position.add(tmp);
		}


		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
			Gdx.app.exit();
		}
		if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
			sprinting = true;
		}

		final Sound walking = Gdx.audio.newSound(Gdx.files.internal("assets/walk.ogg"));
		final Sound walking3 = Gdx.audio.newSound(Gdx.files.internal("assets/sprinting.ogg"));
		frameCounter++;

		if(right || left || backwards || forward){
			if(frameCounter % 30.0  == 0.0) {
				final long id = walking.play();
				walking.setVolume(id, .8f);
				if(sprinting){
					walking.pause(id);
					final long id3 = walking3.play();
				}
				/*if(jump){
					walking.pause(id);
					walking3.pause();
				}*/
			}
		}





	}




	@Override
	public void dispose(){
		modelBatch.dispose();
		rendMan.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {

		if(keycode == Input.Keys.W)
			forward = true;

		if(keycode == Input.Keys.S)
			backwards = true;

		if(keycode == Input.Keys.A)
			left = true;

		if(keycode == Input.Keys.D)
			right = true;

		if(keycode == Input.Keys.SPACE)
			jump = true;



		return true;
	}

	@Override
	public boolean keyUp(int keycode) {

		if(keycode == Input.Keys.W)
			forward = false;

		if(keycode == Input.Keys.S)
			backwards = false;

		if(keycode == Input.Keys.A)
			left = false;
		if(keycode == Input.Keys.SPACE){
			jump = false;
		}

		if(keycode == Input.Keys.D)
			right = false;
		if(keycode == Input.Keys.SHIFT_LEFT){
			sprinting = false;
		}

		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		camera.direction.rotate(camera.up, -(Gdx.input.getDeltaX() * 0.5f));
		tmp.set(camera.direction).crs(camera.up).nor();
		camera.direction.rotate(tmp, -Gdx.input.getDeltaY() * 0.5f);
		return true;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}
}
