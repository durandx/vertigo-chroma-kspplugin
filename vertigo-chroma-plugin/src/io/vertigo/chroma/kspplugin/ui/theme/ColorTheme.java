package io.vertigo.chroma.kspplugin.ui.theme;

import io.vertigo.chroma.kspplugin.utils.UiUtils;

import org.eclipse.swt.graphics.RGB;

/**
 * Th�me de couleur d'Eclipse.
 */
public enum ColorTheme {

	/**
	 * Th�me par d�faut (fond blanc).
	 */
	DEFAULT_THEME,

	/**
	 * Th�me Dark (fond noir).
	 */
	DARK_THEME;

	/**
	 * Retourne le th�me courant.
	 * 
	 * @return Th�me courant.
	 */
	public static ColorTheme getCurrent() {
		return UiUtils.isDarkBackground() ? ColorTheme.DARK_THEME : ColorTheme.DEFAULT_THEME;
	}

	/**
	 * Obtient la couleur RGB pour un nom de couleur donn�, pour ce th�me.
	 * 
	 * @param colorName Nom de la couleur.
	 * @return Couleur RGB.
	 */
	public RGB getColor(ColorName colorName) {
		if (DARK_THEME.equals(this)) {
			return toDarkColor(colorName.getRgb());
		}
		return colorName.getRgb();
	}

	private RGB toDarkColor(RGB rgb) {
		/* On utilise la couleur compl�mentaire au blanc. */
		return new RGB(255 - rgb.red, 255 - rgb.green, 255 - rgb.blue);
	}
}
