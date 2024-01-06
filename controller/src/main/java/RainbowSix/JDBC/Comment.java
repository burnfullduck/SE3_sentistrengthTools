package RainbowSix.JDBC;

import java.sql.Date;

public class Comment {
    private int issueNo;
    private String textNo;
    private String subName;
    private String reviewer;
    private Date time;
    private String text;
    private int senti;

    public int getIssueNo() {
        return issueNo;
    }

    public void setIssueNo(int issueNo) {
        this.issueNo = issueNo;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getTextNo() {
        return textNo;
    }

    public void setTextNo(String textNo) {
        this.textNo = textNo;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getSenti() {
        return senti;
    }

    public void setSenti(int senti) {
        this.senti = senti;
    }
}
