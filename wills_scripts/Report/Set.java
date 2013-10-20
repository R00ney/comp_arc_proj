// Cache Project (ECE 521)
// Neal O'Hara
// ngohara @ ngohara@ncsu.edu
// 10/4/2013


	// Caches use LRU (least-recently-used block replaced)
	// small counter per block in set
	// if hit, block counter set to 0
	// increment other counters THAT ARE LESS THAN it was
	// if miss, replace highest counter, 
	// and set that to 0, increment others
	//

//A Set keep collections of blocks based on index bits of address
// and on the associativity (ie number of blocks in a set)




public class Set {
	
	private Integer setIndex; //set's own index reference
	private Integer setASSOC; //size of the set, and one more than LRU counter
	private Integer num_tag_bits;
	
	public Block[] theseBlocks = new Block[1];
	
	public Set(int setSize, int index, int in_bits){
		this.setASSOC = setSize;
		this.setIndex = index;
		this.num_tag_bits = in_bits;
		
		this.theseBlocks = new Block[setASSOC];
		
		Block empty = new Block(null); //Fill set with non-valid so no nulls
		
		for(int i =0 ; i<setASSOC ; i++){
			theseBlocks[i] = empty;
		}
		
	}//end Set construct

/*---------------------------------------------------------------------------------------------------*/

    //Does this set contain the block with this address?
    // Must already be using appropriate Set index, of coarse
    //If so, consider it a Hit
    public Boolean containsBlock(int address){

		Integer temp_tag = null;
		Integer myTag = (new Block(address).getTag(num_tag_bits));
    	
    	for(int i =0 ; i<setASSOC ; i++){
    		temp_tag = theseBlocks[i].getTag(num_tag_bits);
    		
    		Boolean check = temp_tag.equals(myTag);
    		if(check){
    		 return true; //found match
    		}
    	}
    	return false; //no matches found
    }
    
/*---------------------------------------------------------------------------------------------------*/		
	//check if full by checking valids
	public Boolean isFull(){
		Boolean full = true; //assume full
    	
    	for(int i =0 ; i<setASSOC ; i++){
    		full = theseBlocks[i].isValid();
    		if(!full) break; //any not valids means not full
    	}
    	return full; 
	}
/*---------------------------------------------------------------------------------------------------*/	
	//Reset MRU
	//For blocks in Cache that are read or written
	//Reset them to MRU, 
	//and increment only appropriate selection
	// ie. the blocks that were prev. MRU than itself
	public Block resetMRU(int address){
		
		//First, find current position in Set
		Integer temp_tag = null;
		Integer myTag = (new Block(address).getTag(num_tag_bits));
    	
		int old_pos = 0;
    	for(int i =0 ; i<setASSOC ; i++){
    		temp_tag = theseBlocks[i].getTag(num_tag_bits);
    		
    		Boolean check = temp_tag.equals(myTag);
    		if(check){
    		 old_pos = i;
    		 break; //found match
    		}
    	}
    	
    	//Next, remove block and increment all lower
		Block moving_block =  theseBlocks[old_pos];
		//next, interate through and increment
    	for(int i =old_pos ; i>0 ; i--){
			theseBlocks[i] = theseBlocks[i-1];
		}
		theseBlocks[0] = moving_block; //put reset Block in MRU

		return theseBlocks[0];// return the block just used
								// may be use by read
								//or thrown away by write
	}

/*---------------------------------------------------------------------------------------------------*/		
	//remove block
	//removes LRU block, ie last block
	//should run is full first
	//MUST pass back block, so can check dirty, etc.
	public Block removeLRU(){
		
		Block empty = new Block(null);
		Block removed = new Block(null);
		
		removed = theseBlocks[setASSOC-1];
		
		//next, interate through and increment
    	for(int i =setASSOC-1 ; i>0 ; i--){
			theseBlocks[i] = theseBlocks[i-1];
		}
		theseBlocks[0] = empty; //MRU spot invalid, ready for incomeing block
		
		return removed; 
	}//end remove LRU
/*---------------------------------------------------------------------------------------------------*/

	//increment a nonFullSet
	public void incNonFull(){
		
		//Nonfull will never be up to setASSOC-1
		//so increment without worrry
		Block empty = new Block(null);
		//next, interate through and increment
    	for(int i = (setASSOC-1) ; i>0 ; i--){
			theseBlocks[i] = theseBlocks[i-1];
		}
		theseBlocks[0] = empty; //MRU spot invalid, ready for incomeing block

	}

    
/*---------------------------------------------------------------------------------------------------*/

	//add block
	// new block is always MRU
	// must allocate space prior to adding
	// dirty = true for write commands
	public void addMRU(int address, Boolean dirty){
		Block inMRU = new Block(address);
		inMRU.setDirty(dirty);
		
		theseBlocks[0] = inMRU;
	}
	

/*---------------------------------------------------------------------------------------------------*/	
	//example output
	////Set	0:	20028d D	  20018a
	public void printSet(){
		for(int i =0 ; i<setASSOC ; i++){
			int tag = theseBlocks[i].getTag(num_tag_bits);
			System.out.print(Integer.toHexString(tag) + " " );
			if(theseBlocks[i].isDirty()){
				System.out.print("D	  ");
			}
			else {
				System.out.print(" 	  ");
			}
			
		}
	
	}

/*---------------------------------------------------------------------------------------------------*/
		
}// end class Set