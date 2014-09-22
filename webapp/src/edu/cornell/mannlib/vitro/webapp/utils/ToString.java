/* $This file is distributed under the terms of the license in /doc/license.txt$ */

package edu.cornell.mannlib.vitro.webapp.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.compose.Polyadic;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;

/**
 * Some useful methods for printing out the contents of data structures:
 * OntModels, Models, Datasets, etc.
 */
public class ToString {
	/**
	 * Local implementations of OntModel can display themselves. Built-in Jena
	 * OntModels will show their hashcodes, base models, and sub-models.
	 */
	public static String ontModelToString(OntModel ontModel) {
		if (ontModel == null || isVitroClass(ontModel)) {
			return String.valueOf(ontModel);
		} else {
			Model base = ontModel.getBaseModel();
			Graph baseGraph = base.getGraph();
			List<Graph> subGraphs = ontModel.getSubGraphs();
			return simpleName(ontModel) + "[" + hashHex(ontModel) + ", base="
					+ modelToString(base) + ", subgraphs="
					+ subGraphsToString(subGraphs, baseGraph) + "]";
		}
	}

	/** Show the sub-graphs, except for the base graph. */
	private static String subGraphsToString(Collection<Graph> subGraphs,
			Graph baseGraph) {
		Set<Graph> set = new HashSet<>(subGraphs);
		set.remove(baseGraph);
		return setOfGraphsToString(set);
	}

	private static String setOfGraphsToString(Set<Graph> set) {
		Set<String> strings = new HashSet<>();
		for (Graph g : set) {
			strings.add(graphToString(g));
		}
		return "[" + StringUtils.join(strings, ", ") + "]";
	}

	/**
	 * Local implementations of Model can display themselves. Built-in Jena
	 * Graphs will show their hashcodes and graphs.
	 */
	public static String modelToString(Model model) {
		if (model == null || isVitroClass(model)) {
			return String.valueOf(model);
		} else {
			return simpleName(model) + "[" + hashHex(model) + ", base="
					+ graphToString(model.getGraph()) + "]";
		}
	}

	/**
	 * Local implementations of Graph can display themselves. Built-in Jena
	 * Graphs will show their hashcodes.
	 */
	public static String graphToString(Graph graph) {
		if (graph == null || isVitroClass(graph)) {
			return String.valueOf(graph);
		} else if (graph instanceof Polyadic) {
			return polyadicGraphToString((Polyadic) graph);
		} else {
			return simpleName(graph) + "[" + hashHex(graph) + "]";
		}
	}

	private static String polyadicGraphToString(Polyadic poly) {
		Graph baseGraph = poly.getBaseGraph();
		List<Graph> subGraphs = poly.getSubGraphs();
		return simpleName(poly) + "[" + hashHex(poly) + ", base="
				+ graphToString(baseGraph) + ", subgraphs="
				+ subGraphsToString(subGraphs, baseGraph) + "]";
	}

	/**
	 * If the string is found in ModelNames, return the name of the constant. If
	 * not, use the string itself.
	 * 
	 * TODO: Make it work.
	 */
	public static String modelName(String name) {
		return name;
	}

	public static boolean isVitroClass(Object o) {
		return (o == null) ? false : o.getClass().getName()
				.startsWith("edu.cornell");
	}

	public static String simpleName(Object o) {
		return (o == null) ? "null" : o.getClass().getSimpleName();
	}

	public static String hashHex(Object o) {
		return (o == null) ? "00000000" : Integer.toString(o.hashCode(), 16);
	}

	/**
	 * This class contains only static methods. No need for an instance.
	 */
	private ToString() {
		// Nothing to initialize.
	}
}