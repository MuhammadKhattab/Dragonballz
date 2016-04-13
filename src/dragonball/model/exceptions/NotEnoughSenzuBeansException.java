package dragonball.model.exceptions;

@SuppressWarnings("serial")
public class NotEnoughSenzuBeansException extends NotEnoughResourcesException{

	public NotEnoughSenzuBeansException() {
		super("You have no senzuz ma dear");
	}
}
