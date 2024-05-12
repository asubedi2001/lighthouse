package lighthouse.util;

/**
 * ListNode is an organization class that represents a node in a linked list
 */
public class ListNode {
	private ListNode next;

	/**
     * Constructs a new ListNode with a null reference to the next node.
     */
	public ListNode() {
		next = null;
	}

	/**
     * Obtains the next ListNode in the linked list.
     * @return The reference to the next ListNode.
     */
	public ListNode getNext() {
		return next;
	}

	/**
     * Sets the next ListNode in the linked list.
     * @param next The ListNode to be set as the next node.
     */
	public void setNext(ListNode next) {
		this.next = next;
	}
}
