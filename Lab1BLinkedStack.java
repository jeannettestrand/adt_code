/* 
    A linked list implementation of a stack which uses the structures
    of LinkedList.java. (Therefore, Is a child of LinkedList.)
   
    Complete the code as indicated by the comments
*/

public class LinkedStack extends LinkedList implements Stack {

   public LinkedStack() {
		super();
	}

   public void push(Object data) {
         Node newNode = new Node();
         newNode.data = data;
         if (head == null) {
            head = newNode;
            tail = newNode;
         } else {
            Node pointer = head; //pointer temp ref for head. head is top.
            head = newNode;
            head.next= pointer;
         }
   }

   public Object peek() throws EmptyCollectionException {
      if (head == null) {
			throw new EmptyCollectionException("Stack is Empty");
		} else {
			return head.data;
		}
   }
   

   public Object pop() throws EmptyCollectionException {
	   if (peek() == null) {
			throw new EmptyCollectionException("Stack is Empty");
		} else {
			Object temp = peek();
			head = head.next;
			return temp;
		}
	}
}

     