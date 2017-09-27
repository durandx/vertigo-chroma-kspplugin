package io.vertigo.chroma.kspplugin.utils;

import java.text.MessageFormat;
import java.util.function.BooleanSupplier;

/**
 * M�thodes reprises du StringUtil de Vertigo pour �tre en conformit� sur la gestion des transformations entre les diff�rentes casses.
 * 
 * <p>
 * Voir <a href="https://github.com/KleeGroup/vertigo/blob/master/vertigo-core/src/main/java/io/vertigo/util/StringUtil.java">StringUtil.java</a>.
 * </p>
 */
public final class VertigoStringUtils {

	private VertigoStringUtils() {
		// RAS.
	}

	/**
	 * Impl�mentation du test de la chaine vide. ie null ou blank (espace, \t \n \r \p ...)
	 * 
	 * @param strValue String
	 * @return Si la chaine ne contient que des caract�res blank
	 * @see java.lang.Character isWhitespace(char)
	 */
	public static boolean isEmpty(final String strValue) {
		if (strValue == null) {
			return true;
		}
		// On prefere cette implementation qui ne cr�e pas de nouvelle chaine (contrairement au trim())
		for (int i = 0; i < strValue.length(); i++) {
			if (!Character.isWhitespace(strValue.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Remplacement au sein d'une chaine d'un motif par un autre. Le remplacement avance, il n'est pas r�cursif !!. Attention : pour des char le
	 * String.replace(char old, char new) est plus performant.
	 *
	 * @param str String
	 * @param oldStr Chaine � remplacer
	 * @param newStr Chaine de remplacement
	 * @return Chaine remplac�e
	 */
	public static String replace(final String str, final String oldStr, final String newStr) {
		Assertion.checkNotNull(str);
		// -----
		final StringBuilder result = new StringBuilder(str);
		replace(result, oldStr, newStr);
		return result.toString();
	}

	/**
	 * Remplacement au sein d'une chaine d'un motif par un autre. Le remplacement avance, il n'est pas r�cursif !!. Le StringBuilder est modifi� !! c'est
	 * pourquoi il n'y a pas de return.
	 * 
	 * @param str StringBuilder
	 * @param oldStr Chaine � remplacer
	 * @param newStr Chaine de remplacement
	 */
	public static void replace(final StringBuilder str, final String oldStr, final String newStr) {
		Assertion.checkNotNull(str);
		Assertion.checkNotNull(oldStr);
		Assertion.checkArgument(oldStr.length() > 0, "La chaine a remplacer ne doit pas �tre vide");
		Assertion.checkNotNull(newStr);
		// -----
		int index = str.indexOf(oldStr);
		if (index == -1) {
			return;
		}

		final int oldStrLength = oldStr.length();
		final int newStrLength = newStr.length();
		StringBuilder result = str;
		do {
			result = result.replace(index, index + oldStrLength, newStr);
			index = str.indexOf(oldStr, index + newStrLength);
		} while (index != -1);
	}

	/**
	 * Fusionne une chaine compatible avec les param�tres. Les caract�res { } sont interdits ou doivent �tre echapp�s avec \\.
	 * 
	 * @param msg Chaine au format MessageFormat
	 * @param params param�tres du message
	 * @return Chaine fusionn�e
	 */
	public static String format(final String msg, final Object... params) {
		Assertion.checkNotNull(msg);
		// -----
		if (params == null || params.length == 0) {
			return msg;
		}
		// Gestion des doubles quotes
		// On simple quotes les doubles quotes d�j� pos�es.
		// Puis on double toutes les simples quotes ainsi il ne reste plus de simple quote non doubl�e.
		final StringBuilder newMsg = new StringBuilder(msg);
		replace(newMsg, "''", "'");
		replace(newMsg, "'", "''");
		replace(newMsg, "\\{", "'{'");
		replace(newMsg, "\\}", "'}'");
		return MessageFormat.format(newMsg.toString(), params);
	}

	/**
	 * On abaisse la premiere lettre.
	 * 
	 * @param strValue String non null
	 * @return Chaine avec la premiere lettre en minuscule
	 */
	public static String first2LowerCase(final String strValue) {
		Assertion.checkNotNull(strValue);
		// -----
		if (strValue.isEmpty()) {
			return strValue;
		}

		final char firstChar = strValue.charAt(0);
		if (Character.isUpperCase(firstChar)) { // la m�thode est appell� souvant et la concat�nation de chaine est lourde : on test avant de faire l'op�ration
			return Character.toLowerCase(firstChar) + strValue.substring(1);
		}
		return strValue;
	}

	/**
	 * Capitalisation de la premi�re lettre.
	 *
	 * @param strValue String non null
	 * @return Chaine avec la premiere lettre en majuscule
	 */
	public static String first2UpperCase(final String strValue) {
		Assertion.checkNotNull(strValue);
		// -----
		if (strValue.isEmpty()) {
			return strValue;
		}

		final char firstChar = strValue.charAt(0);
		if (Character.isLowerCase(firstChar)) { // la m�thode est appell� souvant et la concat�nation de chaine est lourde : on test avant de faire l'op�ration
			return Character.toUpperCase(firstChar) + strValue.substring(1);
		}
		return strValue;
	}

	/**
	 * XXX_YYY_ZZZ -> xxxYyyZzz.
	 * 
	 * @param str la chaine de carat�res sur laquelle s'appliquent les transformation
	 * @return camelCase
	 */
	public static String constToLowerCamelCase(final String str) {
		return constToCamelCase(str, false);
	}

	/**
	 * XXX_YYY_ZZZ -> XxxYyyZzz.
	 * 
	 * @param str la chaine de carat�res sur laquelle s'appliquent les transformation
	 * @return CamelCase
	 */
	public static String constToUpperCamelCase(final String str) {
		return constToCamelCase(str, true);
	}

	/**
	 * XXX_YYY_ZZZ -> XxxYyyZzz ou xxxYyyZzz.
	 * 
	 * @param str la chaine de carat�res sur laquelle s'appliquent les transformation
	 * @param first2UpperCase d�finit si la premi�re lettre est en majuscules
	 * @return Renvoie une chaine de carat�re correspondant � str en minuscule et sans underscores, � l'exception des premi�res lettres apr�s les underscores
	 *         dans str
	 */
	private static String constToCamelCase(final String str, final boolean first2UpperCase) {
		Assertion.checkNotNull(str);
		Assertion.checkArgument(str.length() > 0, "Chaine � modifier invalide (ne doit pas �tre vide)");
		Assertion.checkArgument(str.indexOf("__") == -1, "Chaine � modifier invalide : {0} (__ interdit)", str);
		// -----
		final StringBuilder result = new StringBuilder();
		boolean upper = first2UpperCase;
		Boolean digit = null;
		final int length = str.length();
		char c;
		for (int i = 0; i < length; i++) {
			c = str.charAt(i);
			if (c == '_') {
				if (digit != null && digit.booleanValue() && Character.isDigit(str.charAt(i + 1))) {
					result.append('_');
				}
				digit = null;
				upper = true;
			} else {
				if (digit != null) {
					Assertion.checkArgument(digit.equals(Character.isDigit(c)),
							"Chaine � modifier invalide : {0} (lettres et chiffres doivent toujours �tre s�par�s par _)", str);
				}
				digit = Character.isDigit(c);

				if (upper) {
					result.append(Character.toUpperCase(c));
					upper = false;
				} else {
					result.append(Character.toLowerCase(c));
				}
			}
		}
		return result.toString();
	}

	/**
	 * Les chiffres sont assimil�s � des lettres en majuscules XxxYyyZzz ou xxxYyyZzz -> XXX_YYY_ZZZ XxxYZzz ou xxxYZzz -> XXX_Y_ZZZ Xxx123 -->XXX_123 XxxYzw123
	 * --> (interdit) Xxx123Y --> XXX_123_Y. Xxx123y --> XXX_123Y.
	 * 
	 * @param str la chaine de carat�res sur laquelle s'appliquent les transformation
	 * @return Passage en constante d'une cha�ne de caract�res (Fonction inverse de caseTransform)
	 */
	public static String camelToConstCase(final String str) {
		final StringBuilder result = new StringBuilder();
		final int length = str.length();
		char c;
		boolean isDigit = false;
		for (int i = 0; i < length; i++) {
			c = str.charAt(i);
			if (Character.isDigit(c) || c == '_') {
				if (i > 0 && !isDigit) {
					result.append('_');
				}
				isDigit = true;
			} else if (Character.isUpperCase(c)) {
				if (i > 0) {
					result.append('_');
				}
				isDigit = false;
			} else {
				isDigit = false;
			}
			result.append(Character.toUpperCase(c));
		}
		return result.toString();
	}

	/**
	 * Teste si un caract�re est une simple lettre (minuscule ou majuscule, sans accent) ou un chiffre.
	 * 
	 * @param c caract�re
	 * @return boolean
	 */
	public static boolean isSimpleLetterOrDigit(final char c) {
		return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9'; // NOSONAR
	}

	public static final class Assertion {
		private Assertion() {
			// private constructor
		}

		/**
		 * Check if an object is not null. If not a generic exception is thrown.
		 * 
		 * @param o Object object that must be not null
		 */
		public static void checkNotNull(final Object o) {
			if (o == null) {
				throw new NullPointerException();
			}
		}

		/**
		 * Check if an object is not null. If not an exception with a contextual message is thrown.
		 *
		 * @param o Object object that must be not null
		 * @param msg Error message
		 * @param params params of the message
		 */
		public static void checkNotNull(final Object o, final String msg, final Object... params) {
			// Attention si o est un Boolean : il peut s'agir du resultat d'un test (boolean) qui a été autoboxé en Boolean
			if (o == null) {
				throw new NullPointerException(VertigoStringUtils.format(msg, params));
			}
		}

		/**
		 * Check if a test is valid. If not an exception with a contextual message is thrown.
		 *
		 * @param test If the assertion succeeds
		 * @param msg Error message
		 * @param params params of the message
		 */
		public static void checkArgument(final boolean test, final String msg, final Object... params) {
			if (!test) {
				throw new IllegalArgumentException(VertigoStringUtils.format(msg, params));
			}
		}

		/**
		 * Check if a string is not empty. If not an generic exception is thrown.
		 *
		 * @param str String that must be not empty
		 */
		public static void checkArgNotEmpty(final String str) {
			checkNotNull(str);
			if (VertigoStringUtils.isEmpty(str)) {
				throw new IllegalArgumentException("String must not be empty");
			}
		}

		/**
		 * Check if a string is not empty.
		 * 
		 * @param str String that must be not empty
		 * @param msg Error message
		 * @param params params of the message
		 */
		public static void checkArgNotEmpty(final String str, final String msg, final Object... params) {
			checkNotNull(str, msg, params);
			if (VertigoStringUtils.isEmpty(str)) {
				throw new IllegalArgumentException(VertigoStringUtils.format(msg, params));
			}
		}

		/**
		 * Check if a state is valid. This assertion should be used inside a processing to check a step.
		 *
		 * @param test If the assertion succeeds
		 * @param msg Error message
		 * @param params params of the message
		 */
		public static void checkState(final boolean test, final String msg, final Object... params) {
			if (!test) {
				throw new IllegalStateException(VertigoStringUtils.format(msg, params));
			}
		}

		/**
		 * @param ifCondition condition of this assertion
		 * @return Assertion to check if condition is true
		 */
		public static ConditionalAssertion when(final boolean ifCondition) {
			return (test, msg, params) -> {
				if (ifCondition) {
					Assertion.checkState(test.getAsBoolean(), msg, params);
				}
			};
		}

		/**
		 * Function to assert when a condition if fulfilled.
		 * 
		 * @author npiedeloup
		 */
		@FunctionalInterface
		public interface ConditionalAssertion {
			/**
			 * Assert something if test return null
			 * 
			 * @param test BooleanSupplier Check if a state is valid.
			 * @param msg Message Error message
			 * @param params params of the message
			 */
			void check(final BooleanSupplier test, final String msg, final Object... params);
		}
	}

}
