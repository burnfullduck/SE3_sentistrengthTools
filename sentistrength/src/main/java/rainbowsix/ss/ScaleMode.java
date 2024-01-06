package rainbowsix.ss;

/**
 * 单指标分类模式.
 * @author XuChen
 */
public class ScaleMode extends Mode {
    @Override
    public String outputSpace() {
        return " " + this.getScaleValue();
    }

    @Override
    public String outputTable() {
        return "\t" + getScaleValue() + "\t";
    }

    @Override
    public void optDictWeig(final int iMinImp, final boolean bTotDif) {
        getC().optimiseDictionaryWeightingsForCorpusScale(iMinImp);
    }
}
