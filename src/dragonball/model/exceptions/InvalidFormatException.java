package dragonball.model.exceptions;

import java.io.IOException;

@SuppressWarnings("serial")
public abstract class InvalidFormatException extends IOException {

	private String sourceFile;
	private int sourceLine;

	public InvalidFormatException(String sourceFile, int sourceLine) {
		this.sourceFile = sourceFile;
		this.sourceLine = sourceLine;
	}

	public InvalidFormatException(String message, String sourceFile, int sourceLine) {
		super(message+ " Corrupted file: " + sourceFile+ " line: " +sourceLine);
		this.sourceFile = sourceFile;
		this.sourceLine = sourceLine;
	}

	public String getSourceFile() {
		return sourceFile;
	}

	public int getSourceLine() {
		return sourceLine;
	}
	
}
