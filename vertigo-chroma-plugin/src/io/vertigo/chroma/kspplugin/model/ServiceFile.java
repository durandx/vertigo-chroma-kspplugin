package io.vertigo.chroma.kspplugin.model;

import java.util.List;

/**
 * Repr�sente un fichier d'impl�mentation de service m�tier et l'ensemble de ses m�thodes.
 */
public class ServiceFile {

	private List<ServiceImplementation> serviceImplementations;

	/**
	 * Cr�� une nouvelle instance de ServiceFile.
	 * 
	 * @param file Ressource fichier dans le workspace.
	 * @param serviceImplementations Liste des m�thodes.
	 */
	public ServiceFile(List<ServiceImplementation> serviceImplementations) {
		this.serviceImplementations = serviceImplementations;
	}

	/**
	 * @return Renvoie la liste des d�clarations KSP.
	 */
	public List<ServiceImplementation> getServiceImplementations() {
		return serviceImplementations;
	}
}
