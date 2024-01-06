// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst
// Source File Name:   Utilities.java
package rainbowsix.wka;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * weka包使用的各种工具方法的类.
 * <br>
 * 提供打印、添加到类路径和计算时间间隔等方法
 *
 * @author 注释编写 朱甲豪 詹美瑛
 */
public final class Utilities {
    /**
     * 1000的浮点数,用于进制转换.
     */
    private static final float THOUSAND = 1000L;

    /**
     * 构造函数.
     */
    private Utilities() {
    }

    /**
     * 将系统类路径打印到标准输出.
     */
    public static void printClasspath() {
        ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader();
        URL[] urls = ((URLClassLoader) sysClassLoader).getURLs();
        for (URL url : urls) {
            System.out.println(url.getFile());
        }

    }

    /**
     * 将给定的文件路径动态添加到系统类路径.
     *
     * @param s 需要被添加的文件路径
     * @throws Exception 如果文件路径无效或方法调用失败，则抛出异常
     */
    public static void addToClassPath(final String s) throws Exception {
        File f = new File(s);
        URL u = f.toURL();
        URLClassLoader urlClassLoader
                = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<URLClassLoader> urlClass = URLClassLoader.class;
        // use reflection to access the private addURL method of URLClassLoader
        // this may not work in some environments due to security restrictions
        // see https://stackoverflow.com/questions/252893/how-do-you-change-the-classpath-within-java
        // for alternatives
        Method method = urlClass.getDeclaredMethod("addURL", URL.class);
        method.setAccessible(true);
        Object invoke;
        invoke = method.invoke(urlClassLoader, u);
    }

    /**
     * 将方法名称和当前时间打印到标准输出，带换行符.
     *
     * @param methodName 方法名称
     */
    public static void printlnNameAndWarning(final String methodName) {
        System.out.println("Starting " + methodName + " " + now() + " ... ");
    }

    /**
     * 将方法名称和当前时间打印到标准输出，不换行.
     *
     * @param methodName 方法名称
     */
    public static void printNameAndWarning(final String methodName) {
        System.out.print("Starting " + methodName + " " + now() + " ... ");
    }

    /**
     * 得到当前日期时间的字符串.
     *
     * @return 当前日期时间的字符串
     */
    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    /**
     * 得到Date类型的当前日期时间.
     *
     * @return Date类型的当前日期时间
     */
    public static Date getNow() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    /**
     * 计算两个日期时间的间隔，第二个减去第一个.
     *
     * @param firstDate  Date类型的日期时间
     * @param secondDate Date类型的日期时间
     * @return 间隔时间，单位为秒的字符串
     */
    public static String timeGap(final Date firstDate, final Date secondDate) {
        Calendar calFirst = Calendar.getInstance();
        Calendar calSecond = Calendar.getInstance();
        calFirst.setTime(firstDate);
        calSecond.setTime(secondDate);
        long firstMilis = calFirst.getTimeInMillis();
        long secondMilis = calSecond.getTimeInMillis();
        return (secondMilis - firstMilis) / THOUSAND + " secs";
    }
}
