package dragonball.model.exceptions;

import dragonball.model.attack.Attack;

@SuppressWarnings("serial")
public class DuplicateAttackException extends InvalidAssignAttackException {

	private Attack attack;

	public DuplicateAttackException(Attack attack) {
		super("this attack: "+ attack.getName()+ " is duplicate!");
		this.attack = attack;
	}

	public Attack getAttack() {
		return attack;
	}
}
