import matcher.VF2Matcher;
import graph.Graph;


public class main {

	public static void main(String[] args) {
		
		Graph g1 = getGraph1();
		Graph g2 = getGraph2();
		
		g1.printGraph(); // prints adjacency matrix of G1
		g2.printGraph(); // prints adjacency matrix of G1
		
		VF2Matcher matcher = new VF2Matcher();
		matcher.match(g1, g2); // starts pattern search
		
	}
	
	// gets a model graph
	private static Graph getGraph1() {
		
		Graph g = new Graph("Graph1");
		
		g.addNode("A"); // 0
		g.addNode("A"); // 1
		g.addNode("B"); // 2
		g.addNode("B"); // 3
		g.addNode("C"); // 4
		
		g.addEdge(0, 1);
		g.addEdge(0, 2);
		g.addEdge(1, 3);
		g.addEdge(2, 3);
		g.addEdge(3, 4);
		
		return g;
	}
	
	// gets a pattern graph
	private static Graph getGraph2() {
		
		Graph g = new Graph("Graph2");
		
		g.addNode("A"); // 0
		g.addNode("B"); // 1
		g.addNode("B"); // 2
		g.addNode("C"); // 3
		
		g.addEdge(0, 2);
		g.addEdge(1, 2);
		g.addEdge(2, 3);
		
		return g;
	}

}
