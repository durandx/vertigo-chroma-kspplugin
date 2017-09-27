package io.vertigo.chroma.kspplugin.resources.core;

import io.vertigo.chroma.kspplugin.model.Navigable;

import java.util.List;

import org.eclipse.core.resources.IFile;

/**
 * Contrat de la r�alisation d'un magasin de ressources.
 *
 * @param <T> Type de l'�l�ment.
 */
public interface ResourceStoreImplementor<T extends Navigable> {

	/**
	 * Indique si un fichier est candidat pour fournir des �l�ments pour le magasin.
	 * 
	 * @param file
	 * @return
	 */
	boolean isCandidate(IFile file);

	/**
	 * Obtient la liste des �l�ments � stocker dans le magasin pour un fournisseur de fichier donn�.
	 * 
	 * @param fileProvider Fournisseur de fichier.
	 * @return Liste des �l�ments.
	 */
	List<T> getItems(FileProvider fileProvider);
}
