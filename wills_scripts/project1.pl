#!/usr/bin/perl
use warnings;
use strict;
use Data::Dumper;

my $dir = "/afs/unity.ncsu.edu/users/n/ngohara/Desktop/comp/comp_arc_proj/wills_scripts";
print "Opening dir \n";
print "Grepping \n";
print Dumper(grep ((/graph1-/ && !/\.txt$/), readdir MYDIR));
my @files = glob "$dir/graph1-*.txt";
print("Trying to dump\n");
print Dumper(@files);
print "Trying to open\n";
open (MYFILE, '>>project1_graph1.csv') or die "Cannot open csv";
print "Starting to search\n"; 
foreach my $file (@files)
{
	open(my $fh, $file) or warn "Cannot open $file!";
	while (!eof($fh)) {
            defined($_ = <$fh>) or die "readline failed for $_: $!";
            print $_;
            if(/^BLOCKSIZE/) {
            	my $line = substr($_, 15);
            	$line =~ s/^\s+//;
				$line =~ s/\s+$//;
            	print $line . "\n";
            	print MYFILE $line;
            	print MYFILE ',';
            }
            if(/^L1_SIZE/) {
            	my $line = substr($_, 15);
            	$line =~ s/^\s+//;
$line =~ s/\s+$//;
            	print $line . "\n";
            	print MYFILE $line;
            	print MYFILE ',';
            }

            if(/^L1_ASSOC/) {
            	my $line = substr($_, 15);
            	$line =~ s/^\s+//;
$line =~ s/\s+$//;
            	print $line . "\n";
            	print MYFILE $line;
            	print MYFILE ',';
            }
            if(/^L1_PREF_N/) {
            	my $line = substr($_, 15);
            	$line =~ s/^\s+//;
$line =~ s/\s+$//;
            	print $line . "\n";
            	print MYFILE $line;
            	print MYFILE ',';
            }
            if(/^L1_PREF_M/) {
            	my $line = substr($_, 15);
            	$line =~ s/^\s+//;
$line =~ s/\s+$//;
            	print $line . "\n";
            	print MYFILE $line;
            	print MYFILE ',';
            }
            if(/^L2_SIZE/) {
            	my $line = substr($_, 15);
            	$line =~ s/^\s+//;
$line =~ s/\s+$//;
            	print $line . "\n";
            	print MYFILE $line;
            	print MYFILE ',';
            }

            if(/^L2_ASSOC/) {
            	my $line = substr($_, 15);
            	$line =~ s/^\s+//;
$line =~ s/\s+$//;
            	print $line . "\n";
            	print MYFILE $line;
            	print MYFILE ',';
            }
            if(/^L2_PREF_N/) {
            	my $line = substr($_, 15);
            	$line =~ s/^\s+//;
$line =~ s/\s+$//;
            	print $line . "\n";
            	print MYFILE $line;
            	print MYFILE ',';
            }
            if(/^L2_PREF_M/) {
            	my $line = substr($_, 15);
            	$line =~ s/^\s+//;
$line =~ s/\s+$//;
            	print $line . "\n";
            	print MYFILE $line;
            	print MYFILE ',';
            }
            if(/^a\. /) {
            	my @bits = split(':', $_);
            	my $line = $bits[1]; $line =~ s/^\s+//;
$line =~ s/\s+$//; print MYFILE $line;
            	print MYFILE ',';
            }
            if(/^b\. /) {
            	my @bits = split(':', $_);
            	my $line = $bits[1]; $line =~ s/^\s+//;
$line =~ s/\s+$//; print MYFILE $line;
            	print MYFILE ',';
            }
            if(/^c\. /) {
            	my @bits = split(':', $_);
            	my $line = $bits[1]; $line =~ s/^\s+//;
$line =~ s/\s+$//; print MYFILE $line;
            	print MYFILE ',';
            }
            if(/^d\. /) {
            	my @bits = split(':', $_);
            	my $line = $bits[1]; $line =~ s/^\s+//;
$line =~ s/\s+$//; print MYFILE $line;
            	print MYFILE ',';
            }
            if(/^e\. /) {
            	my @bits = split(':', $_);
            	my $line = $bits[1]; $line =~ s/^\s+//;
$line =~ s/\s+$//; print MYFILE $line;
            	print MYFILE ',';
            }
            if(/^f\. /) {
            	my @bits = split(':', $_);
            	my $line = $bits[1]; $line =~ s/^\s+//;
$line =~ s/\s+$//; print MYFILE $line;
            	print MYFILE ',';
            }
            if(/^g\. /) {
            	my @bits = split(':', $_);
            	my $line = $bits[1]; $line =~ s/^\s+//;
$line =~ s/\s+$//; print MYFILE $line;
            	print MYFILE ',';
            }
            if(/^h\. /) {
            	my @bits = split(':', $_);
            	my $line = $bits[1]; $line =~ s/^\s+//;
$line =~ s/\s+$//; print MYFILE $line;
            	print MYFILE ',';
            }
            if(/^i\. /) {
            	my @bits = split(':', $_);
            	my $line = $bits[1]; $line =~ s/^\s+//;
$line =~ s/\s+$//; print MYFILE $line;
            	print MYFILE ',';
            }
            if(/^j\. /) {
            	my @bits = split(':', $_);
            	my $line = $bits[1]; $line =~ s/^\s+//;
$line =~ s/\s+$//; print MYFILE $line;
            	print MYFILE ',';
            }
            if(/^k\. /) {
            	my @bits = split(':', $_);
            	my $line = $bits[1]; $line =~ s/^\s+//;
$line =~ s/\s+$//; print MYFILE $line;
            	print MYFILE ',';
            }
            if(/^l\. /) {
            	my @bits = split(':', $_);
            	my $line = $bits[1]; $line =~ s/^\s+//;
$line =~ s/\s+$//; print MYFILE $line;
            	print MYFILE ',';
            }
            if(/^m\. /) {
            	my @bits = split(':', $_);
            	my $line = $bits[1]; $line =~ s/^\s+//;
$line =~ s/\s+$//; print MYFILE $line;
            	print MYFILE ',';
            }
            if(/^n\. /) {
            	my @bits = split(':', $_);
            	my $line = $bits[1]; $line =~ s/^\s+//;
$line =~ s/\s+$//; print MYFILE $line;
            	print MYFILE ',';
            }
            if(/^o\. /) {
            	my @bits = split(':', $_);
            	my $line = $bits[1]; $line =~ s/^\s+//;
$line =~ s/\s+$//; print MYFILE $line;
            	print MYFILE ',';
            }
            if(/^p\. /) {
            	my @bits = split(':', $_);
            	my $line = $bits[1]; $line =~ s/^\s+//;
$line =~ s/\s+$//; print MYFILE $line;
            	print MYFILE ',';
            }
            if(/^q\. /) {
            	my @bits = split(':', $_);
            	my $line = $bits[1]; $line =~ s/^\s+//;
$line =~ s/\s+$//; print MYFILE $line;
            	print MYFILE ',';
            }
        }
    print MYFILE "\n";
    close($fh);
}
close(MYFILE);
