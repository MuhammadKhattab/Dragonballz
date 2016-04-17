package dragonball.model.attack;

import dragonball.model.battle.BattleOpponent;
import dragonball.model.character.fighter.Fighter;
import dragonball.model.character.fighter.Saiyan;
import dragonball.model.exceptions.NotEnoughKiException;

@SuppressWarnings("serial")
public class UltimateAttack extends Attack {

	public UltimateAttack(String name, int damage) {
		super(name, damage);
	}

	public int getAppliedDamage(BattleOpponent attacker) {
		return getDamage() + ((Fighter) attacker).getBlastDamage();
	}

	public void onUse(BattleOpponent attacker, BattleOpponent defender, boolean defenderBlocking) throws Exception {
		Fighter attackerFighter = (Fighter) attacker;
		if (!(attacker instanceof Saiyan && ((Saiyan) attacker).isTransformed()))
			if (attackerFighter.getKi() >= 3)
				attackerFighter.setKi(attackerFighter.getKi() - 3);
			else
				throw new NotEnoughKiException(3, attackerFighter.getKi());
		super.onUse(attacker, defender, defenderBlocking);
	}

}
