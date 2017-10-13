package com.mygdx.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.PooledLinkedList;
import com.mygdx.Action;
import com.mygdx.exceptions.runtime.GameLogicException;
import com.mygdx.player.Player;
import com.mygdx.objects.units.Unit;
import com.mygdx.game.MyGdxGame;
import com.mygdx.screens.GameScreen;

/**
 * Created by Viteker on 20.07.2017.
 */
public class PlayersControl {
    private Label lPlayer;
    public Player current;
    private int playerIndex;

    public PlayersControl(Player player){
        if(player == null)
            throw new GameLogicException("Start player cannot be null");
        current = player;
        lPlayer = new Label(current.getFraction().name(), MyGdxGame.skin,"black");
        lPlayer.setSize(100,100);
        lPlayer.setPosition(Gdx.graphics.getWidth()>>1, Gdx.graphics.getHeight() - 75);
        GameScreen.addMenuActor(lPlayer);
    }
    public void startStep(){
        lPlayer.setText(current.getFraction().name());

        PooledLinkedList<Unit> units = current.getUnits();
        units.iter();
        Unit unit;
        while ((unit = units.next()) != null){
            unit.refresh();
        }

        PooledLinkedList<Action> activeTowers = current.getActiveTowers();
        activeTowers.iter();
        Action action;
        while ((action = activeTowers.next()) != null){
                boolean life = action.act();
                if(!life){
                    activeTowers.remove();
                }
        }
    }
    public void endStep(){
        if(++playerIndex == GameScreen.players.length)
            playerIndex = 0;
        current = GameScreen.getPlayer(playerIndex);
        if(current == null)
            throw new GameLogicException("Start player cannot be null");
    }
}
