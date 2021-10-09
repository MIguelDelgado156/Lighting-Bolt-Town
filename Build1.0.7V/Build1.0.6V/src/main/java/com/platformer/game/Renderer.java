package com.platformer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import static com.platformer.game.AID.*;

public class Renderer {
    private PerspectiveCamera camera;

    //Decal + Image initial
    private DecalBatch batch;
    private Array<Character> cman = new Array<Character>();

    //Time
    float stateTime;

    public Renderer(PerspectiveCamera cam, Array<Character> C) {
        this.camera = cam;
        batch = new DecalBatch(new CameraGroupStrategy(camera));
        this.cman = C;

        //initialize();
    }

    public void render(float delta) {
        stateTime += delta;

        camera.update();

        cman.get(0).move(-0.05f,0,0);

        //gets all Character - sets positions, orientations, and textures
        for(int i  = 0; i < cman.size; i++) {
            cman.get(i).step(delta);
            cman.get(i).decal.setPosition(cman.get(i).position);
            cman.get(i).decal.setTextureRegion(cman.get(i).anis.getKeyFrame(stateTime, true));
            cman.get(i).decal.lookAt(camera.position, camera.up);
            batch.add(cman.get(i).decal);
        }

        batch.flush();
    }

    public void dispose() {
        batch.dispose();
    }

}
