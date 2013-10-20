// Cache Project (ECE 521)
// Neal O'Hara
// ngohara @ ngohara@ncsu.edu
// 10/4/2013

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
	
	

public class PStream{

	//statistic fields
	public long prefetch_count = 0;
	
	public boolean debugValid = true; //set true for debug statements
	
	//PStreem parameter fields
	public int nStreams;
	private int mBlocks;
	private int tag_bits;
	private int block_bits;
	
	private String cacheName;
	
	//Stream fields
	Set[] myStream = new Set[1];
	Integer[] myLRUorder = new Integer[1];
	
	public PStream(int pre_n, int pre_m, int num_tag_bits, int num_block_bits, String name){
		
		this.nStreams = pre_n;
		this.mBlocks = pre_m;
		this.tag_bits = num_tag_bits;
		this.block_bits= num_block_bits;
		this.cacheName = name;
		
		if(nStreams > 0){
			myStream = new Set[nStreams];
			myLRUorder = new Integer[nStreams];
			for(int k=0;k<nStreams;k++){
				myStream[k] = new Set(mBlocks, k, tag_bits);
				// To start LRU[0] = MRU=0  and LRU[nstreams-1] =nStreams-1,  
				myLRUorder[k] = new Integer(k);
			}
		}
	}//end PStream construct

/*---------------------------------------------------------------------------------------------------*/	
	
	//checks the first entry of all N stream buffers for X
	//returns true if contained, false if not
	//Note that MRu blocks are in position mBlocks-1
	public Boolean containsBlock(int address){
		int mask = ~(int)(Math.pow((double)2,(double)block_bits)-1);
		
		for(int k=0;k<nStreams;k++){
			
			//check for valid first, some could be invalid
			if(myStream[k].theseBlocks[mBlocks-1].isValid()){
				//use mask to hide offset bits
				Integer test_add = (myStream[k].theseBlocks[mBlocks-1].getAddress())&mask;
				if(test_add.equals((Integer) (address&mask))){
					return true; //found matching address
				}
			}
		}
		
		return false; //stream buffers do not contain block
	}

/*---------------------------------------------------------------------------------------------------*/
	
	//must first use containsBlock
	//returns the requested block X, shifting stream by one 
	//and prefetching X+M+1
	public Block fetchBlock(int address, Cache child){
		Block fetched_address = new Block(null);
		Block endStream = new Block(null);
		int kr =0;
		int mask = ~(int)(Math.pow((double)2,(double)block_bits)-1);
		
		for(int k=0;k<nStreams;k++){
			//use mask to hide offset bits
			Integer test_add = (myStream[k].theseBlocks[0].getAddress())&mask;
			if(test_add.equals((Integer) (address&mask))){
				//the kth stream needs to be shifted
				kr = k;
				break;
			}	
		}
		
		
		//Note: pos(0) = X+M while pos(LRU) = X
		fetched_address = myStream[kr].removeLRU();
		//fetch X+M+1 from child cache
		int temp_address = ((int)(address&mask))+ (int)(Math.pow((double)2,(double)block_bits))*mBlocks;
		
		prefetch_count++;
		
		if(debugValid){//Debug print statement
		//L2-SB #2 prefetch 400341b0
		
		int temp = temp_address;
		int lru = getLRUstream();

		System.out.println(cacheName + "-SB #"+lru+" prefetch " + Integer.toHexString(temp));
		}
		
		if(child ==null){
			//hit memory, so create block and store it
			Block mem_block = new Block(temp_address);
			endStream = mem_block;
		} else{
			endStream = child.readMemory(temp_address, true);
		
		}
		
		//add endsStream, remember MRUs are X+M+1 
		myStream[kr].addMRU(endStream.getAddress(), false);
		
		//Now maintain Stream LRU
		streamLRU(kr);
		
		
		return fetched_address;
	}
/*---------------------------------------------------------------------------------------------------*/
	
	//Given X, prefetch X+1 through X+M+1 in LRU Stream
	//LRU Stream then becomes MRU stream
	public void prefetchStream(int address, Cache child){
		int mask = ~(int)(Math.pow((double)2,(double)block_bits)-1);
		int temp_address = address; 
		int block_size = (int)(Math.pow((double)2,(double)block_bits));
	
		for(int k=mBlocks-1; k >= 0; k--){
			
			
			//temp debug statements
			//	System.out.println("  p address = " + Integer.toHexString(address));
			//		System.out.println("  p mask = " + Integer.toBinaryString(mask));
			//		System.out.println("  p block_size = " + Integer.toBinaryString(block_size));
			//		System.out.println("  p address*mask = " + Integer.toHexString(temp_address&mask));
			//		System.out.println("  p address&mask+bocksize = " + Integer.toHexString(((int)(temp_address&mask))+ block_size));
					
				temp_address = ((int)(temp_address&mask))+ block_size;
				prefetch_count++;
				
				if(debugValid){//Debug print statement
					//L2-SB #2 prefetch 400341b0
					
					int temp = temp_address;;
					int lru = getLRUstream();

					System.out.println(cacheName + "-SB #"+lru+" prefetch " + Integer.toHexString(temp));
				}
				
				if(child ==null){
					//hit memory, so create block and store it
					Block mem_block = new Block(temp_address);
					myStream[getLRUstream()].theseBlocks[k] = mem_block;
				} else{
					myStream[getLRUstream()].theseBlocks[k] = child.readMemory(temp_address, true);
				
				}
				
		}
		
		//Then keep LRU of streams
		streamLRU(getLRUstream());
	}

/*---------------------------------------------------------------------------------------------------*/
	
	// myLRUorder maintains indexes of the Streams from MRU to LRU
	// from 0 to nStream size
	// Thus, LRU is always myLRUorder[nStream-1]
	private void streamLRU(int index){
	
		for(int j=0; j<nStreams; j++){
			
			if(myLRUorder[j] == index){
				for(int k=j; k > 0; k--){
						myLRUorder[k] = myLRUorder[k-1];
				}
				myLRUorder[0] = index;
			}
		}
	}
/*---------------------------------------------------------------------------------------------------*/	
	public int getLRUstream(){
		return myLRUorder[nStreams-1].intValue();
	}
/*---------------------------------------------------------------------------------------------------*/
	
	//when writebacks of blocks occur, their copy in stream needs to be invalidated, if there is one. No other solution than to iterate over entire buffer. Keep iterating even after invalidating a copy, as its possible to prefetch multiple copies
	public void invalidateBlock(int address){
		int mask = ~(int)(Math.pow((double)2,(double)block_bits)-1);
		for(int k=0;k<nStreams;k++){
			for(int j=0;j<mBlocks;j++){
				//use mask to hide offset bits
				Integer test_add = (myStream[k].theseBlocks[j].getAddress())&mask;
				if(test_add.equals((Integer) (address&mask))){
					//found match, now invalidate
					myStream[k].theseBlocks[j].setValid(false);
				}
			}
		}
	}

/*---------------------------------------------------------------------------------------------------*/
	
	//prints out in pysical order 
	public void printStreams(){
		int mask = ~(int)(Math.pow((double)2,(double)block_bits)-1);
		for(int k=0;k<nStreams;k++){
			for(int j=(mBlocks-1);j>=0;j--){
				
				System.out.print(Integer.toHexString(myStream[k].theseBlocks[j].getAddress()&mask));
				System.out.print(" ");  
			}
			System.out.println(); //new space after each stream
		}
	
	}
}//end class