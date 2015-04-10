package ics311;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Copyright (c) <2014>, <David Lin>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY <David Lin> ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <copyright holder> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

public class AnalyzeGraph {

	public static void main(String[] args) {
		GraphADT<String, String> graph = new GraphADT<String, String>();
		boolean arcs = false;
		boolean vertexData = false;
		boolean arcStrength = false;
		ArrayList<ArrayList<String>> components = null;
		ArrayList<ArrayList<String>> sortComponents = null;
		boolean undirected = false;
		if(undirected) graph.undirected();
		
		/**
		 * Graphs
		 * 
		 * celegansneural.vna
		 * political-blogs.vna
		 * wiki-Vote.vna
		 * cit-HepTh.vna
		 * email-Enron.vna
		 * ti-full.vna
		 * g-297-2345.vna
		 * g-1490-19025.vna
		 * g-7115-103689.vna
		 * g-27770-352807.vna
		 * g-36692-183831.vna
		 */
		try {
			BufferedReader reader = new BufferedReader(new FileReader(args[0]));
			String line = null;
			while ((line = reader.readLine()) != null) {
				if(line.contains("*tie") || line.contains("*Tie")) arcs = true;
				if(!arcs) {
					if(!line.contains("*node") && !line.contains("*Node")) {
						if(line.contains(" ")) {
							String[] info = line.split("\\s+");
							if(!info[0].equals("ID")) {
								if(info.length == 3) graph.insertVertex(info[0], (info[1] + " " + info[2]));
								else graph.insertVertex(info[0], info[1]);
							}
						}
						else if(!line.equals("ID")) graph.insertVertex(line);
					}
				}
				else {
					if(!line.contains("ata")) {
						if(line.contains("trength")) arcStrength = true;
						if(!line.contains("rom")) {
							if(arcStrength) {
								String[] info = line.split("\\s+");
								graph.insertArc(info[0], info[1], info[2]);
							}
							else {
								String[] info = line.split("\\s+");
								graph.insertArc(info[0], info[1]);
							}
						}
					}
				}
			}
		}
		catch (IndexOutOfBoundsException e) {
			if(args.length == 0) {
				System.out.println("Error: No file path given.");
			}
			else System.out.println("Error: Index out of bounds " + e.getMessage());
			System.exit(0);
		}
		catch (IOException e) {
			System.out.println("Unable to read from file");
			System.exit(0);
		}
		// graph.printAdjacencyList();
		ArrayList<Double> degrees = graph.numbers();
		int vertices = graph.vertexCount();
		int numArcs = graph.arcCount();
		double density = ((double)graph.arcCount() / ((double)vertices * (vertices - 1)));
		System.out.println("-----------------------------------------------------------------------------");
		System.out.println("Graph <" + args[0] + ">");
		System.out.println("-----------------------------------------------------------------------------");
		System.out.println("|V| = " + vertices);
		if(undirected) System.out.println("|E| = " + graph.numEdges());
		else System.out.println("|A| = " + numArcs);
		System.out.printf("Density: %f%n", density);
		// 1$.3
		System.out.println("Degree distribution:		minimum		average			maximum");
		System.out.printf("	inDegree:		%.0f		%.4f			%.0f%n", degrees.get(0), degrees.get(1), degrees.get(2));
		System.out.printf("	outDegree:		%.0f		%.4f			%.0f%n", degrees.get(3), degrees.get(4), degrees.get(5));
		/**
		 * Optional SCC
		if(args[0].contains("SCC") || args[0].contains("celegans")) {
			components = graph.SCC();
			sortComponents = new ArrayList<ArrayList<String>>();
			int size = components.size();
			double percentLargest = 0;
			for(int v = 0; v < size; v++) {
	    		int largest = 0;
	    		int index = 0;
	    		for(int e = 0; e < components.size(); e++) {
	    			if(components.get(e).size() > largest) {
	    				largest = components.get(e).size();
	    				index = e;
	    			}
	    		}
	    		sortComponents.add(components.get(index));
	    		components.remove(index);
			}
			percentLargest = ((double)sortComponents.get(0).size() / (double)graph.vertexCount()) * 100;
			System.out.println("SCC Information");
			System.out.println("Number of Strongly Connected Components: " + sortComponents.size());
			System.out.printf("Percent Vertices in Largest Strongly Connected Components: %1$.2f%n", percentLargest);
			
			System.out.println("SCC	Size	Members:");
			for(int v = 0; v < sortComponents.size(); v++) {
				System.out.print((v+1) + ":	" + sortComponents.get(v).size() + "	");
				for(int e = 0; e < sortComponents.get(v).size(); e++) {
					System.out.print(sortComponents.get(v).get(e));
					if(e != (sortComponents.get(v).size() - 1)) System.out.print(", ");
				}
				System.out.println(" ");
			}
			
		}
		*/
		double reciprocity = graph.reciprocity();
		 System.out.printf("Reciprocity: %f%n", reciprocity);
		ArrayList<Number> mean = graph.BFS();
		double geodesic = (Double) mean.get(0);
		int diameter = (Integer) mean.get(1);
		double cycle = graph.cycleClus();
		double middle = graph.middleClus();
		double inc = graph.inClus();
		double outc = graph.outClustering();
		
		graph.makeUndirected();
		System.out.printf("Degree Correlation: %f%n", graph.degreeCorellation());
		System.out.printf("Clustering Coefficient: %f%n", graph.clustering());
		System.out.printf("Directed Clustering Coefficient Cycle: %f%n", cycle);
		System.out.printf("Directed Clustering Coefficient Middle: %f%n", middle);
		System.out.printf("Directed Clustering Coefficient In: %f%n", inc);
		System.out.printf("Directed Clustering Coefficient Out: %f%n", outc);
		System.out.printf("Mean Geodesic Distance: %f%n", geodesic);
		System.out.println("Diameter: " + diameter);
	}
}
