package ch.epfl.cs107.play.game.icrogue.actor.items;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public abstract class Item extends CollectableAreaEntity {
    public int price;
    protected TextGraphics priceText;

    public Item(Area area, Orientation orientation, DiscreteCoordinates position,int price) {
        super(area, orientation, position);
        this.price = price;
        priceText = new TextGraphics(Integer.toString(price), 0.4f, Color.orange);
        priceText.setAnchor(new Vector(0, 0.1f));
        priceText.setParent(this);
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public void draw(Canvas canvas) {
        if (price != 0) priceText.draw(canvas);
    }
}
