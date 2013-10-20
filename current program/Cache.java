// Cache Project (ECE 521)
// Neal O'Hara
// ngohara @ ngohara@ncsu.edu
// 9/19/13


/*---------------------------------------------------------------------------------------------------*/	
		// A Cache simulator
		// Needs to be variably defined in caceheSize, 
		// associativity: setASSOC
		// blockSize
		// l1_PREF_N
		// l1_PREF_M

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


	// Caches use WBWA (write-back + write allocate)
	// Write allocate means a write miss causes a block to 
	// be allocated in cache (in addition to read misses)
	// Write back means we need dirty bits with each block in set,
	// such that when block evicted, it then writes to  
	// lower cache or memory
	// when cache writebacks, set invalid bits in stream buffer for block



	// Allocation
	// first: make space for requested block
	//		Use LRU to replace in full cache
	//		Use Dirty bits for write back
	// second: bring in requested block
	//		Make a cache read call

	import java.lang.Math;

	public class Cache {

		//cache parameters
		private int cacheSize;
		private int setASSOC;
		private int blockSize;
		private String cacheName = null;
		
				
		//Sets Fields for this cache
		private int numSets;
		public Set[] mySets = new Set[1];
		
		//Pstream
		public PStream myPStream = null;
		
		//address dividers
		public int num_block_offset;
		public int num_index_bits;
		public int num_tag_bits; 

		//cache statistics	
		public long read_count = 0;
		public long read_miss = 0;
		public long write_count = 0;
		public long write_miss = 0;
		
		public long writeback_count = 0;
		
		public long pread_count = 0;
		public long pread_miss = 0;
		
		//next tier cache
		public Cache childCache = null;
		
		public boolean debugValid = false; //set true for debug statements
		
		
		//initializes the cache
		public Cache(int blocksize, int size, int assoc, int prefetchN, int prefetchM, String  name ){
			//Using master try catch to ensure initialized properly
			try{
				this.cacheSize = size;
				
				if(cacheSize==0) return; //if 0, cache does not exist
				
				this.setASSOC = assoc;
				this.blockSize = blocksize;
				this.cacheName = name;
				
				this.numSets = cacheSize/ (blockSize * setASSOC);
				
				this.num_block_offset =   log_2(blockSize);
				this.num_index_bits =   log_2(numSets);
				this.num_tag_bits = 32 - num_index_bits - num_block_offset;
				
				this.mySets = new Set[numSets];
				
				//Create Sets
				for(int k=0;k<numSets;k++){
					mySets[k] = new Set(setASSOC, k, num_tag_bits);
				}
				
				//Create Prefetch Stream
				myPStream = new PStream(prefetchN, prefetchM, num_tag_bits,num_block_offset, cacheName );
				
				
			} catch (Exception e){
			//report error
				System.out.println("Cache Debug: cache did not initialize properly");
				System.out.println(e.toString());
				e.printStackTrace();
			}
		}

/*---------------------------------------------------------------------------------------------------*/
		public Boolean cacheContainsBlock(int address){
			int index = getSetIndex(address);
			Boolean hit = mySets[index].containsBlock(address);

		
			return hit;
		}

/*---------------------------------------------------------------------------------------------------*/

	public void allocateSpace(int address){
		
		int index = getSetIndex(address);
		Boolean full = mySets[index].isFull();

		if(full){//Is full, allocate space
			Block writeBack = mySets[index].removeLRU();
			//handel writeback
			Boolean dirtyBit = writeBack.isDirty();
			
			if(dirtyBit){
				//actual writeback here
				writeback_count ++;
				
				
				if(childCache == null){
					//null, so hard memory, ie throwout block
					
					if(debugValid){//Debug print statement
					Block print = new Block(writeBack.getAddress());
					int masked = writeBack.getAddress() & (~(int)(Math.pow((double)2,(double)num_block_offset)-1));
					Integer tag = print.getTag(num_tag_bits);
					//L1 victim: 4214c9d0 (tag 210a64, index 29, dirty)
					System.out.println(cacheName +" victim: "+ Integer.toHexString(masked)+"  (tag "+Integer.toHexString(tag) + ", index "+index+", dirty)");
					}
					
					if(myPStream.nStreams != 0){
						myPStream.invalidateBlock(writeBack.getAddress());
						
						/*if(debugValid){//Debug print statement
						//L1-SB miss, select LRU buffer #0, update LRU
						int lru = myPStream.getLRUstream();
						System.out.println(cacheName +"-SB miss, select LRU buffer #"+lru+", update LRU");
						}*/
					}
					
				}
				else{
									
					if(debugValid){//Debug print statement
					Block print = new Block(writeBack.getAddress());
					int masked = writeBack.getAddress() & (~(int)(Math.pow((double)2,(double)num_block_offset)-1));
					Integer tag = print.getTag(num_tag_bits);
					//L1 victim: 4214c9d0 (tag 210a64, index 29, dirty)
					System.out.println(cacheName +" victim: "+ Integer.toHexString(masked)+"  (tag "+Integer.toHexString(tag) + ", index "+index+", dirty)");
					}
					
					childCache.writeMemory(writeBack.getAddress());
					
					if(myPStream.nStreams != 0){
						myPStream.invalidateBlock(writeBack.getAddress());
						
						/*if(debugValid){//Debug print statement
						//L1-SB miss, select LRU buffer #0, update LRU
						int lru = myPStream.getLRUstream();
						System.out.println(cacheName +"-SB miss, select LRU buffer #"+lru+", update LRU");
						}*/
					}

					
				}
			}//else ignore clean block
			else{
			
			if(debugValid){//Debug print statement
			Block print = new Block(writeBack.getAddress());
			int masked = writeBack.getAddress() & (~(int)(Math.pow((double)2,(double)num_block_offset)-1));
			Integer tag = print.getTag(num_tag_bits);
			//L1 victim: 4214c9d0 (tag 210a64, index 29, clean)
			System.out.println(cacheName +" victim: "+ Integer.toHexString(masked)+"  (tag "+Integer.toHexString(tag) + ", index "+index+", clean)");
			}
			
			if(myPStream.nStreams != 0){
				//not dirty, no invalidate
				//myPStream.invalidateBlock(writeBack.getAddress());
				
				/*if(debugValid){//Debug print statement
				//L1-SB miss, select LRU buffer #0, update LRU
				int lru = myPStream.getLRUstream();
				System.out.println(cacheName +"-SB miss, select LRU buffer #"+lru+", update LRU");
				}*/
			}
					
			}
			
		}
		else{ //not full, but need to shift for new MRU
			
			mySets[index].incNonFull();
		
			if(debugValid){//Debug print statement			
			//L1 victim: none
			System.out.println(cacheName + " victim: none");
			
				/*if(myPStream.nStreams != 0){
				//L1-SB miss, select LRU buffer #0, update LRU
				int lru = myPStream.getLRUstream();
				System.out.println(cacheName +"-SB miss, select LRU buffer #"+lru+", update LRU");
				}*/
			}
		
			
		}
		
	
	}


/*---------------------------------------------------------------------------------------------------*/
		//either contains block, or space is allocated for block
		public Block readMemory(int address, boolean prefetch){
			
			int index = getSetIndex(address);
			
			if(prefetch){ 
				pread_count++;
			}
			else  read_count++;
			
			
			
			if(debugValid){//Debug print statement
			Block print = new Block(address);
			int masked = address & (~(int)(Math.pow((double)2,(double)num_block_offset)-1));
			Integer tag = print.getTag(num_tag_bits);
			//L1 read : dfcfa0 (tag 6fe7, index 26)
			System.out.println(cacheName +" read : "+ Integer.toHexString(masked)+" (tag "+Integer.toHexString(tag) + ", index "+index+")");
			}
			
			Boolean hit = cacheContainsBlock(address);
			Boolean pstream_hit = false;
			if(myPStream.nStreams != 0){
				pstream_hit = myPStream.containsBlock(address);
			}
			
			if(hit){
				if(debugValid){//Debug print statement
				//L1 hit
				System.out.println(cacheName + " hit");
				//L1 Update LRU
				System.out.println(cacheName + " Update LRU");
				}
				
				Block is_read =  mySets[index].resetMRU(address);
				Block pass = new Block(is_read.getAddress()); //isolate references
				pass.setDirty(false); //blocks passed up are not dirty
				return pass;  //read hit
			}
			else if(pstream_hit){
				if(debugValid){//Debug print statement
				//L1 miss
				System.out.println(cacheName + " miss");			
				}
				
				//allocate space 
				allocateSpace(address);
				//if(prefetch) pread_miss++; 
				//else read_miss++;
				
				if(debugValid){//Debug print statement
				//L1-SB #1 hit, pop first entry, update LRU
				System.out.println(cacheName + "-SB #x hit, pop first entry, update LRU");
				//L1 Update LRU
				System.out.println(cacheName + " Update LRU");
				}
				
				//read from prefetch stream
				Block is_read = myPStream.fetchBlock(address, childCache);
				Block pass = new Block(is_read.getAddress()); //isolate references
				pass.setDirty(false); //blocks passed up are not dirty
				
				mySets[index].addMRU( pass.getAddress(),false);//blocks passed up are not dirty
				
				return pass;
			}
			else{
				if(debugValid){//Debug print statement
				//L1 miss
				System.out.println(cacheName + " miss");			
				}
				
				//allocate space
				allocateSpace(address);
				if(prefetch) pread_miss++; 
				else read_miss++;
				
				if(debugValid){//Debug print statement
				if(myPStream.nStreams != 0){
				//L1-SB miss, select LRU buffer #0, update LRU
						int lru = myPStream.getLRUstream();
						System.out.println(cacheName +"-SB miss, select LRU buffer #"+lru+", update LRU");
				}
				}
				
				
				//next read from child
				if(childCache ==null){
					//hit memory, so create block and pass up
					Block is_read = new Block(address);
					Block pass = new Block(is_read.getAddress()); //isolate references
					pass.setDirty(false); //blocks passed up are not dirty
					mySets[index].addMRU( pass.getAddress(),false);//blocks passed up are not dirty
					
					if(debugValid){//Debug print statement
					//L1 Update LRU
					System.out.println(cacheName + " Update LRU");
					}
					
						//Since miss, now load next memories into stream
					if(myPStream.nStreams != 0){
						myPStream.prefetchStream(address, childCache);
					}
				
					return pass;
				}
				else{
				//now read from child cache, store, and return
					Block is_read = childCache.readMemory(address, false);
					Block pass = new Block(is_read.getAddress()); //isolate references
					pass.setDirty(false); //blocks passed up are not dirty
					
					mySets[index].addMRU( pass.getAddress(),false);//blocks passed up are not dirty
					
					if(debugValid){//Debug print statement
					//L1 Update LRU
					System.out.println(cacheName + " Update LRU");
					}
					
					//Since miss, now load next memories into stream
					if(myPStream.nStreams != 0){
						myPStream.prefetchStream(address, childCache);
					}					
					
					return pass;
				}
				
				
			}
			
			
			
		}/// end read mem


		
/*---------------------------------------------------------------------------------------------------*/		
		public Boolean writeMemory(int address){
			int index = getSetIndex(address);
			write_count++;
			
			Boolean hit = cacheContainsBlock(address);
			Boolean pstream_hit = false;
			if(myPStream.nStreams != 0){
				pstream_hit = myPStream.containsBlock(address);
			}
			
			if(debugValid){//Debug print statement
			Block print = new Block(address);
			int masked = address & (~(int)(Math.pow((double)2,(double)num_block_offset)-1));
			Integer tag = print.getTag(num_tag_bits);
			//L1 read : dfcfa0 (tag 6fe7, index 26)
			System.out.println(cacheName +" Write : "+ Integer.toHexString(masked)+"  (tag "+Integer.toHexString(tag) + ", index "+index+")");
			}
			
			if(hit){
				if(debugValid){//Debug print statement
				//L1 hit
				System.out.println(cacheName + " hit");
				//L1 Update LRU
				System.out.println(cacheName + " Update LRU");
				//L1 set dirty
				System.out.println(cacheName + " set dirty");
				}
				
				Block to_write = mySets[index].resetMRU(address);
				mySets[index].theseBlocks[0].setDirty(true); //Set now current MRU block as dirty, as it was just written to.
				
				return true;  //wrote memory
			}
			else if(pstream_hit){
				if(debugValid){//Debug print statement
				//L1 miss
				System.out.println(cacheName + " miss");			
				}
				
				//allocate space 
				allocateSpace(address);
				//write_miss++;
				
				if(debugValid){//Debug print statement
				//L1-SB #1 hit, pop first entry, update LRU
				
				System.out.println(cacheName + "-SB #x hit, pop first entry, update LRU");
				//L1 Update LRU
				System.out.println(cacheName + " Update LRU");
				
				}
				
				//read from prefetch stream
				Block to_write = myPStream.fetchBlock(address, childCache);
				to_write.setDirty(true);
				mySets[index].addMRU( to_write.getAddress(), to_write.isDirty());
				
				if(debugValid){//Debug print statement
				System.out.println(cacheName + " set dirty");
				//L1 set dirty
				}

				return true; //wrote memory
			}
			else{
				if(debugValid){//Debug print statement
				//L1 miss
				System.out.println(cacheName + " miss");			
				}
				
				//allocate space
				allocateSpace(address);
				write_miss++;
				
				if(debugValid){//Debug print statement
				if(myPStream.nStreams != 0){
				//L1-SB miss, select LRU buffer #0, update LRU
						int lru = myPStream.getLRUstream();
						System.out.println(cacheName +"-SB miss, select LRU buffer #"+lru+", update LRU");
				}
				}
				
				//next read from child
				if(childCache ==null){
					//hit memory, so create block and write to it
					Block to_write = new Block(address);
					to_write.setDirty(true);
					mySets[index].addMRU( to_write.getAddress(), to_write.isDirty());
					
					if(debugValid){//Debug print statement
					//L1 Update LRU
					System.out.println(cacheName + " Update LRU");
					//L1 set dirty
					System.out.println(cacheName + " set dirty");
					}
					
					
				}
				else{
				//now read from child cache, store, and return
					Block to_write = childCache.readMemory(address, false);
					to_write.setDirty(true);
					mySets[index].addMRU( to_write.getAddress(), to_write.isDirty());
					
					if(debugValid){//Debug print statement
					//L1 Update LRU
					System.out.println(cacheName + " Update LRU");
					//L1 set dirty
					System.out.println(cacheName + " set dirty");
					}
					
					
				}
				
				//Since miss, now load next memories into stream
				if(myPStream.nStreams != 0){
					myPStream.prefetchStream(address, childCache);
				}
				
				return true; //wrote memory
			}
		} /// end write Memory
		
/*--------------------------------------------------------------------------------------------------*/		
	
		public int getSetIndex(int address){

			int index = 0;
			if(num_index_bits == 0){
			 index=0; //Fully associative, no index bits
			}
			else{
			int mask = (int) Math.pow((double)2, (double) num_index_bits) -1;	//to make mask of 1s for desired numer of bits
			//System.out.println("mask  " + mask);
			
			int temp = address >>> num_block_offset; //shifts out offset bits
			//System.out.println("temp  " + temp);
			
			index = temp & mask;//masks zeroes tag bits
			}
			//System.out.println("index  " + index);
			
		return index; 
		}

/*---------------------------------------------------------------------------------------------------*/
		private int log_2(int x){
		double y = Math.log( (double) x)/Math.log( (double) 2) ; //log(X)/log(2) yeilds log_2(x)
		return (int) y;
		}

/*---------------------------------------------------------------------------------------------------*/
		//example output
		//Set	0:	20028d D	  20018a
		public void printMemory(){
			for(int k=0;k<numSets;k++){
				System.out.print("Set	"+k+":	");
				mySets[k].printSet();
				System.out.println();
				
			}
			
		}

/*---------------------------------------------------------------------------------------------------*/
	} //end Cache class
