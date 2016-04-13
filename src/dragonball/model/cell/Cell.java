package dragonball.model.cell;

import java.io.*;

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
}
