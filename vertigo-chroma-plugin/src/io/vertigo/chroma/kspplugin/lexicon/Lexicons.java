package io.vertigo.chroma.kspplugin.lexicon;

/**
 * Enum�ration des lexiques.
 */
public enum Lexicons {

	/**
	 * Mots-cl�s SQL (select, from...).
	 */
	SQL_KEY_WORDS("resources/sql_keywords.txt"),

	/**
	 * Verbes (create, alter, ...) des d�clarations KSP.
	 */
	KSP_VERBS("resources/ksp_verbs.txt"),

	/**
	 * Pr�positions des d�clarations KSP.
	 */
	KSP_PREPOSITIONS("resources/ksp_prepositions.txt"),

	/**
	 * Natures (DtDefinition, Task, ...) des d�clarations KSP.
	 */
	KSP_NATURES("resources/ksp_natures.txt"),

	/**
	 * Attributs des d�clarations KSP.
	 */
	KSP_ATTRIBUTES("resources/ksp_attributes.txt"),

	/**
	 * Propri�t�s des d�clarations KSP.
	 */
	KSP_PROPERTIES("resources/ksp_properties.txt");

	private final String path;

	Lexicons(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
}
