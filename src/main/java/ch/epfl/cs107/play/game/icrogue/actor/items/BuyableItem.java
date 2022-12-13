package ch.epfl.cs107.play.game.icrogue.actor.items;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;

public abstract class BuyableItem extends Item{
    public int price;
    protected TextGraphics priceText;

    public BuyableItem(Area area, Orientation orientation, DiscreteCoordinates position, int price) {
        super(area, orientation, position);
        this.price = price;
        priceText = new TextGraphics(Integer.toString(price), 0.4f, Color.orange);
        priceText.setAnchor(new Vector(0, 0.1f));
        priceText.setParent(this);
    }
    @Override
    public void draw(Canvas canvas) {
        if (price != 0) priceText.draw(canvas);
    }
}
