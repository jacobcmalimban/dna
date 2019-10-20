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
	private static int n, l, m, d, p;
	private static final String proteins = "ACTG";


	public static void main(String[] args) {
		System.out.println("Jacob Malimban");
		ArrayList<String> eq = new ArrayList<String>();
		for(int i = 0; i < 80; i++)
			eq.add("=");
		eq.forEach(System.out::print);
		System.out.println();

		// obtain sequences
		String fileName = "input.txt", line;
		List<String> seqIn = new ArrayList<String>();

		try {
			FileReader fReader = new FileReader(fileName);
			BufferedReader bReader = new BufferedReader(fReader);
			boolean first = true;

			while((line = bReader.readLine()) != null) {
				if (first) {
					int ints[] = new int[5];
					int index = 0;
					for (String iStr : line.split("\\s+") )
						ints[index++] = Integer.parseInt(iStr);
					n = ints[0];
					l = ints[1];
					m = ints[2];
					d = ints[3];
					p = ints[4];
					first = false;
				} else
					seqIn.add(line);
			}

			bReader.close();
		} catch(Exception e) {
			System.out.println("Unable to open file" + fileName);
		}

		// begin processing
		int slicount = l-m+1;
		HashSet<String> tempHams = new HashSet<String>();

		for(int i = 0; i < n; i++) { // each sequence
			hams = new HashSet<String>();
			hamsCopy = new HashSet<String>();

			// slice substrings
			for(int j = 0; j < slicount; j++) { // each m-long dna slice
				hams.add( seqIn.get(i).substring(j, j+m) ); // only pure slices
			}

			// create threads, process d-hammings
			threadMe();

			// combine pure slices + d-different hammings
			hams.addAll(hamsCopy);

//System.out.println(hams);
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

	/**
	 * Main thread creates parallel threads and processes certain d-hammings
	 *
	 *
	 */
	private static void threadMe() {
		Thread[] threads = new Thread[p];
		for(int j = 0; j < threads.length; j++) {
			threads[j] = new Thread(new Volunteer("Volunteer " + j, j));
			threads[j].start();
		} // fine if p % 4

		if( (p+1) % 4 == 0) { // p = 3, 7, 11
			sliceMe(proteins.substring(3));
		} else if ( (p-1) % 4 == 0) { // p = 1, 5, 9
			sliceMe(proteins.substring(2,3));
			sliceMe(proteins.substring(3));
		} else if (p % 2 == 0) { // p = 2, 6, 10
			sliceMe(proteins.substring(2,3));
			sliceMe(proteins.substring(3));
		}

		// wait for all seqIn(i)'s hams to exist
		for(int j = 0; j < threads.length; j++)
			try {
				threads[j].join();
			} catch (Exception e) { System.out.println("lol threads gone cray ;((("); }

	}

/** /
SUGGESTION:

to make use of multithreading, have sliceMe use hams[id%4]
will have to change '// slice substrings' to divide slices in ~equal parts into hams[id%4]
also undo Volunteer { ... this.id = id %4 }, so to know which hams[id%4] to use
/**/

	/**
	 * Thread calls method to create d-hammings
	 * @param protein - either A, C, T, G
	 *
	 */
	private static void sliceMe(String protein) {
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

			for ( int i = 0; i+d-1 < s.length(); i++) { // are you kidding me, is this #11's fix?
				ham.add( s.substring(0,i) .concat(protein.repeat(d)) .concat(s.substring(i+d)) );
/** /
String str = s.substring(0,i) .concat(protein.repeat(d)) .concat(s.substring(i+d));// );
ham.add(str);
if(str.length() < 4)
System.out.print(i+"i "+s+":"+str+" "+"pro: "+protein);
/**/
			}

		}

		// combine with original
		sem.acquire();
		hamsCopy.addAll(ham);
		sem.release();
	}

	private static class Volunteer implements Runnable {
		private String name;
		private int id;

		public Volunteer(String name, int id) {
			this.name = name;
			this.id = id % 4;
		}

		public void run() {

			if( p % 4 == 0)
				sliceMe(proteins.substring(id,id+1));
			else if (p < 3) {
				sliceMe(proteins.substring(id,id+1));
				sliceMe(proteins.substring(id+1,id+2));
			} else if( (p+1) % 4 == 0) { // p = 3, 7, 11
				sliceMe(proteins.substring(id,id+1));
			} else if ( (p-1) % 4 == 0) { // p = 5, 9, 13
				sliceMe(proteins.substring(id,id+1));
			} else if (p % 2 == 0) { // p = 6, 10, 14
				sliceMe(proteins.substring(id,id+1));
			}
		}
	}
}
