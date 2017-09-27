package io.vertigo.chroma.kspplugin.ui.editors.hyperlinks;

import io.vertigo.chroma.kspplugin.model.FileRegion;
import io.vertigo.chroma.kspplugin.model.Navigable;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IRegion;

/**
 * Lien vers un fichier.
 */
public class FileHyperLink extends NavigableHyperLink {

	/**
	 * Cr�� une nouvelle instance de FileHyperLink.
	 * 
	 * @param urlRegion R�gion du lien dans le document.
	 * @param file Fichier cibl�.
	 */
	public FileHyperLink(IRegion urlRegion, IFile file) {
		super(urlRegion, createNavigable(file), "Open file");
	}

	/**
	 * Cr�� un navigable � partir du fichier.
	 * 
	 * @param file Fichier.
	 * @return Navigable vers le premier caract�re du fichier.
	 */
	private static Navigable createNavigable(IFile file) {
		return () -> new FileRegion(file, 0, 0);
	}
}
