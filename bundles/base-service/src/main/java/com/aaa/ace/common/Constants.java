package com.aaa.ace.common;

/**
 * Constants defined for services. Contains JCR Query constants
 * 
 * @author yogesh.mahajan
 *
 */
public class Constants {

    /**
     * SQL query.
     */
    public static final String SQL_SELECT_CLAUSE = "SELECT child.* FROM [cq:Page] AS parent "
            + "INNER JOIN [nt:base] AS child ON ISCHILDNODE(child,parent) ";

    /**
     * SQL order by string.
     */
    public static final String SQL_ORDER_BY = " ORDER BY ";

    /**
     * String comma.
     */
    public static final String COMMA = ",";

    /**
     * String colon.
     */
    public static final String COLON = ":";

    /**
     * String AND.
     */
    public static final String AND = " AND ";

    /**
     * String OR.
     */
    public static final String OR = " OR ";

    /**
     * String path separator.
     */
    public static final String STRING_PATH_SEPARATOR = "/";

    /**
     * String HTML extension.
     */
    public static final String HTML_EXTENSION = ".html";

    /**
     * Personalization content base path.
     */
    public static final String CONTENT_PERSONALIZATION_ACE_WWW_PATH = "/content/personalization/ace-www/";

}
