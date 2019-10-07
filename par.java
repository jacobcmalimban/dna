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

public class seq {
	public static void main(String[] args) {

		// create threads
		Thread[] thread = new Thread[4];
		for(int i = 0; i < 4; i++)
			threads[i] = new Thread(new Volunteer("Volunteer " + i, i);

		int m=4;
		String[] perms = new String[(int)Math.pow(4,m)];

		// generate all m-long strings
		// create 0 left padded base 4 strings
		// & convert to A, C, G, T
		for(int i = 0; i<perms.length; i++) {
			perms[i] = String.format("%0" + m + "d", Integer.parseInt(Integer.toString(i, 4)));
			perms[i] = perms[i].replaceAll("0","A").replaceAll("1","C").replaceAll("2","G").replaceAll("3","T");
		}

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
		// calculate hamming distance
		int n = seqIn.size();
		int l = seqIn.get(0).length();
		int slicount = l-m+1;
		String[][] slices = new String[n][slicount];

// what is this array of arraylists
		ArrayList<String>[] hams = new ArrayList[seqIn.size()];
		//Set<String> hams = new HashSet<String>();
		int d = 1;

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

					if (mismatch <= d)
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
		System.out.println(hams[0]);

		}
	}
}

private static class Volunteer {
	private String name;
	private int id;

	pubic Volunteer(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public void run() {
		
	}
}
