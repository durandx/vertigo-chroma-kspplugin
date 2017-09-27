package io.vertigo.chroma.kspplugin.model;

/**
 * Repr�sente un chemin de d�finition de DTO (en Kasper 3).
 */
public class DtoDefinitionPath {

	private final String packageName;
	private final String dtoName;

	public DtoDefinitionPath(String packageName, String dtoName) {
		super();
		this.packageName = packageName;
		this.dtoName = dtoName;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getDtoName() {
		return dtoName;
	}
}
