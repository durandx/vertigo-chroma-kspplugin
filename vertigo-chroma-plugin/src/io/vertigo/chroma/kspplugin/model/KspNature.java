package io.vertigo.chroma.kspplugin.model;

/**
 * Nature de d�claration KSP.
 */
public enum KspNature {

	DT_DEFINITION("DtDefinition", "DT");

	private final String kspKeyword;
	private final String kspKeywordKasper3;

	/**
	 * Cr�� une nouvelle instance de KspNature.
	 * 
	 * @param kspKeyWord Mot-cl�.
	 * @param kspKeyWordKasper3 Mot-cl� pour Kasper 3.
	 */
	KspNature(String kspKeyWord, String kspKeyWordKasper3) {
		this.kspKeyword = kspKeyWord;
		this.kspKeywordKasper3 = kspKeyWordKasper3;
	}

	public String getKspKeyword() {
		return kspKeyword;
	}

	public String getKspKeyWordKasper3() {
		return kspKeywordKasper3;
	}
}
