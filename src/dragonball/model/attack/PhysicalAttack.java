package dragonball.model.attack;

import dragonball.model.battle.BattleOpponent;
import dragonball.model.character.fighter.Fighter;

public class PhysicalAttack extends Attack {

	public PhysicalAttack() {
		super("Physical Attack", 50);
	}

	public int getAppliedDamage(BattleOpponent attacker) {
		Fighter x = (Fighter) attacker;
		return x.getPhysicalDamage() + 50;
	}

	public void onUse(BattleOpponent attacker, BattleOpponent defender, boolean defenderBlocking) throws Exception {
		super.onUse(attacker, defender, defenderBlocking);
		Fighter x = (Fighter) attacker;
		x.setKi(x.getKi() + 1);
	}

}
