package io.vertigo.chroma.kspplugin.ui.editors.hyperlinks;

import io.vertigo.chroma.kspplugin.model.Navigable;

import org.eclipse.jface.text.IRegion;

/**
 * Lien vers une d�claration KSP.
 */
public class KspDeclarationHyperLink extends NavigableHyperLink {

	/**
	 * Cr�� une nouvelle instance de KspDeclarationHyperLink.
	 * 
	 * @param urlRegion R�gion du lien dans le document.
	 * @param navigable Navigable de la d�claration KSP.
	 */
	public KspDeclarationHyperLink(IRegion urlRegion, Navigable navigable) {
		super(urlRegion, navigable, "Open KSP declaration");
	}
}
