/** /
n = string count
l = string length
m = substring length
d = mismatch count, hamming distance

string can be A, C, G, T

n = 3, l = 10, m = 4, d = 1
[ACTG]ACGCAG, TC[ACAA]CGGG, GAGT[CCAG]TT
ACAG
/**/

import java.lang.*;
import java.io.*;
import java.util.*;

public class par {
	private static Semaphore sem = new Semaphore(1);
	private static HashSet<String> hams = new HashSet<String>();
	private static HashSet<String> hamsCopy = new HashSet<String>();
	private static int d, m, n, l;

	public static void main(String[] args) {

		m = 4;
		String[] perms = new String[(int)Math.pow(4,m)];

		// obtain sequences
		String fileName = "input.txt", line;
		List<String> seqIn = new ArrayList<String>();

		try {
			FileReader fReader = new FileReader(fileName);
			BufferedReader bReader = new BufferedReader(fReader);

			while((line = bReader.readLine()) != null) {
				seqIn.add(line);
			}

			bReader.close();
		} catch(Exception e) {
			System.out.println("Unable to open file" + fileName);
		}

		// begin processing
		n = seqIn.size();
		l = seqIn.get(0).length();
		int slicount = l-m+1;
		int d = 1;
		HashSet<String> tempHams = new HashSet<String>();

		for(int i = 0; i < n; i++) {
			hams = new HashSet<String>();
			hamsCopy = new HashSet<String>();

			// slice substrings
			for(int j = 0; j < slicount; j++) { // each m-long dna slice
				hams.add( seqIn.get(i).substring(j, j+m) ); // only pure slices
			}

			// create threads
			Thread[] threads = new Thread[4];
			for(int j = 0; j < threads.length; j++) {
				threads[j] = new Thread(new Volunteer("Volunteer " + j, j));
				threads[j].start();
			}

			// wait for all seqIn(i)'s hams to exist
			for(int j = 0; j < threads.length; j++)
				try {
					threads[j].join();
				} catch (Exception e) { System.out.println("lol threads gone cray ;((("); }

			// combine pure slices + d-different hammings
			hams.addAll(hamsCopy);

			// hams = valid seqIn(i) hams
			if ( i == 0 ) {
				tempHams = new HashSet<String>(hams); // save verified previous hams
			} else {
				tempHams.retainAll(hams);
			}
		}

		System.out.println("Valid hams: " + tempHams);
	}

/** /

Should I make an Array of Hashsets? NO

~If I do, I can just compare for matches at the end~

If I don't, then I have to parallel create another temporary hashset
 for each thread, combine, and check after each combined thing
 ESSENTIALLY DONE THAT

IMPROVEMENT:
Is there a way to not have to generate new hammings and only check the old ones?

YES THERE IS
 but I don't know how...

/**/

	private static class Volunteer implements Runnable {
		private String name;
		private int id;
		private String proteins = "ACTG";

		public Volunteer(String name, int id) {
			this.name = name;
			this.id = id;
		}

		public void run() {
			HashSet<String> ham = new HashSet<String>();

			// program is too fast that hams might update before other threads even start()
			for( String s : new HashSet<String>(hams) ) { // iterate through each slice
				/* Fo GGAG, produce:
					AGAG
					GAAG
					GGAG
					GGAA
				*/

				// add to own ham
				for ( int i = 0; i < s.length(); i++) { // d = 1
					ham.add( s.substring(0,i) .concat(proteins.substring(id,id+1)) .concat(s.substring(i+1)) );
				}

			}

			// combine with original
			sem.acquire();
			hamsCopy.addAll(ham);
			sem.release();
		}
	}
}
