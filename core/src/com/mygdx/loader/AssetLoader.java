package com.mygdx.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.objects.ObjectType;
import com.mygdx.player.Fraction;
import com.mygdx.tiles.TileType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Viteker on 08.07.2017.
 */
public class AssetLoader {
    private Texture tilesTexture,hillAndMountainTexture;

    private TextureRegion[] upObjectsIcons = new TextureRegion[ObjectType.values().length];
    private TextureRegion[] downObjectsIcons = new TextureRegion[ObjectType.values().length];
    private TextureRegion[] natureTextureRegions = new TextureRegion[TileType.values().length];

    public TextureRegion accentuation,markAttack,markMove,clear;
    private TextureRegion upIconStep, downIconStep;

    private FractionTextureLoader[] fractionsTextureLoader = new FractionTextureLoader[Fraction.values().length];

    public void load(Fraction[] fractions, int tileSize){
        assert fractions.length > 1;
        for (Fraction fraction : fractions) {
            fractionsTextureLoader[fraction.ordinal()] = new FractionTextureLoader();
            fractionsTextureLoader[fraction.ordinal()].load(fraction, tileSize);
        }

        tilesTexture = new Texture(Gdx.files.internal("NatureTextures.png"));

        for(int i = 0; i < natureTextureRegions.length - 1; i++){
            natureTextureRegions[i] = new TextureRegion(tilesTexture,i*tileSize,0, tileSize,tileSize);
        }
        int index = natureTextureRegions.length - 1;
        natureTextureRegions[index] = natureTextureRegions[index - 1];//EternalFrost = Mountain

        accentuation = new TextureRegion(new Texture(Gdx.files.internal("Accentuation.png")));
        markAttack = new TextureRegion(new Texture(Gdx.files.internal("MarkAttack.png")));
        markMove = new TextureRegion(new Texture(Gdx.files.internal("MarkMove.png")));
        clear = new TextureRegion(new Texture(Gdx.files.internal("Clear.png")));
        loadIcons();
        }

    private void loadIcons(){
        upIconStep = new TextureRegion(new Texture(Gdx.files.internal("StepUp.png")));
        downIconStep = new TextureRegion(new Texture(Gdx.files.internal("StepDown.png")));

        int length = ObjectType.values().length;
        ObjectType[] types = ObjectType.values();
        for (int i = 0; i < length; i++) {
            upObjectsIcons[i] = new TextureRegion(
                    new Texture(
                            Gdx.files.internal(
                                    types[i].name().concat("IconUp.png")
                            )
                    )
            );
            downObjectsIcons[i] = new TextureRegion(
                    new Texture(
                            Gdx.files.internal(
                                    types[i].name().concat("IconDown.png")
                            )
                    )
            );
        }
    }
    public void dispose() {
        tilesTexture.dispose();
        hillAndMountainTexture.dispose();

        upIconStep.getTexture().dispose();
        downIconStep.getTexture().dispose();

        accentuation.getTexture().dispose();
        markAttack.getTexture().dispose();
        markMove.getTexture().dispose();
        clear.getTexture().dispose();

        for (TextureRegion upObjectsIcon : upObjectsIcons) {
            upObjectsIcon.getTexture().dispose();
        }
        for (TextureRegion downObjectsIcon : downObjectsIcons) {
            downObjectsIcon.getTexture().dispose();
        }
        for (FractionTextureLoader fractionTextureLoader : fractionsTextureLoader) {
            fractionTextureLoader.dispose();
        }
    }
    public TextureRegion getNatureTextureRegion(TileType type){
        return getNatureTextureRegion(type.ordinal());
    }
    public TextureRegion getNatureTextureRegion(int index){
        return natureTextureRegions[index];
    }
    public TextureRegion getTextureRegionBoardaryForTower(Fraction fraction, boolean up,
                                                          boolean down, boolean left, boolean right){
        return fractionsTextureLoader[fraction.ordinal()].getBordary(up, down, left, right);
    }
    public TextureRegion getTextureRegionBoardaryForTower(Fraction fraction,
                                                          byte b){
        return fractionsTextureLoader[fraction.ordinal()].getBordary(b);
    }
    public TextureRegion getUnfractionTextureRegion(ObjectType type){
        throw new NotImplementedException();
    }
    public TextureRegion getFractionTextureRegion(Fraction fraction, ObjectType type) {
        return fractionsTextureLoader[fraction.ordinal()].getTexture(type);
    }
    public TextureRegion getUpIconStep(){
        return upIconStep;
    }
    public TextureRegion getDownIconStep(){
        return downIconStep;
    }
    public TextureRegion getIconUp(ObjectType type){
        return upObjectsIcons[type.ordinal()];
    }
    public TextureRegion getIconDown(ObjectType type){
        return downObjectsIcons[type.ordinal()];
    }


    /**
     * @author Viteker
     */
    public class MyTextureRegion extends TextureRegion{
        public int b;
        public MyTextureRegion(Texture texture, int b){
            super(texture);
            this.b = b;
        }
    }
    /**
     * @author Viteker
     */
    private class FractionTextureLoader{
        // up is more priority than down
        // left is more priority than right
        // left or right are more priority than up or down
        private TextureRegion[] textureRegions = new TextureRegion[ObjectType.values().length];
        private TextureRegion[] bordaries = new TextureRegion[16];

        private void load(Fraction fraction, int tileSize){
            ObjectType[] types = ObjectType.values();
            for (int i = 0; i < textureRegions.length; i++) {
                textureRegions[i] = new TextureRegion(
                        new Texture(
                                Gdx.files.internal(
                                        fraction.name()+types[i].name()+".png"
                                )
                        )
                );
            }
            Pixmap pixmap = new Pixmap(tileSize, tileSize, Pixmap.Format.RGBA8888);
            BordaryDrawer bordaryDrawer = new BordaryDrawer();
            SpriteBatch batch = new SpriteBatch();
            batch.begin();
            for(int i = 1; i < bordaries.length; i++){
                pixmap.setColor(bordaryDrawer.getColorForFraction(fraction));
                bordaryDrawer.drawBoardary(
                        pixmap,
                        tileSize,
                        (byte) i
                );
                //bordaries[i] = new TextureRegion(new Texture(pixmap));
                bordaries[i] = new MyTextureRegion(new Texture(pixmap),i);
                pixmap.setColor(1,1,1,0);
                pixmap.fill();
            }
            batch.end();
            pixmap.dispose();
        }
        private void reloadTexture(ObjectType type, Fraction fraction, int level){
            String path = fraction.name() + type.name() + level;
            Texture texture = new Texture(Gdx.files.internal(path));

            TextureRegion textureRegion = textureRegions[type.ordinal()];
            textureRegion.getTexture().dispose();
            textureRegion.setTexture(texture);
        }
        private TextureRegion getBordary(boolean up, boolean down, boolean left, boolean right){
            byte b = 0;

            if(left)
                b |= 0x01;
            if(right)
                b |= 0x02;
            if(up)
                b |= 0x04;
            if(down)
                b |= 0x08;
            return getBordary(b);
        }

        private TextureRegion getBordary(byte b) {
            assert b < bordaries.length && b >= 0;
            return bordaries[b];
        }

        private TextureRegion getTexture(ObjectType type){
            return textureRegions[type.ordinal()];
        }

        private void dispose() {
            for (TextureRegion bordary : bordaries) {
                bordary.getTexture().dispose();
            }
            for (TextureRegion textureRegion : textureRegions) {
                textureRegion.getTexture().dispose();
            }

        }
    }
}
