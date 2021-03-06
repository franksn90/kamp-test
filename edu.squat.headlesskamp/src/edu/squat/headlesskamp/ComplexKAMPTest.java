package edu.squat.headlesskamp;

import java.util.List;

import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.repository.RequiredRole;

import de.uka.ipd.sdq.componentInternalDependencies.RoleToRoleDependency;
import edu.kit.ipd.sdq.kamp.core.Activity;
import edu.kit.ipd.sdq.kamp.core.ArchitectureAnnotationFactory;
import edu.kit.ipd.sdq.kamp.core.ArchitectureModelFactoryFacade;
import edu.kit.ipd.sdq.kamp.core.ArchitectureModelLookup;
import edu.kit.ipd.sdq.kamp.core.ArchitectureVersion;
import edu.kit.ipd.sdq.kamp.core.ChangePropagationAnalysis;
import edu.kit.ipd.sdq.kamp.core.derivation.DifferenceCalculation;

public class ComplexKAMPTest extends AbstractKAMPTest {
	
	@Override
	public String getInitialModelName() {
		return "TraderCustomerFinancialServicesDatabase";
	}
	
	@Override
	public String getChangeScenarioName() {
		return "ModifyDatabaseSchema";
	}

	@Override
	public ArchitectureVersion setupInitialModel(String initialArchitectureName) {
		ArchitectureVersion baseAV = ArchitectureModelFactoryFacade.createEmptyModel(initialArchitectureName);
		// base model - repository 
		OperationInterface traderInterface = ArchitectureModelFactoryFacade.createInterface(baseAV, "Trader-Interface");
		OperationInterface customerInterface = ArchitectureModelFactoryFacade.createInterface(baseAV, "Customer-Interface");
		OperationInterface traderServerInterface = ArchitectureModelFactoryFacade.createInterface(baseAV, "Trader-Server-Interface");
		OperationInterface customerServerInterface = ArchitectureModelFactoryFacade.createInterface(baseAV, "Customer-Server-Interface");
		OperationInterface financesInterface = ArchitectureModelFactoryFacade.createInterface(baseAV, "FinancialServices-Interface");
		OperationInterface odbcInterface = ArchitectureModelFactoryFacade.createInterface(baseAV, "ODBC-Interface");

		BasicComponent traderClientComponent = ArchitectureModelFactoryFacade.createBasicComponent(baseAV, "Trader-Client");
		BasicComponent customerClientComponent = ArchitectureModelFactoryFacade.createBasicComponent(baseAV, "Customer-Client");
		BasicComponent orderingServerComponent = ArchitectureModelFactoryFacade.createBasicComponent(baseAV, "Ordering-Server");
		BasicComponent databaseComponent = ArchitectureModelFactoryFacade.createBasicComponent(baseAV, "Database");
		BasicComponent financesComponent = ArchitectureModelFactoryFacade.createBasicComponent(baseAV, "FinancialServices");
		
		ProvidedRole traderClient_traderInterfacePR = ArchitectureModelFactoryFacade.createProvidedRole(traderClientComponent, traderInterface);
		RequiredRole traderClient_traderInterfaceRR = ArchitectureModelFactoryFacade.createRequiredRole(traderClientComponent, traderServerInterface);

		ProvidedRole customerClient_customerInterfacePR = ArchitectureModelFactoryFacade.createProvidedRole(customerClientComponent, customerInterface);
		RequiredRole customerClient_customerInterfaceRR = ArchitectureModelFactoryFacade.createRequiredRole(customerClientComponent, customerServerInterface);

		ProvidedRole orderingServer_TraderServerInterfacePR = ArchitectureModelFactoryFacade.createProvidedRole(orderingServerComponent, traderServerInterface);
		ProvidedRole orderingServer_CustomerServerInterfacePR = ArchitectureModelFactoryFacade.createProvidedRole(orderingServerComponent, customerServerInterface);
		RequiredRole orderingServer_financesInterfaceRR = ArchitectureModelFactoryFacade.createRequiredRole(orderingServerComponent, financesInterface);
		RequiredRole orderingServer_odbcInterfaceRR = ArchitectureModelFactoryFacade.createRequiredRole(orderingServerComponent, odbcInterface);

		ArchitectureModelFactoryFacade.createProvidedRole(databaseComponent, odbcInterface);
		ArchitectureModelFactoryFacade.createProvidedRole(financesComponent, financesInterface);

		// base model - system
		ArchitectureModelFactoryFacade.createAssemblyContext(traderClientComponent, baseAV);
		ArchitectureModelFactoryFacade.createAssemblyContext(customerClientComponent, baseAV);
		ArchitectureModelFactoryFacade.createAssemblyContext(orderingServerComponent, baseAV);
		ArchitectureModelFactoryFacade.createAssemblyContext(databaseComponent, baseAV);
		ArchitectureModelFactoryFacade.createAssemblyContext(financesComponent, baseAV);
		
		AssemblyConnector traderClient_orderingServerConnector = ArchitectureModelFactoryFacade.createAssemblyConnector(traderClientComponent, orderingServerComponent, baseAV);
		AssemblyConnector customerClient_orderingServerConnector = ArchitectureModelFactoryFacade.createAssemblyConnector(customerClientComponent, orderingServerComponent, baseAV);
		AssemblyConnector orderingServer_datenbaseConnector = ArchitectureModelFactoryFacade.createAssemblyConnector(orderingServerComponent, databaseComponent, baseAV);
		AssemblyConnector orderingServer_financesConnector = ArchitectureModelFactoryFacade.createAssemblyConnector(orderingServerComponent, financesComponent, baseAV);
		
		// component internal dependencies
		ArchitectureModelFactoryFacade.setupComponentInternalDependenciesPessimistic(baseAV);
		ArchitectureModelFactoryFacade.createComponentInternalDependency(baseAV, traderClient_traderInterfacePR, traderClient_traderInterfaceRR);
		ArchitectureModelFactoryFacade.createComponentInternalDependency(baseAV, customerClient_customerInterfacePR, customerClient_customerInterfaceRR);
		ArchitectureModelFactoryFacade.createComponentInternalDependency(baseAV, orderingServer_TraderServerInterfacePR, orderingServer_financesInterfaceRR);
		ArchitectureModelFactoryFacade.createComponentInternalDependency(baseAV, orderingServer_TraderServerInterfacePR, orderingServer_odbcInterfaceRR);
		ArchitectureModelFactoryFacade.createComponentInternalDependency(baseAV, orderingServer_CustomerServerInterfacePR, orderingServer_odbcInterfaceRR);

		// model annotations 
		
		// development artefacts
		ArchitectureAnnotationFactory.createSourceFileAggregationAnnotation(baseAV, customerClientComponent, 120, "JavaScript");
		ArchitectureAnnotationFactory.createSourceFileAggregationAnnotation(baseAV, traderClientComponent, 130, "Java");
		ArchitectureAnnotationFactory.createSourceFileAggregationAnnotation(baseAV, orderingServerComponent, 152, "PHP");
		ArchitectureAnnotationFactory.createMetadataFileAnnotation(baseAV, customerClientComponent, "DatabaseSchema", "SQL-DDL");
		
		// testing
		ArchitectureAnnotationFactory.createUnitTestAggregation(baseAV, 
				customerClientComponent.getProvidedRoles_InterfaceProvidingEntity().get(0), 30, "");
		ArchitectureAnnotationFactory.createUnitTestAggregation(baseAV, 
				traderClientComponent.getProvidedRoles_InterfaceProvidingEntity().get(0), 33, "");
		ArchitectureAnnotationFactory.createUnitTestAggregation(baseAV, 
				orderingServerComponent.getProvidedRoles_InterfaceProvidingEntity().get(0), 50, "");
		ArchitectureAnnotationFactory.createIntegrationTestAggregation(baseAV, customerClient_orderingServerConnector, 35, "");
		ArchitectureAnnotationFactory.createIntegrationTestAggregation(baseAV, traderClient_orderingServerConnector, 33, "");
		ArchitectureAnnotationFactory.createIntegrationTestAggregation(baseAV, orderingServer_financesConnector, 41, "");
		ArchitectureAnnotationFactory.createIntegrationTestAggregation(baseAV, orderingServer_datenbaseConnector, 28, "");

		ArchitectureAnnotationFactory.createAcceptanceTestAggregation(baseAV, customerClientComponent.getProvidedRoles_InterfaceProvidingEntity().get(0), 38, "");
		ArchitectureAnnotationFactory.createAcceptanceTestAggregation(baseAV, traderClientComponent.getProvidedRoles_InterfaceProvidingEntity().get(0), 42, "");
		
		// building
		ArchitectureAnnotationFactory.createBuildConfiguration(baseAV, new RepositoryComponent[] {customerClientComponent, traderClientComponent, orderingServerComponent}, "build.xml", "");

		// distribution/release management	
		ArchitectureAnnotationFactory.createReleaseConfiguration(baseAV, new RepositoryComponent[] {customerClientComponent}, "", "Install-capable Customer-Client");
		ArchitectureAnnotationFactory.createReleaseConfiguration(baseAV, new RepositoryComponent[] {traderClientComponent}, "", "Install-capable Trader-Client");
		ArchitectureAnnotationFactory.createReleaseConfiguration(baseAV, new RepositoryComponent[] {orderingServerComponent}, "", "Install-capable Ordering-Server");
		
		// deployment
		ArchitectureAnnotationFactory.createRuntimeInstanceAggregation(baseAV, new RepositoryComponent[] {customerClientComponent}, 1000, "");
		ArchitectureAnnotationFactory.createRuntimeInstanceAggregation(baseAV, new RepositoryComponent[] {traderClientComponent}, 10, "");
		ArchitectureAnnotationFactory.createRuntimeInstanceAggregation(baseAV, new RepositoryComponent[] {orderingServerComponent}, 1, "");
		ArchitectureAnnotationFactory.createRuntimeInstanceAggregation(baseAV, new RepositoryComponent[] {databaseComponent}, 1, "");
		
		// staff assignment
		//ArchitectureAnnotationFactory.createStaffAssignmentDeveloper(baseAV, )
		
		return baseAV;
	}
	
	@Override
	public ArchitectureVersion setupChangedModel(String changeScenarioName) {
		ArchitectureVersion changedAV = KAMPHelper.createArchitectureVersionClone(baseAV, changeScenarioName);
		
		RepositoryComponent databaseComponent = ArchitectureModelLookup.lookUpComponentByName(changedAV, "Database");
		ProvidedRole providedRoleOfDatabase = databaseComponent.getProvidedRoles_InterfaceProvidingEntity().get(0);

		ArchitectureModelFactoryFacade.assignInternalModificationMarkToProvidedRoleOfComponent(changedAV, providedRoleOfDatabase);
		return changedAV;
	}
	
	@Override
	public void runChangeAnalysis() {
		ChangePropagationAnalysis changePropagationAnalysis = new ChangePropagationAnalysis();
		changePropagationAnalysis.runChangePropagationAnalysis(changedAV);

		RepositoryComponent orderingServerComponent = ArchitectureModelLookup.lookUpComponentByName(changedAV, "Ordering-Server");
		
		ChangePropagationAnalysis cia = new ChangePropagationAnalysis();
		cia.runChangePropagationAnalysis(changedAV);
		
		@SuppressWarnings("unused")
		List<RoleToRoleDependency> internalDependencies = ArchitectureModelLookup.lookUpComponentInternalDependenciesForComponent(changedAV, (BasicComponent) orderingServerComponent);
		System.out.println("Number of marked assembly connectors must not be three -> " + ArchitectureModelLookup.lookUpMarkedAssemblyConnectors(changedAV).size());
		
		List<Activity> baseActivityListAfterChangePropagationAnalysis = DifferenceCalculation.deriveWorkplan(baseAV, changedAV);
		System.out.println("No activities detected ->" + !baseActivityListAfterChangePropagationAnalysis.isEmpty());
		
		KAMPHelper.printActivities(baseActivityListAfterChangePropagationAnalysis, "Top:");
	}
	
	@Override
	public void runTest() {
		try {
			baseAV = this.setupInitialModel(this.getInitialModelName());
			changedAV = this.setupChangedModel(this.getChangeScenarioName());
			this.deriveWorkplan();
			this.runChangeAnalysis();
			this.tearDown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		ComplexKAMPTest testNew = new ComplexKAMPTest();
		testNew.runTest();
	}
}
