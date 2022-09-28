package com.platformer.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.lwjgl.opengl.GL20;


public class MainMenu implements Screen {
    private final Stage stage;
    private final Game g;         //------------------------------------------------------------------
    private final Texture texture = new Texture(Gdx.files.internal("assets/craftacular/bkimg.jpg"));
    private Image bk = new Image(texture);
    public Skin skin = new Skin(Gdx.files.internal("assets/craftacular/craftacular/skin/craftacular-ui.json"));

    public MainMenu(Game g) {
        this.g = g;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        bk.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        bk.setPosition(Gdx.graphics.getWidth() - bk.getWidth(),
                Gdx.graphics.getHeight() - bk.getHeight());
        stage.addActor(bk);

        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(false);
        stage.addActor(table);


        TextButton playButton = new TextButton("Start", skin);
        TextButton quitButton = new TextButton("Quit", skin);
        TextButton settingsButton = new TextButton("Settings", skin);


        table.add(playButton);
        table.row().pad(10,0,10,0);
        table.add(settingsButton);
        table.row().pad(5,0,10,0);
        table.add(quitButton).fillX().uniformX();

        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                Gdx.app.exit();
            }
        });

        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {

                    g.setScreen(new GameScreen(g)); }
            } //-----------------------------------------------------
        );
        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                g.setScreen(new SettingsScreen(g));
            }
        });
    }


    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }
}
