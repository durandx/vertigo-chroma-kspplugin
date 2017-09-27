package io.vertigo.chroma.kspplugin.model;

/**
 * Candidat pour une autocompl�tion.
 *
 */
public class CompletionCandidate {

	private final String displayString;
	private final String additionalProposalInfo;

	/**
	 * Cr�� une nouvelle instance de CompletionCandidate.
	 * 
	 * @param displayString String � afficher dans la liste de choix.
	 * @param additionalProposalInfo Information suppl�mentaire affich� � la s�lection.
	 */
	public CompletionCandidate(String displayString, String additionalProposalInfo) {
		this.displayString = displayString;
		this.additionalProposalInfo = additionalProposalInfo;
	}

	public String getDisplayString() {
		return displayString;
	}

	public String getAdditionalProposalInfo() {
		return additionalProposalInfo;
	}
}
