
public class ArrayStack implements Stack {

   private final int DEFAULT_STACK_SIZE = 10;
   private int topOfStackIndex;  
   private Object[] objArray;
   

	public ArrayStack(int sizeStack) {
		objArray = new Object[sizeStack];
		topOfStackIndex = 0;
   }
   
	public ArrayStack() {
		objArray = new Object[DEFAULT_STACK_SIZE];
		topOfStackIndex = 0;
   }
   
	public void push(Object element) {
		if (!isEmpty()) //should be more like if objArray.length = sizq... resize
			expandCapacity();
		objArray[topOfStackIndex] = element;
		topOfStackIndex++;
	}

	public Object peek() throws EmptyCollectionException {
		if (isEmpty())
			throw new EmptyCollectionException("Array is Empty");
		return objArray[topOfStackIndex-1];
	}

   public Object pop() throws EmptyCollectionException {
		if (isEmpty())
			throw new EmptyCollectionException("Array is Empty");
	    Object temp = peek();
		topOfStackIndex--;
		return temp;
		}
   
   public boolean isEmpty() {
		return (topOfStackIndex == 0)
   }

   public int size() {
      return topOfStackIndex;
   }

   //public String toString() {
      //return(this.toString());
   //}

   private void expandCapacity() {
    int newSize = (size()*2);
		Object[] expandArrayStack = new Object[newSize];
		for (int i = 0; i < size(); i++){
			expandArrayStack[i] = objArray[i];
		}
		objArray = expandArrayStack;
   }  
}

