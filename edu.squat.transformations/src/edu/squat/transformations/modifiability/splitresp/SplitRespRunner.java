package edu.squat.transformations.modifiability.splitresp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.henshin.interpreter.ApplicationMonitor;
import org.eclipse.emf.henshin.interpreter.EGraph;
import org.eclipse.emf.henshin.interpreter.Engine;
import org.eclipse.emf.henshin.interpreter.Match;
import org.eclipse.emf.henshin.interpreter.RuleApplication;
import org.eclipse.emf.henshin.interpreter.UnitApplication;
import org.eclipse.emf.henshin.interpreter.impl.EGraphImpl;
import org.eclipse.emf.henshin.interpreter.impl.EngineImpl;
import org.eclipse.emf.henshin.interpreter.impl.LoggingApplicationMonitor;
import org.eclipse.emf.henshin.interpreter.impl.RuleApplicationImpl;
import org.eclipse.emf.henshin.interpreter.impl.UnitApplicationImpl;
import org.eclipse.emf.henshin.model.HenshinFactory;
import org.eclipse.emf.henshin.model.HenshinPackage;
import org.eclipse.emf.henshin.model.LoopUnit;
import org.eclipse.emf.henshin.model.Parameter;
import org.eclipse.emf.henshin.model.ParameterKind;
import org.eclipse.emf.henshin.model.Rule;
import org.eclipse.emf.henshin.model.impl.LoopUnitImpl;
import org.eclipse.emf.henshin.model.impl.ParameterMappingImpl;
import org.eclipse.emf.henshin.trace.Trace;
import org.eclipse.emf.henshin.trace.TraceFactory;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;
import org.palladiosimulator.pcm.core.composition.Connector;
import org.palladiosimulator.pcm.core.composition.ProvidedDelegationConnector;
import org.palladiosimulator.pcm.parameter.VariableUsage;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.repository.PassiveResource;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RequiredRole;

import edu.squat.transformations.modifiability.PCMTransformerRunner;
import edu.squat.transformations.modifiability.RunnerHelper;
import edu.squat.transformations.modifiability.Tactic;

@SuppressWarnings("unused")
public class SplitRespRunner extends PCMTransformerRunner {
	private String PARAMETER_1stInterface = "A";
	private String PARAMETER_2ndInterface = "B";
	//
	private final static String TRACE_InitialInterface = "init-int";
	private final static String TRACE_DependentInterface = "dep-int";
	private final static String TRACE_SplitComponent = "split-comp";
	private final static String TRACE_SplitInterface = "split-int";
	private final static String TRACE_SEFFComponent = "seff-comp";
	private final static String TRACE_SEFFInterface = "seff-int";
	private final static String TRACE_ReconnectedComponent = "reconn-comp";
	private final static String TRACE_ReconnectedInterface = "reconn-int";
	private final static String TRACE_AssemblyContext = "assembly-cont";
	private final static String TRACE_AssemblyConnector = "assembly-conn";
	private final static String TRACE_AllocationContext = "allocation-cont";
	//
	private Engine engine;
	//Main model
	private Rule markComponents2Split;
	private Rule splitComp4Interface;
	private Rule migrateSEFF;
	private Rule rewireInterfaces;
	private Rule cloneInternalElements;
	//Glue model
	private Rule propagate2SystemAssemblyContext;
	private Rule propagate2SystemAssemblyConnectors;
	private Rule propagate2Allocation;
	//Cleaning
	private Rule cleanUpManual;
	//
	public SplitRespRunner() {
		super();
	}
	
	@Override
	public void loadRules() {
		markComponents2Split = (Rule) module.getUnit("markComponents2Split");
		//markComponents2Split.setCheckDangling(false);
		splitComp4Interface = (Rule) module.getUnit("splitComp4Interface");
		migrateSEFF = (Rule) module.getUnit("migrateSEFF");
		rewireInterfaces = (Rule) module.getUnit("rewireInterfaces");
		cloneInternalElements = (Rule) module.getUnit("cloneInternalElements");
		//
		propagate2SystemAssemblyContext = (Rule) module.getUnit("propagate2SystemAssemblyContext");
		propagate2SystemAssemblyConnectors = (Rule) module.getUnit("propagate2SystemAssemblyConnectors");
		propagate2Allocation = (Rule) module.getUnit("propagate2Allocation");
		//
		cleanUpManual = (Rule) module.getUnit("cleanUpManual");
	}

	public void run(boolean saveResult) {
		candidateTactics = new ArrayList<Tactic>();
		//Create and configure an engine
		engine = new EngineImpl();
		//engine.getOptions().put(engine.OPTION_CHECK_DANGLING, false);
		
		//Find all possible matches for applying the tactic by checking the first rule
		List<Match> matches = this.findMatchesFirstRule();
		//For all matchings
		for(Match match : matches) {
			//Run the first rule for each potential matching and create the traces
			RuleApplication app = this.configureFirstRule(graph, match);
			//If the transformation was successful continue with the other rules
			if(app.execute(monitor)) {
				//Creating a clone for avoiding undoing things
				EGraph tempGraph = graph.copy(null);
				BasicComponent seed = (BasicComponent) app.getResultParameterValue("seed");
				System.out.println("Successfully created the trace elements for splitting component: " + seed.getEntityName());
				//Configuring and executing the second rule for the 1st interface split
				this.runSecondRule(tempGraph, PARAMETER_1stInterface);
				//Configuring and executing the second rule for the 2nd interface split
				this.runSecondRule(tempGraph, PARAMETER_2ndInterface);
				//Configuring and executing the third rule for the 1st interface split
				this.runThirdRule(tempGraph, PARAMETER_1stInterface);
				//Configuring and executing the third rule for the 2nd interface split
				this.runThirdRule(tempGraph, PARAMETER_2ndInterface);
				//Configuring and executing the fourth rule for the 1st and 2nd interface split
				this.runFourthRule(tempGraph);
				//Configuring and executing the cloning rule
				this.runFifthRule(tempGraph, PARAMETER_1stInterface);
				this.runFifthRule(tempGraph, PARAMETER_2ndInterface);
				//Fixing the system model
				if(systemFilename != null && !systemFilename.isEmpty() && system != null) {
					this.runSixthRule(tempGraph, PARAMETER_1stInterface);
					this.runSixthRule(tempGraph, PARAMETER_2ndInterface);
					this.runSeventhRule(tempGraph, PARAMETER_1stInterface);
					this.runSeventhRule(tempGraph, PARAMETER_2ndInterface);
				}
				//Fixing the allocation model
				if(
					allocationFilename != null && !allocationFilename.isEmpty() && allocation != null &&
					resourceEnvironmentFilename != null && !resourceEnvironmentFilename.isEmpty() && resourceEnvironment != null
				) {
					this.runEighthRule(tempGraph, PARAMETER_1stInterface);
					this.runEighthRule(tempGraph, PARAMETER_2ndInterface);
				}
				//Configuring and executing the cleanUp rule
				this.runLastRule(tempGraph);
				//Store the results
				this.addTactic(seed, tempGraph, match);
				if (saveResult) {
					RunnerHelper.saveRepositoryResult(
							resourceSet, 
							tempGraph, 
							resultRepositoryFilename.replace("#REPLACEMENT#", String.valueOf(matches.indexOf(match)) + "-" + seed.getEntityName()));
					if(systemFilename != null && !systemFilename.isEmpty() && system != null) {
						RunnerHelper.saveSystemResult(
								resourceSet, 
								tempGraph, 
								resultSystemFilename.replace("#REPLACEMENT#", String.valueOf(matches.indexOf(match)) + "-" + seed.getEntityName()));
					}
					if(
						allocationFilename != null && !allocationFilename.isEmpty() && allocation != null &&
						resourceEnvironmentFilename != null && !resourceEnvironmentFilename.isEmpty() && resourceEnvironment != null
					) {
						RunnerHelper.saveResourceEnvironmentResult(
								resourceSet, 
								tempGraph, 
								resultResourceEnvironmentFilename.replace("#REPLACEMENT#", String.valueOf(matches.indexOf(match)) + "-" + seed.getEntityName()));
						RunnerHelper.saveAllocationResult(
								resourceSet, 
								tempGraph, 
								resultAllocationFilename.replace("#REPLACEMENT#", String.valueOf(matches.indexOf(match)) + "-" + seed.getEntityName()));
					}
				}
				app.undo(monitor);
			}
		}
	}

	private List<Match> findMatchesFirstRule() {
		Iterable<Match> matches = engine.findMatches(markComponents2Split, graph, null);
		Iterator<Match> it = matches.iterator();
		List<Match> list = new ArrayList<Match>();
		while(it.hasNext())
			list.add(it.next());
		return list;
	}
	
	private RuleApplication configureFirstRule(EGraph graph, Match match) {
		RuleApplication app = new RuleApplicationImpl(engine);
		app.setEGraph(graph);
		app.setRule(markComponents2Split);
		app.setPartialMatch(match);
		app.setCompleteMatch(match);
		app.setParameterValue("n", PARAMETER_1stInterface);
		app.setParameterValue("m", PARAMETER_2ndInterface);
		return app;
	}
	
	private RuleApplication runSecondRule(EGraph graph, String i) {
		RuleApplication app = new RuleApplicationImpl(engine);
		app.setEGraph(graph);
		app.setRule(splitComp4Interface);
		app.setParameterValue("i", i);
		app.setParameterValue("initIntTrace", TRACE_InitialInterface + i);
		boolean success = app.execute(monitor);
		if(success)
			System.out.println("Successfully splitted component for interface " + i);
		else
			System.out.println("Could not split component for interface " + i);
		return app;
	}
	
	private RuleApplication runThirdRule(EGraph graph, String i) {
		RuleApplication app = new RuleApplicationImpl(engine);
		app.setEGraph(graph);
		app.setRule(migrateSEFF);
		app.setParameterValue("i", i);
		app.setParameterValue("splitCompTrace", TRACE_SplitComponent + i);
		app.setParameterValue("splitIntTrace", TRACE_SplitInterface + i);
		boolean success = app.execute(monitor);
		if(success)
			System.out.println("Successfully migrated SEFFs for component " + i);
		else
			System.out.println("Could not migrate SEFFs for component " + i);
		return app;
	}
	
	private RuleApplication runFourthRule(EGraph graph) {
		RuleApplication app = new RuleApplicationImpl(engine);
		app.setEGraph(graph);
		app.setRule(rewireInterfaces);
		String i = PARAMETER_1stInterface;
		String j = PARAMETER_2ndInterface;
		app.setParameterValue("i", i);
		app.setParameterValue("j", j);
		app.setParameterValue("seffCompTraceI", TRACE_SEFFComponent + i);
		app.setParameterValue("seffIntTraceI", TRACE_SEFFInterface + i);
		app.setParameterValue("seffCompTraceJ", TRACE_SEFFComponent + j);
		app.setParameterValue("seffIntTraceJ", TRACE_SEFFInterface + j);
		boolean success = app.execute(monitor);
		if(success)
			System.out.println("Successfully rewire interfaces for the new components");
		else
			System.out.println("Could not rewire interfaces for the new components");
		return app;
	}
	
	private RuleApplication runFifthRule(EGraph graph, String i) {
		RuleApplication app = new RuleApplicationImpl(engine);
		app.setEGraph(graph);
		app.setRule(cloneInternalElements);
		app.setParameterValue("reconnCompTrace", TRACE_ReconnectedComponent + i);
		app.setParameterValue("reconnIntTrace", TRACE_ReconnectedInterface + i);
		boolean success = app.execute(monitor);
		if(success) {
			BasicComponent oldComponent = (BasicComponent) app.getResultParameterValue("oldComp");
			BasicComponent newComponent = (BasicComponent) app.getResultParameterValue("newComp");
			OperationInterface oldInterface = (OperationInterface) app.getResultParameterValue("oldInt");
			//Duplicating passive resources
			EList<PassiveResource> passiveResources = oldComponent.getPassiveResource_BasicComponent();
			if(passiveResources != null && !passiveResources.isEmpty()) {
				Collection<PassiveResource> passiveResourcesClone = EcoreUtil.copyAll(passiveResources);
				for(PassiveResource pr : passiveResourcesClone)
					pr.setId(EcoreUtil.generateUUID());
				newComponent.getPassiveResource_BasicComponent().addAll(passiveResourcesClone);
			}
			//Duplicating component resources
			EList<VariableUsage> variableUsages = oldComponent.getComponentParameterUsage_ImplementationComponentType();
			if(variableUsages != null && !variableUsages.isEmpty()) {
				Collection<VariableUsage> variableUsagesClone = EcoreUtil.copyAll(variableUsages);
				newComponent.getComponentParameterUsage_ImplementationComponentType().addAll(variableUsagesClone);
			}
			System.out.println("Successfully cloned the internal elements of the component and the associated interface");
		}
		else
			System.out.println("Could not clone the internal elements of the component and the associated interface");
		return app;
	}
	
	private RuleApplication runSixthRule(EGraph graph, String i) {
		RuleApplication app = new RuleApplicationImpl(engine);
		app.setEGraph(graph);
		app.setRule(propagate2SystemAssemblyContext);
		app.setParameterValue("i", i);
		app.setParameterValue("reconnCompTrace", TRACE_ReconnectedComponent + i);
		boolean success = app.execute(monitor);
		if(success)
			System.out.println("Successfully created the assembly context for the new components");
		else
			System.out.println("Could not create the assembly context for the new components");
		return app;
	}
	
	private void runSeventhRule(EGraph graph, String i) {
		Trace traceRoot = RunnerHelper.getTraceRoot(graph);
		Repository repositoryRoot = RunnerHelper.getRepositoryRoot(graph);
		org.palladiosimulator.pcm.system.System systemRoot = RunnerHelper.getSystemRoot(graph);
		List<Trace> assemblies = RunnerHelper.getTraces(traceRoot, TRACE_AssemblyContext + i, false);
		List<Connector> connectorsToAdd = new ArrayList<Connector>();
		for(Trace trace : assemblies) {
			AssemblyContext oldAssemblyContext = (AssemblyContext) trace.getSource().get(0);
			AssemblyContext newAssemblyContext = (AssemblyContext) trace.getTarget().get(0);
			BasicComponent oldComponent = (BasicComponent) oldAssemblyContext.getEncapsulatedComponent__AssemblyContext();
			BasicComponent newComponent = (BasicComponent) newAssemblyContext.getEncapsulatedComponent__AssemblyContext();
			Iterator<Connector> connectors = systemRoot.getConnectors__ComposedStructure().iterator();
			while(connectors.hasNext()) {
				Connector connector = connectors.next();
				if(connector instanceof ProvidedDelegationConnector) {
					ProvidedDelegationConnector oldDelegationConnector = (ProvidedDelegationConnector) connector;
					if(oldDelegationConnector.getAssemblyContext_ProvidedDelegationConnector().equals(oldAssemblyContext) && this.providesInterface(newComponent, oldDelegationConnector.getInnerProvidedRole_ProvidedDelegationConnector().getProvidedInterface__OperationProvidedRole())) {
						ProvidedDelegationConnector newDelegationConnector = CompositionFactory.eINSTANCE.createProvidedDelegationConnector();
						newDelegationConnector.setEntityName(oldDelegationConnector.getEntityName() + "-" + i);
						newDelegationConnector.setAssemblyContext_ProvidedDelegationConnector(newAssemblyContext);
						newDelegationConnector.setInnerProvidedRole_ProvidedDelegationConnector(
								this.searchNewProvidedRole(oldComponent, newComponent, oldDelegationConnector.getInnerProvidedRole_ProvidedDelegationConnector())
						);
						newDelegationConnector.setOuterProvidedRole_ProvidedDelegationConnector(oldDelegationConnector.getOuterProvidedRole_ProvidedDelegationConnector());
						connectorsToAdd.add(newDelegationConnector);
						Trace connectorTrace = TraceFactory.eINSTANCE.createTrace();
						connectorTrace.setName(TRACE_AssemblyConnector + i);
						connectorTrace.getSource().add((EObject) oldDelegationConnector);
						connectorTrace.getTarget().add((EObject) newDelegationConnector);
						traceRoot.getSubTraces().add(connectorTrace);
					}
				}
				if(connector instanceof AssemblyConnector) {
					AssemblyConnector oldConnector = (AssemblyConnector) connector;
					if(oldConnector.getProvidingAssemblyContext_AssemblyConnector().equals(oldAssemblyContext) || oldConnector.getRequiringAssemblyContext_AssemblyConnector().equals(oldAssemblyContext)) {
						AssemblyConnector newConnector = CompositionFactory.eINSTANCE.createAssemblyConnector();
						newConnector.setEntityName(oldConnector.getEntityName() + "-" + i);
						if(oldConnector.getProvidingAssemblyContext_AssemblyConnector().equals(oldAssemblyContext)) {
							newConnector.setProvidingAssemblyContext_AssemblyConnector(newAssemblyContext);
							newConnector.setRequiringAssemblyContext_AssemblyConnector(oldConnector.getRequiringAssemblyContext_AssemblyConnector());
							newConnector.setProvidedRole_AssemblyConnector(this.searchNewProvidedRole(oldComponent, newComponent, oldConnector.getProvidedRole_AssemblyConnector()));
							newConnector.setRequiredRole_AssemblyConnector(oldConnector.getRequiredRole_AssemblyConnector());
						}
						else if(oldConnector.getRequiringAssemblyContext_AssemblyConnector().equals(oldAssemblyContext)) {
							newConnector.setProvidingAssemblyContext_AssemblyConnector(oldConnector.getProvidingAssemblyContext_AssemblyConnector());
							newConnector.setRequiringAssemblyContext_AssemblyConnector(newAssemblyContext);
							newConnector.setProvidedRole_AssemblyConnector(oldConnector.getProvidedRole_AssemblyConnector());
							newConnector.setRequiredRole_AssemblyConnector(this.searchNewRequiredRole(oldComponent, newComponent, oldConnector.getRequiredRole_AssemblyConnector()));
						}
						connectorsToAdd.add(newConnector);
						Trace connectorTrace = TraceFactory.eINSTANCE.createTrace();
						connectorTrace.setName(TRACE_AssemblyConnector + i);
						connectorTrace.getSource().add((EObject) oldConnector);
						connectorTrace.getTarget().add((EObject) newConnector);
						traceRoot.getSubTraces().add(connectorTrace);
					}
				}
			}
		}
		systemRoot.getConnectors__ComposedStructure().addAll(connectorsToAdd);
		if(connectorsToAdd.size() > 0)
			System.out.println("Successfully created the assembly connectors for the new components");
		else
			System.out.println("Could not create the assembly connectors for the new components");
	}
	
	private OperationProvidedRole searchNewProvidedRole(BasicComponent oldComponent, BasicComponent newComponent, OperationProvidedRole oldOperationProvidedRole) {
		for(ProvidedRole newProvidedRole : newComponent.getProvidedRoles_InterfaceProvidingEntity()) {
			if(newProvidedRole instanceof OperationProvidedRole) {
				OperationProvidedRole newOperationProvidedRole = (OperationProvidedRole) newProvidedRole;
				if(newOperationProvidedRole.getProvidedInterface__OperationProvidedRole().equals(oldOperationProvidedRole.getProvidedInterface__OperationProvidedRole()))
					return newOperationProvidedRole;
			}
		}
		return null;
	}
	
	private OperationRequiredRole searchNewRequiredRole(BasicComponent oldComponent, BasicComponent newComponent, OperationRequiredRole oldOperationRequiredRole) {
		for(RequiredRole newRequiredRole : newComponent.getRequiredRoles_InterfaceRequiringEntity()) {
			if(newRequiredRole instanceof OperationRequiredRole) {
				OperationRequiredRole newOperationRequiredRole = (OperationRequiredRole) newRequiredRole;
				if(newOperationRequiredRole.getRequiredInterface__OperationRequiredRole().equals(oldOperationRequiredRole.getRequiredInterface__OperationRequiredRole()))
					return newOperationRequiredRole;
			}
		}
		return null;
	}
	
	private boolean providesInterface(BasicComponent component, OperationInterface oInterface) {
		for(ProvidedRole providedRole : component.getProvidedRoles_InterfaceProvidingEntity())
			if(providedRole instanceof OperationProvidedRole) {
				OperationProvidedRole operationProvidedRole = (OperationProvidedRole) providedRole;
				if(operationProvidedRole.getProvidedInterface__OperationProvidedRole().equals(oInterface))
					return true;
			}
		return false;
	}
	
	private RuleApplication runEighthRule(EGraph graph, String i) {
		RuleApplication app = new RuleApplicationImpl(engine);
		app.setEGraph(graph);
		app.setRule(propagate2Allocation);
		app.setParameterValue("i", i);
		app.setParameterValue("assemblyContextTrace", TRACE_AssemblyContext + i);
		boolean success = app.execute(monitor);
		if(success)
			System.out.println("Successfully created allocation contexts for the new assembly contexts");
		else
			System.out.println("Could not create allocation contexts for the new assembly contexts");
		return app;
	}
	
	private RuleApplication runLastRule(EGraph graph) {
		RuleApplication app = new RuleApplicationImpl(engine);
		app.setEGraph(graph);
		app.setRule(cleanUpManual);
		boolean success = app.execute(monitor);
		if(success) {
			//Removing the old component
			BasicComponent oldComponent = (BasicComponent) app.getResultParameterValue("oldComp");
			EcoreUtil.delete((EObject) oldComponent, true);
			//Removing all the traces
			Trace root = (Trace) app.getResultParameterValue("root");
			List<Trace> assemblyContexts = RunnerHelper.getTraces(root, TRACE_AssemblyContext, true);
			List<Trace> assemblyConnectors = RunnerHelper.getTraces(root, TRACE_AssemblyConnector, true);
			List<Trace> allocationContexts = RunnerHelper.getTraces(root, TRACE_AllocationContext, true);
			for(Trace trace : allocationContexts) {
				AllocationContext oldAllocationContext = (AllocationContext) trace.getSource().get(0);
				EcoreUtil.delete((EObject) oldAllocationContext, false);
			}
			for(Trace trace : assemblyConnectors) {
				EObject oldAssemblyConnector = (EObject) trace.getSource().get(0);
				EcoreUtil.delete((EObject) oldAssemblyConnector, false);
			}
			for(Trace trace : assemblyContexts) {
				AssemblyContext oldAssemblyContext = (AssemblyContext) trace.getSource().get(0);
				EcoreUtil.delete((EObject) oldAssemblyContext, false);
			}
			//EcoreUtil.delete((EObject) root, true);
			System.out.println("Successfully deleted the seed component and its provided/required interfaces");
		}
		else
			System.out.println("Could not delete the seed component and its provided/required interfaces");
		return app;
	}
	
	public static void main(String[] args) {
		String dirPath = "src/edu/squat/transformations/modifiability/splitresp";
		String henshinFilename = "splitresp-modular.henshin";
		String repositoryFilename, systemFilename, resourceEnvironmentFilename, allocationFilename;
		String resultRepositoryFilename, resultSystemFilename, resultResourceEnvironmentFilename, resultAllocationFilename;
		
		SplitRespRunner runner = new SplitRespRunner();

		//Individual testing
		repositoryFilename = "split-test.repository";
		resultRepositoryFilename = "split-test-" + "#REPLACEMENT#" + ".repository";
		//runner.run(dirPath, repositoryFilename, henshinFilename, resultRepositoryFilename, true);
		
		//Multiple testing
		repositoryFilename = "split-dual.repository";
		resultRepositoryFilename = "split-dual-" + "#REPLACEMENT#" + ".repository";
		//runner.run(dirPath, repositoryFilename, henshinFilename, resultRepositoryFilename, true);
		
		//MediaStore3 testing
		repositoryFilename = "ms.repository";
		resultRepositoryFilename = "ms-" + "#REPLACEMENT#" + ".repository";
		//runner.run(dirPath, repositoryFilename, henshinFilename, resultRepositoryFilename, true);
		
		//SimpleTactics testing
		repositoryFilename = "st.repository";
		resultRepositoryFilename = "st-" + "#REPLACEMENT#" + ".repository";
		//runner.run(dirPath, repositoryFilename, henshinFilename, resultRepositoryFilename, true);
		
		//SimpleTactics+ testing
		repositoryFilename = "stplus.repository";
		resultRepositoryFilename = "stplus-" + "#REPLACEMENT#" + ".repository";
		//runner.run(dirPath, repositoryFilename, henshinFilename, resultRepositoryFilename, true);
		
		//Complete multiple testing
		repositoryFilename = "split-dual.repository";
		systemFilename = "split-dual.system";
		resourceEnvironmentFilename = "split-dual.resourceenvironment";
		allocationFilename = "split-dual.allocation";
		resultRepositoryFilename = "split-dual-" + "#REPLACEMENT#" + ".repository";
		resultSystemFilename = "split-dual-" + "#REPLACEMENT#" + ".system";
		resultResourceEnvironmentFilename = "split-dual-" + "#REPLACEMENT#" + ".resourceenvironment";
		resultAllocationFilename = "split-dual-" + "#REPLACEMENT#" + ".allocation";
		runner.run(dirPath, 
				repositoryFilename, systemFilename, resourceEnvironmentFilename, allocationFilename,
				henshinFilename, 
				resultRepositoryFilename, resultSystemFilename, resultResourceEnvironmentFilename, resultAllocationFilename,
				true);
	}
}
