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
	private static String[][] slices;
	//private static ArrayList< LinkedList<String> > dValid; // for optimizing algo
	private static ArrayList< HashSet<String> > hams;
	private static int n, l, m, d, p;
	private static final String proteins = "ACTG";
	private static Thread[] threads;

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

		// slices
		slices = new String[n][l-(m-1)];
		for(int i = 0; i < slices.length; i++) {
			for(int j = 0; j < slices[i].length; j++) {
				slices[i][j] = seqIn.get(i).substring(j,j+m);
			}
		}

		// generate all m-long strings
		// create 0 left padded base 4 strings
		// & convert to A, C, G, T
		perms = new String[(int)Math.pow(4,m)];
		for(int i = 0; i<perms.length; i++) {
			perms[i] = String.format("%0" + m + "d", Integer.parseInt(Integer.toString(i, 4)));
			perms[i] = perms[i].replaceAll("0","A").replaceAll("1","C").replaceAll("2","G").replaceAll("3","T");
		}


		// create n-many HashSets
		hams = new ArrayList< HashSet<String> >();
		for(int i = 0; i < n; i++) {
			hams.add( new HashSet<String>() );
		}

		// create threads
		threads = new Thread[p];
		for(int i = 0; i < p; i++) {
			threads[i] = new Thread(new Volunteer("Volunteer", i));
			threads[i].start();
		}

		// .join()
		try{
			for(int i = 0; i < p; i++)
				threads[i].join();
		} catch(Exception e) {System.out.println("oof");}

		// retain: for each string, compare with first and retain only same
		for(int i = 1; i < n; i++)
			hams.get(0).retainAll( hams.get(i) );

		System.out.println(hams.get(0) + "\nSize:"+hams.get(0).size());
	}

	private static class Volunteer implements Runnable {
		private String name;
		private int id;
		private ArrayList< HashSet<String> > localHam;

		public Volunteer(String name, int id) {
			this.name = name + " " + id;
			this.id = id;
			localHam = new ArrayList<>(hams);
		}

		public void run() {
			for(int i = 0; i < localHam.size(); i++) {

//System.out.println( (perms.length/p * id)+ "<" +(perms.length/p * (id+1)) + "<" + perms.length );

				// calculate haming distance
				for(int j = (perms.length/p * id); j < perms.length/p * (id+1); j++) { //distributed workload

					for(int k = 0; k < slices[i].length; k++){ // look at all [i][k] slices

						int mis = 0;
						for(int x = 0; x < m; x++) { // d hammings
							if(slices[i][k].charAt(x) != perms[j].charAt(x)) {
								mis++;
								if(mis > d)
									break;
							}
						}

						// local hashset gets valid hammings
						if(mis <= d) {
							localHam.get(i).add(perms[j]);
						}

					}
				}
			}

			// semaphore.acquire(), add all local hammings for each string
			sem.acquire();
			for(int i = 0; i < n; i++)
				hams.get(i).addAll( localHam.get(i) );
			sem.release();
		}
	}
}
