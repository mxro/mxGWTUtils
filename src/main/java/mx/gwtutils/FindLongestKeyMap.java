/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package mx.gwtutils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Attempts to find the longest matching string within this maps. Probably more
 * efficiently implemented using some sort of Trie data structure.
 * 
 * @author mroh004
 * 
 */
public class FindLongestKeyMap<GType> {
	
	protected Map<String, GType> map;
	
	public GType get(final String key) {
		return map.get(key);
	}
	
	public GType findLongestKey(final String startingWith) {
		int longestLenght = -1;
		GType longest=null;
		for (final Entry<String, GType> e:map.entrySet()) {
			final int currentLenght = e.getKey().length();
			if (longestLenght < currentLenght && startingWith.startsWith(e.getKey())) {
				longest = e.getValue();
				longestLenght = currentLenght;
			}
		}
		if (longestLenght > -1) {
			return longest;
		} else {
			return null;
		}
	}
	
	public void put(final String key, final GType value) {
		map.put(key, value);
	}

	public FindLongestKeyMap() {
		super();
		this.map = new HashMap<String, GType>();
	}
	
	
	
}
