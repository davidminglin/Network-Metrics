package ics311;
import ics311.DLLDynamicSet.Node;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

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

public class GraphADT<Key extends Comparable<Key>, Data> implements Graph<Key, Data> {
	// Variables
	HashMap<Key, DLLDynamicSet> graph = new HashMap<Key, DLLDynamicSet>();
	HashMap<String, HashMap<Key,String>> annotation = new HashMap<String, HashMap<Key,String>>();
	HashMap<Key, String> vertexAnnote = new HashMap<Key,String>();
	
	private int numArcs = 0;
	private boolean undirected = false;
	
	// Accessors
	/**
	* Returns the number of vertices |V|. 
	*	    
	* @return  the number of vertices |V| in the Graph.  
	*/
    public int vertexCount() {
		return graph.size();
	}
   
    /**
	* Returns the number of arcs |A| in the Graph. 
	*	    
	* @return   Returns the number of arcs |A|in the Graph.   
	*/
    public int arcCount() {
		return numArcs;
	}
    
    /**
	*  Returns an iterator over the arcs (directed edges) A of G. 
	*	    
	* @return   Returns an iterator over the arcs (directed edges) A of G.
	* Arcs are represented by an ArrayList that contains the Vertex Key of the source 
	*  destination at position 0 and   the Vertex Key of the destination at position 1 
	*/
    public Iterator<ArrayList<Key>> arcs() {
		return new arcsIterator();
	}
    
    private class arcsIterator implements Iterator<ArrayList<Key>> {
    	private ArrayList<Key> arc = null;
    	int processed = 0;
    	int E = 0;
    	DLLDynamicSet vertex = null;
    	DLLDynamicSet.Node current = null;
    	Key lastReturned = null;
    	boolean removedAlready = false;
    	Iterator it = null;
    	
    	public arcsIterator() {
    		arc = new ArrayList<Key>();
    		arc.add((Key) "Source");
    		arc.add((Key) "Destination");
    		it = graph.values().iterator();
    		if (arcCount() != 0) {
    			vertex = (DLLDynamicSet) it.next();
    			arc.set(0, (Key) vertex.setDataStructure());
    			current = vertex.head();
    		}
    	}

		@Override
		public boolean hasNext() {
			return(processed < arcCount());
		}

		@Override
		public ArrayList<Key> next() {
			if(hasNext()) {
				removedAlready = false;
				if(E == vertex.size()) {
					E = 0;
					do {
						vertex = (DLLDynamicSet) it.next();
					}
					while(vertex.size() == 0);
					current = vertex.head();
					arc.set(0, (Key) vertex.setDataStructure());
				}
				else {
					E += 1;
				}
				arc.set(1, (Key) current.getKey());
				lastReturned = (Key) current.getKey();
				processed += 1;
				current = current.next();
				return arc;
			}
			throw new NoSuchElementException("No next arc");
		}

		@Override
		public void remove() {
			if(removedAlready) throw new ConcurrentModificationException("Arc removed already");
			if(lastReturned != null) {
				vertex.delete(lastReturned);
				numArcs -= 1;
				processed -= 1;
				E -= 1;
				lastReturned = null;
				removedAlready = true;
			}
			else throw new IllegalStateException("No arc to remove");
		}
    }
    
    /**
  	*  Returns an iterator over the vertices V
  	* 
  	*	    
  	* @return   Returns an iterator over the VertexKeys V.
  	*/
      public Iterator<Key> vertices() {
  		return new verticesIterator();
  	}
      
      private class verticesIterator implements Iterator<Key> {
      	Key lastReturned = null;
      	DLLDynamicSet vertex = null;
      	boolean removedAlready = false;
      	Iterator vertices = graph.keySet().iterator();
      	
      	public verticesIterator() {
      	}

  		@Override
  		public boolean hasNext() {
  			return(vertices.hasNext());
  		}

  		@Override
  		public Key next() {
  			if(hasNext()) {
  				removedAlready = false;
  				lastReturned = (Key) vertices.next();
  				return lastReturned;
  			}
  			throw new NoSuchElementException("No next vertex");
  		}

  		@Override
  		public void remove() {
  			if(removedAlready) throw new ConcurrentModificationException("Vertex removed already");
  			if(lastReturned != null) {
  				for(DLLDynamicSet dynamicSet: graph.values()) {
  					vertex = dynamicSet;
			    		for(DLLDynamicSet.Node current = vertex.head(); current != null; current.next()) {
			    			int compared = -1;
			        		compared = ((String) current.getKey()).compareToIgnoreCase((String) lastReturned);
			        		if(compared == 0)  {
			    				vertex.delete(lastReturned);
			    				numArcs -= 1;
			    			}
			        		else if(compared > 0) break;
			    		}
  		    	}
  				graph.remove(lastReturned);
  				lastReturned = null;
  			}
  			else throw new IllegalStateException("No vertex to remove");
  		}
      }
    
       
    /**
	* Returns TRUE if there exists an arc connecting a source vertex with the Key sourceKey  a target vertex targetKey, otherwise FALSE.
	*	    
	* @return  Returns the Arc that connects client keys source and target, or null if none.
	*/
    public boolean arcExists(Key sourceKey, Key destinationKey) {
    	if(vertexExists(sourceKey)) {
    		DLLDynamicSet.Node current = graph.get(sourceKey).head();
        	while (current != null) {
        		int compared = -1;
        		compared = ((String) current.getKey()).compareToIgnoreCase((String) destinationKey);
        		if(compared == 0) return true;
        		else if(compared > 0) return false;
        		
        		current = current.next();
        	}
    	}
    	return false;
	}
    
    /**
   	* Returns TRUE if there vertex with the Key, otherwise false.
   	*	    
   	* @return  Returns TRUE if there vertex with the Key, otherwise false.
   	*/
    public boolean vertexExists(Key vertexKey) {
    	return (graph.get(vertexKey) != null);
	}
       
  
    /**
   	*   Returns the number of arcs incoming to v.
   	*	    
   	* @return   Returns the number of arcs incoming to v.
   	*/     
    public int inDegree(Key vertexKey) {
		int count = 0;
		for(DLLDynamicSet dynamicSet: graph.values()) {
			for(DLLDynamicSet.Node current = dynamicSet.head(); current != null; current = current.next()) {
				int compared = -1;
        		compared = ((String) current.getKey()).compareToIgnoreCase((String) vertexKey);
        		if(compared == 0)  count += 1;
        		else if(compared > 0) break;
			}
		}
		return count;
	}
        

    /**
   	*   Returns the number of arcs outgoing from v.
   	*	    
   	* @return   Returns the number of arcs outgoing from v.
   	*/  
    public int outDegree(Key vertexKey) {
    	if(vertexExists(vertexKey)) {
    		return graph.get(vertexKey).size();
    	}
    	return (Integer) null;
	}
        

    /**
    *    Returns an iterator over the vertices adjacent to v by incoming arcs.
    *	    
    * @return   Returns an iterator over the vertices adjacent to v by incoming arcs.
    */  
    public Iterator<ArrayList<Key>> inAdjacentVertices(Key vertexKey) {
		return new inAdjacentVerticesIterator(vertexKey);
	}
       
    private class inAdjacentVerticesIterator implements Iterator<ArrayList<Key>> {
    	private ArrayList<Key> adjacents = null;
    	ArrayList<Key> adjVertex = null;
    	Key lastReturned = null;
    	int index = 0;
    	Key destination = null;
    	boolean removedAlready = false;
    	
    	public inAdjacentVerticesIterator(Key vertexKey) {
    		destination = vertexKey;
    		adjacents = new ArrayList<Key>();
    		adjVertex = new ArrayList<Key>();
    		adjVertex.add(null);
    		adjVertex.add(null);
    		adjVertex.set(1, vertexKey);
    		for(DLLDynamicSet dynamicSet: graph.values()) {
    			for(DLLDynamicSet.Node current = dynamicSet.head(); current != null; current = current.next()) {
    				if(current.getKey().equals(vertexKey)) adjacents.add((Key) dynamicSet.setDataStructure());
    			}
    		}
    	}

		@Override
		public boolean hasNext() {
			return(index < adjacents.size());
		}

		@Override
		public ArrayList<Key> next() {
			if(hasNext()) {
				removedAlready = false;
				adjVertex.set(0, adjacents.get(index));
				lastReturned = adjacents.get(index);
				index += 1;
				return adjVertex;
			}
			throw new NoSuchElementException("No next 'in' adjacent vertex");
		}

		@Override
		public void remove() {
			if(removedAlready) throw new ConcurrentModificationException("'In' adjacent vertex removed already");
			if(lastReturned != null) {
				graph.get(lastReturned).delete(destination);
				lastReturned = null;
			}
			else throw new IllegalStateException("No 'in' adjacent vertex to remove");
		}

    }
    /**
    * Returns an iterator over the vertices adjacent to v by outgoing arcs.
    *	    
    * @return  Returns an iterator over the vertices adjacent to v by outgoing arcs.
    */  
    public Iterator<ArrayList<Key>> outAdjacentVertices(Key vertexKey) {
		return new outAdjacentVerticesIterator(vertexKey);
	}
    
    private class outAdjacentVerticesIterator implements Iterator<ArrayList<Key>> {
    	ArrayList<Key> adjVertex = null;
    	DLLDynamicSet vertex = null;
    	DLLDynamicSet.Node current = null;
    	Key lastReturned = null;
    	Key source = null;
    	int processed = 0;
    	boolean removedAlready = false;

    	public outAdjacentVerticesIterator(Key vertexKey) {
    		source = vertexKey;
    		adjVertex = new ArrayList<Key>();
    		adjVertex.add(null);
    		adjVertex.add(null);
    		adjVertex.set(0, vertexKey);
    		vertex = graph.get(vertexKey);
    		current = vertex.head();
    	}
    	
		@Override
		public boolean hasNext() {
			return(processed < vertex.size());
		}

		@Override
		public ArrayList<Key> next() {
			if(hasNext()) {
				removedAlready = false;
				adjVertex.set(1, (Key) current.getKey());
				lastReturned = (Key) current.getKey();
				current = current.next();
				processed += 1;
				return adjVertex;
			}
			throw new NoSuchElementException("No next 'out' adjacent vertex");
		}

		@Override
		public void remove() {
			if(removedAlready) throw new ConcurrentModificationException("'Out' adjacent vertex removed already");
			if(lastReturned != null) {
				vertex.delete(lastReturned);
				processed -= 1;
				numArcs -= 1;
				lastReturned = null;
			}
			else throw new IllegalStateException("No 'out' adjacent vertex to remove");
		}
    	
    }
        
    /**
    *   Returns the client data Object associated with vertex keyed by key.
    *	    
    * @return    Returns the client data Object associated with vertex keyed by key.
    */  
    public Data getVertexData(Key vertexKey) {
		return (Data) graph.get(vertexKey).data();
	}
       

    /**
    *   Returns the client data Object associated with arc (sourceKey, destinationKey).
    *	    
    * @return    Returns the client data Object associated with arc (sourceKey, destinationKey).
    */  
    public Data getArcData(Key sourceKey, Key destinationKey) {
    	DLLDynamicSet.Node current = graph.get(sourceKey).head();
    	while(current != null) {
    		if(current.getKey().equals(destinationKey)) return (Data) current.data();
    		current = current.next();
    	}
    	return null;
	}
       
    
    /**
    *  Returns the weight on arc (key1, key2). If none has been assigned, returns Integer 1.
    *	    
    * @return   Returns the weight on arc (key1, key2). If none has been assigned, returns Integer 1.
    */  
    public Number getArcWeight(Key sourceKey, Key destinationKey) {
    	DLLDynamicSet.Node current = graph.get(sourceKey).head();
    	while(current != null) {
    		if(current.getKey().equals(destinationKey)) return (Number) current.getValue();
    		current = current.next();
    	}
    	return null;
	}
       
  
   // Mutators
   // These are the methods by which you build and change graphs.

    /**
     *  Inserts a new isolated vertex indexed under (retrievable via) key and optionally containing an object data (which defaults to null).
     *	    
     */  
    public void insertVertex(Key vertexKey) {
    	DLLDynamicSet vertex = new DLLDynamicSet((String) vertexKey);
    	graph.put(vertexKey, vertex);
	}
    
    /**
    *  Inserts a new isolated vertex indexed under (retrievable via) key and optionally containing an object data (which defaults to null).
    *	    
    */  
    public void insertVertex(Key vertexKey, Data vertexData) {
    	DLLDynamicSet vertex = new DLLDynamicSet((String) vertexKey);
    	vertex.setData((String) vertexData);
    	graph.put(vertexKey, vertex);
	}
     
    /**
     * Inserts a new arc from an existing vertex to another, optionally containing an object data.
     *	    
     */  
     public  void insertArc(Key sourceKey, Key destinationKey) {
    	 if(undirected) {
    		 if(graph.get(sourceKey).insert(destinationKey, 1) == null) numArcs += 1;
    		 if(graph.get(destinationKey).insert(sourceKey, 1) == null) numArcs += 1;
    	 }
    	 else if(graph.get(sourceKey).insert(destinationKey, 1) == null) numArcs += 1;
	}
     
     public  Object insertArc(Key sourceKey, Key destinationKey, boolean isNew, String data, int finalTime) {
    	 return graph.get(sourceKey).insert(destinationKey, 1, isNew, data, finalTime);
	}
     
     /**
     * Inserts a new arc from an existing vertex to another, optionally containing an object data.
     *	    
     */  
    public void insertArc(Key sourceKey, Key destinationKey, Data arcData) {
    	if(undirected) {
    		if(graph.get(sourceKey).insert(destinationKey, 1, false, (String) arcData, 0) == null) numArcs += 1;
    		if(graph.get(destinationKey).insert(sourceKey, 1, false, (String) arcData, 0) == null) numArcs += 1;
    	}
    	else if(graph.get(sourceKey).insert(destinationKey, 1, false, (String) arcData, 0) == null) numArcs += 1;
	}
      
    /**
     * Changes the data Object associated with Vertex v to data.
     *	    
     */  
    public void setVertexData(Key vertexKey, Data vertexData) {
    	graph.get(vertexKey).setData((String) vertexData);
	}
        

    /**
     * Changes the data Object associated with Arc a to arcData.
     *	    
     */   
    public void setArcData(Key sourceKey, Key destinationKey, Data arcData) {
    	DLLDynamicSet.Node current = graph.get(sourceKey).head();
    	while(current != null) {
    		if(current.getKey().equals(destinationKey)) current.setData((String) arcData);
    		current = current.next();
    	}
	}
      
    /**
     * Deletes a vertex and all its incident arcs (and edges under the undirected extension).
     *	    
     * @return Returns the client data object formerly stored at v.
     */  
    public Data removeVertex(Key vertexKey) {
    	return (Data) graph.remove(vertexKey);
	}
        

    /**
    * Removes an arc with Source vertex having of a Key of sourceKey and Destination vertex having of a Key of destinationKey.
    *	    
    * @return Returns the client data object formerly stored at arc with Source vertex sourceKey , Key destinationKey.
    */   
    public Data removeArc(Key sourceKey, Key destinationKey) {
    	DLLDynamicSet.Node current = null;
    	if(vertexExists(sourceKey)) {
    		current = graph.get(sourceKey).head();
    		while(current != null) {
    			if(current.getKey().equals(destinationKey)) {
    				graph.get(sourceKey).delete(destinationKey);
    				numArcs -= 1;
    				return (Data) current;
    			}
    			current = current.next();
    		}
    	}
    	if(current == null) return null;
    	else return (Data) current.data();
	}
        
    /**
    * Reverse the direction of an arc with Source vertex having of a Key of sourceKey and Destination vertex having of a Key of destinationKey.
    *	    
    */  
    public void reverseDirection(Key sourceKey, Key destinationKey) {
    	if(arcExists(destinationKey, sourceKey)) {
    		System.out.println("Reversed arc of " + sourceKey + " " + destinationKey + " already exists");
    	}
    	else {
    		graph.get(sourceKey).delete(destinationKey);
    		insertArc(destinationKey, sourceKey);
    	}
	}
        
    /**
    * Reverse the direction of all arcs of the graph in place (modifies the graph). 
    *	    
    */  
    public void transposeGraph() {
    	if(!undirected) {
    		DLLDynamicSet vertex = null;
        	Key source = null;
        	Key destination = null;
        	int E = 0;
        	for(DLLDynamicSet dynamicSet: graph.values()) {
        		vertex = dynamicSet;
        		for(DLLDynamicSet.Node current = vertex.head(); current != null; current = current.next()) {
        			if(!current.isNew()) {
        				destination = (Key) vertex.setDataStructure();
        				source = (Key) current.getKey();
        				if(insertArc(source, destination, true, current.data(), current.finish()) == null) vertex.delete(source);
        			}
        		}
        	}
        	for(DLLDynamicSet dynamicSet: graph.values()) {
        		vertex = dynamicSet;
        		for(DLLDynamicSet.Node current = vertex.head(); current != null; current = current.next()) {
        			current.makeOld();
        		}
        	}
    	}
	}
    
    /**
    * Sets the weight on Arc with Source vertex having of a Key of sourceKey and Destination vertex having of a Key of destinationKey.
    *	    
    */  
    public void setArcWeight(Key sourceKey, Key destinationKey, Number weight) {
    	if(vertexExists(sourceKey)) {
    		DLLDynamicSet.Node current = graph.get(sourceKey).head();
    		while(current != null) {
    			if(current.getKey().equals(destinationKey)) current.setValue(weight);
    			current = current.next();
    		}
    	}
	}
    
    
    // Annotators
    /**
    * Adds or changes the Value value for the Property property to a Vertex vertex having a Key vertexKey.
    *	    
    */  
    public void setAnnotation(Key vertexKey, Object property, Object value) {

    	graph.get(vertexKey).annotate(property, value);
    }
    
    /**
    *  Adds or changes the Value for the Property property of an Arc with Source vertex having a Key of sourceKey and Destination vertex having of a Key of destinationKey.
    *	    
    */ 
    public void setAnnotation(Key sourceKey, Key destinationKey, Object property, Object value) {
    	DLLDynamicSet.Node current = graph.get(sourceKey).head();
    	while (current != null) {
    		if(current.getKey().equals(destinationKey)) {
    			current.annotate(property, value);
    			break;
    		}
    		current = current.next();
    	}
	} 
    
    /**
     * Returns the value for Property property of the vertex with the Key vertexKey.
     *
     * @return Returns the value for Property property for the Vertex.
     * 
     */ 
     public Object getAnnotation(Key vertexKey, Object property) {
		return graph.get(vertexKey).getAnnotation(property);
	}
    
    /**
    * Returns the value for for the Property property of an Arc with Source vertex having a Key of sourceKey and Destination vertex having of a Key of destinationKey.
    *
    * @return Returns the value for for the Property property for the Arc.  
    */ 
    public Object getAnnotation(Key sourceKey, Key destinationKey, Object property) {
    	DLLDynamicSet.Node current = graph.get(sourceKey).head();
    	while (current != null) {
    		if(current.getKey().equals(destinationKey)) {
    			return current.getAnnotation(property);
    		}
    		current = current.next();
    	}
    	return null;
    }
   
     /**
     * Returns the value for the property that was removed indexed by the Key annotationKey a vertex with the Key vertexKey.
     * @return Returns the value for the property that was removed from the vertex.
     */ 
    public Object removeAnnotation(Key vertexKey, Object property) {
    	return graph.get(vertexKey).removeAnnotation(property);
    }

    /**
    * Remove the value for the Property property of the arc with the with Source vertex sourceKey , Key destinationKey.
    *
    * @return  Returns the value for the Property property that was removed from the arc.   
    */ 
    public Object removeAnnotation( Key sourceKey, Key destinationKey, Object property ) {
    	DLLDynamicSet.Node current = graph.get(sourceKey).head();
    	while (current != null) {
    		if(current.getKey().equals(destinationKey)) {
    			return current.removeAnnotation(property);
    		}
    		current = current.next();
    	}
    	return null;
	}
   

    /**
    *  Removes all values on vertices or arcs for the Property property. Use this to clean up between runs. 
    *	    
    */ 
    public void clearAnnotations(Object property) {
    	DLLDynamicSet vertex = null;
    	for(DLLDynamicSet dynamicSet: graph.values()) {
    		vertex = dynamicSet;
    		vertex.removeAnnotation(property);
    		for(DLLDynamicSet.Node current = vertex.head(); current != null; current = current.next()) {
    			current.removeAnnotation(property);
    		}
    	}
	}
    
    // Strongly connected components
    /**
     * Computes the strongly connected components
     * 
     * @returns An arraylist of components
     */
    ArrayList<ArrayList<Key>> components = new ArrayList<ArrayList<Key>>();
    public ArrayList<ArrayList<Key>> SCC() {
    	DFS();
    	transposeGraph();
    	return DFST();
    }
    
    /**
     * Regular depth first search
     */
    int time = 0;
    int scc = 0;
    public void DFS() {
    	for(DLLDynamicSet dynamicSet: graph.values()) {
    		dynamicSet.annotate("COLOR", "WHITE");
    		dynamicSet.annotate("PARENT", null);
    	}
    	time = 0;
    	for(DLLDynamicSet dynamicSet: graph.values()) {
    		DLLDynamicSet vertex = dynamicSet;
    		if(vertex.color().equals("WHITE")) {
    			DFSVISIT(vertex);
    		}
    	}
    }

    /**
     * Depth first search on transposed graph to compute SCC
     * 
     * @return Array list of components
     */
    public ArrayList<ArrayList<Key>> DFST() {
    	for(DLLDynamicSet dynamicSet: graph.values()) {
    		dynamicSet.annotate("COLOR", "WHITE");
    		dynamicSet.annotate("PARENT", null);
    	}
    	time = 0;
    	scc = 0;
    	for(int x = 0; x < vertexCount(); x++) {
    		int largest = 0;
    		DLLDynamicSet vertex = null;
    		// Takes from decreasing finish() times
    		for(DLLDynamicSet dynamicSet: graph.values()) {
    			DLLDynamicSet possible = dynamicSet;
    			if(possible.color().equals("WHITE")) {
					if(possible.finish() > largest) {
						largest = possible.finish();
						vertex = possible;
					}
				}
    		}
    		if(vertex != null) {
    			ArrayList<Key> component = new ArrayList<Key>();
    			components.add(component);
    			// System.out.println(vertex.finish()); finish() times (should print in decreasing order if works)
    			scc = scc + 1;
    			componentDFSVISIT(vertex);
    		}
    	}
    	return components;
    }
    
    /**
     * Regular depth first search visit
     * 
     * @param vertex visiting
     */
    private void DFSVISIT(DLLDynamicSet vertex) {
    	time = time + 1;
    	vertex.annotate("DISCOVERY", time);
    	vertex.annotate("COLOR", "GRAY");
    	DLLDynamicSet adjVertex = null;
    	for(DLLDynamicSet.Node arc = vertex.head(); arc != null; arc = arc.next()) {
    		adjVertex = graph.get((Key) arc.getKey());
    		if(adjVertex.color().equals("WHITE")) {
    			adjVertex.annotate("PARENT", vertex.setDataStructure());
    			DFSVISIT(adjVertex);
    		}
    	}
    	vertex.annotate("COLOR", "BLACK");
    	time = time + 1;
    	vertex.annotate("FINISH", time);
    }
    
    /**
     * Component depth first search visit
     * Adds this vertex into right component in array list
     * 
     * @param vertex visiting
     */
    private void componentDFSVISIT(DLLDynamicSet vertex) {
    	time = time + 1;
    	vertex.annotate("DISCOVERY", time);
    	vertex.annotate("COLOR", "GRAY");
    	// vertex.SCC = scc;
    	components.get(scc - 1).add((Key) vertex.setDataStructure());
    	DLLDynamicSet adjVertex = null;
    	for(DLLDynamicSet.Node arc = vertex.head(); arc != null; arc = arc.next()) {
    		adjVertex = graph.get((Key) arc.getKey());
    		if(adjVertex.color().equals("WHITE")) {
    			adjVertex.annotate("PARENT", vertex.setDataStructure());
    			componentDFSVISIT(adjVertex);
    		}
    	}
    	vertex.annotate("COLOR", "BLACK");
    	time = time + 1;
    	vertex.annotate("FINISH", time);
    }
    
    
    // Additional Useful Methods
 	/**
 	 * Loops through adjacency list and prints the list
 	 */
     public void printgraph() {
     	System.out.println("Vertices:");
     	for(DLLDynamicSet dynamicSet: graph.values()) {
     		System.out.print(dynamicSet.setDataStructure() + ": ");
     		for(DLLDynamicSet.Node current = dynamicSet.head(); current != null; current = current.next()) {
     			System.out.print(current.getKey() + " ");
     		}
     		System.out.println(" ");
     	}
     }
    
     /**
      * Sets boolean value undirected to true
      */
     public void undirected() {
 		undirected = true;
 	}
     
     /**
      * Returns the number of edges
      * 
      * @return int number of edges
      */
     public int numEdges() {
     	return numArcs / 2;
     }
     
 	/**
 	 * Loops through the adjacency list to compute the analysis on degrees
 	 * 
 	 * @return array list of double
 	 */
 	public ArrayList<Double> numbers() {
    	ArrayList<Double> numbers = new ArrayList<Double>();
    	double minInDegree = 9999;
    	double totalInDegree = 0;
    	double maxInDegree = -1;
    	double minOutDegree = 9999;
    	double totalOutDegree = 0;
    	double maxOutDegree = -1;
    	numbers.add(null); // minInDegree
    	numbers.add(null); // totalInDegree
    	numbers.add(null); // maxInDegree
    	numbers.add(null); // minOutDegree
    	numbers.add(null); // totalOutDegree
    	numbers.add(null); // maxOutDegree
    	for(DLLDynamicSet dynamicSet: graph.values()) {
    		double inDegree = inDegree((Key) dynamicSet.setDataStructure());
    		double outDegree = outDegree(((Key) dynamicSet.setDataStructure()));
    		totalInDegree += inDegree;
    		totalOutDegree += outDegree;
    		if(inDegree < minInDegree) minInDegree = inDegree;
    		else if(maxInDegree < inDegree) maxInDegree = inDegree;
    		if(outDegree < minOutDegree) minOutDegree = outDegree;
    		else if(maxOutDegree < outDegree) maxOutDegree = outDegree;
    	}
    	// Average
    	totalInDegree = totalInDegree / vertexCount();
    	totalOutDegree = totalOutDegree / vertexCount();
    	// Setting in right places
    	numbers.set(0, minInDegree);
    	numbers.set(1, totalInDegree);
    	numbers.set(2, maxInDegree);
    	numbers.set(3, minOutDegree);
    	numbers.set(4, totalOutDegree);
    	numbers.set(5, maxOutDegree);
    	return numbers;
    }
    
 	/**
 	 * Loops through adjacency list.
 	 * For each arc, it will search the reversed arc if in graph, if not then it will add in new arc.
 	 */
 	public void makeUndirected() {
    	DLLDynamicSet vertex = null;
    	Key source = null;
    	Key destination = null;
    	int E = 0;
    	for(DLLDynamicSet dynamicSet: graph.values()) {
    		vertex = dynamicSet;
    		for(DLLDynamicSet.Node current = vertex.head(); current != null; current = current.next()) {
    			if(!current.isNew()) {
    				destination = (Key) vertex.setDataStructure();
    				source = (Key) current.getKey();
    				insertArc(source, destination, true, current.data(), current.finish());
    			}
    		}
    	}
    	for(DLLDynamicSet dynamicSet: graph.values()) {
    		vertex = dynamicSet;
    		for(DLLDynamicSet.Node current = vertex.head(); current != null; current = current.next()) {
    			current.makeOld();
    		}
    	}
    }
 	
 	
 	
 	
 	
 	
 	/**
 	 * Loops through adjacency list. For each arc, it will search the reversed arc if in graph, 
 	 * if in graph then adds one to the number of reciprocity.
 	 * 
 	 * @returns the number of reciprocity found divided by the number of total arcs
 	 */
 	public double reciprocity() {
 		int numReciprocity = 0;
 		for(DLLDynamicSet dynamicSet: graph.values()) {
 			DLLDynamicSet vertex = dynamicSet;
 			for(DLLDynamicSet.Node current = vertex.head(); current != null; current = current.next()) {
 				int compared = -1;
 				for(DLLDynamicSet.Node recip = graph.get(current.getKey()).head(); recip != null; recip = recip.next()) {
 					compared = ((String) recip.getKey()).compareToIgnoreCase(vertex.setDataStructure());
					if(compared == 0) numReciprocity += 1;
					else if (compared > 0) break;
				}
 			}
 		}
 		return ((double)numReciprocity / (double)numArcs);
 	}
 	
 	// Variables for degree coefficient
 	double r = 0;
 	double ki = 0;
 	double s1 = 0;
 	double s2 = 0;
 	double s3 = 0;
 	double se = 0;
 	double adjDegree = 0;
 	/**
 	 * Loops through the adjacency list
 	 * For every vertex, the degree will be used to compute s1, s2, s3
 	 * For every arc, the degree for the source and destination will be used to compute se
 	 * 
 	 * @return the degree corellation computed with the r equation
 	 */
 	public double degreeCorellation() {
 		for(DLLDynamicSet dynamicSet: graph.values()) {
 			ki = dynamicSet.size();
 			s1 += ki;
 			s2 += (ki * ki);
 			s3 += (ki * ki * ki);
 			for(DLLDynamicSet.Node current = dynamicSet.head(); current != null; current = current.next()) {
 				adjDegree = graph.get(current.getKey()).size();
 				se += (ki * adjDegree);
 			}
 		}
 		r = ((s1*se) - (s2 * s2)) / ((s1*s3) - (s2 * s2));
 		return r;
 	}
 	
 	// Variables for clustering coefficient
 	double numTriangles = 0;
 	double numTriples = 0;
 	/**
 	 * Loops through vertices in adjacency list
 	 * Two loops for the nodes in the linked list for the pairs to check
 	 * 
 	 * @return the number of triangles found divided by the total number of possible triangles
 	 */
 	public double clustering() {
 		numTriangles = 0;
 	 	numTriples = 0;
 		for(DLLDynamicSet dynamicSet: graph.values()) {
 			DLLDynamicSet vertex = dynamicSet;
 			for(DLLDynamicSet.Node current = vertex.head(); current != null; current = current.next()) {
 				for(DLLDynamicSet.Node friendo = current.next(); friendo != null; friendo = friendo.next()) {
 					numTriples += 1;
 					if(graph.get((Key) current.getKey()).retrieve((Key)friendo.getKey()) != null) numTriangles += 1;
 				}
 			}
 		}
 		return((numTriangles)/numTriples);
 	}
 	
 	// Directed Clustering
 	double numCycle = 0;
 	double cycleTriple = 0;
 	double middle = 0;
 	double middleTriple = 0;
 	double numin = 0;
 	double numinTriple = 0;
 	double numout = 0;
 	double numOutTriple = 0;
 	Iterator<ArrayList<Key>> arcs;
 	Iterator<ArrayList<Key>> arcs2;
 	ArrayList<Key> arc = null;
 	ArrayList<Key> arc2 = null;
 	DLLDynamicSet jVertex;
 	DLLDynamicSet hVertex;
 	
 	/**
 	 * Finds the percentage of cycle clustering
 	 * 
 	 * @return double, percent of number of cycles out of possible number of cycles
 	 */
 	public double cycleClus() {
 		numCycle = 0;
 	 	cycleTriple = 0;
 	 	for(DLLDynamicSet dynamicSet: graph.values()) {
 			DLLDynamicSet i = dynamicSet;
 			for(DLLDynamicSet.Node j = i.head(); j != null; j = j.next()) {
 				jVertex = graph.get(j.getKey());
 				for(DLLDynamicSet.Node h = jVertex.head(); h != null; h = h.next()) {
 					numCycle += 1;
 					if(graph.get(h.getKey()).retrieve(i.setDataStructure()) != null) cycleTriple += 1;
 				}
 			}
 	 	}
 	 	
 		return (numCycle/cycleTriple);
 	}
 	
 	/**
 	 * Number of clustering where vertex i is the middle man
 	 * 
 	 * @return double, percent number of clustering where vertex i is the middle man
 	 */
 	public double middleClus() {
 		numin = 0;
 	 	numinTriple = 0;
 		for(DLLDynamicSet dynamicSet: graph.values()) {
 			DLLDynamicSet i = dynamicSet;
 			arcs = inAdjacentVertices((Key) i.setDataStructure());
 			while(arcs.hasNext()) {
 				arc = arcs.next();
 				jVertex = graph.get(arc.get(0));
 				for(DLLDynamicSet.Node h = jVertex.head(); h != null; h = h.next()) {
 					middleTriple += 1;
 					if(i.retrieve((Key) h.getKey()) != null) middle += 1;
 				}
 			}
 		}
 		return (middle/middleTriple);
 	}
 	
 	/**
 	 * Method to return the number of in clustering
 	 * 
 	 * @return the percentage of vertex i being directed by two vertices that directs to each other
 	 */
 	public double inClus() {
 		numin = 0;
 	 	numinTriple = 0;
 		for(DLLDynamicSet dynamicSet: graph.values()) {
 			DLLDynamicSet i = dynamicSet;
 			arcs = inAdjacentVertices((Key) i.setDataStructure());
 			arcs2 = inAdjacentVertices((Key) i.setDataStructure());
 			if(arcs2.hasNext()) {
 				arcs2.next();
 			}
 			while(arcs.hasNext()) {
 				arc = arcs.next();
 				jVertex = graph.get(arc.get(0));
 				for(DLLDynamicSet.Node h = jVertex.head(); h != null; h = h.next()) {
 					middleTriple += 1;
 					if(i.retrieve((Key) h.getKey()) != null) middle += 1;
 				}
 				
 				while(arcs2.hasNext()) {
 					arc2 = arcs2.next();
 					numinTriple += 1;
 					hVertex = graph.get(arc2.get(0));
 					if(jVertex.retrieve(hVertex.setDataStructure()) != null) numin += 1;
 					else if(hVertex.retrieve(jVertex.setDataStructure()) != null) numin += 1;
 				}
 			}
 		}
 		return (numin/numinTriple);
 	}
 	
 	/**
 	 * Method that finds the number of vertices that point to each other that both is pointed by vertex i
 	 * 
 	 * @return the percentage of out clustering that forms a triangle
 	 */
 	public double outClustering() {
 		numTriangles = 0;
 	 	numTriples = 0;
 		for(DLLDynamicSet dynamicSet: graph.values()) {
 			DLLDynamicSet vertex = dynamicSet;
 			for(DLLDynamicSet.Node current = vertex.head(); current != null; current = current.next()) {
 				for(DLLDynamicSet.Node friendo = current.next(); friendo != null; friendo = friendo.next()) {
 					numTriples += 1;
 					if(graph.get((Key) current.getKey()).retrieve((Key)friendo.getKey()) != null) numTriangles += 1;
 					else if(graph.get((Key) friendo.getKey()).retrieve((Key)current.getKey()) != null) numTriangles  += 1;
 				}
 			}
 		}
 		return((numTriangles)/numTriples);
 	}
 	
 	// Variables for mean geodesic distance and diameter
 	int diameter = 0;
 	double geodesic = 0;
 	double numerator = 0;
 	double denominator = 0;
 	
 	/**
 	 * Uses breadth first search for each vertex to compute the mean geodesic distance and diameter
 	 * 
 	 * @return an arraylist with index 0 as the geodesic distance and index 1 as the diameter
 	 */
 	public ArrayList<Number> BFS() {
 		ArrayList<Number> mean = new ArrayList<Number>();
 		for(DLLDynamicSet dynamicSet: graph.values()) {
 			BFS(dynamicSet);
 		}
 		geodesic = numerator / denominator;
 		mean.add(geodesic);
 		mean.add(diameter);
 		return mean;
 	}
 	
 	/**
 	 * Breadth first search that will compute the shortest paths from the given vertex
 	 * 
 	 * @param vertex as source
 	 */
 	private void BFS(DLLDynamicSet vertex) {
 		DLLDynamicSet u;
 		DLLDynamicSet adj;
 		for(DLLDynamicSet dynamicSet: graph.values()) {
 			u = dynamicSet;
 			u.annotate("COLOR", "WHITE");
 			u.annotate("DISCOVERY", 99999);
 			u.annotate("PARENT", null);
 		}
 		vertex.annotate("COLOR", "GRAY");
 		vertex.annotate("DISCOVERY", 0);
 		vertex.annotate("PARENT", null);
 		Queue<DLLDynamicSet> qq = new ArrayDeque<DLLDynamicSet>();
 		qq.add(vertex);
 		while(!qq.isEmpty()) {
 			u = qq.remove();
 			for(DLLDynamicSet.Node current = u.head(); current != null; current = current.next()) {
 				adj = graph.get((Key) current.getKey());
 				if(adj.getAnnotation("COLOR").equals("WHITE")) {
 					adj.annotate("COLOR", "GRAY");
 					adj.annotate("DISCOVERY", ((int)u.getAnnotation("DISCOVERY") + 1));
 					numerator += adj.discovery();
 					denominator += 1;
 					if((int)adj.getAnnotation("DISCOVERY") > diameter) diameter = (int) adj.getAnnotation("DISCOVERY");
 					adj.annotate("PARENT", u.setDataStructure());
 					qq.add(adj);
 				}
 			}
 			u.annotate("COLOR", "BLACK");
 		}
 	}
}
