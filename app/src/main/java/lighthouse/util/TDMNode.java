package lighthouse.util;

public class TDMNode {
    private int documentID;
    private int termCount;
    private double termWeight;
    private TDMNode next;

    public TDMNode(int documentID, int termCount, int termWeight) {
        this.documentID = documentID;
        this.termCount = termCount;
        this.termWeight = termWeight;
        this.next = null;
    }

    public int getDocumentID() {
        return documentID;
    }

    public void setDocumentID(int documentID) {
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