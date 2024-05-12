package lighthouse.util;

// node representing an entry in the term-document matrix
public class TDMNode extends ListNode {
    private String documentID;
    private int termCount;
    private double termWeight;

    public TDMNode(String documentID, int termCount) {
        this.documentID = documentID;
        this.termCount = termCount;
        this.termWeight = 0;
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
        return (TDMNode) super.getNext();
    }

    public void setNext(TDMNode next) {
        super.setNext(next);
    }

}