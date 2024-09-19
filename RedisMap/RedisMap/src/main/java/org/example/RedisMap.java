package org.example;

import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RedisMap implements Map<String, String> {
    private final Jedis jedis;

    public RedisMap(String host, int port) {
        this.jedis = new Jedis(host, port);
    }

    public RedisMap() {
        this.jedis = new Jedis("localhost", 6379);
    }

    @Override
    public int size() {
        return Math.toIntExact(jedis.dbSize());
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return jedis.exists((String) key);
    }

    @Override
    public boolean containsValue(Object value) {
        Set<String> keys = jedis.keys("*");
        return keys.stream().anyMatch(key -> value.equals(jedis.get(key)));
    }

    @Override
    public String get(Object key) {
        return jedis.get((String) key);
    }

    @Override
    public String put(String key, String value) {
        return jedis.set(key, value);
    }

    @Override
    public String remove(Object key) {
        return jedis.del((String) key)> 0 ? "Deleted" : null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> map) {
        jedis.mset(map.entrySet().stream().flatMap(e -> Stream.of(e.getKey(), e.getValue())).toArray(String[]::new));
    }

    @Override
    public void clear() {
        jedis.flushDB();
    }

    @Override
    public Set<String> keySet() {
        return new HashSet<>(jedis.keys("*"));
    }

    @Override
    public Collection<String> values() {
        return keySet().stream().map(jedis::get).collect(Collectors.toList());
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        return keySet().stream().map(key -> new AbstractMap.SimpleEntry<>(key, jedis.get(key))).collect(Collectors.toSet());
    }
}
