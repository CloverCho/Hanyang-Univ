/*********************************************************/
/* 	                      �ΰ����� 2�� ����(Hill Climbing)                */
/*			     �������ڰ��к� 2015001103 ������					*/
/********************************************************/


package nQueens_3;

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;


public class nQueens {

	
	/* Heuristic ��� �Լ�
	 * ���⼭ Heuristic�� "���� ���� ������ ������ ���� ����"�̴�. */
	static int Heuristic(ArrayList<Integer> list) {
		int i, j;
		int heuristic = 0;		
		if (list.size() == 1) return heuristic;				// 1X1 �������� ��� 0�� ��ȯ�Ѵ�.
													
		for (i = 1; i < list.size() ; i++) {						
			for (j = 0; j < i ; j++){		
				if (list.get(j) == list.get(i))				// ���� row ���� ������ ���� ������
					heuristic++;							// heuristic�� 1 ������Ų��.
				if (Math.abs(list.get(i) - list.get(j)) == Math.abs(i - j))		// �밢�� ��鿡 ���� ������
					heuristic++;							// heuristic�� 1 ������Ų��.
			}
		}
		return heuristic;	
	}
	
	
	/* ���� �ʱ� ��ġ ���� �Լ�
	 * �̶� �� ���� �ִ� ���� ��ġ�� �����ϰ� �����Ѵ�.*/
	static ArrayList<Integer> generate(int N){
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		Random r = new Random();
		for (int i=0 ; i<N; i++) {		// �� ���� ���Ͽ�
			list.add(r.nextInt(N));		// ���� ��ġ�� �����ϰ� �����Ѵ�.
		}
		return list;
	}
	
	
	/* HillClimbing �Լ�
	 * ���:	1) generate �޼ҵ带 �̿��Ͽ� �ʱ� ��ġ�� �������� �����Ѵ�.
	 * 		2) �ʱ� ��ġ�� ���� �޸���ƽ ���� ��� �� �����Ѵ�.
	 * 		3) �� ���� ���Ͽ� ���� ��ġ�� ��ĭ�� �ٲپ� ���鼭 �޸���ƽ�� ����ϰ�,
	 *         �� ���� ����� �޸���ƽ ������ ������ �ش� ü���� �� �޸���ƽ�� ���� �����Ѵ�.
	 *   	4) ��� ���� ���Ͽ� 3) ������ �����Ѵ�.
	 *   	5) ü������ ��� ĭ�� ���Ͽ� 4) ������ �����Ͽ������� �޸���ƽ ���� 
	 *         �ʱ�� �״���� ���, Local optimum�� �ش��Ѵٰ� ����, 
	 *         Restart�Ѵ�.(���Ӱ� ü������ �����Ͽ� 2)���� �ٽ� �����Ѵ�.)
	 *   	6) �޸���ƽ�� 0�� ���(��� ���� ���θ� ������ �� ���� ��ġ�� �ִ� ���)
	 *   	      �ش� ü���� �� �ҿ� �ð�, Restart Ƚ���� ���ڿ��� �����Ͽ� ��ȯ�Ѵ�.*/
	
	static String HillClimbing(int N) {				// N : ü������ ������ ����
		
		long startTime = System.nanoTime();			// ���� �ð� ����
		
		String str = ">Hill Climbing\n";
		
		if(N < 1 || N == 2 || N == 3) {				// N�� �����̰ų� 2�Ǵ� 3�� ��� �ش� �������� �ʴ´�.
			str += "No Solution\n\n";
			return str;
		}

		ArrayList<Integer> best = new ArrayList<Integer>();	// �ּ��� �� ��ġ
		ArrayList<Integer> tmp = new ArrayList<Integer>();	// Ž���� �� ��ġ
		
		tmp = generate(N);							// �ʱ���ġ ����
		best = (ArrayList<Integer>)tmp.clone();		
		
		int heuristic = Heuristic(tmp);				// �ʱ� �޸���ƽ ��
		int tmpheuristic = Heuristic(tmp);
		int countRestart = 0;
		int heuristic_before;					// Local optimal �� �������� üũ�ϴ� ����
		
		while(true) {
			heuristic_before = heuristic;		// Hill Climb ���� ��, ������ �޸���ƽ�� �����Ѵ�.

			if(heuristic == 0) {			// ������ queen�� �ʱ� ��ġ�� ó������ ������ ������ ���
				Iterator<Integer> iterator = tmp.iterator();
				while(iterator.hasNext()) {
					str = str + iterator.next() + " ";		// �ش� ��ġ�� ���ڿ��� ����
				}
				long endTime = System.nanoTime();			// ���� �ð� ����
				str = str + "\nTotal Elapsed Time : " + (endTime - startTime)/1000000000.0;		// �ҿ� �ð�
				str = str + "\nNumber of Restart : " + countRestart + "\n\n";			// Restart Ƚ��
				return str;						// ���ڿ� ��ȯ
			}
			
			for(int i = 0; i<N; i++) {						// ��� ���� ���Ͽ� ������ �ݺ��Ѵ�.
				for(int j = 0; j<N; j++) {
					tmp.set(i,j);							// tmp�� i��° ���Ҹ� 1���� N���� �ٲپ� ����.
					tmpheuristic = Heuristic(tmp);			// �޸���ƽ�� ���Ӱ� ����Ѵ�.
					if(tmpheuristic < heuristic) {			// ���� �޸���ƽ���� ���ο� �޸���ƽ�� ������
						best = (ArrayList<Integer>)tmp.clone();	// �ش� ��ġ������ next�� �����ϰ�
						heuristic = tmpheuristic;			// �޸���ƽ ���� ������Ʈ�Ѵ�.
						if(tmpheuristic == 0) {				// �޸���ƽ�� 0�� ���(������ �����ϴ� ���)
							Iterator<Integer> iterator = best.iterator();
							while(iterator.hasNext()) {
								str = str + iterator.next() + " ";		// �ش� ��ġ�� ���ڿ��� �����Ѵ�.
							}
							long endTime = System.nanoTime();			// ���� �ð��� ����
							str = str + "\nTotal Elapsed Time : " + (endTime - startTime)/1000000000.0;	// �ҿ� �ð�
							str = str + "\nNumber of Restart : " + countRestart + "\n\n";	// ����� Ƚ��
							return str;							// ���ڿ� ��ȯ
						}
					}
				}
			}
			if(heuristic_before == heuristic) {		 	// ü������ ��� ���� ���Ͽ� Hill Climbing �۾��� �����Ͽ���
													// �޸���ƽ ���� ������ ���� ���
													// Local optimum�� �����ٰ� ����, Restart �Ѵ�.
				countRestart++;						// Restart Ƚ�� ����
				tmp = generate(N);					// ü������ ���Ӱ� ����
				heuristic = Heuristic(tmp);			// ���ο� ü���ǿ� ���� �޸���ƽ
				tmpheuristic = heuristic;		
			}
		}
	}
	
	
	public static void main(String args[]) {
		
		int N = Integer.parseInt(args[0]);					// ù��° ����(����)
		String Address = args[1].replace("\\", "\\\\");		// �ι�° ����(�����ּ�)
		String Filename = "result"+N+".txt";
		String File = Address + "\\\\" + Filename;
		

		String result = "";
		result = HillClimbing(N);					// HillClimbing Ž�� ����
		FileWriter fw = null;

		try {
			fw = new FileWriter(File, true);		
			fw.write(result);						// �ش� ��ġ�� resultN.txt ���� �� ��� �ۼ�
			fw.flush();								
		} catch(IOException e) {					// ����� ���� �߻���
			e.printStackTrace();
		} finally {
			try {
				if(fw != null) fw.close();			
			} catch(IOException e) {				// ����� ���� �߻���
				e.printStackTrace();
			}
		}
	} 
	
}
