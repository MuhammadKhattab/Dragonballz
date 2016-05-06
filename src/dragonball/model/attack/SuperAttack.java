package dragonball.model.attack;

import dragonball.model.battle.BattleOpponent;
import dragonball.model.character.fighter.Fighter;
import dragonball.model.character.fighter.Saiyan;
import dragonball.model.exceptions.NotASaiyanException;
import dragonball.model.exceptions.NotEnoughKiException;

@SuppressWarnings("serial")
public class SuperAttack extends Attack {

	public SuperAttack(String name, int damage) {
		super(name, damage);
	}

	public int getAppliedDamage(BattleOpponent attacker) {
		Fighter x = (Fighter) attacker;
		return x.getBlastDamage() + getDamage();
	}

	public void onUse(BattleOpponent attacker, BattleOpponent defender, boolean defenderBlocking) throws NotEnoughKiException {
		Fighter attackerFighter = (Fighter) attacker;
		if (!(attacker instanceof Saiyan && ((Saiyan) attacker).isTransformed()))
			if(attackerFighter.getKi() >= 1) {
			attackerFighter.setKi(attackerFighter.getKi() - 1);
			}
			else {
				throw new NotEnoughKiException(1, attackerFighter.getKi());
			}
		super.onUse(attacker, defender, defenderBlocking);
	}

}
