package com.ak.util;

import static com.ak.util.GlobalMessages.NO_ACCESS;

public class ResourceNoAccessException extends RuntimeException {

    public ResourceNoAccessException(int id) {
        super(String.format(NO_ACCESS, id));
    }
}
