package dragonball.model.battle;

import java.io.Serializable;

public enum BattleEventType implements Serializable{
	STARTED, ENDED, NEW_TURN, ATTACK, BLOCK, USE
}
