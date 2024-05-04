package lighthouse.util;

public class TDMNode {
    private String documentID;
    private int termCount;
    private double termWeight;
    private TDMNode next;

    public TDMNode(String documentID, int termCount) {
        this.documentID = documentID;
        this.termCount = termCount;
        this.termWeight = 0;
        this.next = null;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public int getTermCount() {
        return termCount;
    }

    public void setTermCount(int termCount) {
        this.termCount = termCount;
    }

    public double getTermWeight() {
        return termWeight;
    }

    public void setTermWeight(double termWeight) {
        this.termWeight = termWeight;
    }

    public TDMNode getNext() {
        return next;
    }

    public void setNext(TDMNode next) {
        this.next = next;
    }

}