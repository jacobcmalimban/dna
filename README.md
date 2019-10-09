# Sequence Processing

A program that takes sequences of dna and outputs mismatches, up to a specified difference.

## Details

Given n input strings of length l (i.e., there are l alphabets in each input string), the following algorithms:

find all substrings of length m that are common in all n input string with at most d mismatch (i.e., hamming distance of d). The set of possible alphabets in the input strings consists of A, C, G, and T, as those are nucleobases.

Example #1: (n=3, l=10, m=4, and d=1)

Sample input strings: ACTGACGCAG, TCACAACGGG, and GAGTCCAGTT

Expected Outputs: ACAG, CAGA, CCCA, and TCAG

Example #2: (n=3, l=10, m=4, d=1, p=4)

Sample input strings: GGAGGCCTGA, TCAAGTCACA, and GATCTTCAGG

Expected Outputs: AAGG, GGTC, GTCT, CAGA, CAGG, CATG, CCAG, CTCA

### Formatting

File begins with ints:

n l m d p

* n is the number of input rows (i.e., the number of input strings),

* l is the the length of each input string,

* m is the length of targeted motif/sub-string(s),

* d is the maximum Hamming distance permitted for accepted motif/sub-string(s), and

* p is the number of thread(s) (i.e., 1 for sequential execution).

The subsequent n rows of the input file are input strings of length l.
