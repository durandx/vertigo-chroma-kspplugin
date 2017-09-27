package io.vertigo.chroma.kspplugin.ui.editors.analysis;

import io.vertigo.chroma.kspplugin.model.FileRegion;
import io.vertigo.chroma.kspplugin.model.KspDeclarationMainParts;
import io.vertigo.chroma.kspplugin.model.KspRegionType;
import io.vertigo.chroma.kspplugin.utils.DocumentUtils;
import io.vertigo.chroma.kspplugin.utils.ErrorUtils;
import io.vertigo.chroma.kspplugin.utils.KspStringUtils;
import io.vertigo.chroma.kspplugin.utils.MarkerUtils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;

/**
 * Inspecteur de d�claration KSP.
 * <p>
 * V�rifie les r�gles de grammaire et g�n�res des marqueurs de probl�mes.
 * </p>
 */
public class KspDeclarationChecker {

	private IFile file;
	private final IDocument document;
	private static final OpenCloseCouple CURLY_BRACE_COUPLE = new OpenCloseCurlyBrace();
	private static final OpenCloseCouple PARENTHESIS_COUPLE = new OpenCloseParenthesis();
	private static final OpenCloseCouple JAVA_TAG_COUPLE = new OpenCloseJavaTag();
	private final SortedMap<Integer, OpenCloseCharacterOccurence> occurences = new TreeMap<>();

	/**
	 * Cr�� une nouvelle instance de KspDeclarationChecker.
	 * 
	 * @param document Document du KSP.
	 * @param file Fichier du KSP.
	 */
	public KspDeclarationChecker(IDocument document, IFile file) {
		this.file = file;
		this.document = document;
	}

	/**
	 * Tente de cr�er un inspecteur de d�claration KSP si la ligne en contient une.
	 * 
	 * @param file Fichier KSP.
	 * @param document Document du fichier.
	 * @param lineIdx Index en base z�ro de la ligne du document.
	 * @return Inspecteur si pr�sence de d�claration KSP, <code>null</code> sinon.
	 */
	public static KspDeclarationChecker extractChecker(IFile file, IDocument document, int lineIdx) {
		try {
			/* Extrait la ligne du document. */
			IRegion lineInformation = document.getLineInformation(lineIdx);
			String lineContent = document.get(lineInformation.getOffset(), lineInformation.getLength());

			/* Extrait une d�claration KSP. */
			KspDeclarationMainParts declarationParts = KspStringUtils.getVertigoKspDeclarationParts(lineContent);
			if (declarationParts != null) {
				/* Calcule la r�gion du nom de la d�claration KSP */
				String name = declarationParts.getConstantCaseName();
				int fullNameLineOffSet = lineContent.indexOf(name);
				int taskNameOffSet = lineInformation.getOffset() + fullNameLineOffSet;

				/* V�rifie qu'on est dans une r�gion standard */
				/* Permet d'ignorer le contenu des string et des commentaires KSP. */
				if (!DocumentUtils.isContentType(document, taskNameOffSet, KspRegionType.DEFAULT)) {
					return null;
				}

				/* Retourne un inspecteur */
				return new KspDeclarationChecker(document, file);
			}
		} catch (BadLocationException e) {
			ErrorUtils.handle(e);
		}

		/* Pas de d�claration KSP : on retourne null */
		return null;
	}

	/**
	 * Inspecte la ligne du document.
	 * 
	 * @param lineIdx Index en base z�ro de la ligne.
	 */
	public void inspectLine(int lineIdx) {
		new LineInspector(lineIdx).inspectLine();
	}

	/**
	 * G�n�re les marqueurs pour la d�claration courante.
	 */
	public void generateMarkers() {
		generateMarkers(CURLY_BRACE_COUPLE);
		generateMarkers(PARENTHESIS_COUPLE);
		generateMarkers(JAVA_TAG_COUPLE);
	}

	/**
	 * G�n�re les marqueurs pour les probl�mes li�s � un couple de caract�res ouvrants fermants donn�.
	 * 
	 * @param couple Couple de caract�res.
	 */
	private void generateMarkers(OpenCloseCouple couple) {
		/* Pile des caract�res ouvrants */
		Deque<OpenCloseCharacterOccurence> openStack = new ArrayDeque<>();

		/* Parcourt les occurences des caract�res. */
		for (OpenCloseCharacterOccurence occurence : occurences.values()) {
			if (occurence.getOpenCloseCharacter() == couple.getOpenCharacter()) {
				/* Ajoute � la stack des caract�res ouvrants. */
				openStack.add(occurence);
			} else if (occurence.getOpenCloseCharacter() == couple.getCloseCharacter()) {
				if (openStack.isEmpty()) {
					/* Aucun caract�re ouvrant � fermer : erreur */
					addMarker(occurence, couple.getMissingOpeningMessage());
				} else {
					/* Enl�ve le dernier caract�re ouvrant. */
					openStack.pop();
				}
			}
		}

		if (!openStack.isEmpty()) {
			/* Il reste des caract�res ouvrants non ferm�s : erreur */
			for (OpenCloseCharacterOccurence occurence : openStack) {
				addMarker(occurence, couple.getMissingClosingMessage());
			}
		}
	}

	/**
	 * Ajoute un marqueur sur une occurence de aract�re avec un message donn�.
	 * 
	 * @param occurence Occurence de caract�re.
	 * @param message Message.
	 */
	private void addMarker(OpenCloseCharacterOccurence occurence, String message) {
		MarkerUtils.addKspMarker(occurence.getFileRegion(), message, IMarker.SEVERITY_ERROR);
	}

	/**
	 * Inspecteur d'une ligne de d�claration.
	 */
	private class LineInspector {

		private final int lineIdx;
		private IRegion lineInformation;
		private String lineContent;

		/**
		 * Cr�� une nouvelle instance de LineInspector.
		 * 
		 * @param lineIdx Index en base z�ro de la ligne dans le document.
		 */
		public LineInspector(int lineIdx) {
			this.lineIdx = lineIdx;
		}

		/**
		 * Inspecte la ligne.
		 * <p>
		 * Note la pr�sence des caract�res ouvrants et fermants.
		 * </p>
		 */
		public void inspectLine() {
			try {
				/* Obtient la ligne. */
				lineInformation = document.getLineInformation(lineIdx);
				lineContent = document.get(lineInformation.getOffset(), lineInformation.getLength());
				/* R�gion standard : accolades et parenth�ses. */
				checkCharacterCouple(CURLY_BRACE_COUPLE, KspRegionType.DEFAULT);
				checkCharacterCouple(PARENTHESIS_COUPLE, KspRegionType.DEFAULT);
				/* R�gion string SQL : tags Java. */
				checkCharacterCouple(JAVA_TAG_COUPLE, KspRegionType.STRING);
			} catch (BadLocationException e) {
				ErrorUtils.handle(e);
			}
		}

		/**
		 * V�rifie la pr�sence d'un couple de caract�re dans un type de r�gion donn�.
		 * 
		 * @param couple Couple de caract�res.
		 * @param regionType Type de la r�gion.
		 */
		private void checkCharacterCouple(OpenCloseCouple couple, KspRegionType regionType) {
			checkCharacter(couple.getOpenCharacter(), regionType);
			checkCharacter(couple.getCloseCharacter(), regionType);
		}

		/**
		 * V�rifie la pr�sence des caract�res dans un type de r�gion donn�.
		 * 
		 * @param openCloseCharacter Caract�re � chercher.
		 * @param regionType Type de r�gion cibl�.
		 */
		private void checkCharacter(OpenCloseCharacter openCloseCharacter, KspRegionType regionType) {
			/* Recherche le caract�re � partir de son pattern. */
			Pattern pattern = openCloseCharacter.getPattern();
			Matcher matcher = pattern.matcher(lineContent);

			/* Parcourt les r�sultats. */
			while (matcher.find()) {
				for (int group = 1; group <= matcher.groupCount(); group++) {
					/* Construit la r�gion de fichier du caract�re trouv� */
					String characterValue = matcher.group(group);
					int characterLineOffSet = matcher.start(group);
					int characterDocumentOffSet = lineInformation.getOffset() + characterLineOffSet;

					/* V�rifie la r�gion du caract�re trouv�. */
					if (!DocumentUtils.isContentType(document, characterDocumentOffSet, regionType)) {
						return;
					}

					/* Note le caract�re. */
					FileRegion characterRegion = new FileRegion(file, characterDocumentOffSet, characterValue.length(), lineIdx);
					OpenCloseCharacterOccurence occurence = new OpenCloseCharacterOccurence(openCloseCharacter, characterRegion);
					occurences.put(characterDocumentOffSet, occurence);
				}
			}
		}
	}

	/**
	 * Occurence d'un caract�re � une r�gion de fichier donn�.
	 */
	private static class OpenCloseCharacterOccurence {

		private final OpenCloseCharacter openCloseCharacter;
		private final FileRegion fileRegion;

		/**
		 * Cr�� une nouvelle instance de OpenCloseCharacterOccurence.
		 * 
		 * @param openCloseCharacter Caract�re.
		 * @param fileRegion R�gion de fichier du caract�re.
		 */
		public OpenCloseCharacterOccurence(OpenCloseCharacter openCloseCharacter, FileRegion fileRegion) {
			this.openCloseCharacter = openCloseCharacter;
			this.fileRegion = fileRegion;
		}

		/**
		 * @return Le caract�re.
		 */
		public OpenCloseCharacter getOpenCloseCharacter() {
			return openCloseCharacter;
		}

		/**
		 * @return La r�gion de fichier.
		 */
		public FileRegion getFileRegion() {
			return fileRegion;
		}
	}
}
