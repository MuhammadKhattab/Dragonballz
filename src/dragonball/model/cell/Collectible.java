package dragonball.model.cell;

import java.io.Serializable;

public enum Collectible implements Serializable{

	SENZU_BEAN, DRAGON_BALL;
	
	public String toString(){
		return this.equals(Collectible.SENZU_BEAN)? "Senzu Bean": "Dragon Ball";
	}
}
