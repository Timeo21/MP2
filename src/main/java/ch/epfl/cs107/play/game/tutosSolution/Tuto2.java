package ch.epfl.cs107.play.game.tutosSolution;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.tutosSolution.area.Tuto2Area;
import ch.epfl.cs107.play.game.tutosSolution.area.tuto2.Ferme;
import ch.epfl.cs107.play.game.tutosSolution.area.tuto2.Village;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class Tuto2 extends AreaGame {
	public final static float CAMERA_SCALE_FACTOR = 13.f;

	private final String[] areas = {"zelda/Ferme", "zelda/Village"};
	
	private int areaIndex;
	/**
	 * Add all the areas
	 */
	private void createAreas(){

		addArea(new Ferme());
		addArea(new Village());

	}

	@Override
	public boolean begin(Window window, FileSystem fileSystem) {


		if (super.begin(window, fileSystem)) {
			createAreas();
			areaIndex = 0;
			initArea(areas[areaIndex]);
			return true;
		}
		return false;
	}
	
	 private void initArea(String areaKey) {

		 
	 }
	@Override
	public void update(float deltaTime) {
	}

	@Override
	public void end() {
	}

	@Override
	public String getTitle() {
		return "Tuto2";
	}

	protected void switchArea() {


		areaIndex = (areaIndex==0) ? 1 : 0;
	}

}
