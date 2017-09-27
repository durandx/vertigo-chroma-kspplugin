package io.vertigo.chroma.kspplugin.ui.editors.analysis;

import io.vertigo.chroma.kspplugin.utils.ErrorUtils;
import io.vertigo.chroma.kspplugin.utils.MarkerUtils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;

/**
 * V�rificateur de fichier KSP.
 */
public class KspFileChecker {

	private final IFile file;
	private IDocument document;

	/**
	 * Cr�� une nouvelle instance de KspFileChecker.
	 * 
	 * @param file Fichier KSP.
	 */
	public KspFileChecker(IFile file) {
		this.file = file;
	}

	/**
	 * Ex�cute la v�rification du fichier et met � jour les marqueurs.
	 */
	public void check() {
		deleteMarkers();
		createMarkers();
	}

	/**
	 * Supprime les marqueurs du fichier courant.
	 */
	private void deleteMarkers() {
		MarkerUtils.deleteKspMarkers(file);
	}

	/**
	 * Cr�� les marqueurs du fichier courant.
	 */
	private void createMarkers() {
		try {
			/* Obtient le document. */
			TextFileDocumentProvider documentProvider = new TextFileDocumentProvider();
			documentProvider.connect(file);
			document = documentProvider.getDocument(file);

			/* Analyse le document. */
			checkKspDocument();

			/* Lib�re le document. */
			documentProvider.disconnect(file);
		} catch (CoreException e) {
			ErrorUtils.handle(e);
		}
	}

	/**
	 * Analyse le document KSP.
	 */
	private void checkKspDocument() {

		KspDeclarationChecker checker = null;

		/* Parcourt les lignes du document. */
		for (int lineIdx = 0; lineIdx < document.getNumberOfLines(); lineIdx++) {
			/* Extrait un candidat de ligne de d�claration. */
			KspDeclarationChecker candidate = KspDeclarationChecker.extractChecker(file, document, lineIdx);

			/* Cas o� la ligne contient une d�claration KSP */
			if (candidate != null) {
				if (checker != null) {
					/* Cas d'une nouvelle d�claration : on g�n�re les marqueurs pour la d�claration pr�c�dente. */
					checker.generateMarkers();
				}

				/* Mise � jour de la d�claration inspect�e courante. */
				checker = candidate;
			}

			/* Il existe une d�claration courante */
			if (checker != null) {
				/* On inspecte la ligne. */
				checker.inspectLine(lineIdx);
			}
		}

		/* Derni�re d�claration du fichier */
		if (checker != null) {
			checker.generateMarkers();
		}
	}
}
