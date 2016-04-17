package dragonball.model.exceptions;

@SuppressWarnings("serial")
public class MaximumAttacksLearnedException extends InvalidAssignAttackException {

	public MaximumAttacksLearnedException() {
		super("You can't learn more!");
	}
}
