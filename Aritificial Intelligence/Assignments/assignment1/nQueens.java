// Assignment1 : NQueens (DFS, BFS, DFID)
// �������ڰ��к� 2015001103 ������

package nQueens;

import java.util.*;					// Queue, Stack, LinkedList
import java.io.FileWriter;			// resultN.txt�� ������� �ۼ��ϱ� ����
import java.io.IOException;			// resultN.txt�� ������� �ۼ��ϱ� ����
import java.io.File;


public class nQueens {


	/* [���� ����]
	 * 
	 * 1. ü������ ������ row ��ġ�� ��Ÿ���� �ڷᱸ���� LinkedList<Integer>�� ����Ѵ�(���� �� ������ �����ϱ� ����).
	 *    �� �� i ��° column ������ ���� row ���� LinkedList�� i ��° ������ �����ϸ�(i�� 0���� ����Ѵ�.), 
	 *    ��� �ִ� column�� ���ؼ��� ���� �������� �ʴ´�.
	 *    ����, 4X4 ü���ǿ� ���Ͽ� ���� ��ġ�� ������ ���ٰ� ��������.
	 *    x x x x
	 *    o x x x
	 *    x o x x
	 *    x x x x
	 *    �� ��� �ش� ü������ ���� ���¸� ��Ÿ���� [1, 2] �� �ȴ�.
	 *    
	 * 2. BFS�� ��� �ڷᱸ���� ť�� ����ϸ�, DFS �� DFID�� ��� �ڷᱸ���� ������ ����Ѵ�.
	 *    �̶� ������ ť �Ǵ� ���ÿ� ���� �ڷ����� �ռ� 1.���� �����Ͽ��� LinkedList<Integer>�� �ش��Ѵ�.
	 *
	 * 3. ü������ ���¸� �μ��� �޾�, �ش� ü������ ���信 �ش��ϴ� ���� �Ǻ��ϴ� CheckSafe �޼ҵ带 �����Ͽ���.
	 * 	  BFS, DFS �� DFID�� Ž���� ������ �� �ش� �޼ҵ带 ����Ѵ�.
	 * 
	 * 4. BFS, DFS �� DFID �޼ҵ�� ��� ���ڿ��� ��ȯ�Ѵ�.
	 *    Ž�� ���� ���� �ظ� �߰��� ��� �ش� �ؿ� ����� �ð��� �˷��ִ� ���ڿ��� ��ȯ�ϸ�,
	 *    �ظ� �߰��ϴ� ���� ������ ��� No Solution �̶�� �޼����� ����� �ð��� ��Ÿ���� ���ڿ��� ��ȯ�Ѵ�.
	 *    
	 * 5. main �޼ҵ忡���� Argument�� N(����) �� ��� ��� ������ ���� ��θ� �Է¹�����,
	 *    DFS, BFS �� DFID �޼ҵ带 �����Ͽ� ���� ������� �ش� ����� resultN.txt���Ͽ� �ۼ��Ѵ�. 
	 *
	 * */
	
	
	
	/* CheckSafe �޼ҵ�
	 * ����: ü������ ���������� �Է¹ް�, �̰��� ã���� �ϴ� �ؿ� �ش��ϴ����� �Ǻ��Ѵ�.
	 * ����: 1) ���� ������ N���� ������ �Ǻ��Ѵ�.(N���� ������ ã���� �ϴ� �ذ� �� �� ����.)
	 *      2) ���� ������ N�� �ش��� ���, �� ���� ���� ������ ���鿡 ���� ���������� Ȯ���Ѵ�.
	 *      3) ���� ������ �� �ִ� ���� ������ �ϳ��� ���� ��� false��, ��� ���� ������ ��� true�� ��ȯ�Ѵ�.
	 *      
	 *         
	 * */
	public boolean CheckSafe(LinkedList<Integer> list, int N) {
		int i, j;
		
		if (list.size() < N)								// ���� ������ N���� ������ �Ǻ�
			return false;
		
															// ���� ������ N���� ���,
		for (i = 1; i < N ; i++) {							// �� ���� ���� ���鿡 ���� �������� �Ǻ�
			for (j = 0; j < i ; j++){		
				if (list.get(j) == list.get(i))				// ���� row ���� ������ ���� �ִ��� �Ǻ�
					return false;
				if (list.get(j) == list.get(i) + i - j)		// �Ʒ��� �밢�� ��鿡 �Ǵٸ� ���� �ִ��� �Ǻ�
					return false;
				if (list.get(j) == list.get(i) - i + j)		// ���� �밢�� ��鿡 �Ǵٸ� ���� �ִ��� �Ǻ�
					return false;
			}
		}
		return true;										// ��� ���� �����ϸ�(�ذ� �����ϸ�) true�� ��ȯ
	}
	
	
	/* BFS �޼ҵ� 
	 * ����: NXN ü���ǿ� ���Ͽ� BFS �۾��� ������ ������� ����Ѵ�.
	 * ����: 1) ������ �����ϴ� ���� ����Ʈ �� �ش� ����Ʈ�� �����ϴ� ť�� �����Ѵ�.
	 *      2) ť�� �� ����Ʈ 1���� �����Ѵ�(���� �ϳ��� ���� ó���� ü���� ����)
	 *      3) ť���� ����(����Ʈ)�� ������ �ش� ���Ұ� ã�� �ؿ� �ش��ϴ����� �Ǻ��ϰ�, 
	 *         ã�� �ذ� �ƴ� ��� expand �Ͽ� ť�� �߰��Ѵ�.
	 *         (Ž���� ������ ����Ʈ�� ���� ������ N���� ������ �˻��Ͽ� ������ �����ϸ�,
	 *         	 �ش� ����Ʈ�� ���� 0 ~ (N-1)�� �߰��� N���� ���ο� ����Ʈ �����Ͽ� ť�� �����Ѵ�.)
	 *      4) ť�� ����� ���Ұ� ���� ������ 3)�� ������ �ݺ��Ѵ�.
	 * */
	
	String BFS(int N) {
		
		long startTime = System.nanoTime();										// ���� �ð� ����
		String str_bfs = "";													// ��ȯ�� ���� ���ڿ� ����
		if(N < 1) {																// N�� 1���� ������(���ʿ� ü������ ������ �� ������)
			long endTime = System.nanoTime();									// ���� �ð��� �����ϰ�
			str_bfs = str_bfs + ">BFS\nNo Solution\nTime : " + (endTime - startTime)/1000000000.0 + "\n\n";		// �ҿ� �ð��� �����Ͽ� ���ڿ��� �����Ѵ�.
			return str_bfs;														// ���ڿ��� ��ȯ�Ѵ�.
		}
		
		str_bfs += ">BFS \n";
		int i;
		Queue<LinkedList<Integer>> q = new LinkedList<LinkedList<Integer>>();		// ���� ����Ʈ�� ���ҷ� ������ ť ����
		LinkedList<Integer> container = new LinkedList<Integer>();					// ������ ���ҷ� ������ ���� ����Ʈ ����
		q.add((LinkedList<Integer>)container.clone());								// ť�� �� ����Ʈ([])�� ���� : ���� 0���� ü����


		while(!q.isEmpty()) {														// ť�� ���Ұ� ���� ������ �ݺ�			
		
		
				container = q.remove();											// ť�� ���Ҹ� �ϳ� ������.
				if(CheckSafe(container, N)) {									// ���� ����(����Ʈ)�� ã�� �ؿ� �ش��ϴ����� �Ǻ��Ͽ�,
																				// ã�� �ؿ� �ش��ϸ� Location�� ���ڿ��� �����Ѵ�.
					Iterator<Integer> iterator = container.iterator();
					str_bfs += "Location : ";
					while(iterator.hasNext()) {
						str_bfs = str_bfs + iterator.next() + " ";
					}
					long endTime = System.nanoTime();												// ���� �ð��� �����Ͽ�
					str_bfs = str_bfs + "\nTime : " + (endTime - startTime)/1000000000.0 + "\n\n";	// �۾� ����ð��� ����Ͽ� ���ڿ��� �����Ѵ�.

					return str_bfs;																	// ���ڿ��� ��ȯ�Ѵ�.
				}
			
				if(container.size() < N) {										// ť���� ���� ����(����Ʈ)�� ����� N���� ���� ���(���� ������ ������ ���)
					for (i = 0; i < N ; i++) {									// �ش� ����Ʈ�� ���� 0 ~ (N-1) �� �߰��� N���� ���ο� ����Ʈ�� ť�� �����Ѵ�.
						container.addLast(i);
						q.add((LinkedList<Integer>)container.clone());
						container.removeLast();
					}
				}		
			}
		
																										// ť�� ��� ���ҿ� ���Ͽ� Ž���� �����Ͽ������� �ظ� ã�� ���� ���,
			long endTime = System.nanoTime();															// ���� �ð��� �����ϰ�
			str_bfs = str_bfs + "No Solution\nTime : " + (endTime - startTime)/1000000000.0 + "\n\n";	// �ظ� ã�� ���Ͽ��ٴ� �޼����� �Բ� �ҿ�ð��� ���ڿ��� �����Ѵ�.

		return str_bfs;															// ���ڿ��� ��ȯ�Ѵ�.
	}
	
	
	
	/* DFS �޼ҵ� 
	 * ����: NXN ü���ǿ� ���Ͽ� DFS �۾��� ������ ������� ����Ѵ�.
	 * ����: 1) ������ �����ϴ� ���� ����Ʈ �� �ش� ����Ʈ�� �����ϴ� ������ �����Ѵ�.
	 *      2) ���ÿ� �� ����Ʈ 1���� �����Ѵ�(���� �ϳ��� ���� ó���� ü���� ����)
	 *      3) ���ÿ��� ����(����Ʈ)�� ������ �ش� ���Ұ� ã�� �ؿ� �ش��ϴ����� �Ǻ��ϰ�, 
	 *         ã�� �ذ� �ƴ� ��� expand �Ͽ� ���ÿ� �߰��Ѵ�.
	 *         (Ž���� ������ ����Ʈ�� ���� ������ N���� ������ �˻��Ͽ� ������ �����ϸ�,
	 *         	 �ش� ����Ʈ�� ���� 0 ~ (N-1)�� �߰��� N���� ���ο� ����Ʈ �����Ͽ� ���ÿ� �����Ѵ�.)
	 *      4) ���ÿ� ����� ���Ұ� ���� ������ 3)�� ������ �ݺ��Ѵ�.
	 * */
	
	
	String DFS(int N) {
		long startTime = System.nanoTime();										// ���� �ð� ����
		String str_dfs = "";													// ��ȯ�� ���� ���ڿ� ����
		if(N < 1) {																// N�� 1���� ������(���ʿ� ü������ ������ �� ������)
			long endTime = System.nanoTime();									// ���� �ð��� �����ϰ�
			str_dfs = str_dfs + ">DFS\nNo Solution\nTime : " + (endTime - startTime)/1000000000.0 + "\n\n";		// �ҿ� �ð��� �����Ͽ� ���ڿ��� �����Ѵ�.
			return str_dfs;														// ���ڿ��� ��ȯ�Ѵ�.
		}
		str_dfs += ">DFS \n";
		int i;
		Stack<LinkedList<Integer>> s = new Stack<LinkedList<Integer>>();		// ���� ����Ʈ�� ���ҷ� ������ ���� ����
		LinkedList<Integer> container = new LinkedList<Integer>();				// ������ ���ҷ� ������ ���� ����Ʈ ����
		
		s.push((LinkedList<Integer>)container.clone());							// ���ÿ� �� ����Ʈ([])�� ���� : ���� 0���� ü����
			
		while(!s.isEmpty()) {													// ���ÿ� ���Ұ� ���� ������ �ݺ�
				
			container = s.pop();												// ������ ���Ҹ� �ϳ� ������.
			if(CheckSafe(container, N)) {										// ���� ���Ұ� ã�� �ؿ� �ش��� ���
				Iterator<Integer> iterator = container.iterator();
				str_dfs += "Location : ";		
				while(iterator.hasNext()) {										// ���� ��ġ�� ���ڿ��� �����Ѵ�.
					str_dfs = str_dfs + iterator.next() + " ";
				}
				long endTime = System.nanoTime();								// ���� �ð��� �����Ѵ�.
				str_dfs = str_dfs + "\nTime : " + (endTime - startTime)/1000000000.0 + "\n\n";		// �ҿ� �ð��� ����Ͽ� ���ڿ��� �����Ѵ�.
				return str_dfs;													// ���ڿ��� ��ȯ�Ѵ�.
			}
			
			if(container.size() < N) {											// ���ÿ��� ���� ����(����Ʈ)�� ����� N���� ���� ���(���� ������ ������ ���)
				for (i = 0; i < N ; i++) {										// �ش� ����Ʈ�� ���� 0 ~ (N-1) �� �߰��� N���� ���ο� ����Ʈ�� ���ÿ� �����Ѵ�.
					container.addLast(i);
					s.push((LinkedList<Integer>)container.clone());
					container.removeLast();
				}
			}		
		}		
																				// ť�� ��� ���ҿ� ���Ͽ� Ž���� �����Ͽ������� �ظ� ã�� ���� ���,
		long endTime = System.nanoTime();										// ���� �ð��� �����ϰ�,
		str_dfs = str_dfs + "No Solution\nTime : " + (endTime - startTime)/1000000000.0 + "\n\n";	// �ҿ� �ð��� ����Ͽ� ���ڿ��� �����Ѵ�.

		return str_dfs;															// ���ڿ��� ��ȯ�Ѵ�.
		
	}

	
	
	/* DFID �޼ҵ� 
	 * ����: NXN ü���ǿ� ���Ͽ� DFID �۾��� ������ ������� ����Ѵ�.
	 * ����:	1 ~ N �� limit�� ���Ͽ� ���� �۾��� �����Ѵ�(�� limit�� ���� DFS Ž�� ����).
	 * 
	 * 		1) ������ �����ϴ� ���� ����Ʈ �� �ش� ����Ʈ�� �����ϴ� ������ �����Ѵ�.
	 *      2) ���ÿ� �� ����Ʈ 1���� �����Ѵ�(���� �ϳ��� ���� ó���� ü���� ����)
	 *      3) ���ÿ��� ����(����Ʈ)�� ������ �ش� ���Ұ� ã�� �ؿ� �ش��ϴ����� �Ǻ��ϰ�, 
	 *         ã�� �ذ� �ƴ� ��� expand �Ͽ� ���ÿ� �߰��Ѵ�.
	 *         (Ž���� ������ ����Ʈ�� ���� ������ limit���� ������ �˻��Ͽ� ������ �����ϸ�,
	 *         	 �ش� ����Ʈ�� ���� 0 ~ (N-1)�� �߰��� N���� ���ο� ����Ʈ �����Ͽ� ���ÿ� �����Ѵ�.)
	 *      4) ���ÿ� ����� ���Ұ� ���� ������ 3)�� ������ �ݺ��Ѵ�.
	 * */
	
	String DFID(int N) {
		
		long startTime = System.nanoTime();										// ���� �ð� ����
		String str_dfid = "";													// ��ȯ�� ���� ���ڿ� ����
		if(N < 1) {																// N�� 1���� ������(���ʿ� ü������ ������ �� ������)
			long endTime = System.nanoTime();									// ���� �ð��� �����ϰ�
			str_dfid = str_dfid + ">DFID\nNo Solution\nTime : " + (endTime - startTime)/1000000000.0 + "\n\n";		// �ҿ� �ð��� �����Ͽ� ���ڿ��� �����Ѵ�.
			return str_dfid;														// ���ڿ��� ��ȯ�Ѵ�.
		}
		str_dfid += ">DFID\n";
		int i, limit;

		for (limit = 1 ; limit <= N ; limit++) {								// 1���� N������ limit�� ���� DFS Ž���� �����Ѵ�.
			Stack<LinkedList<Integer>> s = new Stack<LinkedList<Integer>>();	// ���� ����Ʈ�� ���ҷ� ������ ���� ����
			LinkedList<Integer> container = new LinkedList<Integer>();			// ������ ���ҷ� ������ ���� ����Ʈ ����
	
			s.push((LinkedList<Integer>)container.clone());						// ���ÿ� �� ����Ʈ([])�� ���� : ���� 0���� ü����
					
			while(!s.isEmpty()) {												// ���ÿ� ���Ұ� ���� ������ �ݺ�
					
				container = s.pop();											// ������ ���Ҹ� �ϳ� ������.
				if(CheckSafe(container, N)) {									// ���� ���Ұ� ã�� �ؿ� �ش��ϴ� ���
					Iterator<Integer> iterator = container.iterator();
					str_dfid += "Location : ";
					while(iterator.hasNext()) {									// ���� ��ġ�� ���ڿ��� �����Ѵ�.
						str_dfid = str_dfid + iterator.next() + " ";
					}
					long endTime = System.nanoTime();							// ���� �ð��� �����Ѵ�.
					str_dfid = str_dfid + "\nTime : " + (endTime - startTime)/1000000000.0 + "\n\n";	// �ҿ� �ð��� ����Ͽ� ���ڿ��� �����Ѵ�.
					return str_dfid;											// ���ڿ��� ��ȯ�Ѵ�.
				}
				
				if(container.size() < limit) {									// ���ÿ��� ���� ����(����Ʈ)�� ����� limit���� ���� ���(���� ������ ������ ���)							
					for (i = 0; i < N ; i++) {									// �ش� ����Ʈ�� ���� 0 ~ (N-1) �� �߰��� N���� ���ο� ����Ʈ�� ���ÿ� �����Ѵ�.
						container.addLast(i);
						s.push((LinkedList<Integer>)container.clone());
						container.removeLast();
					}
				}		
			}
			
		}
																				// ��� limit�� ���Ͽ� �۾��� �����Ͽ��µ��� �ظ� ã�� ���� ���
		long endTime = System.nanoTime();										// ���� �ð��� �����Ѵ�.
		str_dfid = str_dfid + "No Solution\nTime : " + (endTime - startTime)/1000000000.0 + "\n\n";		// �ҿ� �ð��� ����Ͽ� ���ڿ��� �����Ѵ�.
		return str_dfid;														// ���ڿ��� ��ȯ�Ѵ�.
		
	}
	
	
	/* ���� �޼ҵ� 
	 * ����: 1) N�� resultN.txt�� ��ΰ��� Argument�� �޴´�
	 * 		2) NXN�� ���� DFS, BFS �� DFID Ž���� �����Ͽ� �� ����� �Է¹��� ����� resultN.txt�� ������Ʈ�Ѵ�.
	 * */
	
	public static void main(String args[]) {

		int N = Integer.parseInt(args[0]);					// N�� ���������� ��ȯ�Ѵ�.
		String Address = args[1].replace("\\", "\\\\");		// �ùٸ� ��θ��� ����� ���Ͽ�, �Է¹��� ��� ���ڿ��� ���Ͽ� replace �۾��� �����Ѵ�.
															// �ڹٿ��� ���Ͽ� ������ ��� ��θ��� C:\\AI\\Assignment1\\resultN.txt �� ���� \\ �� ����ϳ�,
															// �����쿡���� ���� ��θ� C:\AI\Assignment1\resultN.txt�� ���� \dmf ����ϹǷ�
															// \�� \\���� ��ȯ���� �־�� �Ѵ�.
		String Filename = "result" + args[0] + ".txt";
		String textfile = Address + "\\\\" + Filename;			// ���� �۾��� ������ txt������ ���� ���

		File file = new File(textfile);						// �Է¹��� ��ġ�� resultN.txt ���� ����
		nQueens NQueens = new nQueens();					// nQueens Ŭ������ �����Ѵ�.
		
		String str_bfs, str_dfs, str_dfid;
		String str;

		str_dfs = NQueens.DFS(N);							// NXN ü���ǿ� ���� DFS �۾��� �����Ͽ� ����� �����Ѵ�.
		str_bfs = NQueens.BFS(N);							// NXN ü���ǿ� ���� BFS �۾��� �����Ͽ� ����� �����Ѵ�.
		str_dfid = NQueens.DFID(N);							// NXN ü���ǿ� ���� DFID �۾��� �����Ͽ� ����� �����Ѵ�.
		
		str = str_dfs + str_bfs + str_dfid;					// ���Ͽ� �ۼ��� ���� �����
	

		FileWriter fw = null;								// FileWriter ������ �����Ѵ�.
		try {
			fw = new FileWriter(file, false);				// true: �̾� ����			false: ������ ������ �����ϰ� ���� ����			
			fw.write(str);									// DFS, BFS, DFID �۾��� ������ ������� write�Ѵ�.
			fw.flush();										// ���۸� ����.							
		} catch(IOException e) {							// ������ �߻��� ���
			e.printStackTrace();							// ������ ���� �ڼ��� ������ ȭ�鿡 ����Ѵ�.
		} finally {
			try {
				if(fw != null) fw.close();					// I/O ��Ʈ���� �ݴ´�
			} catch(IOException e) {						// ������ �߻��� ���
				e.printStackTrace();						// ������ ���� �ڼ��� ������ ȭ�鿡 ����Ѵ�.
			}
		}
		
	}
	
}
