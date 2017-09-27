package io.vertigo.chroma.kspplugin.ui.editors.analysis;

/**
 * Contrat d'un couple de caract�res ouvrants et fermants.
 *
 */
public interface OpenCloseCouple {

	/**
	 * @return Caract�re ouvrant.
	 */
	OpenCloseCharacter getOpenCharacter();

	/**
	 * @return Caract�re fermant.
	 */
	OpenCloseCharacter getCloseCharacter();

	/**
	 * @return Message pour un caract�re ouvrant manquant.
	 */
	String getMissingOpeningMessage();

	/**
	 * @return Message pour un caract�re fermant manquant.
	 */
	String getMissingClosingMessage();
}
