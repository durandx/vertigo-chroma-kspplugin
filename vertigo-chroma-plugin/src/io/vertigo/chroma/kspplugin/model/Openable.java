package io.vertigo.chroma.kspplugin.model;

import org.eclipse.swt.graphics.Image;

/**
 * Contrat des objets utilisable dans une fen�tre de recherche.
 */
public interface Openable extends Navigable {

	/**
	 * Renvoie le texte principal � afficher.
	 * 
	 * @return Texte.
	 */
	String getText();

	/**
	 * Renvoie le texte qualifiant compl�tement l'�l�ment.
	 * 
	 * @return Texte.
	 */
	String getQualifier();

	/**
	 * Renvoie l'ic�ne illustrant l'�l�ment.
	 * 
	 * @return Image.
	 */
	Image getImage();
}
