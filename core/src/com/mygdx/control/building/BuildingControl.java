package com.mygdx.control.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.PooledLinkedList;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.map.GameTiledMap;
import com.mygdx.objects.*;
import com.mygdx.screens.GameScreen;
import com.mygdx.tiles.TileOwnership;

/**
 * Created by Viteker on 19.07.2017.
 */
public class BuildingControl {
    private final TileBuildingAccessList buildingAccessList;
    private BuildingControl.BuildingMenu buildMenu;
    private GameObjectOnTileAddition addition;

    public BuildingControl(GameObjectOnTileAddition addition) {
        this.buildingAccessList = new TileBuildingAccessList();
        this.buildMenu = new BuildingMenu();
        this.addition = addition;
    }

    public boolean build(ObjectOwner owner, TiledMapTile tiledMapTile, GameTiledMap.MapCoordinates coordinates){
        if(!(tiledMapTile instanceof TileOwnership))//May Tile belongs someone
            return false;
        TileOwnership tile = (TileOwnership) tiledMapTile;
        if(tile.getTileOwner() != owner)
            return false;

        PooledLinkedList<com.mygdx.objects.ObjectType> list = buildingAccessList.getBuildingAccesList(tile);

        if(list.size() == 0)//if nothing are built
            return false;

        buildMenu.createMenu(list,tile,owner,coordinates);
        return false;
    }
    private void buildObjectTypeOnTile(ObjectOwner owner, TileOwnership tile, ObjectType type, GameTiledMap.MapCoordinates coordinates){
        assert owner != null;

        TileObject object = ObjectCreater.getObject(tile,owner,type);

        TextureRegion textureRegion = MyGdxGame.assetLoader.getFractionTextureRegion(owner.getFraction(),type);
        object.setTextureRegion(textureRegion);
        object.setCoordinates(coordinates);

        addition.addObject(object);
    }

    private class BuildingMenu{
        private void createMenu(PooledLinkedList<ObjectType> list,
                                TileOwnership tile,
                                ObjectOwner owner,
                                GameTiledMap.MapCoordinates coordinates){

            ButtonIconListener listener = new ButtonIconListener();
            list.iter();
            ObjectType type;
            while((type = list.next()) != null){

                TextureRegionDrawable drawableUp = new TextureRegionDrawable(MyGdxGame.assetLoader.getIconUp(type));
                TextureRegionDrawable drawableDown = new TextureRegionDrawable(MyGdxGame.assetLoader.getIconDown(type));

                ButtonIcon buttonIcon = new ButtonIcon(drawableUp,drawableDown,type,tile,owner,coordinates);
                buttonIcon.addListener(listener);

                float width = drawableUp.getRegion().getRegionWidth();
                float height = drawableUp.getRegion().getRegionHeight();
                buttonIcon.setSize(width,height);
                float buttonX = Gdx.graphics.getWidth()/2 - width/2;
                buttonIcon.setPosition(buttonX, height);

                GameScreen.addActorOnStage(buttonIcon);
            }
        }
    }
    private class ButtonIcon extends ImageButton{
        public final ObjectType object;
        public final TileOwnership tile;
        public GameTiledMap.MapCoordinates coordinates;
        public ObjectOwner owner;

        private ButtonIcon(Drawable imageUp,
                           Drawable imageDown,
                           ObjectType object,
                           TileOwnership tile,
                           ObjectOwner owner,
                           GameTiledMap.MapCoordinates coordinates) {

            super(imageUp, imageDown);
            this.object = object;
            this.tile = tile;
            this.owner = owner;
            this.coordinates = coordinates;
        }
    }
    private class ButtonIconListener extends InputListener{
        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            ButtonIcon bIcon = (ButtonIcon) event.getListenerActor();
            buildObjectTypeOnTile(bIcon.owner,bIcon.tile,bIcon.object,bIcon.coordinates);
            GameScreen.removeBuildingActors();
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            return true;
        }
    }
}
