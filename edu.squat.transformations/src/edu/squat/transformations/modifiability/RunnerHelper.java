package edu.squat.transformations.modifiability;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.henshin.interpreter.EGraph;
import org.eclipse.emf.henshin.model.resource.HenshinResourceSet;
import org.eclipse.emf.henshin.trace.Trace;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

public class RunnerHelper {
	
	public static List<Trace> getTraces(EGraph graph, String kind, boolean partialMatch) {
		Trace root = RunnerHelper.getTraceRoot(graph);
		return RunnerHelper.getTraces(root, kind, partialMatch);
	}
	
	public static List<Trace> getTraces(Trace root, String kind, boolean partialMatch) {
		List<Trace> traces = new ArrayList<Trace>();
		for(Trace t : root.getSubTraces()) {
			if(partialMatch) {
				if(t.getName().startsWith(kind))
					traces.add(t);
			}
			else {
				if(t.getName().equals(kind))
					traces.add(t);
			}
		}
		return traces;
	}

	public static Trace getTraceRoot(EGraph graph) {
		Trace root = null;
		Iterator<EObject> i = graph.getRoots().iterator();
		while(i.hasNext() && root == null) {
			EObject e = i.next();
			if(e instanceof Trace && ((Trace)e).getName().equals("root"))
				root = (Trace) e;
		}
		return root;
	}
	
	public static Repository getRepositoryRoot(EGraph graph) {
		Repository repository = null;
		Iterator<EObject> i = graph.getRoots().iterator();
		while(i.hasNext() && repository == null) {
			EObject e = i.next();
			if(e instanceof Repository)
				repository = (Repository) e;
		}
		return repository;
	}
	
	public static org.palladiosimulator.pcm.system.System getSystemRoot(EGraph graph) {
		org.palladiosimulator.pcm.system.System system = null;
		Iterator<EObject> i = graph.getRoots().iterator();
		while(i.hasNext() && system == null) {
			EObject e = i.next();
			if(e instanceof org.palladiosimulator.pcm.system.System)
				system = (org.palladiosimulator.pcm.system.System) e;
		}
		return system;
	}
	
	public static Allocation getAllocationRoot(EGraph graph) {
		Allocation allocation = null;
		Iterator<EObject> i = graph.getRoots().iterator();
		while(i.hasNext() && allocation == null) {
			EObject e = i.next();
			if(e instanceof Allocation)
				allocation = (Allocation) e;
		}
		return allocation;
	}
	
	public static ResourceEnvironment getResourceEnvironmentRoot(EGraph graph) {
		ResourceEnvironment environment = null;
		Iterator<EObject> i = graph.getRoots().iterator();
		while(i.hasNext() && environment == null) {
			EObject e = i.next();
			if(e instanceof ResourceEnvironment)
				environment = (ResourceEnvironment) e;
		}
		return environment;
	}
	
	public static void saveRepositoryResult(HenshinResourceSet resourceSet, EGraph graph, String fileName) {
		Repository repoRoot = RunnerHelper.getRepositoryRoot(graph);
		RunnerHelper.saveResult(resourceSet, graph, fileName, (EObject) repoRoot);
	}
	
	public static void saveSystemResult(HenshinResourceSet resourceSet, EGraph graph, String fileName) {
		org.palladiosimulator.pcm.system.System systemRoot = RunnerHelper.getSystemRoot(graph);
		RunnerHelper.saveResult(resourceSet, graph, fileName, (EObject) systemRoot);
	}
	
	public static void saveAllocationResult(HenshinResourceSet resourceSet, EGraph graph, String fileName) {
		Allocation allocationRoot = RunnerHelper.getAllocationRoot(graph);
		RunnerHelper.saveResult(resourceSet, graph, fileName, (EObject) allocationRoot);
	}
	
	public static void saveResourceEnvironmentResult(HenshinResourceSet resourceSet, EGraph graph, String fileName) {
		ResourceEnvironment environmentRoot = RunnerHelper.getResourceEnvironmentRoot(graph);
		RunnerHelper.saveResult(resourceSet, graph, fileName, (EObject) environmentRoot);
	}
	
	private static void saveResult(HenshinResourceSet resourceSet, EGraph graph, String fileName, EObject modelRoot) {
		try {
			Resource resource = resourceSet.createResource(fileName);
			Trace traceRoot = RunnerHelper.getTraceRoot(graph);
			resource.getContents().add(modelRoot);
			resource.getContents().add((EObject) traceRoot);
			resource.save(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
