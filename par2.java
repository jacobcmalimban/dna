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

public class par2 {
	private static Semaphore sem = new Semaphore(1);
	private static HashSet<String> hams = new HashSet<String>();
	private static HashSet<String> hamsCopy = new HashSet<String>();
	private static int n, l, m, d, p;
	private static String proteins = "ACTG";


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

		for(int i = 0; i < n; i++) {
			hams = new HashSet<String>();
			hamsCopy = new HashSet<String>();

			// slice substrings
			for(int j = 0; j < slicount; j++) { // each m-long dna slice
				hams.add( seqIn.get(i).substring(j, j+m) ); // only pure slices
			}

			// create threads
			Thread[] threads = new Thread[p];
			for(int j = 0; j < threads.length; j++) {
				threads[j] = new Thread(new Volunteer("Volunteer " + j, j));
				threads[j].start();
			}

/** /

if p-threads:
 == 4, okay
 % 4, make hams into p%4 partitions and have threads take care of it

p = 2,
 threads do their id and id+1 proteins

p = 6, 10

p = 1
 assume p=2, but have main work as well

p = 3, 7, 11, (4n -1)
 assume p % 4

p = 5, 9
 maybe have 


...
What if we just create Array of Hashsets, 
List<
/**/

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

	private static void threadMe() {
	}

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
			for ( int i = 0; i < s.length(); i++) { // assumes 4x threads
				ham.add( s.substring(0,i) .concat(protein.repeat(d) ) .concat(s.substring(i+d)) );
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
			this.id = id;
		}

		public void run() {
			if (p % 2 == 0)
				sliceMe(proteins.substring(id,id+1));
				if(p % 4 != 0) // runs when p = 2, 6, 10, ...
					sliceMe(proteins.substring(id+1, id+2));
			else // p = 1, 5, 9	3, 7, 11
				sliceMe(proteins.substring(id,id+1));
		}
	}
}
