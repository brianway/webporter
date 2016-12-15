package com.brianway.webporter.data;

/**
 * Created by brian on 16/12/15.
 */
public interface DuplicateRemover<ID> {

    boolean isDuplicate(ID id);

    void resetDuplicateCheck();

    int getTotalCount();

}
