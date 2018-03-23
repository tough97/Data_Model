package com.ynjs.data;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ynjs.data.excp.IllegalJSONFormatException;

import java.text.SimpleDateFormat;

public final class PropertyValue {

    protected static final String VALUE = "v";
    protected static final String TYPE = "t";

    private static SimpleDateFormat formatter;

    static {
        formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    }

    private String value;
    private Class type;

    public PropertyValue() {
    }

    public PropertyValue(final ObjectNode node) throws IllegalJSONFormatException {
        final JsonNode valueNode = node.get(VALUE);
        final JsonNode typeNode = node.get(TYPE);
        if (valueNode == null || typeNode == null) {
            throw new IllegalJSONFormatException(value == null ? VALUE : TYPE + " attribute is missing from JSON node, parsing failed");
        }
        this.value = valueNode.asText();
        switch (typeNode.asText()) {
            case "int":
                this.type = int.class;
                break;
            case "short":
                this.type = short.class;
                break;
            case "byte":
                this.type = byte.class;
                break;
            case "long":
                this.type = long.class;
                break;
            case "boolean":
                this.type = boolean.class;
                break;
            case "float":
                this.type = float.class;
                break;
            case "double":
                this.type = double.class;
                break;
            case "string":
                this.type = String.class;
                break;
            case "date":
                this.type = java.util.Date.class;
                break;
            case "char":
                this.type = char.class;
                break;
            default:
                try {
                    this.type = Class.forName(typeNode.asText());
                } catch (final ClassNotFoundException e) {
                    throw new IllegalJSONFormatException(e);
                }
                break;
        }
    }

    private boolean checkType(final Class... targets) {
        if (targets == null || targets.length == 0) {
            return false;
        }
        for (final Class target : targets) {
            if (target.equals(type)) {
                return true;
            }
        }
        return false;
    }

    public boolean isNumericalType() {
        return checkType(int.class, short.class, byte.class, long.class, float.class, double.class);
    }

    public boolean isIntegerType() {
        return checkType(int.class, short.class, byte.class, long.class);
    }

    public boolean isBooleanType() {
        return checkType(boolean.class);
    }

    public boolean isDateType() {
        return checkType(java.util.Date.class);
    }

    public synchronized PropertyValue setValue(final String value) {
        this.value = value;
        type = String.class;
        return this;
    }

    public synchronized PropertyValue setValue(final int value) {
        this.value = String.valueOf(value);
        type = int.class;
        return this;
    }

    public synchronized PropertyValue setValue(final float value) {
        this.value = String.valueOf(value);
        type = float.class;
        return this;
    }

    public synchronized PropertyValue setValue(final double value) {
        this.value = String.valueOf(value);
        type = double.class;
        return this;
    }

    public synchronized PropertyValue setValue(final java.util.Date value) {
        this.value = formatter.format(value);
        type = java.util.Date.class;
        return this;
    }

    public synchronized PropertyValue setValue(final short value) {
        this.value = String.valueOf(value);
        type = short.class;
        return this;
    }

    public synchronized PropertyValue setValue(final byte value) {
        this.value = String.valueOf(value);
        type = byte.class;
        return this;
    }

    public synchronized PropertyValue setValue(final long value) {
        this.value = String.valueOf(value);
        type = long.class;
        return this;
    }

    public synchronized PropertyValue setValue(final boolean value) {
        this.value = String.valueOf(value);
        type = boolean.class;
        return this;
    }

    public synchronized PropertyValue setValue(final char value) {
        this.value = Character.toString(value);
        type = char.class;
        return this;
    }

    //CAUTION: this is temporary solution, value supports
    //Objects now, but use it with caution
    public synchronized PropertyValue setValue(final Object object) {
        this.value = object.toString();
        type = object.getClass();
        return this;
    }

    public String getValue() {
        return value;
    }

    public Class getType() {
        return type;
    }

    public ObjectNode putValue(final ObjectNode objectNode){
        objectNode.put(VALUE, value).put(TYPE, type.equals(String.class) ? "string" : type.equals(java.util.Date.class) ? "date" : type.getCanonicalName());
        return objectNode;
    }

    public ObjectNode toObjectNode() {
        return new ObjectMapper().createObjectNode().put(VALUE, value)
                .put(TYPE, type.equals(String.class) ? "string" : type.equals(java.util.Date.class) ? "date" : type.getCanonicalName());
    }

    public static void main(String[] args) throws ClassNotFoundException, IllegalJSONFormatException {
    }

}