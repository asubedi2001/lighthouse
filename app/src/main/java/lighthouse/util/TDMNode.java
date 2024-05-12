package lighthouse.util;

/**
 * TDMNode is a node representing a node in the Term-Document Matrix. 
 * Extends ListNode and contains information regarding term count, weight, documentID where term is found.
 */
public class TDMNode extends ListNode {
    private String documentID;
    private int termCount;
    private double termWeight;

    /**
     * Constructs a TDMNode with the specified document ID and term count.
     *
     * @param documentID The id of the document associated with the node.
     * @param termCount  The count for the frequency of occurence of this term in the document.
     */
    public TDMNode(String documentID, int termCount) {
        this.documentID = documentID;
        this.termCount = termCount;
        this.termWeight = 0;
    }

    /**
     * Obtains document ID associated with TDMNode
     * @return document ID associated with TDMNode
     */
    public String getDocumentID() {
        return documentID;
    }

    /**
     * Sets document ID associated with TDMNode
     * @param documentID documentID used to replace the previous documentID
     */
    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    /**
     * Gets term count associated with this TDMNode
     * @return term count associated with this TDMNode
     */
    public int getTermCount() {
        return termCount;
    }

    /**
     * Sets term count associated with TDMNode
     * @param termCount term count used to replace the previous term count
     */
    public void setTermCount(int termCount) {
        this.termCount = termCount;
    }

    /**
     * Gets term weight associated with this TDMNode
     * @return term weight associated with this TDMNode
     */
    public double getTermWeight() {
        return termWeight;
    }

    /**
     * Sets term weight associated with TDMNode
     * @param termWeight term weight used to replace the previous term weight
     */
    public void setTermWeight(double termWeight) {
        this.termWeight = termWeight;
    }

    /**
     * Gets next node associated with this TDMNode
     * @return next node that follows this TDMNode
     */
    public TDMNode getNext() {
        return (TDMNode) super.getNext();
    }

    /**
     * Sets next node associated with this TDMNode
     * @param next node that will follow this TDMNode
     */
    public void setNext(TDMNode next) {
        super.setNext(next);
    }

}