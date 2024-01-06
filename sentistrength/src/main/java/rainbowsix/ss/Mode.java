package rainbowsix.ss;

/**
 * Mode类.
 * @author XuChen
 */
public class Mode {
    /**
     * 三元值.
     */
    private int trinaryValue = 0;

    /**
     * 获取三元值.
     * @return 三元值
     */
    public int getTrinaryValue() {
        return trinaryValue;
    }

    /**
     * 获取单指标值.
     * @return 单指标值
     */
    public int getScaleValue() {
        return scaleValue;
    }

    /**
     * 单指标值.
     */
    private int scaleValue = 0;

    /**
     * 获取语料库.
     * @return 语料库
     */
    public Corpus getC() {
        return c;
    }

    /**
     * 语料库.
     */
    private Corpus c;

    /**
     * 设置语料库.
     * @param corpus 语料库
     */
    public void setC(final Corpus corpus) {
        this.c = corpus;
    }

    /**
     * 根据具体类别情况得到相应值，默认为单指标值.
     * @return 值
     */
    public int getValue() {
        return scaleValue;
    }

    /**
     * 设置三元值、单指标值.
     * @param newTrinaryValue 三元值
     * @param newScaleValue 单指标值
     */
    public void setValue(final int newTrinaryValue, final int newScaleValue) {
        this.scaleValue = newScaleValue;
        this.trinaryValue = newTrinaryValue;
    }

    /**
     * space output.
     * @return 根据子类情况决定的字符串
     */
    public String outputSpace() {
        return "";
    }

    /**
     * table output.
     * @return 根据子类情况决定的字符串
     */
    public String outputTable() {
        return "\t";
    }

    /**
     * 优化字典权重.
     * @param iMinImp 最小提升
     * @param bTotDif 是否运用全差分
     */
    public void optDictWeig(final int iMinImp, final boolean bTotDif) {
        c.optimiseDictionaryWeightingsForCorpusPosNeg(iMinImp, bTotDif);
    }
}
