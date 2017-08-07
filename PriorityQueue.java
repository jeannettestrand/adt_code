import java.util.Random;


public class PriorityQueue {
	private static class Node {
		public Object data;
		public int priority;
	}

	private Node[] array;
	private int nextIndex;

	public PriorityQueue() {
		array = new Node[16];
		nextIndex = 0;
	}
   
	public void enqueue(Object data, int priority) {
		if (nextIndex >= array.length) {
			//System.out.println("In resize");
			Node[] oldArray = array;
			Node[] newArray = new Node[array.length*2];
			array = newArray;
			
			int i = 0;
			for (Node cell : oldArray) {
				array[i] = cell;
				i++;
			}
		}
		//System.out.println("In enqueue, adding: " + data + " " + priority);
		array[nextIndex] = new Node();
			array[nextIndex].data = data;
			array[nextIndex].priority = priority;
		bubbleUp(nextIndex);
		nextIndex++;
	}
   
	public Object dequeue() {
	   if (nextIndex == 0) {
		   //System.out.println("Array is empty");
		   return null;
	   }
	   
	   else {
   		    //System.out.println("In dequeue, swapping with: " + array[nextIndex-1].data.toString() + " " +  array[nextIndex-1].priority);
			Object rootData = array[0].data;
			//System.out.println("Return root value: " + rootData.toString());
			array[0] = array[nextIndex-1];
			//System.out.println("Success on swapping last indext to array[0]");
			nextIndex--;
			bubbleDown(0);
			return rootData;
	   }
	}
	
   
	private void bubbleUp(int index) {
		if (index == 0) return;
		else {
			int parent = ( (index - 1) / 2 );
			if (array[parent].priority < array[index].priority) {
				Node temp = array[index];
				array[index] = array[parent];
				array[parent] = temp;
				bubbleUp(parent);
			}
			else 
				return;
		}
	}
   
   
	private void bubbleDown(int index) {
		if (index > nextIndex) return; 
		//System.out.println("In bubbleDown, starting with index: " + index);
			int l = (index * 2 + 1);
			//System.out.println("Left child: " + l);
			int r = (index * 2 + 2);
			//System.out.println("right child: " + r);
			int h = 0;
		
		if (l > nextIndex && r > nextIndex) return;
		
		if (array[l]!= null && array[r] != null) {
				if (array[l].priority > array[index].priority || array[r].priority > array[index].priority){
					if (array[l].priority > array[r].priority)
						h = l;
					else
						h = r;
					Node temp = array[h];
					array[h] = array[index];
					array[index] = temp;
					bubbleDown(h);	
				}
		}
		else if (array[l] == null && array[r]!= null) {
			if (array[r].priority > array[index].priority) {
				Node temp = array[r];
				array[r] = array[index];
				array[index] = temp;
				bubbleDown(r);	
			}
		}
		
		else if (array[r] == null && array[l]!= null) {
			if (array[l].priority > array[index].priority) {
				Node temp = array[l];
				array[l] = array[index];
				array[index] = temp;
				bubbleDown(l);	
			}
		}
	}
	

   
   // This method is for testing purposes; do not alter.
   public Object getDataAtIndex(int index) { return array[index].data; }
   
	public static void main(String[] args) {
		// create a queue and add some values with various priorities.
		PriorityQueue q = new PriorityQueue();
		Random rand = new Random();
		
		int i = 0;
		while (i < 1000) {
			int next = rand.nextInt();
			q.enqueue(i, next);
			System.out.println("adding: " + i + " " + next);
			i++;
		}
		
		Object last = q.dequeue();
		System.out.println(last.toString());
		while (last != null) {
			last = q.dequeue();
			System.out.println(last.toString());
		}



		}
}