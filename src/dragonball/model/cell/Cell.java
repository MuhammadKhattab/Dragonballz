package dragonball.model.cell;

import java.io.*;

import dragonball.model.character.fighter.NonPlayableFighter;

@SuppressWarnings("serial")
public abstract class Cell implements Serializable {

	private CellListener listener;

	public abstract String toString();

	public abstract void onStep();

	public CellListener getListener() {
		return listener;
	}

	public void setListener(CellListener listener) {
		this.listener = listener;
	}

	protected void notifyOnFoeEncountered(NonPlayableFighter foe) {
		if (listener != null) {
			listener.onFoeEncountered(foe);
		}
	}

	protected void notifyOnCollectibleFound(Collectible collectible) {
		if (listener != null) {
			listener.onCollectibleFound(collectible);
		}
	}

}
