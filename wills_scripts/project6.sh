#!/bin/bash
make clean
make
rm graph6-*.txt



for I in 1024 2048 4096 8192 16384
do
	for J in 16 32 64
	do
		for K in 1 2 3 4
		do
			for L in 1 2 3 4
			do
				echo Run $I $J
				echo graph6-$I-$J-$K-$L.txt
				java sim_cache $J $I 4 $K $L 65536 8 0 0 gcc_trace.txt > graph6-$I-$J-$K-$L.txt
			done
		done
	done
done
perl project6.pl
