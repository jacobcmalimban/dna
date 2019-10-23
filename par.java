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
	private static String[] perms;
	private static String[][] dValid;
	private static HashSet<String> hams = new HashSet<String>();
	private static HashSet<String> hamsCopy = new HashSet<String>();
	private static int n, l, m, d, p;
	private static final String proteins = "ACTG";

	public static void main(String[] args) {
		System.out.println("Jacob Malimban");
		for(int i = 0; i < 80; i++)
			System.out.print("=");
		System.out.println();

		// obtain sequences
		String fileName = "input.txt", line;
		List<String> seqIn = new ArrayList<String>();

		try {
			FileReader fReader = new FileReader(fileName);
			BufferedReader bReader = new BufferedReader(fReader);
			boolean first = true;

			while((line = bReader.readLine()) != null) {
				if(first) {
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

		perms = new String[(int)Math.pow(4,m)];

		// generate all m-long strings
		// create 0 left padded base 4 strings
		// & convert to A, C, G, T
		for(int i = 0; i<perms.length; i++) {
			perms[i] = String.format("%0" + m + "d", Integer.parseInt(Integer.toString(i, 4)));
			perms[i] = perms[i].replaceAll("0","A").replaceAll("1","C").replaceAll("2","G").replaceAll("3","T");
		}

// CHANGE ME

/** /
		// generate valid d-distance difference
		dValid = new String[(int)Math.pow(4,d)];
		for(int i = 0; i < dValid.length; i++) { // d hammings
			dValid[i] = String.format("%0" + d + "d", Integer.parseInt(Integer.toString(i, 4)));
			dValid[i] = dValid[i].replaceAll("0","A").replaceAll("1","C").replaceAll("2","G").replaceAll("3","T");
		}
/**/

		// generate valid d-distance difference
		dValid = new String[p][(int)Math.pow(4,d)];
		for(int i = 0; i < dValid[0].length; ) { // d hammings
			for(int j = 0; j < p; j++) { // distribute workload
				dValid[j][i] = String.format("%0" + d + "d", Integer.parseInt(Integer.toString(i, 4)));
				dValid[j][i] = dValid[j][i].replaceAll("0","A").replaceAll("1","C").replaceAll("2","G").replaceAll("3","T");
				i++;
			}
		}

		// create threads
		for(int i = 0; i < p; i++) {
			
		}
		// .join()


/** /
		// slice substrings
		// calculate hamming distance
		int slicount = l-m+1;
		String[][] slices = new String[n][slicount];

		for(int i = 0; i < slices.length; i++) { // each dna sequence
			hams[i] = new ArrayList<>();

			for(int j = 0; j < slices[i].length; j++) { // each m-long dna slice
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

					//if (mismatch <= d)
						hams[i].add(perms[k]);
				}
			}
		}

		// find same perm across all sequences
		// 1, 2, 3, 4
		// 2, 4, 1
		// 1, 3
		// 1, 2

		for (int i = 1; i < hams.length; i++)
			hams[0].retainAll(hams[i]);

		// remove duplicates
		Set<String> set = new LinkedHashSet<>();
		set.addAll(hams[0]);
		hams[0].clear();
		hams[0].addAll(set);

		System.out.println(hams[0]);
// </CHANGE ME>
/**/
	}

	private static class Volunteer implements Runnable {
		private String name;
		private int id;

		public Volunteer(String name, int id) {
			this.name = name;
			this.id = id % 4;
		}

		public void run() {
			// distribute workload
			// calculate haming distance
			// local hashset gets valid hammings
			// semaphore.aquire()
		}
	}
}
