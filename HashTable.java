import java.util.ArrayList;

/**
 * A basic HashTable that uses Separate Chaining (as opposed to probing) for storage of its elements
 * @author samberling
 *
 * @param <K> the key type for accessing elements in this table
 * @param <V> the value type of elements stored in this table
 */
public class HashTable<K,V>{
	private int size;

	private ArrayList<ArrayList<Node<K,V>>> table; //list of lists for Separate Chaining

	private int m;
	private short modsIndex;
	private double loadFactor;

	private final int[] MODS = {11, 19, 41, 79, 163, 317, 641, 1279, 2557, 5119, 10243, 20479, 40961, 81919, 163841, 327673}; 
	private static final double MAX_LOAD = .75;

	/**
	 * This constructor sets the initial size of the table to 0, as well as the modsIndex 
	 * which keeps track of which mod operator, m, we are using for the size of our table 
	 */
	public HashTable(){
		size = 0;
		modsIndex = 0;
		m = MODS[modsIndex];

		table = new ArrayList<ArrayList<Node<K,V>>>(); 	
	}

	/**
	 * This method puts an element into the table.
	 * @param key the key for the element to be stored
	 * @param value the value of the element to be stored
	 */
	public void put(K key, V value){
		if(contains(key)){ //if the Node is already in the Table
			helpfulContains(key).value = value; //just change its value
		}
		else{ //Node is not already in the table
			int k = hash(key);
			table.get(k).add(new Node<K,V>(key, value));
			size++;
			loadFactor = ((double) size/ (double) m);
			checkLoadFactor();
		}
	}

	/**
	 * This method returns the value associated with the given key
	 * @param key the key for the element we wish to access
	 * @return the value associated with the given key 
	 */
	public V get(K key){
		try{
			return helpfulContains(key).value;
		}
		catch(NullPointerException e){
			return null;
		}
	}

	/**
	 * This method returns whether the table contains any value associated with the provided key
	 * @param key the key for which we are searching for a value
	 * @return true if it finds something when looking up the key, false if not
	 */
	public boolean contains(K key){
		return helpfulContains(key) != null;
	}

	/**
	 * Deletes the Node (key-value pair) found by looking up the given key
	 * @param key the key for the element to be deleted
	 */
	public void delete(K key){
		if(contains(key)){
			size--;
			table.get(hash(key)).remove(helpfulContains(key));
		}
	}

	/**
	 * This method tells us the number of elements stored in the table
	 * @return the number of elements stored in the table
	 */
	public int size(){
		return size;
	}

	/**
	 * This method is a more useful version of a generic contains(key) method. 
	 * @param key the for the element we are searching for
	 * @return null if an element associated with the given key cannot be found and returns the Node element otherwise
	 */
	private Node<K,V> helpfulContains(K key){
		int k = hash(key);
		if(table.size() <= k){
			while(table.size() <= k){
				table.add(new ArrayList<Node<K,V>>());
			}
		}
		else{
			for(Node<K,V> node : table.get(k)){
				if(node.key.equals(key)){
					return node;
				}
			}
		}
		return null;
	}

	/**
	 * Returns our hashed value of an object which is necessarily positive and modded by our mod factor m
	 * @param object the object to be hashed
	 * @return our version of the hash value of the object
	 */
	private int hash(K object){
		int hash = object.hashCode();
		hash &= 0x7fffffff;
		return hash%m;
	}

	/**
	 * This method reorganizes the table as soon as the threshold for the capacity of table
	 * (i.e. the number of elements has surpassed the loadFactor in relation to our mod operator m)
	 */
	private void rehash(){
		if(modsIndex < MODS.length-1){
			size = 0; //reset size for all the put calls
			m = MODS[++modsIndex];
			ArrayList<ArrayList<Node<K,V>>> temp = new ArrayList<ArrayList<Node<K,V>>>();
			for(ArrayList<Node<K,V>> sublist : table){
				temp.add(sublist);
			}
			table = new ArrayList<ArrayList<Node<K,V>>>(); //reset our table
			for(ArrayList<Node<K,V>> sublist : temp){
				for(Node<K,V> node : sublist){ //refill our table
					put(node.key, node.value);
				}
			}
		}
	}

	/**
	 * This method does a simple check to see whether or not its time to rehash
	 */
	private void checkLoadFactor() {
		if(loadFactor > MAX_LOAD){
			rehash();
		}
	}
}

/**
 * This class acts a simple Tuple for holding the Key-Value pairs for the information we're trying to hold
 * @author samberling
 *
 * @param <K> the Key type
 * @param <V> the Value type
 */
class Node<K,V>{
	protected K key;
	protected V value;

	/**
	 * The constructor for our tuple
	 * @param k the key to be stored
	 * @param v the value to be stored
	 */
	public Node(K k, V v){
		key = k;
		value = v;
	}
}

