package com.ogzcm.enums;

public enum ResponseType {
	
	SUCCESS ("Success"), FAIL ("Fail");

    private final String text;

    /**
     * @param text
     */
    private ResponseType(final String text) {
        this.text = text;
    }

	public String getText() {
		return text;
	}
}
