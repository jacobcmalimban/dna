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
				System.out.println(line);
			}

			bReader.close();
		} catch(Exception e) {
			System.out.println("Unable to open file" + fileName);
		}

		// slice substrings

		// generate m-long strings from first input dna sequence
			// create 0 left padded base 4 strings
			// & convert to A, C, G, T

		// calculate hamming distance
		n = seqIn.size();
		l = seqIn.get(0).length();
		int slicount = l-m+1;
		int d = 1;
		HashSet<String> tempHams = new HashSet<String>();


		for(int i = 0; i < n; i++) {
			hams = new HashSet<String>();
			hamsCopy = new HashSet<String>();

			for(int j = 0; j < slicount; j++) { // each m-long dna slice
				hams.add( seqIn.get(i).substring(j, j+m) ); // only pure slices
			}
System.out.println("\n\n"+hams+"\n");
			// create threads
			Thread[] threads = new Thread[4];
			for(int j = 0; j < threads.length; j++) {
				threads[j] = new Thread(new Volunteer("Volunteer " + j, j));
				threads[j].start();
			}

			// wait for all sequence1's hams to exist
			for(int j = 0; j < threads.length; j++)
				try {
					threads[j].join();
				} catch (Exception e) { System.out.println("lol threads gone cray ;((("); }

			hams.addAll(hamsCopy);
			// hams = valid seqIn(i) hams
			if ( i == 0 ) {
				tempHams = new HashSet<String>(hams); // save verified previous hams
			} else {
				tempHams.retainAll(hams);
			}
System.out.println("temphams: " + tempHams);

		}

		System.out.println("Valid hams: " + tempHams);
	}

/** /

Should I make an Array of Hashsets?

If I do, I can just compare for matches at the end

If I don't, then I have to parallel create another temporary hashset
 for each thread, combine, and check after each combined thing


Is there a way to not have to generate new hammings and only check the old ones?

YES THERE IS
 but I don't know how...

/**/


/** /
				String slice = slices[i][j] = seqIn.get(i).substring(j, j+m);

				outie:
				for(int k = 0; k < perms.length; k++) { // perms = 256
					int mismatch = 0;
		// calculate hamming distance
					for(int x = 0; x < m; x++) { // each char in slice
						if ( slice.charAt(x) == perms[k].charAt(x) )
							continue;
						else
							if (++mismatch > d)
								continue outie; //break
					}

					if (mismatch <= d)
						hams[i].add(perms[k]);
				}
			}
		}
/**/

		// find same perm across all sequences
		// 1, 2, 3, 4
		// 2, 4, 1
		// 1, 3
		// 1, 2

/** /		for (int i = 1; i < hams.size(); i++)
			hams[0].retainAll(hams[i]);
		System.out.println(hams[0]);
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
			for( String s : new HashSet<String>(hams) ) { // iterate through each legal
//				System.out.println(id + ":" + s);// if GGAG
				/*
					AGAG
					GAAG
					GGAG
					GGAA
				*/
				// add to own ham

				for ( int i = 0; i < s.length(); i++) { // d = 1
					if (id == 3)
						System.out.print( s.substring(0,i)
 .concat(proteins.substring(id,id+1)) .concat(s.substring(i+1)) + " " );

					ham.add( s.substring(0,i) .concat(proteins.substring(id,id+1)) .concat(s.substring(i+1)) );
				}

			}
/* */
			// combine with original
			sem.acquire();
			System.out.println(id+":"+ham.size());
			hamsCopy.addAll(ham);
			sem.release();
/* */
		}
	}

	private static class Volunteer2 implements Runnable {
		private String name;
		private int id;

		public Volunteer2(String name, int id) {
			this.name = name;
			this.id = id;
		}

		public void run() {
			HashSet<String> ham = new HashSet<String>();
			for( String s : hams ) { // iterate through each legal
				System.out.println(id + ":" + s);// if GGAG
				/*
					AGAG
					GAAG
					GGAG
					GGAA
				*/
				// add to own ham
				ham.add(s);

			}

			// combine with original
			sem.acquire();
			hams.addAll(ham);
			sem.release();
		}
	}
}
