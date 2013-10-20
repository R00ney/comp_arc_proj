// Cache Project (ECE 521)
// Neal O'Hara
// ngohara @ ngohara@ncsu.edu
// 10/4/2013

/* Blocks functions
createBlock(Integer )
getAddress()
getCountLRU()
incrementCountLRU(int )
isValid()
isDirty()
setDirty()

*/

	// All Addresses are 32 bits,
	// Addresses are addressed in hexadecimal format
	// Addresses are divided into parts via
	// (MSB) Tag  |  Index  |  Block offset (LSB)
	// num_block_offset = log_2(blockSize)
	// num_index_bits = log_2(numSets)
	// num_tag_bits = 32 - num_index_bits - num_block_offset




//The block is the virtual representation of a block of memory
//Each block will hold it's own valid & dirty bits
// Null block declarations, ie for empty cache sets and streams,
// should have valid bits of zero
public class Block{
	
	//A Blocks Fields
	private Integer address;
	private boolean valid;
	private boolean dirty;
	
	public Block(Integer new_address){
	
	
		if(new_address == null){
			this.address = 0;	//valid is false, so address doesn't matter
			this.valid = false;
			this.dirty= false;
		}else{
			this.address = new_address;
			this.valid = true;
			this.dirty= false;
		}
		
	}//end create block

	public int getAddress(){
	return (int) this.address;
	}

	
	public Integer getTag(int num_tag_bits){
	 return (Integer) this.address>>>(32-num_tag_bits);
	}
	

	public boolean isValid(){
	return this.valid;
	}
	
	public void setValid(boolean val){
		this.valid = val;
	}
	
	public boolean isDirty(){
	return this.dirty;
	}
	
	public void setDirty(boolean xyz){
	this.dirty = xyz;
	}


}//end Block class