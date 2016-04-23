package edu.squat.headlesskamp;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.repository.RequiredRole;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.system.SystemPackage;

import de.uka.ipd.sdq.componentInternalDependencies.ComponentInternalDependenciesPackage;
import de.uka.ipd.sdq.componentInternalDependencies.ComponentInternalDependencyRepository;
import de.uka.ipd.sdq.componentInternalDependencies.RoleToRoleDependency;
import de.uka.ipd.sdq.internalmodificationmark.InternalmodificationmarkPackage;
import edu.kit.ipd.sdq.kamp.core.ArchitectureModelFactoryFacade;
import edu.kit.ipd.sdq.kamp.core.ArchitectureModelLookup;
import edu.kit.ipd.sdq.kamp.core.ArchitectureVersion;
import edu.kit.ipd.sdq.kamp.core.ChangePropagationAnalysis;
import edu.kit.ipd.sdq.kamp.model.fieldofactivityannotations.FieldOfActivityAnnotationRepository;
import edu.kit.ipd.sdq.kamp.model.modificationmarks.ModificationRepository;

public class HeadlessKAMPLauncher {
	public static String dirPath = "file:////Users/alejandrorago/Documents/Implementacion/Repositorios/kamp-test/MediaStore3-Nightly/Model/MediaStore3_Model/";
	
	static {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		//PCM Packages
		EPackage.Registry.INSTANCE.put(RepositoryPackage.eNS_URI, RepositoryPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(ResourceenvironmentPackage.eNS_URI, ResourceenvironmentPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(SystemPackage.eNS_URI, SystemPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(AllocationPackage.eNS_URI, AllocationPackage.eINSTANCE);
		//KAMP Packages
		EPackage.Registry.INSTANCE.put(ComponentInternalDependenciesPackage.eNS_URI, ComponentInternalDependenciesPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(de.uka.ipd.sdq.fieldOfActivityAnnotations.FieldOfActivityAnnotationsPackage.eNS_URI, de.uka.ipd.sdq.fieldOfActivityAnnotations.FieldOfActivityAnnotationsPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(InternalmodificationmarkPackage.eNS_URI, InternalmodificationmarkPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(edu.kit.ipd.sdq.kamp.model.fieldofactivityannotations.FieldofactivityannotationsPackage.eNS_URI, edu.kit.ipd.sdq.kamp.model.fieldofactivityannotations.FieldofactivityannotationsPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(edu.kit.ipd.sdq.kamp.model.modificationmarks.modificationmarksPackage.eNS_URI, edu.kit.ipd.sdq.kamp.model.modificationmarks.modificationmarksPackage.eINSTANCE);
	}

	private Repository repository;
	private ResourceEnvironment resourceEnvironment;
	private System baseSystem;
	private Allocation allocation;
	
	private EObject load(String inputString) {
		URI resourceURI = URI.createURI(inputString);
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.getResource(resourceURI, true);
		EObject content = resource.getContents().get(0);
		return content;
	}
	
	private void loadPCM() {
		String repositoryFile = dirPath + "ms.repository";
		String resourceEnvFile = dirPath + "ms.resourceenvironment";
		String baseSystemFile = dirPath + "ms_base.system";
		String baseAllocationFile = dirPath + "ms_base.allocation";
		repository = (Repository) this.load(repositoryFile);
		resourceEnvironment = (ResourceEnvironment) this.load(resourceEnvFile);
		baseSystem = (System) this.load(baseSystemFile);
		allocation = (Allocation) this.load(baseAllocationFile);
	}
	
	public void invokeKAMP() {
		// Loading original PCM models
		this.loadPCM();
		// Setting up KAMP additional models (empty)
		FieldOfActivityAnnotationRepository fieldOfActivityRepository = ArchitectureModelFactoryFacade.createFieldOfActivityAnnotationsRepository();
		ModificationRepository internalModificationMarkRepository = ArchitectureModelFactoryFacade.createModificationMarkRepository();
		ComponentInternalDependencyRepository componentInternalDependencyRepository = ArchitectureModelFactoryFacade.createComponentInternalDependencyRepository();
		ArchitectureVersion archVersion = new ArchitectureVersion("MediaStore3", 
				repository, baseSystem, 
				fieldOfActivityRepository, internalModificationMarkRepository, componentInternalDependencyRepository);
		// Executing the propagation algorithm
		ChangePropagationAnalysis cpa = new ChangePropagationAnalysis();
		cpa.runChangePropagationAnalysis(archVersion);
		// Test the results
		//List<RequiredRole> requiredRoles = cpa.calculateInterComponentPropagation(baseSystem.getProvidedRoles_InterfaceProvidingEntity(), archVersion);
		//List<ProvidedRole> providedRoles = cpa.calculateIntraComponentPropagation(baseSystem.getRequiredRoles_InterfaceRequiringEntity(), archVersion);

		RepositoryComponent bestellserver = ArchitectureModelLookup.lookUpComponentByName(archVersion, "DownloadLoadBalancer");
		List<RoleToRoleDependency> internalDependencies = ArchitectureModelLookup.lookUpComponentInternalDependenciesForComponent(archVersion, (BasicComponent) bestellserver);
		java.lang.System.out.println("Number of internal dependencies: " + internalDependencies.size());
		List<AssemblyConnector> assemblyConnectors = ArchitectureModelLookup.lookUpMarkedAssemblyConnectors(archVersion);
		java.lang.System.out.println("Number of marked assembly connectors: " + assemblyConnectors.size());
	}
	
	public static void main(String[] args) {
		HeadlessKAMPLauncher launcher = new HeadlessKAMPLauncher();
		launcher.invokeKAMP();
	}
}