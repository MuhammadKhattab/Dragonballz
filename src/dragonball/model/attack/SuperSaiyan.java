package dragonball.model.attack;

import dragonball.model.battle.BattleOpponent;
import dragonball.model.character.fighter.Saiyan;
import dragonball.model.exceptions.NotASaiyanException;
import dragonball.model.exceptions.NotEnoughKiException;

@SuppressWarnings("serial")
public class SuperSaiyan extends UltimateAttack {

	public SuperSaiyan() {
		super("Super Saiyan", 0);
	}

	public int getAppliedDamage(BattleOpponent attacker) {
		return 0;
	}

	public void onUse(BattleOpponent attacker, BattleOpponent defender, boolean defenderBlocking) throws Exception {
		if (attacker instanceof Saiyan) {
			if (((Saiyan) attacker).getKi() >= 3)
				((Saiyan) attacker).setTransformed(true);
			else 
				throw new NotEnoughKiException(3, ((Saiyan)attacker).getKi());
		} else
			throw new NotASaiyanException();
	}

}
