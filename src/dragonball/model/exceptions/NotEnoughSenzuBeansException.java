package dragonball.model.exceptions;

@SuppressWarnings("serial")
public class NotEnoughSenzuBeansException extends NotEnoughResourcesException{

	public NotEnoughSenzuBeansException() {
		super("You have 0 senzu beans!");
	}
}
