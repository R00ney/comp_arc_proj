// Cache Project (ECE 521)
// Neal O'Hara
// ngohara @ ngohara@ncsu.edu
// 9/19/13

// A Cache simulator
// Needs to be variably defined in Size, 
// associativity: ASSOC
// and BlockSize

// Every cache is N-way set associative
// #sets = #cache blocks / ASSOC =  Size/ BlockSize * ASSOC
// where ASSOC is the associativity
//

// Caches use LRU (least-recently-used block replaced)
// small counter per block in set
// if hit, block counter set to 0
// increment other counters THAT ARE LESS THAN it was
// if miss, replace highest counter, 
// and set that to 0, increment others
//

// Caches use WBWA (write-back + write allocate)
// Write allocate means a write miss causes a block to 
// be allocated in cache (in addition to read misses)
// Write back means we need dirty bits with each block in set,
// such that when block evicted, it then writes to  
// lower cache or memory

// basic structures:

// Cache will call Sets
// Each Set has individual index bits
// Sets will call Blocks

// Each Block in Set will have LRU Counter 
// Each Block will have dirty bit
// Each Block has individual offset bits

// Blocks generate bytes
// Each Byte has individual Address


// Allocation
// first: make space for requested block
//		Use LRU to replace in full cache
//		Use Dirty bits for write through
// second: bring in requested block
//		Make a cache read call


public class Cache {

	
	public static int SIZE = null;
	public static int ASSOC = null;
	public static int BLOCKSIZE = null;
	
	public static void main (String[] args) {
	
	} // end main

	public Cache(int size, int assoc, int blocksize){
		this.SIZE = size;
		this.ASSOC = assoc;
		this.BLOCKSIZE = blocksize;
		
	}
	
	private readMemory(int address){
	
	}
	
	
	
	
} //end Cache class

