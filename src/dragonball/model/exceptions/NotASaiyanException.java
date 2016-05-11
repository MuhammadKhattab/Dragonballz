package dragonball.model.exceptions;

@SuppressWarnings("serial")
public class NotASaiyanException extends InvalidAssignAttackException {

	public NotASaiyanException() {
		super("You have to be a Saiyan!");}
}
