package com.ynjt.data.tree.util;

import java.util.UUID;

public class UUIDGenerator implements IDGenerator{

    public String generateUniuqeID() {
        return UUID.randomUUID().toString();
    }

}
