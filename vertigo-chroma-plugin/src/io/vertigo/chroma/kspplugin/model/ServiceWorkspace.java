package io.vertigo.chroma.kspplugin.model;

import java.util.List;

/**
 * Stocke l'ensemble des m�thodes d'impl�mentation de service m�tier de tout un workspace.
 */
public class ServiceWorkspace {

	private final List<ServiceImplementation> serviceImplementations;

	public ServiceWorkspace(List<ServiceImplementation> serviceImplementations) {
		this.serviceImplementations = serviceImplementations;
	}

	public List<ServiceImplementation> getServiceImplementations() {
		return serviceImplementations;
	}
}
