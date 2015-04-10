package ics311;
import static org.junit.Assert.*;
import ics311.DLLDynamicSet.Node;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

import org.junit.Test;

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

public class TestGraphADT {
	GraphADT<String, String> graph = new GraphADT<String, String>();
	Iterator<ArrayList<String>> arcs = null;
	Iterator<String> vertices = null;
	ArrayList<String> arc = null;

	@Test
	public void testVertexCount() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		assertEquals(3, graph.vertexCount());
	}

	@Test
	public void testArcCount() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.insertArc("lul", ":D");
		graph.insertArc("lul", "yesh");
		assertEquals(2, graph.arcCount());
	}

	@Test
	public void testArcs() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.insertArc("lul", ":D");
		graph.insertArc("lul", "yesh");
		arcs = graph.arcs();
		arc = arcs.next();
		assertEquals("lul", arc.get(0));
		assertEquals(":D", arc.get(1));
		arcs.remove();
		assertEquals(1, graph.arcCount());
		arc = arcs.next();
		assertEquals("lul", arc.get(0));
		assertEquals("yesh", arc.get(1));
		arcs.remove();
		assertEquals(0, graph.arcCount());
		assertFalse(arcs.hasNext());
		// arcs.next();
		// arcs.remove();
	}

	@Test
	public void testVertices() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		vertices = graph.vertices();
		assertTrue(vertices.hasNext());
		vertices.next();
		assertTrue(vertices.hasNext());
		vertices.next();
		assertTrue(vertices.hasNext());
		vertices.next();
		assertFalse(vertices.hasNext());
		// vertices.next();
		// vertices.remove();
	}
	
	@Test
	public void testArcExists() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.insertArc("lul", ":D");
		graph.insertArc("lul", "yesh");
		assertTrue(graph.arcExists("lul", ":D"));
		assertFalse(graph.arcExists(":D", "yeshD"));
	}

	@Test
	public void testVertexExists() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		assertTrue(graph.vertexExists(":D"));
		assertFalse(graph.vertexExists("hue"));
	}

	@Test
	public void testInDegree() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.insertArc("lul", ":D");
		graph.insertArc("lul", "yesh");
		assertEquals(0, graph.inDegree("lul"));
		assertEquals(1, graph.inDegree(":D"));
		assertEquals(1, graph.inDegree("yesh"));
	}

	@Test
	public void testOutDegree() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.insertArc("lul", ":D");
		graph.insertArc("lul", "yesh");
		assertEquals(2, graph.outDegree("lul"));
		assertEquals(0, graph.outDegree(":D"));
		assertEquals(0, graph.outDegree("yesh"));
	}

	@Test
	public void testInAdjacentVertices() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.insertArc("lul", ":D");
		graph.insertArc("lul", "yesh");
		arcs = graph.inAdjacentVertices(":D");
		assertEquals("lul", arcs.next().get(0));
		arcs.remove();
		assertEquals(1, graph.outDegree("lul"));
		assertFalse(arcs.hasNext());
		// arcs.next();
		// arcs.remove();
	}

	@Test
	public void testOutAdjacentVertices() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.insertArc("lul", ":D");
		graph.insertArc("lul", "yesh");
		arcs = graph.outAdjacentVertices("lul");
		assertEquals(":D", arcs.next().get(1));
		arcs.remove();
		assertEquals(1, graph.arcCount());
		assertEquals("yesh", arcs.next().get(1));
		arcs.remove();
		assertEquals(0, graph.arcCount());
		assertFalse(arcs.hasNext());
		// arcs.next();
		// arcs.remove();
	}

	@Test
	public void testGetVertexData() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.setVertexData("yesh", "Works");
		assertEquals("Works", graph.getVertexData("yesh"));
		assertNull(graph.getVertexData("lul"));
	}

	@Test
	public void testGetArcData() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.insertArc("lul", ":D");
		graph.insertArc("lul", "yesh", "plz");
		assertEquals("plz", graph.getArcData("lul", "yesh"));
	}

	@Test
	public void testGetArcWeight() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.insertArc("lul", ":D");
		graph.insertArc("lul", "yesh");
		assertEquals(1, graph.getArcWeight("lul", ":D"));
	}

	@Test
	public void testInsertVertexKey() {
		graph.insertVertex("lul");
		assertEquals(1, graph.vertexCount());
	}

	@Test
	public void testInsertVertexKeyData() {
		graph.insertVertex("lul");
		graph.insertVertex(":D", "plz");
		graph.insertVertex("yesh");
		assertEquals("plz", graph.getVertexData(":D"));
	}

	@Test
	public void testInsertArcKeyKey() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.insertArc("lul", ":D");
		graph.insertArc("lul", "yesh");
		assertEquals(2, graph.arcCount());
	}

	@Test
	public void testInsertArcKeyKeyData() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.insertArc("lul", ":D");
		graph.insertArc("lul", "yesh", "alright");
		assertEquals("alright", graph.getArcData("lul", "yesh"));
	}

	@Test
	public void testSetVertexData() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.setVertexData("yesh", "Works");
		assertEquals("Works", graph.getVertexData("yesh"));
	}

	@Test
	public void testSetArcData() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.insertArc("lul", ":D");
		graph.insertArc("lul", "yesh");
		graph.setArcData("lul", "yesh", "alright");
		assertEquals("alright", graph.getArcData("lul", "yesh"));
	}

	@Test
	public void testRemoveVertex() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.removeVertex("yesh");
		assertEquals(2, graph.vertexCount());
		graph.removeVertex(":D");
		assertEquals(1, graph.vertexCount());
		graph.removeVertex("lul");
		assertEquals(0, graph.vertexCount());
		assertEquals(null, graph.removeVertex("hue"));
		assertEquals(0, graph.vertexCount());
	}

	@Test
	public void testRemoveArc() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.insertArc("lul", ":D");
		graph.insertArc("lul", "yesh");
		assertEquals(null, graph.removeArc("lul", "wat"));
		graph.removeArc("lul", "yesh");
		assertEquals(1, graph.arcCount());
	}

	@Test
	public void testReverseDirection() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.insertArc("lul", ":D");
		graph.insertArc("lul", "yesh");
		graph.reverseDirection("lul", ":D");
		assertEquals(1, graph.outDegree("lul"));
		assertEquals(1, graph.outDegree(":D"));
		assertEquals(0, graph.inDegree(":D"));
		graph.insertArc("lul", ":D");
		// graph.reverseDirection("lul", ":D");
	}

	@Test
	public void testTransposeGraph() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.insertVertex("4th");
		graph.insertVertex("5th");
		graph.insertArc("lul", ":D");
		graph.insertArc("lul", "yesh");
		graph.insertArc("4th", "5th");
		// graph.printAdjacencyList();
		assertEquals(5, graph.vertexCount());
		assertEquals(3, graph.arcCount());
		graph.transposeGraph();
		assertEquals(3, graph.arcCount());
		assertEquals(0, graph.outDegree("lul"));
		assertEquals(1, graph.outDegree(":D"));
		assertEquals(1, graph.outDegree("yesh"));
		assertEquals(0, graph.inDegree(":D"));
		assertEquals(0, graph.inDegree("yesh"));
		assertEquals(0, graph.outDegree("4th"));
		assertEquals(1, graph.inDegree("4th"));
		// graph.printAdjacencyList();
		graph.transposeGraph();
		assertEquals(3, graph.arcCount());
		assertEquals(2, graph.outDegree("lul"));
		assertEquals(0, graph.outDegree(":D"));
		assertEquals(0, graph.outDegree("yesh"));
		assertEquals(1, graph.inDegree(":D"));
		assertEquals(1, graph.inDegree("yesh"));
		assertEquals(1, graph.outDegree("4th"));
		assertEquals(0, graph.inDegree("4th"));
		// graph.printAdjacencyList();
	}

	@Test
	public void testSetArcWeight() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.insertArc("lul", ":D");
		graph.insertArc("lul", "yesh");
		graph.setArcWeight("lul", ":D", 5);
		assertEquals(5, graph.getArcWeight("lul", ":D"));
	}

	@Test
	public void testSetAnnotationKeyObjectObject() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.setAnnotation(":D", "COLOR", "whitey");
		assertEquals("whitey", graph.getAnnotation(":D", "COLOR"));
		graph.setAnnotation("lul", "COLOR", "brown");
		assertEquals("brown", graph.getAnnotation("lul", "COLOR"));
		graph.setAnnotation("yesh", "COLOR", "good");
		assertEquals("good", graph.getAnnotation("yesh", "COLOR"));
	}

	@Test
	public void testSetAnnotationKeyKeyObjectObject() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.insertArc("lul", ":D");
		graph.insertArc("lul", "yesh");
		graph.setAnnotation("lul", "yesh", "PARENT", "hue");
	}

	@Test
	public void testGetAnnotationKeyObject() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.setAnnotation(":D", "COLOR", "whitey");
		assertEquals("whitey", graph.getAnnotation(":D", "COLOR"));
	}

	@Test
	public void testGetAnnotationKeyKeyObject() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.insertArc("lul", ":D");
		graph.insertArc("lul", "yesh");
		graph.setAnnotation("lul", "yesh", "DISCOVERY", 2);
		assertEquals(2, graph.getAnnotation("lul", "yesh", "DISCOVERY"));
	}

	@Test
	public void testRemoveAnnotationKeyObject() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.setAnnotation(":D", "COLOR", "whitey");
		assertEquals("whitey", graph.getAnnotation(":D", "COLOR"));
		graph.setAnnotation("lul", "COLOR", "brown");
		assertEquals("brown", graph.getAnnotation("lul", "COLOR"));
		graph.setAnnotation("yesh", "COLOR", "good");
		assertEquals("good", graph.getAnnotation("yesh", "COLOR"));
		graph.removeAnnotation("lul", "COLOR");
		assertEquals(null, graph.getAnnotation("lul", "COLOR"));
	}

	@Test
	public void testRemoveAnnotationKeyKeyObject() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.insertArc("lul", ":D");
		graph.insertArc("lul", "yesh");
		graph.setAnnotation("lul", "yesh", "DISCOVERY", 2);
		assertEquals(2, graph.getAnnotation("lul", "yesh", "DISCOVERY"));
		graph.removeAnnotation("lul", "yesh", "DISCOVERY");
		assertEquals(-1, graph.getAnnotation("lul", "yesh", "DISCOVERY"));
	}

	@Test
	public void testClearAnnotations() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.insertArc("lul", ":D");
		graph.insertArc("lul", "yesh");
		graph.setAnnotation(":D", "COLOR", "whitey");
		assertEquals("whitey", graph.getAnnotation(":D", "COLOR"));
		graph.setAnnotation("lul", "COLOR", "brown");
		assertEquals("brown", graph.getAnnotation("lul", "COLOR"));
		graph.setAnnotation("lul", "yesh", "DISCOVERY", 2);
		assertEquals(2, graph.getAnnotation("lul", "yesh", "DISCOVERY"));
		graph.clearAnnotations("COLOR");
		assertNull(graph.getAnnotation(":D", "COLOR"));
		assertNull(graph.getAnnotation("lul", "COLOR"));
		graph.clearAnnotations("DISCOVERY");
		assertEquals(-1, graph.getAnnotation("lul", "yesh", "DISCOVERY"));
	}

	@Test
	public void SCC() {
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.insertVertex("4th");
		graph.insertVertex("5th");
		graph.insertArc("lul", ":D");
		graph.insertArc("lul", "yesh");
		assertEquals(5, graph.SCC().size());
	}
	
	@Test
	public void undirected() {
		graph.undirected();
		graph.insertVertex("lul");
		graph.insertVertex(":D");
		graph.insertVertex("yesh");
		graph.insertVertex("4th");
		graph.insertVertex("5th");
		graph.insertArc("lul", ":D");
		graph.insertArc("lul", "yesh");
		graph.insertArc(":D", "lul");
		assertEquals(2, graph.numEdges());
		// graph.printAdjacencyList(); 
	}
}
