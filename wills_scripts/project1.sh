#!/bin/bash
make clean
make
rm graph1-*.txt
for I in 1024 2048 4096 8192 16384 32768 65536 131072 262144 524288 1048576
do
	for J in 1 2 4 8
	do
		echo Run $I $J
		echo graph1-$I-$J.txt
		java sim_cache 32 $I $J 0 0 0 0 0 0 gcc_trace.txt > graph1-$I-$J.txt
	done
done
java sim_cache 32 1024 32 0 0 0 0 0 0 gcc_trace.txt > graph1-1024-fa.txt
java sim_cache 32 2048 64 0 0 0 0 0 0 gcc_trace.txt > graph1-2048-fa.txt
java sim_cache 32 4096 128 0 0 0 0 0 0 gcc_trace.txt > graph1-4096-fa.txt
java sim_cache 32 8192 256 0 0 0 0 0 0 gcc_trace.txt > graph1-8192-fa.txt
java sim_cache 32 16384 512 0 0 0 0 0 0 gcc_trace.txt > graph1-16384-fa.txt
java sim_cache 32 32768 1024 0 0 0 0 0 0 gcc_trace.txt > graph1-32768-fa.txt
java sim_cache 32 65536 2048 0 0 0 0 0 0 gcc_trace.txt > graph1-65536-fa.txt
java sim_cache 32 131072 4096 0 0 0 0 0 0 gcc_trace.txt > graph1-131072-fa.txt
java sim_cache 32 262144 8192 0 0 0 0 0 0 gcc_trace.txt > graph1-262144-fa.txt
java sim_cache 32 524288 16384 0 0 0 0 0 0 gcc_trace.txt > graph1-524288-fa.txt
java sim_cache 32 1048576 32768 0 0 0 0 0 0 gcc_trace.txt > graph1-1048576-fa.txt
perl project1.pl


for I in 1024 2048 4096 8192 16384 32768 65536 131072 262144
do
	for J in 1 2 4 8
	do
		echo Run $I $J
		echo graph3-$I-$J.txt
		java sim_cache 32 $I $J 0 0 524288 8 0 0 gcc_trace.txt > graph3-$I-$J.txt
	done
done
java sim_cache 32 1024 32 0 0 524288 8 0 0 gcc_trace.txt > graph3-1024-fa.txt
java sim_cache 32 2048 64 0 0 524288 8 0 0 gcc_trace.txt > graph3-2048-fa.txt
java sim_cache 32 4096 128 0 0 524288 8 0 0 gcc_trace.txt > graph3-4096-fa.txt
java sim_cache 32 8192 256 0 0 524288 8 0 0 gcc_trace.txt > graph3-8192-fa.txt
java sim_cache 32 16384 512 0 0 524288 8 0 0 gcc_trace.txt > graph3-16384-fa.txt
java sim_cache 32 32768 1024 0 0 524288 8 0 0 gcc_trace.txt > graph3-32768-fa.txt
java sim_cache 32 65536 2048 0 0 524288 8 0 0 gcc_trace.txt > graph3-65536-fa.txt
java sim_cache 32 131072 4096 0 0 524288 8 0 0 gcc_trace.txt > graph3-131072-fa.txt
java sim_cache 32 262144 8192 0 0 524288 8 0 0 gcc_trace.txt > graph3-262144-fa.txt
perl project3.pl

for I in 1024 2048 4096 8192 16384 32768
do
	for J in 16 32 64 128
	do
		echo Run $I $J
		echo graph4-$I-$J.txt
		java sim_cache $J $I 4 0 0 524288 8 0 0 gcc_trace.txt > graph4-$I-$J.txt
	done
done
perl project4.pl

for I in 1024 2048 4096 8192 16384 32768 65536 131072 262144
do
	for J in 32768 65536 131072 262144 524288 1048576
	do
		echo Run $I $J
		echo graph5-$I-$J.txt
		java sim_cache 32 $I 4 0 0 $J 8 0 0 gcc_trace.txt > graph5-$I-$J.txt
	done
done
perl project5.pl

for I in 1024 2048 4096 8192 16384
do
	for J in 16 32 64
	do
		for K in 1 2 3 4
		do
			for L in 1 2 3 4
			do
				echo Run $I $J
				echo graph6-$I-$J.txt
				java sim_cache $J $I 4 $K $L 65536 8 0 0 gcc_trace.txt > graph6-$I-$J.txt
			done
		done
	done
done
perl project6.pl


