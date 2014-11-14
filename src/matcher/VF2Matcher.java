package matcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import graph.Edge;
import graph.Graph;
import graph.Node;

public class VF2Matcher {
	
	// finds all subgraph isomorphisms and prints them to the console
	// modelGraph is the big graph
	// patternGraph is the small graph which is searched for in the big one
	public void match(Graph modelGraph, Graph patternGraph) {
		
		State state = new State(modelGraph, patternGraph);
		this.matchInternal(state, modelGraph, patternGraph); 
	}
	
	// internal method for finding subgraphs. called recursively
	private void matchInternal(State s, Graph modelGraph, Graph patternGraph) {
		
		// abort search if we reached the final level of the search tree 
		if (s.depth == patternGraph.nodes.size()) {
			s.printMapping(); // all pattern nodes matched -> print solution
		} 
		else
		{	
			// get candidate pairs
			Map<Integer,Integer> candiatePairs = this.getCandidatePairs(s, modelGraph, patternGraph);
			
			// iterate through candidate pairs
			for (Integer n : candiatePairs.keySet()) {
				int m = candiatePairs.get(n);
				
				// check if candidate pair (n,m) is feasible 
				if (checkFeasibility(s,n,m)) {
					
					s.match(n, m); // extend mapping
					matchInternal(s, modelGraph, patternGraph); // recursive call
					s.backtrack(n, m); // remove (n,m) from the mapping
					
				}
			}
		}
	}
	
	// determines all candidate pairs to be checked for feasibility
	private Map getCandidatePairs(State s, Graph m, Graph p) {
		
		return this.pairGenerator(s.unmapped1, s.unmapped2);
	}
	
	// generates pairs of nodes
	// outputs a map from model nodes to pattern nodes
	private Map pairGenerator(Collection<Integer> modelNodes , Collection<Integer> patternNodes) {
		
		TreeMap<Integer,Integer> map = new TreeMap<Integer,Integer>(); // the map storing candidate pairs
		
		// find the largest among all pattern nodes (the one with the largest ID)!
		// Note: it does not matter how to choose a node here. The only important thing is to have a total order, i.e., to uniquely choose one node. If you do not do this, you might get multiple redundant states having the same pairs of nodes mapped. The only difference will be the order in which these pairs have been included (but the order does not change the result, so these states are all the same!).
		int nextPatternNode = -1;
		for (Integer i : patternNodes)
			nextPatternNode = Math.max(nextPatternNode, i);
		
		// generate pairs of all model graph nodes with the designated pattern graph node
		for (Integer i : modelNodes)
			map.put(i, nextPatternNode);
		
		return map; // return node pairs
	}
	
	// checks whether or not it makes sense to extend the mapping by the pair (n,m)
	// n is a model graph node
	// m is a pattern graph node
	private Boolean checkFeasibility(State s , int n , int m) {
		
		Boolean passed = true;
		
		passed = passed && checkRpredAndRsucc(s,n,m); // check Rpred / Rsucc conditions (subgraph isomorphism definition)
		
		return passed; // return result
	}
	
	// checks if extending the mapping by the pair (n,m) would violate the subgraph isomorphism definition
	private Boolean checkRpredAndRsucc(State s , int n , int m) {
		
		Boolean passed = true;
		
		// check if the structure of the (partial) model graph is also present in the (partial) pattern graph 
		// if a predecessor of n has been mapped to a node n' before, then n' must be mapped to a predecessor of m 
		Node nTmp = s.modelGraph.nodes.get(n);
		for (Edge e : nTmp.incomingEdges) {
			if (s.core_1[e.source.id] > -1) {
				passed = passed && (s.patternGraph.getAdjacencyMatrix()[s.core_1[e.source.id]][m] == 1);
			}
		}
		// if a successor of n has been mapped to a node n' before, then n' must be mapped to a successor of m
		for (Edge e : nTmp.outgoingEdges) {
			if (s.core_1[e.target.id] > -1) {
				passed = passed && (s.patternGraph.getAdjacencyMatrix()[m][s.core_1[e.target.id]] == 1);
			}
		}
		
		// check if the structure of the (partial) pattern graph is also present in the (partial) model graph
		// if a predecessor of m has been mapped to a node m' before, then m' must be mapped to a predecessor of n
		Node mTmp = s.patternGraph.nodes.get(m);
		for (Edge e : mTmp.incomingEdges) {
			if (s.core_2[e.source.id] > -1) {
				passed = passed && (s.modelGraph.getAdjacencyMatrix()[s.core_2[e.source.id]][n] == 1);
			}
		}
		// if a successor of m has been mapped to a node m' before, then m' must be mapped to a successor of n
		for (Edge e : mTmp.outgoingEdges) {
			if (s.core_2[e.target.id] > -1) {
				passed = passed && (s.modelGraph.getAdjacencyMatrix()[n][s.core_2[e.target.id]] == 1);
			}
		}
		
		return passed; // return the result
	}
	
}
