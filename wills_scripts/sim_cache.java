// Cache Project (ECE 521)
// Neal O'Hara
// ngohara @ ngohara@ncsu.edu
// 9/19/13

/*----------------------------------------------------------------------------*/
//Specific Actions to count and output

/*
a. number of L1 reads
b. number of L1 read misses, excluding L1 read misses that hit in the stream buffers
c. number of L1 writes
d. number of L1 write misses, excluding L1 write misses that hit in the stream buffers
e. L1 miss rate = MRL1 = (L1 read misses + L1 write misses)/(L1 reads + L1 writes)
f. number of writebacks from L1 to next level
g. number of L1 prefetches (prefetch requests from L1 to next level, if prefetch unit is enabled)
h. number of L2 reads that did not originate from L1 prefetches(should match b+d: L1 read 
misses + L1 write misses)
i. number of L2 read misses that did not originate from L1 prefetches, excluding such L2 read 
misses that hit in the stream buffersif L2 prefetch unit is enabled
j. number of L2 reads that originated from L1 prefetches (should match g: L1 prefetches)
k. number of L2 read misses that originated from L1 prefetches, excluding such L2 read misses 
that hit in the stream buffers if L2 prefetch unit is enabled
l. number of L2 writes(should match f: number of writebacks from L1)
m. number of L2 write misses, excluding L2 write misses that hit in the stream buffers if L2 
prefetch unit is enabled
n. L2 miss rate (from standpoint of stalling the CPU) = MRL2 = (item i)/(item h)
o. number of writebacks from L2 to memory
p. number of L2 prefetches (prefetch requests from L2 to next level, if prefetch unit is enabled)
q. total memory traffic = number of blocks transferred to/from memory
(with L2,should match i+k+m+o+p:
all L2 read misses + L2 write misses + writebacks from L2+ L2 prefetches)
(without L2,should match b+d+f+g:
L1 read misses + L1 write misses + writebacks from L1+ L1 prefetches)
*/

/*----------------------------------------------------------------------------*/


/*Parameters Pass to main:

BLOCKSIZE: Positive integer. Block size in bytes. (Same block size for all caches in the memory hierarchy.)

L1_SIZE: Positive integer. L1 cache size in bytes.

L1_ASSOC: Positive integer. L1 set-associativity (1 is direct-mapped).

L1_PREF_N: Positive integer. Number of Stream Buffers in the L1 prefetch unit. L1_PREF_N = 0 disables the L1 prefetch unit.

L1_PREF_M: Positive integer. Number of memory blocks in each Stream Buffer in the L1 prefetch unit.

L2_SIZE: Positive integer. L2 cache size in bytes. L2_SIZE = 0 signifies that there is no L2 cache.

L2_ASSOC: Positive integer. L2 set-associativity (1 is direct-mapped).

L2_PREF_N: Positive integer. Number of Stream Buffers in the L2 prefetch unit. L2_PREF_N = 0 disables the L2 prefetch unit.

L2_PREF_M: Positive integer. Number of memory blocks in each Stream Buffer in the L2 prefetch unit.

trace_file: Character string. Full name of trace file including any extensions.
*/
/*----------------------------------------------------------------------------*/

import java.lang.String;
import java.lang.Math;
import java.util.Date;
import java.io.*;




public class sim_cache {
	
	//starts the appropriate cache simulator with the following command and parameters:
	//sim_cache <BLOCKSIZE> <L1_SIZE> <L1_ASSOC> <L1_PREF_N> <L1_PREF_M> <L2_SIZE> <L2_ASSOC> <L2_PREF_N> <L2_PREF_M> <trace_file>
	public static void main (String[] args) {
		
		//check for appropriate parameters
		if(args.length != 10){
			System.out.println("Please set the proper 10 cache parameters with the following command: ");
			System.out.println("   sim_cache <BLOCKSIZE> <L1_SIZE> <L1_ASSOC> <L1_PREF_N> <L1_PREF_M> <L2_SIZE> <L2_ASSOC> <L2_PREF_N> <L2_PREF_M> <trace_file> ");
			return;
		}
		
		sim_cache simulator = new sim_cache( args );

	
	}//end main
	
	//Global Cache Fields
	public Cache[] myCache = new Cache[2]; //hardcoded for two caches
	public int L1 = 0;
	public int L2 = 1;
	
	//Debug simulator fields
	public long debug_count=0;
	public boolean debugValid = false; //set true for debug statements
	
	
	//public void sim_cache(String[] args_run){
	public sim_cache(String[] args_run){	
				

		// A L1 Cache simulator
		// Needs to be variably defined in , 
		int blockSize = Integer.parseInt(args_run[0]);
		int caceheSize = Integer.parseInt(args_run[1]);
		int setASSOC = Integer.parseInt(args_run[2]);
		int l1_PREF_N = Integer.parseInt(args_run[3]);
		int l1_PREF_M = Integer.parseInt(args_run[4]);
		myCache[L1] = new Cache( blockSize, caceheSize, setASSOC, l1_PREF_N , l1_PREF_M, "L1");
		

						// A L2 Cache simulator
		// Needs to be variably defined in 
		int l2_blockSize = Integer.parseInt(args_run[0]);
		int l2_cacheSize = Integer.parseInt(args_run[5]);
		int l2_setASSOC = Integer.parseInt(args_run[6]);
		int l2_PREF_N = Integer.parseInt(args_run[7]);
		int l2_PREF_M = Integer.parseInt(args_run[8]);
		myCache[L2] = new Cache( l2_blockSize, l2_cacheSize, l2_setASSOC, l2_PREF_N , l2_PREF_M, "L2");
		
		
		//Tell L1 who his child is
		if(l2_cacheSize==0){//no L2
			//myCache[L2] = null;
			myCache[L1].childCache = null;
		}
		else{	
			myCache[L1].childCache = myCache[L2];
		}
		
		//open trace file
		BufferedReader trace_reader = null;
		try{
		trace_reader = new BufferedReader(new FileReader(args_run[9]));
		} catch (IOException ioe){
			System.out.println("Error: File Open failed");
			ioe.printStackTrace();
		}
		
		//Output sim_cach config specs
		printConfig(args_run);
		
		//read trace file, and run program
		String traceCurrentLine = null;
		try{
			while ((traceCurrentLine = trace_reader.readLine()) != null){
				//for debug print
				//System.out.println(traceCurrentLine);
				
				
				/* Cach Runs with input commands Here*/
				
				Boolean check = runCommand(traceCurrentLine);
				
				if(check){ //System.out.println("Debug: command success");//Succes!
				}
				else {
					//System.out.println("Debug: command fail"); //Fail
					break; //leave after error
				}
			}
		} catch (IOException ioep){
			System.out.println("Error: File Read failed");
			ioep.printStackTrace();
		}
		
		/* Print the cache Memmory */
		Boolean print_cache = printCache(args_run);
		
		/* Print the cache staticstics */
		Boolean print_check = printStats(args_run);
		
		
		//close and finish program
		try{
			if(trace_reader != null) trace_reader.close();
		} catch (IOException ioex){
			System.out.println("Error: File close fail");
			ioex.printStackTrace();
		}
		
	}

/*---------------------------------------------------------------------------------------------------*/
	
//runs the "r/w address" command recieved
//executes appropriate cache actions
// returns false if a failure occures
	public Boolean runCommand(String command){
		try{
			String[] parts = command.split(" ");

			int address = (int) Long.parseLong(parts[1], 16);

			
			if(parts[0].equals("r")){
				//handle read command
				
				
				if(debugValid){//Debug print statement
					debug_count++;
					System.out.println("----------------------------------------");
					System.out.println("# "+ debug_count + " : read "+ Integer.toHexString(address));
				} 
				
				Block readBlock = myCache[L1].readMemory(address, false);
				
				
			}
			else if(parts[0].equals("w")){
				//handle write command
				
				
				if(debugValid){//Debug print statement
					debug_count++;
					System.out.println("----------------------------------------");
					System.out.println("# "+ debug_count + " : write "+ Integer.toHexString(address));
				}
				
				
				Boolean test = myCache[L1].writeMemory(address);
			}
			else{
				System.out.println("Error: Unrecognized Trace Input");
			
			}
			
			
		} catch (Exception e){
			System.out.println("Error: Failure in running command");
			e.printStackTrace();
			System.out.println();
			System.out.println(e.toString());
			return false;
		}
		return true;
	}
		
		
/*---------------------------------------------------------------------------------------------------*/


	//Output sim_cach config specs
	private void printConfig(String[] args_config){
	System.out.println("===== Simulator configuration =====");
	System.out.println("BLOCKSIZE:             " + args_config[0]);
	System.out.println("L1_SIZE:               " + args_config[1]);
	System.out.println("L1_ASSOC:              " + args_config[2]);
	System.out.println("L1_PREF_N:             " + args_config[3]);
	System.out.println("L1_PREF_M:             " + args_config[4]);
	System.out.println("L2_SIZE:               " + args_config[5]);
	System.out.println("L2_ASSOC:              " + args_config[6]);
	System.out.println("L2_PREF_N:             " + args_config[7]);
	System.out.println("L2_PREF_M:             " + args_config[8]);
	System.out.println("trace_file:            " + args_config[9]);
	
	}
/*---------------------------------------------------------------------------------------------------*/
	private Boolean printCache(String[] args_stats){
	try{
		
		//===== L1 contents =====
		//Set	0:	20028d D	  20018a 
		System.out.println("===== L1 contents =====");
		myCache[L1].printMemory();

		if(Integer.parseInt(args_stats[3]) > 0){
		//===== L1-SB contents =====
		//System.out.println("===== L1-SB contents =====");
		myCache[L1].myPStream.printStreams();
		}
		
		if(Integer.parseInt(args_stats[6]) > 0){
			//===== L2 contents =====
			//Set	0:	80066 D	  800a3 D	  800ac D	  800ab D	
			System.out.println("===== L2 contents =====");
			myCache[L2].printMemory();
		}
		

		
		if(Integer.parseInt(args_stats[7]) > 0){
		//===== L2-SB contents =====
		//System.out.println("===== L2-SB contents =====");
		myCache[L2].myPStream.printStreams();
		}
	
	} catch (Exception e){
		System.out.println("Error : could not print cache memory");
		e.printStackTrace();
		return false; //error
	}
	
	return true; //printed correctly
	}

/*---------------------------------------------------------------------------------------------------*/
	private Boolean printStats(String[] args_stats){
	String temp = "filler";
	
	try{
		System.out.println("===== Simulation results (raw) =====");
		//Print L1 Stuff
		System.out.println("a. number of L1 reads:                 " + myCache[L1].read_count);
		System.out.println("b. number of L1 read misses:           "+ myCache[L1].read_miss);
		System.out.println("c. number of L1 writes:                "+ myCache[L1].write_count);
		System.out.println("d. number of L1 write misses:          "+ myCache[L1].write_miss);
		//calculate miss rate
		float L1_miss_rate = (float)(myCache[L1].read_miss+myCache[L1].write_miss)/ (myCache[L1].read_count+myCache[L1].write_count);;
		System.out.print("e. L1 miss rate:                       ");
		System.out.printf("%.6f", L1_miss_rate);
		System.out.println();
		
		System.out.println("f. number of L1 writebacks:            " + myCache[L1].writeback_count);
		System.out.println("g. number of L1 prefetches:            " +myCache[L1].myPStream.prefetch_count);
		
		//L2 stuff
		System.out.println("h. number of L2 reads that did not originate from L1 prefetches:	"+ myCache[L2].read_count);
		System.out.println("i. number of L2 read misses that did not originate from L1 prefetches:	"+ myCache[L2].read_miss);
		System.out.println("j. number of L2 reads that originated from L1 prefetches:		" +myCache[L2].pread_count);
		System.out.println("k. number of L2 read misses that originated from L1 prefetches:	" +myCache[L2].pread_miss);
		System.out.println("l. number of L2 writes:		            "+ myCache[L2].write_count);
		System.out.println("m. number of L2 write misses:          "+ myCache[L2].write_miss);
		//calculate L2 miss rate
		if(myCache[L2].read_count ==0){
			System.out.println("n. L2 miss rate:                      " +(int)0);
		} else{
			float L2_miss_rate = (float) (myCache[L2].read_miss)/ (myCache[L2].read_count);
			System.out.print("n. L2 miss rate:                      ");
			System.out.printf("%.6f", L2_miss_rate);
			System.out.println();
		}
		
		System.out.println("o. number of L2 writebacks:            " + myCache[L2].writeback_count);
		if(myCache[L2].myPStream == null){
			System.out.println("p. number of L2 prefetches:	          " +0);
		} else {
			System.out.println("p. number of L2 prefetches:	          " +myCache[L2].myPStream.prefetch_count);
		}
		//calculate traffic
		long traffic = 0;
		if(Integer.parseInt(args_stats[5])==0){ //if L2 size  = 0
			traffic = myCache[L1].read_miss+myCache[L1].write_miss + myCache[L1].writeback_count + myCache[L1].myPStream.prefetch_count;
		} else{
			traffic = myCache[L2].read_miss+myCache[L2].pread_miss+myCache[L2].write_miss + myCache[L2].writeback_count + myCache[L2].myPStream.prefetch_count;
		}
		System.out.println("q. total memory traffic:             " +traffic);
		
	}catch(Exception e){
		System.out.println("Error: Couldn't access statistics");
		e.printStackTrace();
		return false;
	}
	
	return true; //All occured correctly
	}


/*---------------------------------------------------------------------------------------------------*/
} //end sim cache 