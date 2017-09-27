package io.vertigo.chroma.kspplugin.model;

/**
 * Contrat des objets vers lequel on peut naviguer.
 */
@FunctionalInterface
public interface Navigable {

	/**
	 * @return R�gion de fichier.
	 */
	FileRegion getFileRegion();
}
