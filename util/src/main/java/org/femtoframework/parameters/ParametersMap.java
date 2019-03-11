package org.femtoframework.parameters;


import org.femtoframework.io.DataCodec;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.*;

/**
 * Parameters based on HashMap
 *
 * Created by xshao on 9/16/16.
 */
public class ParametersMap<V> extends AbstractMap<String, V> implements Parameters<V>, Externalizable {

    protected Map<String, V> map;

    public ParametersMap() {
        map = new HashMap<>();
    }

    public ParametersMap(Map<String, V> map) {
        this.map = map;
    }

    public V get(Object key) {
        return map.get(key);
    }

    public V put(String key, V value) {
        return map.put(key, value);
    }

    public Set<String> keySet() {
        return map.keySet();
    }

    public Collection<V> values() {
        return map.values();
    }

    public void clear() {
        map.clear();
    }

    public int size() {
        return map.size();
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public Set<Entry<String, V>> entrySet() {
        return map.entrySet();
    }

    public Map<String, V> toMap() {
        return map;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        if (map == null) {
            out.writeInt(-1);
        }
        else {
            out.writeInt(map.size());
            for (Map.Entry<String, V> entry : map.entrySet()) {
                DataCodec.writeSingle(out, entry.getKey());
                out.writeObject(entry.getValue());
            }
        }
    }

    @Override
    public void readExternal(ObjectInput ois) throws IOException, ClassNotFoundException {
        int size = ois.readInt();
        if (size > 64 * 1024) {
            throw new IOException("The map size is too big:" + size);
        }

        if (size < 0) {
            map = null;
            return;
        }

        if (map == null) {
            map = new HashMap<>(size == 0 ? 4 : (int)(size*1.2));
        }
        else {
            map.clear();
        }

        for (int i = 0; i < size; i++) {
            String key = DataCodec.readSingle(ois);
            V value = (V)ois.readObject();
            map.put(key, value);
        }
    }
}
