package com.ak.util;

import static com.ak.util.GlobalMessages.NOT_FOUND_ID;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(int id) {
        super(String.format(NOT_FOUND_ID, id));
    }
}
