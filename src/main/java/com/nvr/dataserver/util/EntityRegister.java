package com.nvr.dataserver.util;

/**
 * Created with IntelliJ IDEA.
 * User: vvarm1
 * Date: 8/22/13
 * Time: 2:21 PM
 * To change this template use File | Settings | File Templates.
 */
public enum  EntityRegister {
    INDEX(Index.class,new String[]{"name"},new String[]{}),
    SECURITY(Security.class,new String[]{"symbol","series","isinNumber"},new String[]{"company","listing"});
    Class aClass;
    String[] required;
    String[] optional;

    private EntityRegister(Class aClass, String[] required,String[] optional) {
        this.aClass = aClass;
        this.required = required;
        this.optional = optional;
    }

    public Class getaClass() {
        return aClass;
    }

    public String[] getRequired() {
        return required;
    }

    public String[] getOptional() {
        return optional;
    }
}
