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
		//simulator.run(args);
	
	}//end main
	
	
	//public void sim_cache(String[] args_run){
	public sim_cache(String[] args_run){	
		System.out.println("fucks given");
		
		//int tempa = Integer.parseInt(args_run[1]);
		//int tempb = Integer.parseInt(args_run[2]);
		//int tempc = Integer.parseInt(args_run[0]);
		Cache L1 = new Cache( 1, 2, 3);
	}
	


} //end sim cache 