package rainbowsix.ss;

/**
 * 三元分类模式.
 * @author XuChen
 */
public class TrinaryMode extends Mode {
    @Override
    public int getValue() {
        return getTrinaryValue();
    }

    @Override
    public String outputSpace() {
        return " " + this.getTrinaryValue();
    }

    @Override
    public String outputTable() {
        return "\t" + getTrinaryValue() + "\t";
    }

    @Override
    public void optDictWeig(final int iMinImp, final boolean bTotDif) {
        getC().optimiseDictionaryWeightingsForCorpusTrinaryOrBinary(iMinImp);
    }
}
