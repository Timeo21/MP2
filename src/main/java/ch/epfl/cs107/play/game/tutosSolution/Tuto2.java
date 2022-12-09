package ch.epfl.cs107.play.game.tutosSolution;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.tutosSolution.actor.SimpleGhost;
import ch.epfl.cs107.play.game.tutosSolution.area.tuto2.Ferme;
import ch.epfl.cs107.play.game.tutosSolution.area.tuto2.Village;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Window;

public class Tuto2 extends AreaGame {
	public final static float CAMERA_SCALE_FACTOR = 13.f;

	private final String[] areas = {"zelda/Ferme", "zelda/Village"};
	
	private int areaIndex;
	SimpleGhost player;
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
			Area area = setCurrentArea(areas[areaIndex], true);

			player = new SimpleGhost(new Vector(18, 7), "ghost.1");
			area.registerActor(player);
			area.setViewCandidate(player);
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
			Area currentArea = getCurrentArea();

			currentArea.unregisterActor(player);

			areaIndex = (areaIndex == 0)? 1 : 0;

			currentArea = setCurrentArea(areas[areaIndex], false);
			currentArea.registerActor(player);
			currentArea.setViewCandidate(player);

			player.strengthen();
		}

}
