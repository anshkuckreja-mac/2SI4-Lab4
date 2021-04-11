
public class HashTableLin {
	
	
	private Integer[] keyarr; //theta(n)
	private int tableSize;
	private int numKeys;
	private double maxlf;
	
	public HashTableLin(int maxNum, double load) {
		
		// as per instructions
		this.maxlf = load;
		
		// starts with 0 keys
		this.numKeys = 0;
		
		// so this gets the integer that's just above a potential decimal
		
		this.tableSize = (int)(Math.ceil(maxNum/maxlf));
		
		// now need to find next highest prime number
		// use the prime number function i made
		// so loop from the number up, and once the next prime number has been hit,
		// create the table from the new tableSize
		
		/*
		for (int i = tableSize; i <= (10^6); i++) {
			if (isPrime(i)) {
				tableSize = i;
				keyarr = new Integer[tableSize];
				break;
			}
		}
		*/
		
		int i = tableSize;
		while (true){
			if (isPrime(i)) {
				tableSize = i;
				keyarr = new Integer[tableSize];
				break;
			}
			i++;
		}
		
	}
	
	
	// really quick method I wrote to check if something is prime
	public static boolean isPrime (int num) {
		
		// boundary conditions
		if (num <= 1) { return false;}
		
		for (int i = num - 1; i > 1; i--) {
			if (num % i == 0) {
				return false;
			}
		}
		return true;
	}
	
	public void insert(int n) {
		
		// so first, if the maximum number of keys over the size of the table is > load factor
		// ^ that's the equation, 
		// we need to rehash!
		
		
		if (((numKeys + 1.0) / tableSize) > maxlf) {
			
			
            rehash();
        }
		
		// rehashing doesn't insert tho, so we still need to do that nonetheless
		
		int ind = 0;
		// loop through all the indices of the table
		for(int i = 0; i < tableSize; i++) {
			
			// so this is the hash function,
			// and we add i because in the first case, i is always 0
			// so if it's not at the location, next increment, i will increase by 1
			// THIS SYNTAX WAS GIVEN BY TA IN OFFICE HOURS 
			ind = (int)(((long)(n)+((long)i)) % this.tableSize);
			
			// so if there's nothing there, assign ez
			if (keyarr[ind] == null) {
				keyarr[ind] = n;
				numKeys++;
				break;
			}
			// so if that SAME value is already there
			// just don't do anything
			else if(keyarr[ind] == n) {
				return;
			}
			
			// the "ELSE" case would just be to go through the loop again and keep searching
			// nice
		}
	}
	
	
	private void rehash() {
		
		// allocates a bigger table because the load > maxlf
		// size is smallest prime number larger than double of original sizE
		// to find this, literally just call the constructor again with the double number of keys because it 
		// automatically finds the next prime number
		
		
		// now make a new hash table with the new size
		HashTableLin t2 = new HashTableLin(numKeys*2,this.maxlf);
		
		
		// now need to insert all the original values into this new hash table
		for(int i = 0; i < tableSize; i++) {
			if(keyarr[i] != null) {
				t2.insert(this.keyarr[i]);
			}
		}
		
		// and now just copy all the original data over
		// should have the same number of keys as the original
		this.keyarr = t2.getKeyArr();
		this.tableSize = t2.getTableSize();
		
		

		
		
	}
	
	
	public boolean isIn(int n) {
		
		// returns true if integer n is already a key in the table
		// so loop through the table and check
		// the fastest way to do so is to first find where the element would be, and then move from there
		// because that's how the insert function works
		
		int ind = 0;
		
		// loop through all the indices of the table
		for(int i = 0; i < tableSize; i++) {
			
			// so this is the hash function,
			// and we add i because in the first case, i is always 0
			// so if it's not at the location, next increment, i will increase by 1
			ind = (int)(((long)(n)+((long)i)) % this.tableSize);
			
			// so if there's nothing there, you know the key isn't there
			// because it would've been inserted there
			
			// so just check if the key is the index literally
			if (keyarr[ind] == null) {
				return false;
			}
			// so if that SAME value is already there
			// just don't do anything
			else if(keyarr[ind] == n) {
				return true;
			}
			
		}
		
		return false;
		
		
	}
	
	public void printKeys() {
		
		// very straightforward, print keys in array in no particular order
		for (int i = 0; i < tableSize; i++) {
			if (keyarr[i] != null) {
				System.out.println(keyarr[i]);
			}
			
		}
	}
	
	public void printKeysAndIndexes() {
		
		// this is almost identical to the other one
		for (int i = 0; i < tableSize; i++) {
			if (keyarr[i] != null) {
				System.out.println("Index: " + i + " Value: " + keyarr[i]);
			}
		}
	}
	
	public Integer[] getKeyArr() {
		return this.keyarr;
	}
	
	
	public int getTableSize() {
		return this.tableSize;
	}
	
	public int getNumKeys() {
		return numKeys;
	}
	
	public double getMaxLoadFactor(){
		return maxlf;
	}
	
	
	// returns the number of probes needed to insert integer n
	// as per lab instructions
	public int insertCount(int n) {
		
		int probes = 0; // will be the outputted value
		
		
		// so just like in insert, we rehash if necessary
		if (((numKeys + 1.0) / tableSize) > maxlf) {
            rehash();
        }
		
		// recall load factor = # elements in hash table / size of hash table
		
		int ind = 0;
		int probes_till_success = 0;
		int total_successes = 0;
		double lambda; // need lambda to be a decimal, but the other two are integers
		
		for (int i = 0; i < tableSize; i++) {
			
			probes++; // every iteration, you've probes once more
			ind = (int)(((long)(n)+((long)i)) % this.tableSize); // hash function again
			
			
			// so like last time, if there's nothing there, you're going to insert the value there
			
			if (keyarr[ind] == null) {
				keyarr[ind] = n;
				numKeys++;
				
				// except now you're ALSO going to count the number of probes it took to get there
				probes_till_success += probes;
				total_successes ++;
				lambda = probes_till_success / 1.0 * total_successes; // need lambda to be a decimal
				
				// note we add ON TOP of the values so that the function can be called a bajillion times and averages can be taken
				
				return probes;
			}
			// so if that SAME value is already there
			// then we stop searching
			// the RETURN here means we only stop once it's been found or added
			
			else if(keyarr[ind] == n) {
				return probes;
			}
			
			
		}
		
		
		
		return probes;
	}
	
	// so this is almost identical to insertCount
	// except it only does unsuccessful accounts
	// so like the same but it doesn't insert - idk the TA said to do it like this idrk why it works
	public int searchCount(int n) {
		
		int probes = 0; // will be the outputted value
		
		
		
		// recall load factor = # elements in hash table / size of hash table
		
		int ind = 0;
		int probes_till_success = 0;
		int total_successes = 0;
		double lambda; // need lambda to be a decimal, but the other two are integers
		
		for (int i = 0; i < tableSize; i++) {
			
			probes++; // every iteration, you've probes once more
			ind = (int)(((long)(n)+((long)i)) % this.tableSize); // hash function again // hash function again
			
			
			// now you're NOT going to insert the value there
			
			if (keyarr[ind] == null) {
				
				
				// except now you're ONLY going to count the number of probes it took to get there
				probes_till_success += probes;
				total_successes ++;
				lambda = probes_till_success / 1.0 * total_successes; // need lambda to be a decimal
				
				// note we add ON TOP of the values so that the function can be called a bajillion times and averages can be taken
				
				return probes;
			}
			// so if that SAME value is already there
			// then we stop searching
			// the RETURN here means we only stop once it's been found or added
			
			else if(keyarr[ind] == n) {
				return probes;
			}
			
			
		}
		
		
		
		return probes;
	}
	
	
	
	
	
	
	
	public static void main(String[] args) {
		
		System.out.println(isPrime(4));
	}
}
