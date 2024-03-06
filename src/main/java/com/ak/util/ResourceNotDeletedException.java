package com.ak.util;

import static com.ak.util.GlobalMessages.NOT_DELETED;

public class ResourceNotDeletedException extends RuntimeException {
    public ResourceNotDeletedException(int id) {
        super(String.format(NOT_DELETED, id));
    }
}
