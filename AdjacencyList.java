	
	import java.awt.BorderLayout;
	import java.awt.Color;
	import java.awt.Graphics;
	import java.awt.Point;
	import java.awt.event.MouseAdapter;
	import java.awt.event.MouseEvent;
	import java.awt.event.MouseMotionAdapter;
	import java.util.*;
	
	import javax.swing.JFrame;
	import javax.swing.JPanel;
	
	
	public class AdjacencyList extends JPanel
	{
		
		public static ArrayList<LinkedList<Integer>> adjacencyList = new ArrayList<LinkedList<Integer>>();
		public static ArrayList<LinkedList<Edge>> weightedAdjacencyList = new ArrayList<LinkedList<Edge>>();
		public static ArrayList<Node> vertexList = new ArrayList<Node>();
		public static int numNodes = 0;
		public Point lastPoint;
		public Point currentPoint;
		public boolean mouseDrag = false;
		public final int NODE_WIDTH = 64, NODE_HEIGHT = 64;
		int from; // variables keep track of the ID of the node you started on to the node you ended on
		Integer to;
		Node tempNode = null; 
		public static char[] nodes = null;
		public static boolean isDirected = false;
		public static char[] path = null;
		// set this value to true when finding the shortest path
		public static boolean drawPath = false;
		public static boolean isWeighted = false;
		
		// hi
		// this function returns the shortest path between two nodes by performing breadth first search and reconstructing the path from start node to end node
		
		public static char[] shortestPath(char startNode, char endNode) 
		{
			char [] prev = new char[numNodes];
			// create previous array to store the parents of each node stored at their respective index i.e if B is the parent of A then the value at arr[A] = B
			
			prev = bfs(startNode, prev);
			
			return reconstructPath(startNode, endNode, prev);
		}
		
		// this function performs breadth first search and returns an array of parent nodes
		
		public static char[] bfs(char startNode, char[] prev)
		{
			
			// create queue data structure to store our nodes in the breadth first search
			
			Queue<Integer> queue = new LinkedList<>();
			// create visited array, set startNode position in visited array to true
			 boolean[] visited = new boolean[numNodes];
			 visited[startNode - 48] = true;
			 
			 // add the starting node to the queue
			 
			 queue.add(startNode - 48);
			 
			 int currentNode = 0;
			 
			 while(!queue.isEmpty())
			 {
				
				 currentNode = queue.remove();
				 
				 // get all the neighbors of current node
				 
				 LinkedList<Integer> neighbors = new LinkedList<Integer>();
				 
				 neighbors = adjacencyList.get(currentNode);
				 
				 for(int eachNode : neighbors)
				 {
					 if(visited[eachNode] == false)
					 {
						 
						 visited[eachNode] = true;
						 queue.add(eachNode);
						 
						 prev[eachNode] = (char)(currentNode + 48);
						 
					 } 
				 }
	
			 }
			 
			 return prev;
				 
		}
		
		// find the path from the start node to the end node given startNode and endNodes, then reverse the array
		
		public static char[] reconstructPath(char startNode, char endNode, char[] prev)
		{
			// create path array to store reconstructed path
			
			ArrayList<Character> path = new ArrayList<Character>();
			
			// loop through the prev array and add the parents from the end node back to the start node which is indicated by the presence of a null entry
		
			for(int i = endNode - 48; i != 0; i = prev[i] - 48)
			{
				if(i == endNode - 48)
				{
					path.add(endNode);
					path.add(prev[i]);
				}
				else path.add(prev[i]);
				
			}
			
			// next reverse the order of the path
			
			// create char array of size equal to path
			
			char [] shortestPath = new char[path.size()];
			
			// start at the end of the path and insert the elements in the opposite order till we reach the start 
			
			for(int i = path.size() - 1; i >= 0; i--)
			{
				
				shortestPath[(path.size() - 1) - i] = path.get(i);
			}
			
			// finally return the shortest path from start to end node in ascending order
			
			return shortestPath;
			
		}
		
		
		public AdjacencyList() 
		{
		
			
			addMouseListener(new MouseAdapter() 
					{
				
				public void mousePressed(MouseEvent e)
				{
				 
					Point clickedPoint = new Point(e.getX(), e.getY());
			// Creating a new node		 
				if(clickedOnEmptySpace(clickedPoint) == true)
				{
					
					if(mouseDrag == false)
					{
						
					tempNode = new Node(clickedPoint);
					vertexList.add(tempNode);
					adjacencyList.add(new LinkedList<Integer>());
					if (isWeighted == true) weightedAdjacencyList.add(new LinkedList<Edge>());
					
					tempNode = null;
					
					}
					else
					{
						lastPoint = null;
						mouseDrag = false;
					}
					
				} 
				else if(mouseDrag != true) // clicking on a node and attempting to create an edge, here the line will follow the cursor
				{
					mouseDrag = true;
					lastPoint = new Point(e.getX(), e.getY());
					 tempNode = getNode(lastPoint);
					from = tempNode.nodeID;
					tempNode = null;
				
				} // if clicked again and on a node then create an edge depending on whether its directed or undirected
				else if(mouseDrag == true && currentPoint != null)
				{
					
					mouseDrag = false;
					currentPoint = new Point(e.getX(), e.getY());
					 tempNode = getNode(currentPoint);
					to = tempNode.nodeID;
					tempNode = null;
					
					if(isDirected == true)
					{
					// create an edge using to and from 
						// This is called if it is a weighted graph 
						if (isWeighted == true)
						{
							if(adjacencyList.get(from).contains(to) == false) 
							{
								 adjacencyList.get(from).add(to);
								 System.out.println("What would you liike the weight from " + from + " to " + to + " to be?");
								 Scanner input = new Scanner(System.in);
								 int weight = input.nextInt(); 
								 weightedAdjacencyList.get(from).add(new Edge(to,weight));
						
								 int counter = 0;
								 for(LinkedList<Edge> eachEdge: weightedAdjacencyList)
									{
									 	counter  = 0;
										System.out.println(counter + " => " + eachEdge);
										counter++;
									}
							}
						}
						// This is called if it is an unweighted graph broo
						else
						{
							if(adjacencyList.get(from).contains(to) == false) adjacencyList.get(from).add(to);
							
							for(int i = 0; i < adjacencyList.size(); i++)
							{
								System.out.println(i + " => " + adjacencyList.get(i));
							}
						}
					
					}
					else
					{
						// create two edges using to from, from to
						
						if (isWeighted == true)
						{
							if(adjacencyList.get(from).contains(to) == false) 
							{
								 adjacencyList.get(from).add(to);
								 System.out.println("What would you like the weight from " + from + " to " + to + " to be?");
								 Scanner input = new Scanner(System.in);
								 int weight = input.nextInt(); 
								 weightedAdjacencyList.get(from).add(new Edge(to,weight));
								
								 adjacencyList.get(to).add(from);
									
							}
					/*
							 
							 int counter = 0;
							 for(LinkedList<Edge> eachEdge: weightedAdjacencyList)
								{
								 	counter  = 0;
									System.out.println(counter + " => " + eachEdge);
									counter++;
								}*/
						}
						else
						{
							if(adjacencyList.get(to).contains(from) == false) adjacencyList.get(to).add(from);
							
							for(int i = 0; i < adjacencyList.size(); i++)
							{
								System.out.println(i + " => " + adjacencyList.get(i));
							}
						}
			
					}
					currentPoint = null;
					lastPoint = null;
			
				
				}
				
				
				
				}
					}
			);
			
			addMouseMotionListener(new MouseMotionAdapter()
					{
				
				public void mouseMoved(MouseEvent e)
				{
					
					Graphics g = getGraphics();
				if(lastPoint != null && mouseDrag == true) 
					{
					currentPoint = new Point(e.getX(), e.getY());
					
					g.drawLine(lastPoint.x,lastPoint.y, currentPoint.x, currentPoint.y);
						
					repaint();
					}
				
				repaint();
					g.dispose();
					
				}
				
				
					});
			
		}
	
		public static class Edge
		{
			int to;
			int weight;
			
			public Edge(int to, int weight)
			{
				this.to = to;
				this.weight = weight;
			}
		}
		
		public static class Node 
		{
			
			Point coord;
			char name;
			int color;
			Node next;
			int nodeID = 0;
			
	
			
			public Node(Point p, char name)
			{ 
				coord = p;
				this.name = name;
				nodeID = numNodes;
				numNodes++;
			}
			
			public Node(Point p)
			{
				
			coord = p;
			nodeID = numNodes;
			name = (char)(numNodes + '0');
			numNodes++;
						
			}
			
		}
		
		public void paintComponent(Graphics g)
		{
			
			
			// fill screen
			g.setColor(Color.white);
			g.fillRect(0,0 ,800, 800);
			
			// draw all nodes
			
			g.setColor(Color.black);
			
			// keep up to date the current visualization of the graph
			
			if(lastPoint != null && currentPoint != null) g.drawLine(lastPoint.x, lastPoint.y, currentPoint.x, currentPoint.y);
			// draw the graph
			if(!vertexList.isEmpty() && !adjacencyList.isEmpty())
			{
			Node startingNode, endingNode;
			Point p1, p2;
			for(int i = 0; i < vertexList.size(); i++)
			{
				startingNode = vertexList.get(i); // get the starting node
				p1 = startingNode.coord;
				
				// check all neighbors
				 // keep track of position in l
				for(Integer eachNeighbor : adjacencyList.get(i))
				{
	
					endingNode = vertexList.get(eachNeighbor);
					p2 = endingNode.coord;
					
					g.drawLine(p1.x + NODE_WIDTH / 2, p1.y + NODE_HEIGHT / 2, p2.x + NODE_WIDTH / 2, p2.y + NODE_HEIGHT / 2);
				
				}
			}
			}
			
				g.setColor(Color.white);
			
			for(Node eachNode : vertexList)
				{
					g.fillOval(eachNode.coord.x, eachNode.coord.y, NODE_WIDTH, NODE_HEIGHT);
					
				}
	
			
			// run this part of the code if you are to draw the shortest path
			
			if(drawPath == true)
			{
				
				g.setColor(Color.green);
				// loop through the path array and color in the path edges and nodes with green
				
				
				// first color all the path nodes green
				
				for(int i = 0; i <= path.length - 2; i++)
				{
					
					Node startNode = vertexList.get(path[i] - 48);
					Node endNode = vertexList.get(path[i + 1] - 48);
					
					Point p1 = startNode.coord;
					Point p2 = endNode.coord;
					
					g.fillOval(startNode.coord.x, startNode.coord.y, NODE_WIDTH, NODE_HEIGHT);
					g.fillOval(endNode.coord.x, endNode.coord.y, NODE_WIDTH, NODE_HEIGHT);
					
					g.drawLine(p1.x + NODE_WIDTH / 2, p1.y + NODE_HEIGHT / 2, p2.x + NODE_WIDTH / 2, p2.y + NODE_HEIGHT / 2);
					
				}
		
			}
			
	
			g.setColor(Color.black);
			
			nodes = new char[vertexList.size()];
			for(int i = 0; i < vertexList.size(); i++ )
			{
				nodes[i] = vertexList.get(i).name;
			}
			int k = 0;
			for(Node eachNode : vertexList){
				{
					g.drawOval(eachNode.coord.x, eachNode.coord.y, NODE_WIDTH, NODE_HEIGHT);
					g.drawChars(nodes, k , 1 , eachNode.coord.x + NODE_WIDTH/2, eachNode.coord.y + NODE_HEIGHT/2);
					k++;
					
				}
			}
		
			
		}
		public static void main(String args[])
		{
			
			String choice = null;
			boolean isValid = false;
			
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			System.out.println("Welcome to the Graph Maker!!\n");
			System.out.println("Do you want to make a directed or undirected graph?");
			System.out.println("1-DIRECTED");
			System.out.println("2-UNDIRECTED");
			
			
			Scanner input = new Scanner(System.in);
			
			do
			{
				
			choice = input.next();
			
			if('1' > choice.charAt(0) || choice.charAt(0) > '2') System.out.println("Error! \n Enter in a 1 or 2");
				
			
					
			}while('1' > choice.charAt(0) || choice.charAt(0) > '2');
			
			isDirected = choice.charAt(0) == '2' ? true : false;
			
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			System.out.println("\nDo you want to make your graph weighted or unweighted");
			System.out.println("1-UNWEIGHTED");
			System.out.println("2-WEIGHTED");
			
			Scanner Input = new Scanner(System.in);
			
			do
			{
				
			choice = Input.next();
			
			if('1' > choice.charAt(0) || choice.charAt(0) > '2') System.out.println("Error! \n Enter in a 1 or 2");
				
			
					
			}while('1' > choice.charAt(0) || choice.charAt(0) > '2');
			
			isWeighted = choice.charAt(0) == '2' ? true : false;
			
			String graphType;
			String isItWeighted;
			String article;
			
			if (isDirected)
				{
					graphType = "Undirected ";
					article = "an";
				}
			else 
				{
					graphType = "Directed ";
					article = "a";
				}
			
			if (isWeighted)
				{
					isItWeighted = " Weighted ";
					article = "an";
				}
			else 
				{
					isItWeighted = " Unweighted ";
					article = "a";
				}
			
			System.out.println("Congratulations, you are working with " + article + isItWeighted + graphType + "Graph");
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			JFrame frame = new JFrame("Graph Maker");
			frame.getContentPane().add(new AdjacencyList(), BorderLayout.CENTER);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(800,800);
			frame.setVisible(true);
			/*
			System.out.println("Lets perform bfs");
			choice = input.next();
			path = shortestPath('0', choice.charAt(0));
			drawPath = true;
			System.out.println(path);
			
		*/
		}
	
		public boolean clickedOnEmptySpace(Point p) 
		{
			
			// loop through each node and check if you're clicking on one
			
			for(Node eachNode : vertexList)
			{
				
				if((p.x >= eachNode.coord.x && p.x <= eachNode.coord.x + NODE_WIDTH) && (p.y >= eachNode.coord.y && p.y <= eachNode.coord.y + NODE_HEIGHT)) return false;
			}
			
			return true;
		}
		
		public Node getNode(Point p) 
		{
			
			for(Node eachNode : vertexList)
			{
				
				if((p.x >= eachNode.coord.x && p.x <= eachNode.coord.x + NODE_WIDTH) && (p.y >= eachNode.coord.y && p.y <= eachNode.coord.y + NODE_HEIGHT)) return eachNode;
			
			}
			
			return null;
	
		}
		
	}
