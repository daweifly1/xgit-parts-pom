package cn.com.xgit.parts.auth.account.manager.cache;

public class CacheTime {
    /**
     * 缓存失效 1秒
     */
    public static final int CACHE_EXP_SECOND = 1;
    /**
     * 缓存时效 1分钟
     */
    public static final int CACHE_EXP_MINUTE = 60;

    /**
     * 缓存时效 10分钟
     */
    public static final int CACHE_EXP_MINUTES = 60 * 10;

    /**
     * 缓存时效 60分钟
     */
    public static final int CACHE_EXP_HOURS = 60 * 60;

    /**
     * 缓存时效 6:小时
     */
    public static final int CACHE_EXP_QUARTER_DAY = 6 * 60 * 60;

    /**
     * 缓存时效 12小时
     */
    public static final int CACHE_EXP_HALF_DAY = 12 * 60 * 60;

    /**
     * 缓存时效 1天
     */
    public static final int CACHE_EXP_DAY = 3600 * 24;

    /**
     * 缓存时效 1周
     */
    public static final int CACHE_EXP_WEEK = 3600 * 24 * 7;

    /**
     * 缓存时效 1月
     */
    public static final int CACHE_EXP_MONTH = 3600 * 24 * 30;

    /**
     * 缓存时效 永久
     */
    public static final int CACHE_EXP_FOREVER = 0;
}
