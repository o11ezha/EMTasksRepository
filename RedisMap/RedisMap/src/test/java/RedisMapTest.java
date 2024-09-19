import org.example.RedisMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RedisMapTest {
    private RedisMap redisMap;
    private Jedis jedis;


    @BeforeEach
    void setUp(){
        redisMap = new RedisMap("localhost", 6379);
        jedis = new Jedis("localhost", 6379);
    }

    @AfterEach
    void clearUp(){
        jedis.flushAll();
        jedis.close();
    }

    @Test
    void testPutAndGet() {
        redisMap.put("key1","value1");
        assertEquals("value1", redisMap.get("key1"));
    }

    @Test
    void testRemove(){
        redisMap.put("key2","value2");
        assertEquals("Deleted", redisMap.remove("key2"));
        assertNull(redisMap.get("key2"));
    }

    @Test
    void testSize(){
        redisMap.put("key3","value3");
        redisMap.put("key4","value4");
        assertEquals(2, redisMap.size());
    }

    @Test
    void testIsEmpty(){
        assertTrue(redisMap.isEmpty());
        redisMap.put("key5","value5");
        assertFalse(redisMap.isEmpty());
    }

    @Test
    void testContainsKey(){
        redisMap.put("key6", "value6");
        assertTrue(redisMap.containsKey("key6"));
        assertFalse(redisMap.containsKey("key7"));
    }

    @Test
    void testContainsValue(){
        redisMap.put("key7","value7");
        assertTrue(redisMap.containsValue("value7"));
        assertFalse(redisMap.containsValue("value8"));
    }

    @Test
    void testPutAll(){
        Map<String, String> map = Map.of("key8","value8", "key9","value9");
        redisMap.putAll(map);
        assertEquals("value8", redisMap.get("key8"));
        assertEquals("value9", redisMap.get("key9"));
    }

    @Test
    void testClear(){
        redisMap.put("key10","value10");
        redisMap.put("key11","value11");
        redisMap.clear();
        assertTrue(redisMap.isEmpty());
    }

    @Test
    void testKeySet(){
        redisMap.put("key12", "value12");
        Set<String> keys = redisMap.keySet();
        assertTrue(keys.contains("key12"));
    }

    @Test
    void testValues(){
        redisMap.put("key13", "value13");
        assertTrue(redisMap.values().contains("value13"));
    }

    @Test
    void testEntrySet(){
        redisMap.put("key14", "value14");
        assertTrue(redisMap.entrySet().contains(Map.entry("key14", "value14")));
    }
}
