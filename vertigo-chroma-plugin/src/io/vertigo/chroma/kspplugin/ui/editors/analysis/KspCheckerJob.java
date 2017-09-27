package io.vertigo.chroma.kspplugin.ui.editors.analysis;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * Job de v�rification d'un fichier Ksp. L'encapsulation dans un WorkspaceJob permet d'ex�cuter la v�rification de mani�re asynchrone et sans acc�s concurrent
 * aux ressources du projet.
 */
public final class KspCheckerJob extends WorkspaceJob {

	private final IFile file;

	/**
	 * Cr�� une nouvelle instance de KspCheckerJob.
	 * 
	 * @param file Fichier KSP.
	 */
	private KspCheckerJob(IFile file) {
		super("KspCheckerJob");
		this.file = file;
	}

	@Override
	public IStatus runInWorkspace(IProgressMonitor monitor) {
		new KspFileChecker(file).check();
		return Status.OK_STATUS;
	}

	/**
	 * D�marre une nouvelle instance du job pour un fichier donn�.
	 * 
	 * @param file Fichier KSP.
	 */
	public static void start(IFile file) {
		KspCheckerJob job = new KspCheckerJob(file);
		job.setRule(ResourcesPlugin.getWorkspace().getRoot());
		job.schedule();
	}
}
