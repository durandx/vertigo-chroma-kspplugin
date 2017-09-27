package io.vertigo.chroma.kspplugin.ui.editors.kpr;

import org.eclipse.ui.editors.text.TextEditor;

/**
 * Editeur des fichiers KPR.
 */
public class KprEditor extends TextEditor {

	/**
	 * Cr�� une nouvelle instance de KspTextEditor.
	 */
	public KprEditor() {
		super();

		/* D�finit une configuration de SourceViewer pour d�finir des scanner. */
		KprSourceViewerConfiguration configuration = new KprSourceViewerConfiguration(this);
		setSourceViewerConfiguration(configuration);
	}
}
