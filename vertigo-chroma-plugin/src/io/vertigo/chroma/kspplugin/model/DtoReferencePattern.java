package io.vertigo.chroma.kspplugin.model;

/**
 * Enum�ration des pattern de r�f�rencement d'un DTO dans un KSP.
 */
public enum DtoReferencePattern {

	/**
	 * Le DTO est r�f�rence comme un domaine
	 * <p>
	 * Exemple : DO_DT_UTILISATEUR_DTO, DO_DT_UTILISATEUR_DTC
	 * </p>
	 */
	DOMAIN,

	/**
	 * Le DTO est r�f�renc� comme un nom simple.
	 * <p>
	 * Exemple : DT_UTILISATEUR.
	 * </p>
	 */
	SIMPLE_NAME
}
