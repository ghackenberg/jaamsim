/*
 * JaamSim Discrete Event Simulation
 * Copyright (C) 2012 Ausenco Engineering Canada Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package com.jaamsim.collada;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * ColNode's are the nodes of an internal DOM like collada tree representation.
 * @author matt.chudleigh
 *
 */
public class ColNode {

	public class ChildIterable implements Iterable<ColNode> {

		@Override
		public Iterator<ColNode> iterator() {
			return new ChildIterator();
		}
	}
	public class ChildIterator implements Iterator<ColNode> {
		private int index = 0;

		@Override
		public boolean hasNext() {
			return index < _children.size();
		}

		@Override
		public ColNode next() {
			return _children.get(index++);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	private ColNode _parent;
	private HashMap<String, String> _attribs = new HashMap<String, String>();

	private String _tag;
	private String _fragID;

	/**
	 * Content can be a String, double[], int[], boolean[] or String[] depending on tag type
	 */
	private Object _content;

	private ArrayList<ColNode> _children;

	public ColNode(ColNode parent, String tag, String fragID) {
		_parent = parent;
		_tag = tag;
		_fragID = fragID;

		_children = new ArrayList<ColNode>();
	}

	public void addAttrib(String name, String value) {

		assert(!_attribs.containsKey(name));

		_attribs.put(name, value);
	}

	public String getAttrib(String name) {
		return _attribs.get(name);
	}

	public boolean hasAttrib(String name) {
		return _attribs.containsKey(name);
	}

	public void addChild(ColNode child) {
		_children.add(child);
	}

	public int getNumChildren() {
		return _children.size();
	}

	public ColNode getChild(int index) {
		if (index < 0 || index >= _children.size()) {
			assert(false);
			return null;
		}
		return _children.get(index);
	}

	public String getTag() {
		return _tag;
	}

	public String getFragID() {
		return _fragID;
	}

	public ColNode getParent() {
		return _parent;
	}

	public ChildIterable children() {
		return new ChildIterable();
	}

	/**
	 * Return the number of child attributes of this tag
	 * @param name
	 * @return
	 */
	public int getNumChildrenByTag(String tag) {
		int num = 0;
		for (ColNode child : _children) {
			if (child.getTag().equals(tag))
				num++;
		}
		return num;
	}

	public void setContent(Object content) {
		_content = content;
	}

	public Object getContent() {
		return _content;
	}

	@Override
	public String toString() {
		return "<" + _tag + ">";
	}

	/**
	 * Find the first child tag of type 'tag'
	 * @param tag
	 * @param recurse
	 * @return
	 */
	public ColNode findChildTag(String tag, boolean recurse) {
		for (ColNode child : _children) {
			if (child.getTag().equals(tag)) {
				return child;
			}
		}
		// Note, this does a pseudo breadth first search
		if (recurse) {
			// Try all our children
			for (ColNode child : _children) {
				ColNode val = child.findChildTag(tag, true);

				if (val != null)
					return val;
			}
		}
		return null;
	}
}
