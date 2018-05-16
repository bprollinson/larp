import static org.junit.Assert.assertEquals;
import org.junit.Test;

import larp.util.SetMap;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SetMapTest
{
    @Test
    public void testGetReturnsEmptySetForNonExistentKey()
    {
        SetMap<String, Integer> setMap = new SetMap<String, Integer>();

        Set<Integer> expectedSet = new HashSet<Integer>();
        assertEquals(expectedSet, setMap.get("key"));
    }

    @Test
    public void testSetMapMapsSingleKeyToSingleEntry()
    {
        SetMap<String, Integer> setMap = new SetMap<String, Integer>();
        setMap.put("key", 1);

        Set<Integer> expectedSet = new HashSet<Integer>();
        expectedSet.add(1);

        assertEquals(expectedSet, setMap.get("key"));
    }

    @Test
    public void testSetMapMapsSingleKeyToMultipleEntries()
    {
        SetMap<String, Integer> setMap = new SetMap<String, Integer>();
        setMap.put("key", 1);
        setMap.put("key", 2);

        Set<Integer> expectedSet = new HashSet<Integer>();
        expectedSet.add(1);
        expectedSet.add(2);

        assertEquals(expectedSet, setMap.get("key"));
    }

    @Test
    public void testSetMapMapsMultileKeysToSingleEntryEach()
    {
        SetMap<String, Integer> setMap = new SetMap<String, Integer>();
        setMap.put("key1", 1);
        setMap.put("key2", 2);

        Set<Integer> expectedSet1 = new HashSet<Integer>();
        expectedSet1.add(1);
        assertEquals(expectedSet1, setMap.get("key1"));

        Set<Integer> expectedSet2 = new HashSet<Integer>();
        expectedSet2.add(2);
        assertEquals(expectedSet2, setMap.get("key2"));
    }

    @Test
    public void testSetMapMapsMultipleKeysToMultipleEntriesEach()
    {
        SetMap<String, Integer> setMap = new SetMap<String, Integer>();
        setMap.put("key1", 1);
        setMap.put("key1", 2);
        setMap.put("key2", 3);
        setMap.put("key2", 4);

        Set<Integer> expectedSet1 = new HashSet<Integer>();
        expectedSet1.add(1);
        expectedSet1.add(2);
        assertEquals(expectedSet1, setMap.get("key1"));

        Set<Integer> expectedSet2 = new HashSet<Integer>();
        expectedSet2.add(3);
        expectedSet2.add(4);
        assertEquals(expectedSet2, setMap.get("key2"));
    }

    @Test
    public void testSetMapMapsNullKey()
    {
        SetMap<String, Integer> setMap = new SetMap<String, Integer>();
        setMap.put(null, 1);

        Set<Integer> expectedSet = new HashSet<Integer>();
        expectedSet.add(1);
        assertEquals(expectedSet, setMap.get(null));
    }

    @Test
    public void testEntrySetReturnsEmptySetForEmptySetMap()
    {
        SetMap<String, Integer> setMap = new SetMap<String, Integer>();

        Set<Map.Entry<String, Integer>> expectedSet = new HashSet<Map.Entry<String, Integer>>();
        assertEquals(expectedSet, setMap.entrySet());
    }

    @Test
    public void testEntrySetReturnsEntrySetForNonEmptySetMap()
    {
        SetMap<String, Integer> setMap = new SetMap<String, Integer>();
        setMap.put(null, 0);
        setMap.put("key1", 1);
        setMap.put("key2", 2);
        setMap.put("key2", 3);

        Set<Map.Entry<String, Set<Integer>>> expectedSet = new HashSet<Map.Entry<String, Set<Integer>>>();
        Set<Integer> set0 = new HashSet<Integer>();
        set0.add(0);
        expectedSet.add(new AbstractMap.SimpleEntry<String, Set<Integer>>(null, set0));
        Set<Integer> set1 = new HashSet<Integer>();
        set1.add(1);
        expectedSet.add(new AbstractMap.SimpleEntry<String, Set<Integer>>("key1", set1));
        Set<Integer> set2 = new HashSet<Integer>();
        set2.add(2);
        set2.add(3);
        expectedSet.add(new AbstractMap.SimpleEntry<String, Set<Integer>>("key2", set2));
        assertEquals(expectedSet, setMap.entrySet());
    }
}
