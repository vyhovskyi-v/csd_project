package com.github.vyhovskyi.constans;

public class Page {
    private static final String PREFIX = "src/main/resources/";
    private static final String ERROR_PREFIX = "errors/";
    private static final String SUFFIX = ".html";

    private Page() {}

    public static final String LOGIN_VIEW = PREFIX + "login" + SUFFIX;
    public static final String HOME_VIEW = PREFIX + "main" + SUFFIX;

    public static final String ALL_PRODUCTS_VIEW = PREFIX + "goods" + SUFFIX;
    public static final String ADD_UPDATE_PRODUCT_VIEW = PREFIX + "addUpdateGood" + SUFFIX;

    public static final String ALL_GROUPS_VIEW = PREFIX + "groups" + SUFFIX;
    public static final String ADD_UPDATE_GROUP_VIEW = PREFIX + "addUpdateGroup" + SUFFIX;



    public static final String PAGE_NOT_FOUND_VIEW = PREFIX + "pageNotFound" + SUFFIX;
}
