package io.vertigo.chroma.kspplugin.legacy;

import io.vertigo.chroma.kspplugin.model.DtoField;
import io.vertigo.chroma.kspplugin.model.DtoReferencePattern;
import io.vertigo.chroma.kspplugin.model.KspAttribute;
import io.vertigo.chroma.kspplugin.model.KspDeclarationParts;
import io.vertigo.chroma.kspplugin.model.KspNature;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IType;

/**
 * Contrat des strat�gies de gestion d'un framework Vertigo/Kasper donn�.
 */
public interface LegacyStrategy {

	/**
	 * Analyse une ligne de document KSP et renvoie la description d'une d�claration KSP si elle en contient une.
	 * 
	 * @param lineContent Contenu de la ligne du document KSP.
	 * @return Description de la d�claration.
	 */
	KspDeclarationParts getKspDeclarationParts(String lineContent);

	/**
	 * Obtient le nom Java pour un nom en constant case d'une d�claration KSP d'une nature donn�e.
	 * 
	 * @param constantCaseNameOnly Nom en constant case.
	 * @param nature Nature de la d�claration.
	 * @return Nom Java.
	 */
	String getKspDeclarationJavaName(String constantCaseNameOnly, String nature);

	/**
	 * Analyse une ligne de document KSP et renvoie un attribut KSP si elle en contient un.
	 * 
	 * @param lineContent Contenu de la ligne du document KSP.
	 * @return Attribut KSP.
	 */
	KspAttribute getKspAttribute(String lineContent);

	/**
	 * Indique si un type est Java est un DTO.
	 * 
	 * @param type Type Java.
	 * @return <code>true</code> si c'est un DTO.
	 */
	boolean isDtoType(IType type);

	/**
	 * Indique si un fichier est candidat pour �tre un DTO.
	 * 
	 * @param file Fichier.
	 * @return <code>true</code> si c'est un candidat.
	 */
	boolean isDtoCandidate(IFile file);

	/**
	 * Extrait la liste des champs d'un DTO.
	 * 
	 * @param type Type JDT.
	 * @return Liste des champs.
	 */
	List<DtoField> parseDtoFields(IType type);

	/**
	 * Indique si un fichier est candidat pour �tre une impl�mentation de service m�tier.
	 * 
	 * @param file Fichier.
	 * @return <code>true</code> si c'est un candidat.
	 */
	boolean isServiceCandidate(IFile file);

	/**
	 * Indique si un fichier est candidat pour �tre un DAO/PAO.
	 * 
	 * @param file Fichier.
	 * @return <code>true</code> si c'est un candidat.
	 */
	boolean isDaoCandidate(IFile file);

	/**
	 * Obtient le mot-cl� KSP pour une nature de d�claration donn�e.
	 * 
	 * @param kspNature Nature.
	 * @return Mot-cl�.
	 */
	String getKspKeyword(KspNature kspNature);

	/**
	 * Obtient le pattern utilis� pour r�f�rencer un DTO dans un KSP.
	 * 
	 * @return Pattern.
	 */
	DtoReferencePattern getDtoReferenceSyntaxe();
}
