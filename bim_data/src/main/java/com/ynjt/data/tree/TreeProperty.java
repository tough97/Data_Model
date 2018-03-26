package com.ynjt.data.tree;


import java.text.SimpleDateFormat;

public final class TreeProperty {

    private static SimpleDateFormat formatter;

    static {
        formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    }

    private String value;
    private Class type;

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

    public synchronized TreeProperty setValue(final String value) {
        this.value = value;
        type = String.class;
        return this;
    }

    public synchronized TreeProperty setValue(final int value) {
        this.value = String.valueOf(value);
        type = int.class;
        return this;
    }

    public synchronized TreeProperty setValue(final float value) {
        this.value = String.valueOf(value);
        type = float.class;
        return this;
    }

    public synchronized TreeProperty setValue(final double value) {
        this.value = String.valueOf(value);
        type = double.class;
        return this;
    }

    public synchronized TreeProperty setValue(final java.util.Date value) {
        this.value = formatter.format(value);
        type = java.util.Date.class;
        return this;
    }

    public synchronized TreeProperty setValue(final short value) {
        this.value = String.valueOf(value);
        type = short.class;
        return this;
    }

    public synchronized TreeProperty setValue(final byte value) {
        this.value = String.valueOf(value);
        type = byte.class;
        return this;
    }

    public synchronized TreeProperty setValue(final long value) {
        this.value = String.valueOf(value);
        type = long.class;
        return this;
    }

    public synchronized TreeProperty setValue(final boolean value) {
        this.value = String.valueOf(value);
        type = boolean.class;
        return this;
    }

    public synchronized TreeProperty setValue(final char value) {
        this.value = Character.toString(value);
        type = char.class;
        return this;
    }

    public synchronized String getValue(){
        return value;
    }

    public synchronized TreeProperty setType(final Class type) {
        this.type = type;
        return this;
    }

    public synchronized Class getType(){
        return type;
    }
    
    //CAUTION: this is temporary solution, value supports
    //Objects now, but use it with caution
    public synchronized TreeProperty setValue(final Object object) {
        this.value = object.toString();
        type = object.getClass();
        return this;
    }

    public TreeProperty clone(){
        final TreeProperty property = new TreeProperty();
        synchronized (this){
            property.value = this.value;
            property.type = this.type;
        }
        return property;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TreeProperty property = (TreeProperty) o;

        if (value != null ? !value.equals(property.value) : property.value != null) return false;
        return type != null ? type.equals(property.type) : property.type == null;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}