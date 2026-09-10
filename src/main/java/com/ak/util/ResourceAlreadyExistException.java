package com.ak.util;

import static com.ak.util.GlobalMessages.ALREADY_EXISTS;

public class ResourceAlreadyExistException extends RuntimeException {
    public ResourceAlreadyExistException(long id) {
        super(String.format(ALREADY_EXISTS, id));
    }
}
