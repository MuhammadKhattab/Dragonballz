package dragonball.model.exceptions;

@SuppressWarnings("serial")
public class NotEnoughAbilityPointsException extends NotEnoughResourcesException{

	public NotEnoughAbilityPointsException() {
		super("You have 0 ability points!\nYou should level up to get more ability points!");
	}
}
