package com.curso.androidt.earthquake.dao;

import com.curso.androidt.earthquake.Quake;
import com.curso.androidt.earthquake.QuakeDto;

import java.util.List;

/**
 * Created by davasens on 5/22/2015.
 */
public interface QuakeDao extends AbstractDao<Quake,String> {

    List<Quake> find(QuakeDto dto);
}
