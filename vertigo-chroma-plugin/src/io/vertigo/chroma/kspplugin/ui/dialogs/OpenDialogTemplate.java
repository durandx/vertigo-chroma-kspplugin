package io.vertigo.chroma.kspplugin.ui.dialogs;

/**
 * Template pour la factory permettant d'ouvrir une fen�tre de recherche.
 */
public interface OpenDialogTemplate {

	/**
	 * Renvoie la nature de l'objet recherch�.
	 * 
	 * @return Nature.
	 */
	String getNature();

	/**
	 * Charge la source de donn�es de la fen�tre de dialogue.
	 * 
	 * @return Liste des �l�ments � rechercher.
	 */
	Object[] getElements();
}
