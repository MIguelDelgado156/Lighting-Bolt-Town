package com.platformer.game;

import com.badlogic.gdx.graphics.Texture;

public enum SPR {
    BLANK(null),
    SKIN1(new Texture("assets/spritesheets/ss_skin1.png")),
    SKIN2(new Texture("assets/spritesheets/ss_skin2.png")),
    SKIN3(new Texture("assets/spritesheets/ss_skin3.png")),
    SKIN4(new Texture("assets/spritesheets/ss_skin4.png")),
    HAIR1_BLACK(new Texture("assets/spritesheets/ss_mhair1_black.png")),
    HAIR1_BLUE(new Texture("assets/spritesheets/ss_mhair1_blue.png")),
    HAIR1_GOLDBROWN(new Texture("assets/spritesheets/ss_mhair1_goldbrown.png")),
    HAIR1_GRAY(new Texture("assets/spritesheets/ss_mhair1_gray.png")),
    HAIR1_RED(new Texture("assets/spritesheets/ss_mhair1_red.png")),
    EYES_RED(new Texture("assets/spritesheets/ss_eyes_red.png"));

    private Texture tex;

    SPR(Texture texture) {
        tex = texture;
    }

    public Texture tex() {return tex;}
}
