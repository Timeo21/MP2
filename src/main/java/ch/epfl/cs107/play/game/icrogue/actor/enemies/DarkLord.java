package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class DarkLord extends ICRogueActor {
    private Animation[] walkingAnimation;
    private Animation[] shieldAnimation;
    private Animation[] attackAnimation;
    public DarkLord(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        life = 5;

        Sprite[][] sprites = Sprite.extractSprites("zelda/darkLord",3,1f,1f,this,16,32,
                new Orientation[]{Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
        walkingAnimation = Animation.createAnimations(5,sprites);

        sprites = Sprite.extractSprites("zelda/darkLord.spell",3,1f,1f,this,16,32,
                new Orientation[]{Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
        attackAnimation = Animation.createAnimations(5,sprites);

        sprites = Sprite.extractSprites("custom/darkLordShield",3,1f,1f,this,16,32,
                new Orientation[]{Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
        shieldAnimation = Animation.createAnimations(5,sprites);
    }

}
