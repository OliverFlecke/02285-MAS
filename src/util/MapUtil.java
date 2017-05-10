package util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MapUtil {
	
	private MapUtil() {}
	
	public static <K, V> void initMap(Map<K, Set<V>> map, Set<K> keys)
	{
		for (K key : keys)
		{
			map.put(key, new HashSet<V>());
		}
	}
	
	public static <K, V> void addToMap(Map<K, Set<V>> map, K key, V value)
	{
		if (map.containsKey(key))
		{
			map.get(key).add(value);
		}
		else
		{
			map.put(key, new HashSet<V>(Arrays.asList(value)));
		}
	}
}
