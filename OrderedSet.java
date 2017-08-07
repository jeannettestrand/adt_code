public interface OrderedSet {
	
	public void add(Comparable data);
	public boolean contains(Comparable data);
	public Comparable first();
	public Comparable next();
	public int size();
	
}