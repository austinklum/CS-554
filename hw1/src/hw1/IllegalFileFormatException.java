package hw1;

import java.io.IOException;

public class IllegalFileFormatException extends IOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public IllegalFileFormatException() {
		super();
	}
	public IllegalFileFormatException(String loggingMsg) {
		super(loggingMsg);
	}

}
