package com.aaa.ace.common;

/**
 * Constants defined for services. Contains JCR Query constants
 * 
 * @author yogesh.mahajan
 *
 */
public class Constants {

    public static final String SQL_SELECT_CLAUSE = "SELECT child.* FROM [cq:Page] AS parent "
            + "INNER JOIN [nt:base] AS child ON ISCHILDNODE(child,parent) ";

    public static final String SQL_ORDER_BY = " ORDER BY ";

    public static final String COMMA = ",";

    public static final String AND = " AND ";

    public static final String OR = " OR ";

}
