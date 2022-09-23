package com.vilcart.util;

public class LineNumber {

	/**
	 * Get the current line number.
	 * 
	 * @return int - Current line number.
	 */
	public static int getLineNumber() {
		return Thread.currentThread().getStackTrace()[2].getLineNumber();
	}
}
