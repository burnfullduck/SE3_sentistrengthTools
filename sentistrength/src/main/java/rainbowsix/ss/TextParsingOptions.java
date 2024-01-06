// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst
// Source File Name:   TextParsingOptions.java
package rainbowsix.ss;


/**
 * 文本处理选项类.
 * <br>包含文本性质，如粒度尺寸、是否包含标点符号等
 *
 * @author 注释编写 徐晨
 */
public class TextParsingOptions {

    /**
     * 是否包含标点.
     */
    private boolean bgIncludePunctuation;
    /**
     * 粒度尺寸.
     */
    private int igNgramSize;
    /**
     * 是否用到翻译.
     */
    private boolean bgUseTranslations;
    /**
     * 是否加入强调.
     */
    private boolean bgAddEmphasisCode;

    /**
     * 构造方法.
     */
    public TextParsingOptions() {
        setBgIncludePunctuation(true);
        setIgNgramSize(1);
        setBgUseTranslations(true);
        setBgAddEmphasisCode(false);
    }

    /**
     * 获取是否包含标点.
     *
     * @return 是否包含标点.
     */
    public boolean isBgIncludePunctuation() {
        return bgIncludePunctuation;
    }

    /**
     * 设置是否包含标点.
     *
     * @param b 是否包含标点.
     */
    public void setBgIncludePunctuation(final boolean b) {
        this.bgIncludePunctuation = b;
    }

    /**
     * 获取粒度尺寸.
     *
     * @return 粒度尺寸.
     */
    public int getIgNgramSize() {
        return igNgramSize;
    }

    /**
     * 设置粒度尺寸.
     *
     * @param i 粒度尺寸.
     */
    public void setIgNgramSize(final int i) {
        this.igNgramSize = i;
    }

    /**
     * 获取是否用到翻译.
     *
     * @return 是否用到翻译.
     */
    public boolean isBgUseTranslations() {
        return bgUseTranslations;
    }

    /**
     * 设置是否用到翻译.
     *
     * @param b 是否用到翻译.
     */
    public void setBgUseTranslations(final boolean b) {
        this.bgUseTranslations = b;
    }

    /**
     * 获取是否加入强调.
     *
     * @return 是否加入强调.
     */
    public boolean isBgAddEmphasisCode() {
        return bgAddEmphasisCode;
    }

    /**
     * 设置是否加入强调.
     *
     * @param b 是否加入强调.
     */
    public void setBgAddEmphasisCode(final boolean b) {
        this.bgAddEmphasisCode = b;
    }
}
