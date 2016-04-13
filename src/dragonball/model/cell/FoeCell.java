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
		char x = foe.isStrong() ? 'b' : 'w';
		return "[" + x + "]";
	}

	@Override
	public void onStep() {
		CellListener l = getListener();
		if (l != null)
			l.onFoeEncountered(getFoe());
	}
}
