package dragonball.model.attack;

import dragonball.model.battle.BattleOpponent;
import dragonball.model.character.fighter.Fighter;

@SuppressWarnings("serial")
public class MaximumCharge extends SuperAttack {

	public MaximumCharge() {
		super("Maximum Charge", 0);
	}

	public int getAppliedDamage(BattleOpponent attacker) {
		return 0;
	}

	public void onUse(BattleOpponent attacker, BattleOpponent defender, boolean defenderBlocking) {
		Fighter x = (Fighter) attacker;
		x.setKi(x.getKi() + 3);
	}
}
