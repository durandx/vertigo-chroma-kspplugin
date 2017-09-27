package io.vertigo.chroma.kspplugin.ui.editors.analysis;

import java.util.regex.Pattern;

/**
 * Enum�ration des caract�res ouvrants fermants dans un KSP.
 */
public enum OpenCloseCharacter {

	/**
	 * Accolade ouvrante.
	 */
	OPEN_CURLY_BRACE("(\\{)"),

	/**
	 * Accolade fermante.
	 */
	CLOSE_CURLY_BRACE("(\\})"),

	/**
	 * Parenth�se ouvrante.
	 */
	OPEN_PARENTHESIS("(\\()"),

	/**
	 * Parenth�se fermante.
	 */
	CLOSE_PARENTHESIS("(\\))"),

	/**
	 * Tag Java ouvrant.
	 */
	OPEN_JAVA_TAG("(<%)"),

	/**
	 * Tag Java fermant.
	 */
	CLOSE_JAVA_TAG("(%>)");

	private final Pattern pattern;

	/**
	 * Cr�� une nouvelle instance de OpenCloseCharacter.
	 * 
	 * @param regex Regex permettant de d�tecter le caract�re.
	 */
	OpenCloseCharacter(String regex) {
		this.pattern = Pattern.compile(regex);
	}

	/**
	 * Obtient le pattern permettant de d�tecter le caract�re.
	 * 
	 * @return Pattern regex.
	 */
	public Pattern getPattern() {
		return pattern;
	}
}
