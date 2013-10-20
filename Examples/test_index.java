
import java.lang.Math;

public class test_index{


			private Integer setIndex; //set's own index reference
	private Integer setASSOC; //size of the set, and one more than LRU counter
	
	public Block[] theseBlocks = new Block[1];
	
	private Integer num_tag_bits;
	
	
	public test_index(int setSize, int index){
		this.setASSOC = setSize;
		this.setIndex = index;
		num_tag_bits = 24;
		
		this.theseBlocks = new Block[setASSOC];
		
		theseBlocks[0] = new Block(null);
		theseBlocks[1] = new Block(null);
		theseBlocks[2] = new Block(null);
		theseBlocks[3] = new Block(null);
		
		theseBlocks[0] = new Block(1234058942);

	
		
		theseBlocks[1] = new Block(23523494);
	
		this.readBlock(235109284);
		

		
	}//end Set construct
		
		
		public static void main(String[] args){
		
		test_index asdasf = new test_index(4, 2);
		

		
		}
	
		/*
	public Integer getTag(int num_tag_bits){
	System.out.println("num_tag_bits " +num_tag_bits);
	System.out.println("get tag " + this.address>>>(32-num_tag_bits));
	 return (Integer) this.address>>>(32-num_tag_bits);
	}*/
	

		    //Does this set contain this block?
    //If so, consider it a Hit
    public Boolean readBlock(int address){

		Integer temp_tag = null;
		Integer myTag = address>>>(32-num_tag_bits);
		System.out.println("Add " + address + " Tag " + myTag);
    	
    	for(int i =0 ; i<setASSOC ; i++){
    		temp_tag = theseBlocks[i].getAddress() >>>(32-num_tag_bits);
    		System.out.println("TempAdd " + theseBlocks[i].getAddress() + " TempTag " + temp_tag);
    				
    		Boolean check = temp_tag.equals(myTag);
    		if(check){
    		 return true; //found match
    		}
    	}
    	return false; //no matches found
    }
    
/*---------------------------------------------------------------------------------------------------*/		
	//LRU block
	//return int pointer to LRU block
/*	public int fetchLRUBlock()
	{
		int posLRU = 0;
		
		for(int i =0 ; i<setASSOC ; i++){

    		if( theseBlocks[i].getCountLRU() > theseBlocks[posLRU].getCountLRU() ){
    			posLRU =i;
    		}

    	}
    	
    	return posLRU;
	}
	
		//LRU block with int
	//return int pointer to LRU block
	public int fetchLRUBlock(int top)
	{
		int posLRU = 0;
		
		for(int i =0 ; i<setASSOC ; i++){

    		if( theseBlocks[i].getCountLRU() > theseBlocks[posLRU].getCountLRU() ){
    			if(theseBlocks[i].getCountLRU() < top){
    				posLRU =i;
    			}
    		}

    	}
    	
    	return posLRU;
	}*/
/*---------------------------------------------------------------------------------------------------*/	

	
}
