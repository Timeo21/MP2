package ch.epfl.cs107.play.game.icrogue;

import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.window.Window;

public class ICRogueBehavior extends AreaBehavior {

    public enum ICRogueCellType{
        NONE(0,false), // Should never been used except in the toType method
        GROUND(-16777216, true), // traversable
        WALL(-14112955, false), // non traversable
        HOLE(-65536, true); // "traversable"

        final int type;
        final boolean isWalkable;
        ICRogueCellType(int type, boolean isWalkable){
            this.type = type;
            this.isWalkable = isWalkable;
        }

        public static ICRogueCellType toType(int type){
            for(ICRogueCellType icRogueCellType : ICRogueCellType.values()){
                if(icRogueCellType.type == type)
                    return icRogueCellType;
            }
            System.out.println(type);
            return NONE;
        }

    }

    /**
     * Default AreaBehavior Constructor
     *
     * @param window (Window): graphic context, not null
     * @param name   (String): name of the behavior image, not null
     */
    public ICRogueBehavior(Window window, String name) {
        super(window, name);
        int height = getHeight();
        int width = getWidth();
        for(int y = 0; y < height; y++){
            for (int x = 0; x < width; x++) {
                ICRogueCellType color = ICRogueCellType.toType(getRGB(height-1-y,x));
                setCell(x,y, new ICRogueCell(x,y,color));
            }
        }
    }


    @Override
    public void cellInteractionOf(Interactor interactor) {
        super.cellInteractionOf(interactor);
    }

    @Override
    public void viewInteractionOf(Interactor interactor) {
        super.viewInteractionOf(interactor);
    }

    public class ICRogueCell extends Cell{

        private final ICRogueCellType type;

        /**
         * Default Cell constructor
         *
         * @param x (int): x-coordinate of this cell
         * @param y (int): y-coordinate of this cell
         */
        public ICRogueCell(int x, int y, ICRogueCellType type) {
            super(x, y);
            this.type = type;
        }

        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        @Override
        protected boolean canEnter(Interactable entity) {
            if (entity.takeCellSpace()){
                for (Interactable onCell : entities){
                    if(onCell.takeCellSpace()) return false;
                }
            }
            return type.isWalkable;
        }

        @Override
        public boolean isCellInteractable() {
            return true;
        }

        @Override
        public boolean isViewInteractable() {
            return true;
        }

        @Override
        public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
            ((ICRogueInteractionHandler) v).interactWith(this,isCellInteraction);
        }

        public ICRogueCellType getCellType(){
            return this.type;
        }
    }
}
