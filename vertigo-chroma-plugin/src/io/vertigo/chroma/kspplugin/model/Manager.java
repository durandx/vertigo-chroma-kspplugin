package io.vertigo.chroma.kspplugin.model;

/**
 * Contrat des managers.
 * <p>
 * Les managers ont un �tat qui doit �tre initialis� au d�marrage du plugin.
 * </p>
 */
@FunctionalInterface
public interface Manager {

	/**
	 * Initialise le manager.
	 */
	void init();
}
