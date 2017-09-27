package io.vertigo.chroma.kspplugin.utils;

import io.vertigo.chroma.kspplugin.model.KspRegionType;
import io.vertigo.chroma.kspplugin.model.WordSelectionType;

import java.util.function.Predicate;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPartitioningException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextSelection;

/**
 * M�thodes utilitaires pour manipuler des documents.
 */
public final class DocumentUtils {

	private DocumentUtils() {
		// RAS.
	}

	/**
	 * Renvoie le mot courant � partir d'une s�lection dans un document.
	 * 
	 * @param document Document.
	 * @param selection S�lection. Peut �tre vide.
	 * @param wordSelectionType Type de s�lection.
	 * @return Mot s�lectionn�, <code>null</code> si aucun mot trouv�.
	 */
	public static ITextSelection findCurrentWord(IDocument document, ITextSelection selection, WordSelectionType wordSelectionType) {
		return new CurrentWordFinder(document, selection, wordSelectionType).find();
	}

	/**
	 * Indique si l'offset d'un document donn� se trouve dans une r�gion donn�e.
	 * 
	 * @param document Document.
	 * @param offset Offset.
	 * @param contentType Content type de la r�gion.
	 * @return
	 */
	public static boolean isContentType(IDocument document, int offset, KspRegionType regionType) {
		try {
			/* Extrait le type de la partition. */
			IDocumentExtension3 de3 = (IDocumentExtension3) document;
			String regionContentType = de3.getContentType(KspRegionType.PARTITIONING, offset, true);

			/* V�rifie que la r�gion correspond. */
			return regionType.getContentType().equals(regionContentType);
		} catch (BadLocationException | BadPartitioningException e) {
			ErrorUtils.handle(e);
		}

		return false;
	}

	/**
	 * Indique si une s�lection d'un document repr�sente exactement une r�gion de string (aux double quote pr�s).
	 * 
	 * @param document Document.
	 * @param selection S�lection de texte.
	 * @return <code>true</code> si c'est exactement une r�gion de string.
	 */
	public static boolean isExactKspString(IDocument document, ITextSelection selection) {
		IDocumentExtension3 extension = (IDocumentExtension3) document;
		try {
			/* Charge les r�gions du document couverte par la s�lecion. */
			ITypedRegion[] regions = extension.computePartitioning(KspRegionType.PARTITIONING, selection.getOffset(), selection.getLength(), false);

			/* V�rifie qu'on a une seule r�gion. */
			if (regions.length != 1) {
				return false;
			}

			/* Charge la r�gion enti�re */
			ITypedRegion region = extension.getPartition(KspRegionType.PARTITIONING, selection.getOffset(), false);

			/* V�rifie que c'est une r�gion de string KSP. */
			if (!region.getType().equals(KspRegionType.STRING.getContentType())) {
				return false;
			}

			/* V�rifie que la r�gion couvre exactement la s�lection */
			int selectionWithQuoteOffset = selection.getOffset() - 1; // Prend en compte la double quote ouvrante.
			int selectionWithQuoteLength = selection.getLength() + 2; // Prend en compte les deux double quote.
			if (region.getOffset() == selectionWithQuoteOffset && region.getLength() == selectionWithQuoteLength) {
				return true;
			}
		} catch (BadLocationException | BadPartitioningException e) {
			ErrorUtils.handle(e);
		}

		return false;
	}

	/**
	 * Classe pour trouver le mot courant dans un document.
	 */
	private static class CurrentWordFinder {

		private final IDocument document;
		private final ITextSelection selection;
		private final Predicate<String> wordTest;
		private final StringBuilder builder = new StringBuilder();
		private int currentOffset;
		private int currentLength;

		/**
		 * Cr�� une nouvelle instance de CurrentWordFinder.
		 * 
		 * @param document Document.
		 * @param selection S�lection dans le document.
		 * @param wordSelectionType Type de s�lection.
		 */
		public CurrentWordFinder(IDocument document, ITextSelection selection, WordSelectionType wordSelectionType) {
			this.document = document;
			this.selection = selection;
			this.wordTest = wordSelectionType.getTester();
		}

		/**
		 * Trouve le mot courant.
		 * 
		 * @return
		 */
		public ITextSelection find() {

			/* Initialise un buffer avec la s�lection. */
			String initialSelection = selection.getText();
			if (!initialSelection.isEmpty() && !wordTest.test(initialSelection)) {
				return null;
			}
			builder.append(initialSelection);

			/* Initialise le parcourt du document. */
			currentOffset = selection.getOffset();
			currentLength = selection.getLength();

			/* Trouve le d�but du mot. */
			findWordStart();

			/* Trouve la fin du mot. */
			findWordEnd();

			/* Aucun mot s�lectionn� : on renvoie null. */
			if (builder.length() == 0) {
				return null;
			}

			/* Renvoie de la s�lection. */
			return new TextSelection(document, currentOffset, currentLength);
		}

		private void findWordStart() {
			/* On se place au d�but de la s�lection initiale. */
			int offset = selection.getOffset();
			/* On parcourt les caract�res vers la gauche. */
			while (true) { // NOSONAR
				offset--;
				try {
					/* Obtention du caract�re. */
					String currentChar = document.get(offset, 1);
					if (wordTest.test(currentChar)) {
						/* Test ok : on l'insert au d�but du mot courant. */
						builder.insert(0, currentChar);
						/* On met � jour les coordonn�es du mot courant. */
						currentOffset--;
						currentLength++;
					} else {
						/* Test ko : le d�but du mot est atteint. */
						break;
					}
				} catch (BadLocationException e) { // NOSONAR
					break;
				}
			}
		}

		private void findWordEnd() {
			/* On se place � la fin de la s�lection initiale. */
			int offset = selection.getOffset() + selection.getLength() - 1;
			/* On parcourt les caract�res vers la droite. */
			while (true) { // NOSONAR
				offset++;
				try {
					/* Obtention du caract�re. */
					String currentChar = document.get(offset, 1);
					if (wordTest.test(currentChar)) {
						/* Test ok : on l'insert � la fin du mot courant. */
						builder.append(currentChar);
						/* On met � jour les coordonn�es du mot courant. */
						currentLength++;
					} else {
						/* Test ko : la fin du mot est atteint. */
						break;
					}
				} catch (BadLocationException e) { // NOSONAR
					break;
				}
			}
		}
	}
}
