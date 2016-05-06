package dragonball.model.cell;

import dragonball.model.character.fighter.*;

public class FoeCell extends Cell {

	private NonPlayableFighter foe;

	public FoeCell(NonPlayableFighter foe) {
		this.foe = foe;
	}

	public NonPlayableFighter getFoe() {
		return foe;
	}

	public String toString() {
		return foe.isStrong() ? "[b]" : "[w]";
	}

	@Override
	public void onStep() {
		notifyOnFoeEncountered(foe);
	}
}
