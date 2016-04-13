package dragonball.model.cell;

public class CollectibleCell extends Cell {

	private Collectible collectible;

	public CollectibleCell(Collectible collectible) {
		this.collectible = collectible;
	}

	public Collectible getCollectible() {
		return collectible;
	}

	public String toString() {
		char x = collectible.equals(Collectible.DRAGON_BALL) ? 'd' : 's';
		return "[" + x + "]";
	}

	@Override
	public void onStep() {
		CellListener l = getListener();
		if (l != null)
			l.onCollectibleFound(collectible);
	}

}
