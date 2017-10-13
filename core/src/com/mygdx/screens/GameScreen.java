package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.control.*;
import com.mygdx.control.ControlHandler;
import com.mygdx.coordinates.Coordinates;
import com.mygdx.game.GameRenderer;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.map.GameMapHandle;
import com.mygdx.objects.units.Unit;
import com.mygdx.player.Player;

/**
 * Created by Viteker on 22.06.2017.
 */
public class GameScreen implements Screen {

    private final GameRenderer gameRenderer;

    public static final float rotationGame = 0;
    public static ControlHandler controlHandler;
    private final PlayersControl playersControl;
    private OrthographicCamera orthographicCamera;
    public static com.mygdx.player.Player[] players;

    private static final int STAGE_INDEX = 0,
                                INPUT_CONTROL_INDEX = 1;

    public static Stage stage = new Stage();
    private static int startBuildingActor;
    private final static InputMultiplexer inputMultiplexer = new InputMultiplexer();

    public GameScreen(Player[] players,MyGdxGame game, int xx, int yy){
        prepareStage();
        this.players = players;
        playersControl = new PlayersControl(players[0]);
        prepareControl(xx,yy);
        float unitScale = 1f;
        this.gameRenderer = new GameRenderer(MyGdxGame.gameTiledMap,unitScale,MyGdxGame.settings.mapSettings.mapWidth,
                MyGdxGame.settings.mapSettings.TILE_SIZE);
        prepareInputMultiplexer();
    }

    private void prepareControl(int xx, int yy) {
        orthographicCamera = new OrthographicCamera();

        float x = xx * MyGdxGame.settings.mapSettings.TILE_SIZE;
        float y = yy * MyGdxGame.settings.mapSettings.TILE_SIZE;

        orthographicCamera.setToOrtho(false);
        orthographicCamera.position.x = x;
        orthographicCamera.position.y = y;
        orthographicCamera.rotate(rotationGame);

        this.controlHandler = new ControlHandler(orthographicCamera, MyGdxGame.gameTiledMap);
    }

    private void prepareInputMultiplexer() {
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(controlHandler.getFirstInputProccessor());
    }

    private void prepareStage() {
        Drawable iconUp = new TextureRegionDrawable(MyGdxGame.assetLoader.getUpIconStep());
        Drawable iconDown = new TextureRegionDrawable(MyGdxGame.assetLoader.getDownIconStep());

        ImageButton buttonStep = new ImageButton(iconUp, iconDown);
        float x = Gdx.graphics.getWidth() - buttonStep.getWidth();
        float y = 0;
        buttonStep.setPosition(x,y);

        buttonStep.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                playersControl.endStep();
                playersControl.startStep();
            }
        });

            addMenuActor(buttonStep);
    }
    private void preparePlayers(com.mygdx.player.Fraction[] fractions){
        players = new com.mygdx.player.Player[fractions.length];
        for (int i = 0; i < players.length; i++) {
            players[i] = new com.mygdx.player.Player(fractions[i]);
        }
    }
    public static void setInputControl(InputProcessor processor){
        int start = INPUT_CONTROL_INDEX;
        int end = inputMultiplexer.size() - 1;
        inputMultiplexer.getProcessors().removeRange(start, end);
        inputMultiplexer.addProcessor(INPUT_CONTROL_INDEX,processor);
    }
    public static void addInputControl(InputProcessor processor){
        int index = inputMultiplexer.size();
        inputMultiplexer.addProcessor(index,processor);
    }
    public static void addMenuActor(Actor actor){
        startBuildingActor++;
        stage.addActor(actor);
    }
    public static void addActorOnStage(Actor actor){
        stage.addActor(actor);
    }
    public static void removeBuildingActors(){
        int size = stage.getActors().size;
        int start = startBuildingActor;
        if(size == start)
            return;
        stage.getActors().removeRange(start,size - 1);
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        orthographicCamera.update();
        gameRenderer.setView(orthographicCamera);
        gameRenderer.render();
        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

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
        gameRenderer.dispose();
        stage.dispose();
    }

    public static com.mygdx.player.Player getPlayer(int playerIndex) {
        return players[playerIndex];
    }
}
