package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.map.GameTiledMap;
import com.mygdx.player.Fraction;
import com.mygdx.player.Player;
import com.mygdx.screens.MainMenuScreen;
import com.mygdx.loader.AssetLoader;
import com.mygdx.loader.SettingsLoader;
import com.mygdx.tiles.TileOwnership;
import com.mygdx.tiles.TilePreparer;

public class MyGdxGame extends Game {
	static public Skin skin;
	final static public AssetLoader assetLoader = new AssetLoader();
	final static public SettingsLoader settings = new SettingsLoader();
	static public GameTiledMap gameTiledMap;
	@Override
	public void create () {
		skin = new Skin(Gdx.files.internal("glassy/skin/glassy-ui.json"));
		MainMenuScreen mainMenuScreen = new MainMenuScreen(this);
		setScreen(mainMenuScreen);

	}

	@Override
	public void render() {
		super.render();
	}

	public void startGame(Fraction[] fractions){
		assetLoader.load(fractions,settings.mapSettings.TILE_SIZE);
		GamePreparer gamePreparer = new GamePreparer(this);
		int mapSize = settings.mapSettings.mapWidth;
		int tileSize = settings.mapSettings.TILE_SIZE;
		int length = fractions.length;
		Player[] players = new Player[length];
		for(int i = 0; i < length; i++){
			players[i] = new Player(fractions[i]);
		}
		new TilePreparer().prepare(players.length);
		gameTiledMap = gamePreparer.prepareMap(players,mapSize, tileSize);
		gamePreparer.start(players,0,0);
	}
	@Override
	public void dispose () {
		super.dispose();
		skin.dispose();
		assetLoader.dispose();
	}
}
