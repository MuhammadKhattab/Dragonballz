package dragonball.model.exceptions;

import dragonball.model.attack.Attack;

@SuppressWarnings("serial")
public class DuplicateAttackException extends InvalidAssignAttackException {

	private Attack attack;

	public DuplicateAttackException(Attack attack) {
		super(attack.getName()+ " is already assigned to this fighter!");
		this.attack = attack;
	}

	public Attack getAttack() {
		return attack;
	}
}
