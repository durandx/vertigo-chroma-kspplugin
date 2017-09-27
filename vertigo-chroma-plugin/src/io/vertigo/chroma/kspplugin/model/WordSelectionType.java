package io.vertigo.chroma.kspplugin.model;

import io.vertigo.chroma.kspplugin.utils.StringUtils;

import java.util.function.Predicate;

/**
 * Enum�ration des types de s�lection de mot.
 * <p>
 * Utilis� pour la d�tection de mot courant dans un �diteur, pour les d�tecteurs de lien et l'autocompl�tion.
 * </p>
 */
public enum WordSelectionType {

	/**
	 * Mot en CONSTANT_CASE.
	 */
	CONSTANT_CASE(StringUtils::isConstantCase),

	/**
	 * Mot en camelCase.
	 */
	CAMEL_CASE(StringUtils::isCamelCase),

	/**
	 * Mot en casse SQL (minuscule, majuscule, chiffre, underscore).
	 */
	SNAKE_CASE(StringUtils::isSnakeCase),

	/**
	 * Mot en casse SQL (minuscule, majuscule, chiffre, underscore) avec #.
	 */
	SQL_PARAMETER_NAME(StringUtils::isSqlParameterName),

	/**
	 * Mot repr�sentant un nom canonique Java.
	 */
	CANONICAL_JAVA_NAME(StringUtils::isCanonicalJavaName),

	/**
	 * Mot sans espace.
	 */
	NOT_SPACE(StringUtils::isNotSpace);

	private final Predicate<String> tester;

	WordSelectionType(Predicate<String> tester) {
		this.tester = tester;
	}

	public Predicate<String> getTester() {
		return tester;
	}
}
