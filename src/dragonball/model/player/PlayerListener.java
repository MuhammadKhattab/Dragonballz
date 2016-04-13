package dragonball.model.player;

import java.util.*;

import dragonball.model.dragon.DragonWish;

public interface PlayerListener extends EventListener {
	void onDragonCalled();

	void onWishChosen(DragonWish wish);
}
