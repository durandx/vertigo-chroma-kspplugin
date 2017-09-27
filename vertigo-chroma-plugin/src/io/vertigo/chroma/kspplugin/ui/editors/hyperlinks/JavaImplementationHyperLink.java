package io.vertigo.chroma.kspplugin.ui.editors.hyperlinks;

import io.vertigo.chroma.kspplugin.model.Navigable;

import org.eclipse.jface.text.IRegion;

/**
 * Lien vers une impl�mentation Java.
 */
public class JavaImplementationHyperLink extends NavigableHyperLink {

	/**
	 * Cr�� une nouvelle instance de JavaImplementationHyperLink.
	 * 
	 * @param urlRegion R�gion du lien dans le document.
	 * @param navigable Navigable de l'impl�mentation Java.
	 */
	public JavaImplementationHyperLink(IRegion urlRegion, Navigable navigable) {
		super(urlRegion, navigable, "Open Java Implementation");
	}
}
