package com.curso.androidt.earthquake.dao;

import java.util.List;

/**
 * Created by davasens on 5/22/2015.
 */
public interface AbstractDao<K,V> {

    K get(V id);
    List<K> findAll();
    K insert(K entity);
    K update(K entity);
    void delete();
}
