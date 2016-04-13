package dragonball.model.exceptions;

@SuppressWarnings("serial")
public class NotEnoughKiException extends NotEnoughResourcesException{

	private int availableKi;
	private int requiredKi;
	
	public NotEnoughKiException(int requiredKi, int availableKi) {
		super("available ki: " + availableKi + " required ki: "+ requiredKi);
		this.availableKi = availableKi;
		this.requiredKi = requiredKi;
	}

	public int getAvailableKi() {
		return availableKi;
	}

	public int getRequiredKi() {
		return requiredKi;
	}

	
}
