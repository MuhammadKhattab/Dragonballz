package dragonball.model.exceptions;

@SuppressWarnings("serial")
public class NotASaiyanException extends InvalidAssignAttackException {

	public NotASaiyanException() {
		super("This is not a Saiyan!");
	}
}
