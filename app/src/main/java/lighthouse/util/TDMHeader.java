package lighthouse.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class TDMHeader implements Iterable<TDMNode> {
    private String term;
    private int len;
    private TDMNode listHead;

    public TDMHeader(String term, TDMNode node) {
        this.term = term;
        this.listHead = node;
        this.len = 1;
    }

    public String getTerm() {
        return term;
    }

    public int getLen() {
        return len;
    }

    public TDMNode getListHead() {
        return listHead;
    }

    public void append(TDMNode node) {
        TDMNode tail = this.listHead;
        for (final TDMNode n : this) {
            tail = n;
        }
        len++;
        tail.setNext(node);
    }

    @Override
    public Iterator<TDMNode> iterator() {
        return new TDMIterator(listHead);
    }

    private class TDMIterator implements Iterator<TDMNode> {
        // iterator interface to allow for-each linkedlist traversal
        private TDMNode iter;

        public TDMIterator(TDMNode listHead) {
            this.iter = listHead;
        }

        @Override
        public boolean hasNext() {
            return iter != null;
        }

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