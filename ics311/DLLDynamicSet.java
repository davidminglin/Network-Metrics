package ics311;
import java.util.Map.Entry;

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

public class DLLDynamicSet<Key extends Comparable<Key>, Value> implements
		DynamicSet<Key, Value> {
	public class Node<Key, Value> implements Entry<Key, Value> {
		private Node next;
		private Node previous;
		private Key key;
		private Value value;
		private boolean isNew = false;
		private String data = null;
		private String COLOR = null;
		private String PARENT = null;
		private int DISCOVERY = -1;
		private int FINISH = -1;

		public Node() {
			this.key = null;
			this.next = null;
			this.previous = null;
			this.value = null;
		}

		/**
		 * Sets the values of the key the value of data stored in a set.
		 * 
		 * @param value
		 *            that will overwrite the current value stored.
		 * @param key
		 *            The key under which this data is stored.
		 */
		public Node(Key key, Value value) {
			this.key = key;
			this.value = value;
			this.next = null;
			this.previous = null;
		}
		
		public Node(Key key, Value value, boolean isNew, String data) {
			this.key = key;
			this.value = value;
			this.next = null;
			this.previous = null;
			this.isNew = isNew;
			this.data = data;
		}
		/**
		 * Returns the key under which the value of the object is stored in a
		 * set.
		 * 
		 * @return key The key under which this data is stored.
		 */
		@Override
		public Key getKey() {

			return key;
		}

		/**
		 * Returns the value of the object the is stored under the key in a set.
		 * 
		 * @return value The data currently stored under the given key.
		 */
		@Override
		public Value getValue() {

			return value;
		}

		/**
		 * Changes the current value to the argument's value.
		 * 
		 * @param value
		 *            that will overwrite the current value stored.
		 * @return value The previous value stored which has been replaced.
		 */
		@Override
		public Value setValue(Value value) {
			Value oldValue = this.value;
			this.value = value;
			return oldValue;
		}

		/*
		 * Creates a new node Sets k from parameter given and next reference to
		 * null
		 * 
		 * @param k to identify the value
		 */
		public Node(Key k, Node next) {
			this.key = k;
			this.next = next;
			this.previous = null;
			this.value = (Value) k;
		}

		/*
		 * Creates a node and sets the values
		 * 
		 * @param k to identify the value, and reference to link as next
		 */
		public Node(Key k, Node next, Node prev) {
			this.key = k;
			this.next = next;
			this.previous = prev;
			this.value = (Value) k;
		}
		
		public void setData(String arcData) {
			this.data = arcData;
		}
		
		public void annotate(Object property, Object value) {
			if(property.equals("COLOR")) this.COLOR = (String) value;
			else if(property.equals("PARENT")) this.PARENT = (String) value;
			else if(property.equals("DISCOVERY")) this.DISCOVERY = (int) value;
			else if(property.equals("FINISH")) this.FINISH = (int) value;
		}
		
		public Object getAnnotation(Object property) {
			if(property.equals("COLOR")) return this.COLOR;
			else if(property.equals("PARENT")) return this.PARENT;
			else if(property.equals("DISCOVERY")) return this.DISCOVERY;
			else if(property.equals("FINISH")) return this.FINISH;
			return null;
		}
		
		public Object removeAnnotation(Object property) {
			Object old = null;
			if(property.equals("COLOR")) {
				old = COLOR;
				COLOR = null;
			}
			else if(property.equals("PARENT")) {
				old = PARENT;
				PARENT = null;
			}
			else if(property.equals("DISCOVERY")) {
				old = DISCOVERY;
				DISCOVERY = -1;
			}
			else if(property.equals("FINISH")) {
				old = FINISH;
				FINISH = -1;
			}
			return old;
		}
		// Accessors
		public String parent() {
			return PARENT;
		}
		
		public String color() {
			return COLOR;
		}
		
		public int discovery() {
			return DISCOVERY;
		}
		
		public int finish() {
			return FINISH;
		}
		
		public Node next() {
			return next;
		}
		
		public String data() {
			return data;
		}
		
		public boolean isNew() {
			return isNew;
		}
		
		public void makeOld() {
			this.isNew = false;
		}
	}

	private Node head;
	private Node tail;
	private int size;
	private String dataStructureName;
	private String data = null;
	private String COLOR = null;
	private String PARENT = null;
	private int DISCOVERY = -1;
	private int FINISH = -1;

	public DLLDynamicSet(String dataStructureName) {
		this.dataStructureName = dataStructureName;
		head = null;
		tail = null;
		size = 0;
	}

	/**
	 * Returns the name of Data Structure this set uses.
	 * 
	 * @return the name of Data Structure this set uses.
	 */
	@Override
	public String setDataStructure() {
		return dataStructureName;
	}

	public void setData(String vertexData) {
		this.data = vertexData;
	}
	
	public String parent() {
		return PARENT;
	}
	
	public String color() {
		return COLOR;
	}
	
	public int discovery() {
		return DISCOVERY;
	}
	
	public int finish() {
		return FINISH;
	}
	
	public Node head() {
		return head;
	}
	
	public String data() {
		return data;
	}
	
	public void annotate(Object property, Object value) {
		if(property.equals("COLOR")) this.COLOR = (String) value;
		else if(property.equals("PARENT")) this.PARENT = (String) value;
		else if(property.equals("DISCOVERY")) this.DISCOVERY = (int) value;
		else if(property.equals("FINISH")) this.FINISH = (int) value;
	}
	
	public Object getAnnotation(Object property) {
		if(property.equals("COLOR")) return this.COLOR;
		else if(property.equals("PARENT")) return this.PARENT;
		else if(property.equals("DISCOVERY")) return this.DISCOVERY;
		else if(property.equals("FINISH")) return this.FINISH;
		return null;
	}
	
	public Object removeAnnotation(Object property) {
		Object old = null;
		if(property.equals("COLOR")) {
			old = COLOR;
			COLOR = null;
		}
		else if(property.equals("PARENT")) {
			old = PARENT;
			PARENT = null;
		}
		else if(property.equals("DISCOVERY")) {
			old = DISCOVERY;
			DISCOVERY = -1;
		}
		else if(property.equals("FINISH")) {
			old = FINISH;
			FINISH = -1;
		}
		return old;
	}
	
	/**
	 * Returns the number of key-value mappings in this set. This method returns
	 * zero if the set is empty <br>
	 * 
	 * @return the number of key-value mappings in this set.
	 */
	@Override
	public int size() {
		return (this.size);
	}

	/*
	 * void method that constructs a new node after a node
	 * 
	 * @param string k, Node reference that have a new node after
	 */
	public void addAfter(Node node, Node reference) {
		if (reference != tail) {
			node.next = reference.next;
			node.previous = reference;
			reference.next.previous = node;
			reference.next = node;
		} else { // added at end, update tail value
			reference.next = node;
			node.previous = reference;
			tail = node;
		}
	}

	/*
	 * void method that adds new node to end of list
	 * 
	 * @param E value that is a number, string k that identifies the value k
	 * 
	 * @throws RuntimeException
	 */
	public void addAtEnd(Node node) {
		if (tail == null) {
			throw new RuntimeException("invalid call to addAtEnd, tail is null");
		}
		tail.next = node;
		tail = node;
	}

	/*
	 * void method that adds to front of list, updates head
	 * 
	 * @param E value that is a number, string k that identifies the value k
	 */
	public void addAtFront(Node node) {
		if (head != null) {
			head.previous = node;
			node.next = head;
			head = node;
		} else
			head = node;
		if (tail == null) {
			tail = head;
		}
	}

	/**
	 * Associates the specified value with the specified key in this map. If the
	 * map previously contained a mapping for the key, the old value is
	 * replaced.<br>
	 * If there is no current mapping for this key return null, otherwise the
	 * previous value associated with key.
	 * 
	 * @param key
	 *            - key with which the specified value is to be associated
	 * @param value
	 *            - value to be associated with the specified key
	 * 
	 * @return the previous value associated with key, or null if there was no
	 *         mapping for key.<br>
	 *         (A null return can also indicate that the map previously
	 *         associated null with key.)
	 */
	@Override
	public Value insert(Key key, Value value) {
		boolean found = false;
		boolean addFront = false;
		boolean addBetween = false;
		Node current = null;
		Node tmp = null;
		/*
		 * Loop that searches if k already in list If found in list, boolean
		 * found is set to true Tests if next node is there or if next node is
		 * bigger than k to compare and sets the values
		 */
		for (Node n = head; n != null; n = n.next) {
			int compared = (((String) n.key).compareToIgnoreCase((String) key));
			if (compared == 0) {
				tmp = n;
				found = true;
				break;
			}

			else if (compared > 0) {
				addFront = true;
				break;
			}

			else if (compared < 0) {
				if ((n.next == null)
						|| (((String) n.next.key)
								.compareToIgnoreCase((String) key) > 0)) {
					tmp = n;
					addBetween = true;
					break;
				}
			}
		}
		if (!found) {
			/*
			 * Updates size as it is added Checks booleans to add in right
			 * position
			 */
			Node node = new Node(key, value);
			size++;
			if ((head == null) || (addFront)) {
				addAtFront(node);
			} else if (addBetween) {
				addAfter(node, tmp);
			} else {
				addAtEnd(node);
			}
			return null;
		}
		// Found, already in list
		return (Value) (tmp.setValue(value));
	}
	
	public Value insert(Key key, Value value, boolean isNew, String data, int finalTime) {
		boolean found = false;
		boolean addFront = false;
		boolean addBetween = false;
		Node current = null;
		Node tmp = null;
		/*
		 * Loop that searches if k already in list If found in list, boolean
		 * found is set to true Tests if next node is there or if next node is
		 * bigger than k to compare and sets the values
		 */
		for (Node n = head; n != null; n = n.next) {
			int compared = (((String) n.key).compareToIgnoreCase((String) key));
			if (compared == 0) {
				tmp = n;
				found = true;
				break;
			}

			else if (compared > 0) {
				addFront = true;
				break;
			}

			else if (compared < 0) {
				if ((n.next == null)
						|| (((String) n.next.key)
								.compareToIgnoreCase((String) key) > 0)) {
					tmp = n;
					addBetween = true;
					break;
				}
			}
		}
		if (!found) {
			/*
			 * Updates size as it is added Checks booleans to add in right
			 * position
			 */
			Node newNode = new Node(key, value, isNew, data);
			newNode.FINISH = finalTime;
			size++;
			if ((head == null) || (addFront)) {
				addAtFront(newNode);
			} else if (addBetween) {
				addAfter(newNode, tmp);
			} else {
				addAtEnd(newNode);
			}
			return null;
		}
		// Found, already in list
		return (Value) (tmp.setValue(value));
	}

	/**
	 * Removes the mapping for this key from this set if present.
	 * 
	 * @param key
	 *            - key for which mapping should be removed
	 * 
	 * @return the previous value associated with key, or null if there was no
	 *         mapping for key.<br>
	 *         (A null return can also indicate that the map previously
	 *         associated null with key.)
	 */
	@Override
	public Value delete(Key key) {
		if (head != null) {
			Node delete = null;
			Value value = null;
			int compared;
			for (Node n = head; n != null; n = n.next) {
				compared = ((((String) n.key).compareToIgnoreCase((String) key)));
				if (compared == 0) {
					delete = n;
					break;
				}
				if (compared > 0) {
					return null;
				}
			}
			if (delete != null) {
				size -= 1;
				value = (Value) delete.getValue();
				if (delete == head)
					head = delete.next;
				else {
					delete.previous.next = delete.next;
				}
				if (delete == tail)
					tail = delete.previous;
				else
					delete.next.previous = delete.previous;
			}
			return value;
		}
		return null;
	}

	/**
	 * Returns the value to which the specified key is mapped, or null if this
	 * set contains no mapping for the key.
	 * 
	 * @param key
	 *            The key under which this data is stored.
	 * 
	 * @return the Value of element stored in the set under the Key key.
	 */
	@Override
	public Value retrieve(Key key) {
		if (head == null)
			return null;
		int compared = -1;
		Node found = null;
		for (Node n = head; n != null; n = n.next) {
			compared = ((((String) n.key).compareToIgnoreCase((String) key)));
			if (compared == 0) {
				found = n;
				break;
			}
			if (compared > 0) {
				return null;
			}
		}
		if(found == null) return null;
		else return (Value) found.getValue();
	}

	/**
	 * Returns a key-value mapping associated with the least key in this map, or
	 * null if the set is empty. IMPORTANT: This operation only applies when
	 * there is a total ordering on the Key <br>
	 * Returns null if the set is empty. If there is not total ordering on the
	 * Key returns null.
	 * 
	 * @return an entry with the least key, or null if this map is empty
	 */
	@Override
	public Entry<Key, Value> minimum() {
		return head;
	}

	/**
	 * Returns a key-value mapping associated with the greatest key in this map,
	 * or null if the map is empty. IMPORTANT: This operation only applies when
	 * there is a total ordering on the Key <br>
	 * Returns null if the set is empty. If there is not total ordering on the
	 * key returns null.
	 * 
	 * @return an entry with the greatest key, or null if this map is empty.
	 */
	@Override
	public Entry<Key, Value> maximum() {
		return tail;
	}

	/**
	 * Returns a key-value mapping associated with the least key strictly
	 * greater than the given key, or null if there is no such key. IMPORTANT:
	 * This operation only applies when there is a total ordering on the key <br>
	 * Returns null if the set is empty or the key is not found. <br>
	 * Returns null if the key is the maximum element. <br>
	 * If there is not total ordering on the key for the set returns null.
	 * 
	 * @param key
	 *            - the key
	 * 
	 * @return an entry with the greatest key less than key, or null if there is
	 *         no such key
	 */
	@Override
	public Entry<Key, Value> successor(Key key) {
		if (head != null) {
			int compared = -1;
			Node found = null;
			for (Node n = head; n != null; n = n.next) {
				compared = ((((String) n.key).compareToIgnoreCase((String) key)));
				if (compared == 0) {
					found = n;
					break;
				}
				if (compared > 0) {
					return null;
				}
			}
			if (found.next != null)
				return found.next;
		}
		return null;
	}

	/**
	 * Returns a key-value mapping associated with the greatest key strictly
	 * less than the given key, or null if there is no such key. IMPORTANT: This
	 * operation only applies when there is a total ordering on the key <br>
	 * Returns null if the set is empty or the key is not found. <br>
	 * Returns null if the key is the minimum element. <br>
	 * If there is not total ordering on the key for the set returns null.
	 * 
	 * @param key
	 *            - the key
	 * 
	 * @return an entry with the greatest key less than key, or null if there is
	 *         no such key
	 */
	@Override
	public Entry<Key, Value> predecessor(Key key) {
		if (head != null) {
			int compared = -1;
			Node found = null;
			for (Node n = head; n != null; n = n.next) {
				compared = ((((String) n.key).compareToIgnoreCase((String) key)));
				if (compared == 0) {
					found = n;
					break;
				}
				if (compared > 0) {
					return null;
				}
			}
			if (found.previous != null)
				return found.previous;
		}
		return null;
	}

	/*
	 * The toString that prints out the list
	 * 
	 * @return String of linked nodes
	 */
	public String toString() {
		System.out.println("Size: " + size());
		Node node = head;
		StringBuilder result = new StringBuilder();
		while (node != null) {
			result.append(node.key.toString());
			node = node.next;
			if (node != null) {
				result.append(" ==> ");
			}
		}
		return result.toString();
	}
}
