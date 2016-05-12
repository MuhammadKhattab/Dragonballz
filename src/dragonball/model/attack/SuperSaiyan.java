package dragonball.model.attack;

import dragonball.model.battle.BattleOpponent;
import dragonball.model.character.fighter.Saiyan;
import dragonball.model.exceptions.NotEnoughKiException;

@SuppressWarnings("serial")
public class SuperSaiyan extends UltimateAttack {

	public SuperSaiyan() {
		super("Super Saiyan", 0);
	}

	public int getAppliedDamage(BattleOpponent attacker) {
		return 0;
	}

	@Override
	public void onUse(BattleOpponent attacker, BattleOpponent defender, boolean defenderBlocking)
			throws NotEnoughKiException {
		if (((Saiyan) attacker).getKi() > 2)
			((Saiyan) attacker).setTransformed(true);
		else
			throw new NotEnoughKiException(3, ((Saiyan) attacker).getKi());
	}

}
