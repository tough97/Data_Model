package com.ynjt.data.tree.io;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ynjt.data.tree.TreeElement;
import com.ynjt.data.tree.TreeLeaf;
import com.ynjt.data.tree.TreeNode;
import com.ynjt.data.tree.TreeProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a testing purpose class,
 * mainly used to provide tree operation comparision
 * please fo not use this class in production environment
 * it provides only the basic idea about tree operations and
 * serializations
 *
 * Author: Gang-Liu
 */

public class TreeJSONConvertor {

    private static final String TREE_ELEMENT_ID = "ti";
    private static final String TREE_ELEMENT_NAME = "tn";
    private static final String TREE_ELEMENT_DEPTH = "td";
    private static final String SUB_ELEMENT_KEY = "children";
    private static final String PROPERTIES = "props";
    private static final String PROPERTY_KEY = "pk";
    private static final String PROPERTY_VALUE = "pv";
    private static final String PROPERTY_VALUE_TYPE = "pvt";
    private static final String TREE_ELEMENT_TYPE_KEY = "te";
    private static final String TREE_NODE = "node";
    private static final String TREE_LEAF = "leaf";

    private static final Map<String, Class> typeNameMap = new HashMap<>();
    static{
        typeNameMap.put(int.class.toString(), int.class);
        typeNameMap.put(short.class.toString(), short.class);
        typeNameMap.put(byte.class.toString(), byte.class);
        typeNameMap.put(long.class.toString(), long.class);
        typeNameMap.put(float.class.toString(), float.class);
        typeNameMap.put(double.class.toString(), double.class);
        typeNameMap.put(boolean.class.toString(), boolean.class);
        typeNameMap.put(char.class.toString(), char.class);
        typeNameMap.put("string", String.class);
        typeNameMap.put("date", java.util.Date.class);
    }

    private Gson gson = new Gson();

    public JsonObject propertyToJson(final TreeElement element) {
        final JsonObject elementJson = new JsonObject();
        elementJson.addProperty(TREE_ELEMENT_ID, element.getId());
        elementJson.addProperty(TREE_ELEMENT_NAME, element.getName());
        elementJson.addProperty(TREE_ELEMENT_DEPTH, element.getDepth());

        if (element.getPropertiSize() > 0) {
            final JsonArray propertyArray = new JsonArray();
            for (final String key : element.getPropertyKeySet()) {
                propertyArray.add(propertyToJson(key, element.getProperty(key)));
            }
            elementJson.add(PROPERTIES, propertyArray);
        }

        if (element instanceof TreeNode) {
            if (((TreeNode) element).getChildrenSize() > 0) {
                final JsonArray childArray = new JsonArray();
                for (final TreeElement child : ((TreeNode) element).getChildElements()) {
                    childArray.add(propertyToJson(child));
                }
                elementJson.add(SUB_ELEMENT_KEY, childArray);
            }
            elementJson.addProperty(TREE_ELEMENT_TYPE_KEY, TREE_NODE);
        } else {
            elementJson.addProperty(TREE_ELEMENT_TYPE_KEY, TREE_LEAF);
        }

        return elementJson;
    }

    private JsonObject propertyToJson(final String key, final TreeProperty property) {
        final JsonObject object = new JsonObject();
        object.addProperty(PROPERTY_KEY, key);
        object.addProperty(PROPERTY_VALUE, property.getValue());
        object.addProperty(PROPERTY_VALUE_TYPE, getTypeName(property.getClass()));
        return object;
    }

    public TreeElement jsonToElement(final JsonObject object) throws JsonFormatException{
        final String type = getStringProperty(object, TREE_ELEMENT_TYPE_KEY);
        final TreeElement ret = type.equals(TREE_NODE) ? new TreeNode() : new TreeLeaf();
        ret.setId(getStringProperty(object, TREE_ELEMENT_ID))
        .setName(getStringProperty(object, TREE_ELEMENT_NAME))
        .setDepth(Integer.parseInt(getStringProperty(object, TREE_ELEMENT_DEPTH)));

        final JsonArray propes = object.getAsJsonArray(PROPERTIES);
        if(propes != null){
            for(int index = 0; index < propes.size(); index++){
                final JsonObject propertyObject = propes.get(index).getAsJsonObject();
                final TreeProperty property = jsonToProperty(propes.get(index).getAsJsonObject());
                final String key = getStringProperty(propertyObject, PROPERTY_KEY);
                ret.setProperty(key, property);
            }
        }

        if(ret instanceof TreeNode){
            final JsonArray nodeArray = object.getAsJsonArray(SUB_ELEMENT_KEY);
            for(int index = 0; index < nodeArray.size(); index++){
                final TreeElement child = jsonToElement(nodeArray.get(index).getAsJsonObject());
                child.setParent((TreeNode) ret);
            }
        }

        return ret;
    }

    private String getStringProperty(final JsonObject object, final String key) throws JsonFormatException{
        if(key == null){
            throw new JsonFormatException("Key in object can not be null(" + key + ")");
        }
        final JsonElement valueElement = object.get(key);
        if(valueElement == null){
            throw new JsonFormatException("Value corresponding to (" + key + ") not found");
        }
        return valueElement.getAsString();
    }

    private TreeProperty jsonToProperty(final JsonObject jsonObject) throws JsonFormatException{
        final TreeProperty property = new TreeProperty();
        final String value = jsonObject.get(PROPERTY_VALUE).getAsString();
        final String className = jsonObject.get(PROPERTY_VALUE_TYPE).getAsString();
        final Class type = typeNameMap.get(className);
        if(type == null){
            throw new JsonFormatException("Data type not supported in deserialization : " + className);
        }
        property.setValue(value);
        property.setType(type);
        return property;
    }
    

    private String getTypeName(final Class type) {
        return type.equals(java.util.Date.class) ? "date" : type.equals(String.class) ? "string" : type.toString();
    }


    public static void main(String[] args) throws ClassNotFoundException {
        final Class[] clazz = {int.class, short.class, boolean.class, long.class, double.class, float.class, byte.class, char.class};
        for(final Class c : clazz){
            System.out.println(c);
        }
    }

}