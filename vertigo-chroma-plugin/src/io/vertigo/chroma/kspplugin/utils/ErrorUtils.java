package io.vertigo.chroma.kspplugin.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * M�thodes utilitaires pour g�rer les erreurs.
 */
public final class ErrorUtils {

	private static final Logger LOGGER = Logger.getLogger("vertigo.chroma.kspplugin");

	private ErrorUtils() {
		// RAS
	}

	/**
	 * G�re les exceptions attrap�es par le plugin.
	 * 
	 * @param e Exception.
	 */
	public static void handle(Exception e) {
		LOGGER.log(Level.SEVERE, "Erreur dans le plugin KSP.", e);
	}
}
