package com.brianway.webporter.data;

import com.google.common.collect.Sets;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by brian on 16/12/15.
 * TODO 看能不能去掉 guava 依赖
 */
public class HashSetDuplicateRemover<ID> implements DuplicateRemover<ID> {

    private Set<ID> ids = Sets.newSetFromMap(new ConcurrentHashMap<>());

    @Override
    public boolean isDuplicate(ID id) {
        return !ids.add(id);
    }

    @Override
    public void resetDuplicateCheck() {
        ids.clear();
    }

    @Override
    public int getTotalCount() {
        return ids.size();
    }

}
