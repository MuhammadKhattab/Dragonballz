package dragonball.model.exceptions;

@SuppressWarnings("serial")
public class NotASaiyanException extends InvalidAssignAttackException {

	public NotASaiyanException() {
		super("You must be a Saiyan to do this!");
	}
}
