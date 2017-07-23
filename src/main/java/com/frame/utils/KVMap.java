package com.frame.utils;

import java.util.HashMap;

/**
 * 继承并重写方法，方便直接new以及连续add
 */
public class KVMap<K, V> extends HashMap<K, V> {
	private static final long serialVersionUID = 1L;

	public KVMap<K, V> add(K key, V value) {
		super.put(key, value);
		return this;
	}
}
