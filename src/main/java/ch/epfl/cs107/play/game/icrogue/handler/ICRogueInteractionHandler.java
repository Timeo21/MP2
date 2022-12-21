package ch.epfl.cs107.play.game.icrogue.handler;

import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.DarkLord;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.FlameSkull;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.actor.items.Key;
import ch.epfl.cs107.play.game.icrogue.actor.items.MedKit;
import ch.epfl.cs107.play.game.icrogue.actor.items.SpeedShoes;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Arrow;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.FireBall;

public interface ICRogueInteractionHandler extends AreaInteractionVisitor {
    default void interactWith(Turret turret,boolean isCellInteraction){
    }
    default void interactWith(ICRogueBehavior.ICRogueCell cell, boolean isCellInteraction) {
    }
    default void interactWith(ICRoguePlayer player, boolean isCellInteraction) {
    }
    default void interactWith(Key key, boolean isCellInteraction) {
    }
    default void interactWith(Staff staff, boolean isCellInteraction) {
    }
    default void interactWith(FireBall fireBall, boolean isCellInteraction) {
    }

    default void interactWith(Connector connector, boolean isCellInteraction) {
    }
    default void interactWith(Arrow arrow, boolean isCellInteraction){
    }
    default void interactWith(FlameSkull flameSkull, boolean isCellInteraction){
    }
    default void interactWith(SpeedShoes speedShoes, boolean isCellInteraction){
    }
    default void interactWith(MedKit medKit, boolean isCellInteraction){
    }
    default void interactWith(DarkLord darkLord, boolean isCellInteraction){
    }

}
