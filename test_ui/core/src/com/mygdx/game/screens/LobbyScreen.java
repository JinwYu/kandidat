package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;

import javafx.application.Application;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*; // Bra att importera!

/**
 * Created by sofiekhullar on 16-03-02.
 */
public class LobbyScreen implements Screen {

    // App reference
    private final MyGdxGame app;
    // Stage vars
    private Stage stage;
    private Rectangle viewport;
    private Skin skin;
    // Buttons
    private TextButton buttonBack;
    // width och heigth
    private float w = Gdx.graphics.getWidth();
    private float h = Gdx.graphics.getHeight();
    // Texture
    private Texture background;

    public LobbyScreen(final MyGdxGame app){
        this.app = app;
        this.stage = new Stage(new StretchViewport(w , h));
        this.viewport = new Rectangle();
    }

    // Kallas varje gång man vill att denna screen ska visas
    @Override
    public void show() {
        System.out.println("Show");
        Gdx.input.setInputProcessor(stage); // hanterar olika input events

        this.skin = new Skin();
        this.skin.addRegions(app.assets.get("ui/uiskin.atlas" , TextureAtlas.class));
        this.skin.add("default-font", app.font24); // Sätter defaulf font som vår ttf font
        this.skin.load(Gdx.files.internal("ui/uiskin.json"));

        background = app.assets.get("img/background1.jpg", Texture.class);

        initButtons();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // set viewport
        Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,
                (int) viewport.width, (int) viewport.height);

        update(delta);

        app.batch.begin();
        app.batch.draw(background, Gdx.graphics.getHeight() / 2 - background.getHeight() / 2, Gdx.graphics.getWidth() / 2 - background.getWidth() / 2);
        app.font24.draw(app.batch, "Screen: SETTING", 20, 20);
        app.batch.end();

        stage.draw();
    }

    public void update(float delta)
    {
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {
        //calculate new viewport
        float aspectRatio = (float)width/(float)height;
        float scale = 1f;
        Vector2 crop = new Vector2(0f, 0f);

        if(aspectRatio > app.ASPECT_RATIO)
        {
            scale = (float)height/(float)app.VIRTUAL_HEIGHT;
            crop.x = (width - app.VIRTUAL_WIDTH*scale)/2f;
        }
        else if(aspectRatio < app.ASPECT_RATIO)
        {
            scale = (float)width/(float)app.VIRTUAL_WIDTH;
            crop.y = (height - app.VIRTUAL_HEIGHT*scale)/2f;
        }
        else
        {
            scale = (float)width/(float)app.VIRTUAL_WIDTH;
        }

        float w = (float)app.VIRTUAL_WIDTH*scale;
        float h = (float)app.VIRTUAL_HEIGHT*scale;
        viewport = new Rectangle(crop.x, crop.y, w, h);
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
        System.out.println("dispose");
        stage.dispose();
    }

    private void initButtons() {

        int size_x = 280;
        int size_y = 60;

        buttonBack = new TextButton("Back", skin, "default");
        buttonBack.setSize(size_x, size_y);
        buttonBack.setPosition(Gdx.graphics.getWidth() / 2 - size_x / 2, Gdx.graphics.getHeight() / 2 - size_y / 2);
        buttonBack.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.setScreen(app.mainMenyScreen);
            }
        });

        stage.addActor(buttonBack);
    }
}
