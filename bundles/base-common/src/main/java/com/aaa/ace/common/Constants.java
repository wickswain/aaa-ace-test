package com.aaa.ace.common;

/**
 * The class contains common application constants.
 */
public final class Constants {

    /**
     * EMPTY String.
     */
    public static final String EMPTY_STRING = "";

    /**
     * EMPTY Space String.
     */
    public static final String EMPTY_SPACE_STRING = " ";

    /**
     * Charset name.
     */
    public static final String CHARSET_NAME = "UTF-8";

    /**
     * The content node name used in AEM.
     */
    public static final String JCR_CONTENT = "jcr:content";

    /**
     * New line character.
     */
    public static final String NEW_LINE = "\n";

    /**
     * HTML extension.
     */
    public static final String HTML_EXTENSION = ".html";

    /**
     * JSON content type.
     */
    public static final String JSON_CONTENT_TYPE = "application/json";

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
     * Personalization content base path.
     */
    public static final String CONTENT_PERSONALIZATION_ACE_WWW_PATH = "/content/personalization/ace-www/";

    /**
     * Instantiates a new constants.
     */
    private Constants() {
    }

}
