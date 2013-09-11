package com.nvr.dataserver;

import com.nvr.dataserver.util.Security;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vvarm1
 * Date: 9/4/13
 * Time: 5:51 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Parser {
    <T> List<T> getEntityListFromFile();

    Class parseHeaderAndGetEntityClass(int lineNumber);

    int getHeaderFromFile();

}
