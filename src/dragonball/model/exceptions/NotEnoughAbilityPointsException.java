package dragonball.model.exceptions;

@SuppressWarnings("serial")
public class NotEnoughAbilityPointsException extends NotEnoughResourcesException{

	public NotEnoughAbilityPointsException() {
		super("Why you no get some ability points and come back");
	}
}
