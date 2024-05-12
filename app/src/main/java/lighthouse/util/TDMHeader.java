package lighthouse.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * TDMHeader represents the header node of the Term-Document Matrix. 
 * Includes an iterator interface for traversal of the linked list
 */
public class TDMHeader implements Iterable<TDMNode> {
    private String term;
    private int len;
    private TDMNode listHead;

    /**
     * Constructs a new TDMHeader with passed term and initial node.
     * @param term term associated with TDMHeader
     * @param node intial node used for TDMHeader
     */
    public TDMHeader(String term, TDMNode node) {
        this.term = term;
        this.listHead = node;
        this.len = 1;
    }

    /**
     * Obtains the term associated with the TDMHeader
     * @return term associated with the TDMHeader
     */
    public String getTerm() {
        return term;
    }

    /**
     * Obtains the length of the linked list associated with the TDMHeader
     * @return length of the linked list associated with TDMHeader
     */
    public int getLen() {
        return len;
    }

    /**
     * Obtains the head of the linked list associated with the TDMHeader
     * @return head of the linked list associated with TDMHeader
     */
    public TDMNode getListHead() {
        return listHead;
    }

    /**
     * Appends a TDMNode to the end of the linked list associated with the TDMHeader.
     * @param node The TDMNode to be appended.
     */
    public void append(TDMNode node) {
        TDMNode tail = this.listHead;
        for (final TDMNode n : this) {
            tail = n;
        }
        len++;
        tail.setNext(node);
    }

    /**
     * Returns an iterator over the leements in the linked list associated with the TDMHeader
     * @return TDM Iterator starting at head of TDM linked list
     */
    @Override
    public Iterator<TDMNode> iterator() {
        return new TDMIterator(listHead);
    }

    /**
     * TDMIterator is an iterator over the linked list associated with TDMHeader. 
     */
    private class TDMIterator implements Iterator<TDMNode> {
        // iterator interface to allow for-each linkedlist traversal
        private TDMNode iter;

        /**
         * Constructs a TDMIterator with the passed head node
         * @param listHead head node to be used in the TDMIterator
         */
        public TDMIterator(TDMNode listHead) {
            this.iter = listHead;
        }

        /**
         * returns boolean indicating if there is a next value
         * @return true if there exists a next element in iterator, false otherwise
         */
        @Override
        public boolean hasNext() {
            return iter != null;
        }

        /**
         * Obtains the next node in the iteration.
         * @return the next TDMNode in the iteration
         * @throws NoSuchElementException if there are no more elements that can be iterated over after
         */
        @Override
        public TDMNode next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            TDMNode nextNode = iter;
            iter = iter.getNext();

            return nextNode;
        }
    }

}