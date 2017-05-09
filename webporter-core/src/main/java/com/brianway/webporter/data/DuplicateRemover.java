package com.brianway.webporter.data;

public interface DuplicateRemover<ID> {

    boolean isDuplicate(ID id);

    void resetDuplicateCheck();

    int getTotalCount();

}
