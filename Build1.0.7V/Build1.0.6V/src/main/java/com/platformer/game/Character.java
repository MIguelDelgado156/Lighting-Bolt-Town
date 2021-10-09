package com.platformer.game;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import static com.platformer.game.AID.*;

public class Character {
    public Model character;
    public ModelInstance c;
    public byte hp;
    public String id;
    public Decal decal;
    public int state = IDLE.val();
    private byte b = -1;
    public Vector3 position, prevPosition;
    private Vector3 velocity = new Vector3(0f,0f,0f);
    private Texture sprite;
    private TextureRegion texMain[];
    private Array<TextureRegion[]> texi = new Array<TextureRegion[]>();
    public Animation<TextureRegion> anis;
    private PerspectiveCamera cam;
    private float stateTime;
    private float animSpeed = 0.2f;

    public Character(Model c1,PerspectiveCamera camera, String ID, SPR Skin, SPR Hair, SPR Hat, SPR Shirt, float x, float y, float z) {
        this.cam = camera;
        this.character = c1;
        position = new Vector3(x,y,z);
        prevPosition = new Vector3(x,y,z);
        c = new ModelInstance(character, position);
        this.hp = 100;
        this.id = idCheck(ID);

        //create personal texture
        initialTex(Skin, Hair, Hat, Shirt);
    }

    public void move(float x, float y, float z) {
        this.c.transform.trn(x,y,z);
    }

    public void step(float delta) {
        this.position = this.c.transform.getTranslation(new Vector3());
        Vector3 v = getVelocity(prevPosition, delta);

        if(!v.epsilonEquals(0f,0f,0f)) {
            b = checkDir(Orchestrator.camera.position, v);

            switch (b) {
                case 0:
                    setAnim(WALK_AWAY);
                    System.out.println("Away");
                    break;

                case 1:
                    setAnim(WALK_RIGHT);
                    System.out.println("Right");
                    break;

                case 2:
                    setAnim(WALK_TOWARDS);
                    System.out.println("towards");
                    break;

                case 3:
                    setAnim(WALK_LEFT);
                    System.out.println("Left");
            }
        }

        //if(hp < 1); //die
        this.prevPosition = this.position;
    }

    private void setAnim(AID a) {
        if(this.texMain != texi.get(a.val()) ) {
            this.texMain = texi.get(a.val());
            anis = new Animation<TextureRegion>(animSpeed, texMain);
        }
        return;
    }

    private String idCheck(String id) {
        String tempid = null;
        if (id == "Bob") {
            tempid = "Bob";
        }
        return tempid;
    }

    private void initialTex(SPR skin, SPR hair, SPR hat, SPR shirt) {
        int cols = 26, rows = 10;

        //combine textures to create the decal region
        this.sprite = combineTextures(skin.tex(), hair.tex(), hat.tex(), shirt.tex());

        //initilize animations

        TextureRegion tmp[][] = TextureRegion.split(this.sprite,this.sprite.getWidth()/cols,this.sprite.getHeight()/rows);

        TextureRegion tmpR[] = new TextureRegion[4];

        //Animation creation
        //idle - loc 0
        tmpR[0] = tmp[1][4];
        tmpR[1] = tmp[1][5];
        tmpR[2] = tmp[1][6];
        tmpR[3] = tmp[1][5];

        texi.add(tmpR);

        TextureRegion tmpR1[] = new TextureRegion[4];

        //walk right - loc 1
        tmpR1[0] = tmp[2][0];
        tmpR1[1] = tmp[2][1];
        tmpR1[2] = tmp[2][2];
        tmpR1[3] = tmp[2][1];

        texi.add(tmpR1);

        TextureRegion tmpR2[] = new TextureRegion[4];

        //walk left - loc 2
        tmpR2[0] = tmp[4][0];
        tmpR2[1] = tmp[4][1];
        tmpR2[2] = tmp[4][2];
        tmpR2[3] = tmp[4][1];

        texi.add(tmpR2);

        TextureRegion tmpR3[] = new TextureRegion[4];

        //walk away - loc 2
        tmpR3[0] = tmp[1][4];
        tmpR3[1] = tmp[1][5];
        tmpR3[2] = tmp[1][6];
        tmpR3[3] = tmp[1][5];

        texi.add(tmpR3);

        TextureRegion tmpR4[] = new TextureRegion[4];

        //walk towards - loc 2
        tmpR4[0] = tmp[1][4];
        tmpR4[1] = tmp[1][5];
        tmpR4[2] = tmp[1][6];
        tmpR4[3] = tmp[1][5];

        texi.add(tmpR4);

        setAnim(IDLE);

        anis = new Animation<TextureRegion>(animSpeed, texMain);

        decal = Decal.newDecal(4,4,texi.get(IDLE.val())[0],true);
        decal.setPosition(position);

        return;
    }

    private static Texture combineTextures(Texture texture1, Texture texture2, Texture texture3, Texture texture4) {
        texture1.getTextureData().prepare();
        Pixmap pixmap1 = texture1.getTextureData().consumePixmap();

        if (texture2 != null) {
            texture2.getTextureData().prepare();
            Pixmap pixmap2 = texture2.getTextureData().consumePixmap();
            pixmap1.drawPixmap(pixmap2, 0, 0);
            pixmap2.dispose();
        }

        if (texture3 != null) {
            texture3.getTextureData().prepare();
            Pixmap pixmap3 = texture3.getTextureData().consumePixmap();
            pixmap1.drawPixmap(pixmap3, 0, 0);
            pixmap3.dispose();
        }

        if (texture4 != null) {
            texture4.getTextureData().prepare();
            Pixmap pixmap4 = texture4.getTextureData().consumePixmap();
            pixmap1.drawPixmap(pixmap4,0,0);
            pixmap4.dispose();
        }

        Texture textureResult = new Texture(pixmap1);

        pixmap1.dispose();

        return textureResult;
    }

    private byte checkDir(Vector3 camPosition, Vector3 tmpC) {
        //0 - Away
        //1 - Right
        //2 - Towards
        //3 - Left

        Vector3 tmpP = new Vector3(this.position.x-camPosition.x,this.position.y-camPosition.y,this.position.z-camPosition.z);
        double piMan;
        byte tmpD = 0;

        piMan = Math.acos(  (tmpP.dot(tmpC)) / (tmpP.len() * tmpC.len())  );

        if(piMan < Math.PI) tmpD = 3;
        else if(piMan > Math.PI) tmpD = 1;
        else tmpD = 2;

        System.out.println("theta - "+piMan);
        return tmpD;
    }

    private Vector3 getVelocity(Vector3 prevPosition, float deltaTime) {
        if(prevPosition.epsilonEquals(this.position)) {
               velocity.set(0f,0f,0f);
               b = -1;
        }
        else {
            velocity = new Vector3(position.x-prevPosition.x,position.y-prevPosition.y,position.z-prevPosition.z);
            velocity.set(velocity.x / deltaTime, velocity.y / deltaTime, velocity.z / deltaTime);
            //System.out.println("Velocity - x"+velocity.x+"- y"+velocity.y+"- z"+velocity.z);
        }

        return velocity;
    }

}
