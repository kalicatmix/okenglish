package com.oe.okenglish.res;

public class Res {
    public static boolean ISDEV = true;
    public static final String[] settingLottieAnims = new String[]{
            "role/avatar-girl-blink.json",
            "role/beard-man-blink.json",
            "role/dog-avatar.json",
            "role/genius-avatar.json",
            "role/girl-with-phone.json",
            "role/marketing-agency.json"
    };
    public static final String OKENGLISH_DB = "okenglish.db";
    public static final String USER_DB = "user.db";
    public static String NONE_STRING = "";
    public static Object NONE = null;
    public static final String SQL_QUERY_WORD = "select Word , Description from word where WordID==?;";
    public static final String SQL_QUERY_VOICE = "select Voice from voice where Word==?;";
    public static final String SQL_QUERY_ID_BY_PLAN = "SELECT WordID FROM bookdata where BookID== ? ORDER BY WordID;";

    public static final String SQL_CREATE_USERTB = "create table user(id int primary key,name text,plan int,dayword int);";
    public static final String SQL_CREATE_DEFAULT_USER = "insert into user values(1,\"default\",1,30);";
    public static final String SQL_CREATE_FAVORITETB = "create table favo(id int ,word text,trans text);";
    public static final String SQL_CREATE_FAVORITETB_DEFALUT = "insert into favo values(1,\"Okenglish\",\"爱看英语\");";
    public static final String SQL_INSERT_USERTB = "insert into user values(?,?,?,?)";
    public static final String SQL_INSERT_FAVORITETB = "insert into favo values(?,?,?)";
    public static final String AUTHORITIES = "com.oe.okenglish";
    public static final String USER_CONTENT_URL = "content://" + AUTHORITIES + "/user";
    public static final String FAVO_CONTENT_URL = "content://" + AUTHORITIES + "/favo";
    public static final int USER_DB_VERSION = 1;
    public static final String CURRENT_LOGIN_ACCOUNT = "currentAccount";
    public static final String VOICE_DIR = "/voice";

    public static final String TOKEN_HEADER="token_auth";
    public static final String NETWORK_ERROR = "请检查网络情况";

    public static final String SHARE_TEXT = "快来试试OkEnglish吧，快乐学习外语";

}
