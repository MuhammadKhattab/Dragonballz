package dragonball.model.exceptions;

@SuppressWarnings("serial")
public class MaximumAttacksLearnedException extends InvalidAssignAttackException {

	public MaximumAttacksLearnedException() {
		super("You can't assign more attacks...\nbut you can replace old attacks with new ones!");
	}
}
