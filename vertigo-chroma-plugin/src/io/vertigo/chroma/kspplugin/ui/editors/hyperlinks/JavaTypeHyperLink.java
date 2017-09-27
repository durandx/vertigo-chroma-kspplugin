package io.vertigo.chroma.kspplugin.ui.editors.hyperlinks;

import io.vertigo.chroma.kspplugin.utils.ErrorUtils;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.PartInitException;

/**
 * Lien vers un fichier Java � partir de son type JDT.
 */
public class JavaTypeHyperLink implements IHyperlink {

	private final IRegion urlRegion;
	private final IType type;

	/**
	 * Cr�� une nouvelle instance de JavaTypeHyperLink.
	 * 
	 * @param urlRegion R�gion du lien dans le document.
	 * @param type Type Java.
	 */
	public JavaTypeHyperLink(IRegion urlRegion, IType type) {
		this.urlRegion = urlRegion;
		this.type = type;
	}

	@Override
	public IRegion getHyperlinkRegion() {
		return urlRegion;
	}

	@Override
	public String getTypeLabel() {
		return null;
	}

	@Override
	public String getHyperlinkText() {
		return "Open Java file";
	}

	@Override
	public void open() {
		try {
			JavaUI.openInEditor(type);
		} catch (PartInitException | JavaModelException e) {
			ErrorUtils.handle(e);
		}
	}
}
