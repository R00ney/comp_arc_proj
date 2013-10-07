// Cache Project (ECE 521)
// Neal O'Hara
// ngohara @ ngohara@ncsu.edu
// 9/19/13


/*---------------------------------------------------------------------------------------------------*/	
	// A Cache simulator
	// Needs to be variably defined in caceheSize, 
	// associativity: setASSOC
	// and blockSize

	// Every cache is N-way set associative
	// numSets = #cache blocks / setASSOC =  cacheSize/ (blockSize * setASSOC)
	// where setASSOC is the associativity assigned to the sets of this cache

	// All Addresses are 32 bits,
	// Addresses are addressed in hexadecimal format
	// Addresses are divided into parts via
	// (MSB) Tag  |  Index  |  Block offset (LSB)
	// num_block_offset = log_2(blockSize)
	// num_index_bits = log_2(numSets)
	// num_tag_bits = 32 - num_index_bits - num_block_offset


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
	// when cache writebacks, set invalid bits in stream buffer for block


	//Stream buffer Prefetching
	// Prefetch is enable or disabled for each cache
	// has N Stream buffers
	// Each buffer contains M memory blocks
	// Each has M valid bits
	// N = 0 disables prefetch
	//
	// cache receives read or write, checks cache and first entry in stream
	// if miss both, prefetch and replace next X+1 through X+M into stream buffer
	// no writeback from stream buffer
	// if hit in stream, allocate X from stream, shift stream and fetch X+M+1
	// multiple stream buffers are searched in parrallel, with LRU stream being updated
	// when dirty blocks evicted from cache, that block invalidated in stream buffer




	/*----------------------------------------------------------------------------*/

	// Basic Structures:

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

		
		public int SIZE;
		public int ASSOC;
		public int BLOCKSIZE;
		

		
		//initializes the cache
		public Cache(int size, int assoc, int blocksize){
			//Using master try catch to ensure initialized properly
			try{
				this.SIZE = size;
				this.ASSOC = assoc;
				this.BLOCKSIZE = blocksize;
				
			} catch (Exception e){
			//report error
				System.out.println("Cache Debug: cache did not initialize properly");
				System.out.println(e.toString());
			}
		}

/*---------------------------------------------------------------------------------------------------*/		
		private String readMemory(int address, int tag){
			
			return "complete";  //temp return statement
		}/// end read mem
		
/*---------------------------------------------------------------------------------------------------*/		
		private String writeThrough(int address, int tag){
			
			return "complete";  //temp return statement
		} /// end write through
		
/*---------------------------------------------------------------------------------------------------*/		
	
		
		
/*---------------------------------------------------------------------------------------------------*/		
	} //end Cache class
