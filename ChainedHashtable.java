import java.util.LinkedList;
import java.util.Arrays;

public class ChainedHashtable {
	private int numDrops;
    private LinkedList[] table;
	private int currArraySize;
	
	/////////////////////////////////////////////////////////////////	
	
	public ChainedHashtable() {
		numDrops = 0;
		table = new LinkedList[10];
		currArraySize = 10;
	}
	
	/////////////////////////////////////////////////////////////////
	
	public void put(Object key, Object value) {
		if (numDrops > (.75 * currArraySize)){
			resize();
		}
		
		HashData drop = new HashData();
			drop.key = key;
			drop.value = value;
		
		int i = Math.abs(drop.key.hashCode() % (currArraySize - 1));

		LinkedList<HashData> bucket = table[i];

		if (bucket == null) {
			table[i] = new LinkedList();
			table[i].add(drop);
			numDrops++;
		} 
		if (bucket != null){
			for(int l = 0; l < bucket.size(); l++){
				HashData keyQuery = bucket.get(l);
				if (keyQuery.key.equals(drop.key)){
					keyQuery.key = drop.key;
					keyQuery.value = drop.value;
				}
			}
			bucket.add(drop);
			numDrops ++;
		}
	}
	/////////////////////////////////////////////////////////////////
    
	public Object get(Object key){
		int j = Math.abs(key.hashCode() % (currArraySize-1));
		if (table[j] != null){
			LinkedList<HashData> accessBucket = table[j];
			for(int k = 0; k < accessBucket.size(); k++ ) {
				HashData returnValue = accessBucket.get(k);
				if (returnValue.key.equals(key)) return returnValue.value;
			}
		}
		return null;
	}			
	
	/////////////////////////////////////////////////////////////////    
	
	public int getSize(){
		return numDrops;
	}
    
	/////////////////////////////////////////////////////////////////	
	public Object[] getTable(){
		return table;
	}
	
	/////////////////////////////////////////////////////////////////	
	
	private void resize() {
		currArraySize = currArraySize*2;
		LinkedList[] newTable = new LinkedList[currArraySize];
        LinkedList[] oldTable = table;
		numDrops = 0;
		table = newTable;
		for (int i = 0; i < oldTable.length; i ++) {
			if (oldTable[i] != null){
				LinkedList<HashData> list = oldTable[i];
				for (int j = 0; j < list.size(); j++){
					HashData item = list.get(j);
					put(item.key, item.value);
				}
			}
		}
	}
}	
	
