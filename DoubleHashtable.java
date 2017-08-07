import java.util.Arrays;
import java.lang.Integer;


public class DoubleHashtable implements LabHashtable {
	private int currArraySize;
	private int currStepFactor;
    private int numDrops;
    private HashData[] table;

//////////////////////////////////////////////////////////////////////////////////////	
	public DoubleHashtable () {
		table = new HashData[11];
		currStepFactor = stepFactor();
		currArraySize = table.length;
		numDrops = 0; 
	}
//////////////////////////////////////////////////////////////////////////////////////
	public void put(Object key, Object value){
		if (numDrops > (.75 * table.length))
			resize();
		
		HashData drop = new HashData();
			drop.key = key;
			drop.value = value;
		
		int i = Math.abs(key.hashCode()) % (currArraySize);
		
		if (table[i] == null){
			table[i] = drop;
			numDrops++;
		} 
		else {
			while (table[i] != null && !table[i].key.equals(key)) {
				i += currStepFactor; 
				i %= currArraySize;	
			}
			if (table[i] == null){
				table[i] = drop;
				numDrops++;
			}
			if (table[i].key.equals(key))
				table[i].value = value;
		}
	}		
	

//////////////////////////////////////////////////////////////////////////////////////
	public Object get(Object key){
		int i = Math.abs(key.hashCode()) % currArraySize;
		
		if (table[i] != null) {
			while (table[i] != null && !table[i].key.equals(key)){
				i += currStepFactor;
				i %= table.length;
			}
			if (table[i] == null) return null;
			if (table[i].key.equals(key)) return table[i].value;
		} 
		return null;
	}
//////////////////////////////////////////////////////////////////////////////////////
	public int getSize(){
		return currArraySize;
	}

///////////////////////////////////////////////////////////////////////////////////////	
	
	public Object[] getTable(){
		return table;
	}

//////////////////////////////////////////////////////////////////////////////////////
	
	private int stepFactor() {
        if (table.length != currArraySize) {
            currStepFactor = Primes.nextPrime(Math.max(30, table.length / 1000)) % table.length;
            currArraySize = table.length;
        }
        return currStepFactor;
    }
	
//////////////////////////////////////////////////////////////////////////////////////

	private void resize() {
		currArraySize = Primes.nextPrime(currArraySize*2);
		
		numDrops = 0;
		
		HashData[] newTable = new HashData[currArraySize];
        HashData[] oldTable = table;
		table = newTable;
		
		for(int i = 0; i < oldTable.length; i ++){
			if (oldTable[i] != null) {
				put(oldTable[i].key, oldTable[i].value);
			}
		}
	}
//////////////////////////////////////////////////////////////////////////////////////
/*    public static void main(String[] args) {

	DoubleHashtable doubleTable = new DoubleHashtable();
	String[] targets2 = {"the", "Adventure", "fish", "murmured", "committed","the", "Adventure", "fish", "murmured", "committed","the", "Adventure", "fish", "murmured", "committed","the", "Adventure", "fish", "murmured", "committed"};
	
	for (int i =0; i < targets2.length; i++){
		Integer getValue = (Integer) doubleTable.get(targets2[i]);
		if (getValue == null){
				doubleTable.put(targets2[i], 1);
			} 
		if (getValue != null) {
				int unwrap = getValue.intValue();
				unwrap++;
				doubleTable.put(targets2[i], unwrap);
		}
	}
	
	for (int i =0; i < targets2.length; i++){
		Integer getValue2 = (Integer) doubleTable.get(targets2[i]);
		System.out.println(targets2[i].toString() + " "+ getValue2.toString());
	}
}*/
}