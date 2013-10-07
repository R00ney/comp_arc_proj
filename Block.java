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




//The block is the virtual representation of a block of memory
//Each block will hold it's own valid & dirty bits
// Null block declarations, ie for empty cache sets and streams,
// should have valid bits of zero
public class Block{
	
	//A Blocks Fields
	private int address;
	private int countLRU;
	private boolean valid;
	private boolean dirty;
	
	public void createBlock(Integer new_address){
		
		if(new_address == null){
			this.address = 0;	//valid is false, so address doesn't matter
			this.countLRU = 0;
			this.valid = false;
			this.dirty= false;
		}else{
			this.address = new_address.intValue();
			this.countLRU = 0;	//Always initialize as most recently used
			this.valid = true;
			this.dirty= false;
		}
		
	}//end create block

	public int getAddress(){
	
	return this.address;
	}
	
	public int getCountLRU(){
	return this.countLRU;
	}
	
	//Only increment if less than provided count limit
	//return incremented value (to be used as next blocks LRU limit)
	public int incrementCountLRU(int limitCount){
		if(this.countLRU < limitCount ){
			this.countLRU ++;
		}
		return this.countLRU;
	}
	
	public boolean isValid(){
	return this.valid;
	}
	
	public boolean isDirty(){
	return this.dirty;
	}
	
	public void setDirty(boolean xyz){
	this.dirty = xyz;
	}


}//end Block class