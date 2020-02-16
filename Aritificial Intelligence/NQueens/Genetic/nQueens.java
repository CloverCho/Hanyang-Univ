/*********************************************************/
/* 	                      �ΰ����� 3�� ����(Genetic Algorithm)                */

package nQueens_4;

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

public class nQueens {

	
	static int population = 100;							// �� ������ �α�
	static double parent_rate = 0.2;						// �θ� ������ ����						
	static double crossover_rate = 0.4;						// crossover�� ����
	static double mutation_rate = 0.4;						// mutation�� ����
	
	
	/* Fitness ��� �Լ�
	 * ���⼭ Fitness�� "���� ���� ������ ������ ���� ����"�̴�. */
	int fitness(ArrayList<Integer> list) {
		int i, j;
		int fitness = 0;		
		if (list.size() == 1) return fitness;				// 1X1 �������� ��� 0�� ��ȯ�Ѵ�.
													
		for (i = 1; i < list.size() ; i++) {						
			for (j = 0; j < i ; j++){		
				if (list.get(j) == list.get(i))				// ���� row ���� ������ ���� ������
					fitness++;							// fitness�� 1 ������Ų��.
				if (Math.abs(list.get(i) - list.get(j)) == Math.abs(i - j))		// �밢�� ��鿡 ���� ������
					fitness++;							// fitness�� 1 ������Ų��.
			}
		}
		return fitness;	
	}
	
	
	/* ���� �ʱ� ��ġ ���� �Լ�
	 * ��� ���� ���� �ϳ��� �������� ��ġ�ϵ�,
	 * ���� �� ���� ������ ���� ������ �����Ѵ�.*/
	static ArrayList<Integer> generate(int N){
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		Random r = new Random();				// �������� ����
		for (int i=0 ; i<N; i++) {				// �� ���� ���Ͽ�
			list.add(i, r.nextInt(N));			// ���� ��ġ�� �����ϰ� �����Ѵ�.
			for(int j=0; j<i; j++) {
				if(list.get(i)==list.get(j)){	// ���� �� ���� ������ ���� �̹� �ִ� ���
					list.remove(i);				// �ٽ� ���� ��ġ�Ѵ�.
					i--;
				}
			}
		}
		return list;
	}
	
	
	/* Crossover �Լ� 
	 * 1) �������� ���� �ٸ� ��ġ array1, array2�� �����ϸ�, 
	 * 	  array1���� ���� ���Ѵ�.(�̶� ���� ������ ���̴� �������� �����Ѵ�.)
	 * 2) (array2�� �պκ�) + (array1���� ������ �κ�) + (array2�� �޺κ�)���� ���ο� ��ġ�� �����Ѵ�.
	   ## crossover ������ �ռ� ������ ���������� ������.
	 * */
	
	ArrayList<ArrayList<Integer>> Crossover(ArrayList<ArrayList<Integer>> list){
		
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();	
		Random random = new Random();						// �������� ����
		int array1, array2;									
		int p, len;
		int size = list.size();
		int n = list.get(0).size();
		ArrayList<Integer> tmp = new ArrayList<Integer>();		// crossover�� �����
		
		for(int i=0; i<population*crossover_rate; i++) {		// �ݺ� Ƚ���� ���� ������ ������ population �� 
																// crossover_rate(ũ�ν����� ����)�� ������.
			array1 = random.nextInt(size);						// ù��° ��ġ�� ���� �ε��� ��ȣ
			array2 = random.nextInt(size);						// �ι�° ��ġ�� ���� �ε��� ��ȣ
			while(array2 == array1)	array2=random.nextInt(size);	// ���õ� �� ��ġ�� ������ ��� �ٽ� �������� �����Ѵ�.
			p = random.nextInt(n-1);								// ù��° ��ġ���� ���� ���� ���� ���� ��ġ
			len = random.nextInt(n-p)+1;							// ù��° ��ġ���� ���� ���� ����
			while(p==0 && len == n-1) {							// ��ġ ��ü���� ���� ���� ���
																// Crossover�� �ǹ̰� �����Ƿ�
				p = random.nextInt(n-1);							// ���� ���� �ٽ� �������� �����Ѵ�.
				len = random.nextInt(n-p)+1;
			}

			for(int j=0; j<p; j++) {							// array2�� �պκ��� tmp�� �����Ѵ�.
				tmp.add(list.get(array1).get(j));
			}
			for(int j=0; j<len;j++) {							// array1���� ������ �κ��� tmp�� �����Ѵ�.
				tmp.add(list.get(array2).get(p+j));
			}
			for(int j=0; j<n-p-len;j++) {						// array2�� �޺κ��� tmp�� �����Ѵ�.
				tmp.add(list.get(array1).get(p+len+j));
			}
			
			result.add((ArrayList<Integer>)tmp.clone());		// �ϼ��� tmp�� result�� �����Ѵ�.
			tmp.clear();										// tmp�� �ʱ�ȭ�Ѵ�.
		}
		
		return result;
	}
	
	
	/* Mutation �Լ� 
	 * 1) �������� �ϳ��� ��ġ array�� �����Ѵ�.
	 * 2) array���� ���� ���� �����Ѵ�.(������ �� ����)
	 * 3) ���ο� ��ġ tmp�� �����Ͽ�, array���� ���õ� ���� array�� ������ �ε����� �����ϰ�,
	 * 	   ������ �κ��� �������� �����Ѵ�.
	   ## Mutation ������ �ռ� ������ �������� �� mutation_rate�� ������.*/
	ArrayList<ArrayList<Integer>>  Mutation(ArrayList<ArrayList<Integer>> list) {
		
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		Random random = new Random();						// �������� ����
		int array;											// �θ�� ����� ��ġ�� list������ �ε��� ��
		int start, len;										// �����Ǵ� �κ��� �������� ����
		int size = list.size();								// list �ȿ� �ִ� ��ġ���� ����
		int n = list.get(0).size();							// �ϳ��� ��ġ�� ���� ���� ����
		int plus;
		int jpast;
		ArrayList<Integer> tmp = new ArrayList<Integer>();	// ���Ӱ� ������ ��ġ
		
		for (int i=0; i< population*mutation_rate; i++) {	// �����̼� ������ ���������� ������.
			array = random.nextInt(size);					
	
			start = random.nextInt(n-1);					// �����Ǵ� �κ��� ���ۿ�(������ ���� �����ϰ� �������� ����)
			len = random.nextInt(n-start)+1;				// �����Ǵ� �κ��� ����(�������� ����)
			while(start==0 && len == n-1) {					// �����Ǵ� �κ����� �θ� ��ü�� ������ ���
															// �����̼��� �ǹ̰� �����Ƿ� �ٽ� �����Ǵ� �κ��� �����Ѵ�.
				start = random.nextInt(n-1);
				len = random.nextInt(n-start)+1;
			}
			
			for(int j=0; j<len;j++) {						// �����Ǵ� �κ� ��ġ
				tmp.add(list.get(array).get(start+j));
			}
			
			for(int j=0; j<start; j++) {					// �������� ���� �κ��� ������ ������ ��ġ
															// �̶� ���� ���� ������ ���� ���� ������ �����Ѵ�.
				
				plus = random.nextInt(n);					// �������� ���� �κ��� ���� ��ġ(�������� ����)
				jpast = j;
				
				for(int k=0; k < len+j; k++) {
					if(plus == tmp.get(k)) {				// ���� �� ���� ������ ���� ���� �����Ѵٸ�
						j--; break;							// ���� ��ġ�� �ٽ� �����Ѵ�/
					}
				}
				if (jpast == j) tmp.add(0,plus);
				
			}
			
			for(int j=0; j<n-start-len;j++) {				// �������� ���� �κ��� ������ ������ ��ġ
				plus = random.nextInt(n);
				jpast = j;
				for (int k=0; k < start+len+j; k++) {		// ���� �� ���� ������ ���� ���� �����Ѵٸ� ���� ��ġ�� �ٽ� �����Ѵ�.
					if(plus == tmp.get(k)) {				
						j--; break;
					}
				}
				if (jpast == j) tmp.add(plus);
			}
			result.add((ArrayList<Integer>)tmp.clone());	// ���ο� ��ġ�� ����
			tmp.clear();									// tmp �ʱ�ȭ
		}
		
		return result;
	}
	
	
	/* Parent Selection�� ���� Tournament �Լ�
	 * ����: �Է¹��� ��ġ ����Ʈ�� ����  ��Ʈ�Ͻ� ���� ���� 20%�� ��ġ���� �ɷ�����.
	 * ����: �Է¹��� ����Ʈ�� ��ġ�鿡 ���� ��Ʈ�Ͻ� ���� ����ϸ�,
	 * 		���� ������ �̿��Ͽ� ��Ʈ�Ͻ� ���� ���� ������� ���� 20%�� ��ġ���� ��ȯ�Ѵ�.*/
	ArrayList<ArrayList<Integer>>  Tournament(ArrayList<ArrayList<Integer>> list) {
		
		nQueens nQueens = new nQueens();
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();	// ��µǴ� ����Ʈ
		int size = list.size();					// �Է� ��ġ���� ����
		int n = list.get(0).size();				// ü���� ������
		
		ArrayList<Integer> array_index = new ArrayList<Integer>();		// �ε�����(fitness�� ���� ��)
		ArrayList<Integer> array_fitness = new ArrayList<Integer>();	// fitness��(�������� ����)
		
		int fitness;
		
		array_fitness.add(nQueens.fitness(list.get(0)));		// array_fitness �ʱ� ����
		array_index.add(0);										// array_index �ʱ� ����
	
		for(int i=1; i<size; i++) {								// �Է� ��ġ�鿡 ���Ͽ�
			fitness = nQueens.fitness(list.get(i));				// fitness �� ���
			for(int j=0; j<i; j++) {							// ���� ���� �������
				if(fitness <= array_fitness.get(j)) {			// fitness ���� ���� �������
					array_fitness.add(j,fitness);				// array_fitness ��
					array_index.add(j,i);						// array_index�� �����Ѵ�.
					break;
				}
				array_fitness.add(fitness);						// fitness ����  ���� ū ��� �迭�� �޺κп� �����Ѵ�.
				array_index.add(i);
			}
		}
		
		for (int N=0; N<population*parent_rate; N++) {			// parent_rate �� ������ŭ ����Ѵ�.
			result.add((ArrayList<Integer>) list.get(array_index.get(N)).clone());
		}
		
		return result;
	}
	
	/* Genetic �˰��� ���� �Լ� 
	 * ����: 1) generate �Լ��� �̿��Ͽ� �ʱ� ��ġ ����Ʈ�� �����Ѵ�.
	 * 		2) ��ʸ�Ʈ ����� �̿��Ͽ� ���� 20%�� fitness ���� ������ ��ü���� parent�� �����,
	 * 		3) parent�� ������ crossover �� mutation �۾��� ���� ���ο� ��ġ ����Ʈ�� �����Ѵ�.
	 * 		4) fitness�� 0�� ���� ������(��� ���� ���θ� ������ �� ���� ���) �ش� ��ġ �� ���� �ð�, generation�� ����Ѵ�.
	 * 		5) generation�� 1�� �÷� �����鼭 2)~4)�� �ݺ��Ѵ�.*/
	String Genetic(int N) {
		
		String str = ">Genetic Algorithm\n";
		str = str+"population : "+ population+"\nParent_rate : "+parent_rate;
		str = str+"\nCrossover rate : "+crossover_rate +"\nMutation rate : "+mutation_rate+"\n\n";
		
		if(N < 1 || N == 2 || N == 3) {				// N�� �����̰ų� 2�Ǵ� 3�� ��� �ش� �������� �ʴ´�.
			str += "No Solution\n\n";
			return str;
		}
		
		long startTime = System.nanoTime();			// ���� �ð� ����
		nQueens nQueens = new nQueens();
		
		

		int generation = 1;						// �� �������� �˷��ִ� ��ǥ
		
		ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();		// �� ������ ��ġ���� ���� ����Ʈ
		ArrayList<ArrayList<Integer>> crossover = new ArrayList<ArrayList<Integer>>();	// crossover �����
		ArrayList<ArrayList<Integer>> mutation = new ArrayList<ArrayList<Integer>>();	// mutation �����
		ArrayList<ArrayList<Integer>> tournament = new ArrayList<ArrayList<Integer>>();	// parent �����
		
		for(int i=0; i<population; i++) {							// �ʱ� ��ġ���� ����
			list.add(nQueens.generate(N));
		}
		
		while(true) {
			tournament = nQueens.Tournament(list);							// ��ʸ�Ʈ ����� ���Ͽ� �θ� ���븦 ����
			if(nQueens.fitness(tournament.get(0)) == 0) {					// fitness�� 0�� ��ü�� �ִ� ���
				Iterator<Integer> iterator = tournament.get(0).iterator();	
				while(iterator.hasNext()) {									// �ش� ��ġ �� ���� �ð�, ���븦 ���
					str = str + iterator.next() + " ";		// �ش� ��ġ�� ���ڿ��� ����
				}
				long endTime = System.nanoTime();			// ���� �ð� ����
				str = str + "\nTotal Elapsed Time : " + (endTime - startTime)/1000000000.0;		// �ҿ� �ð�
				str = str + "\nGeneration : " + generation + "\n\n";			// �� ���븦 ���ƴ��� ���
				return str;
			}
	
			list.clear();									// ���ο� ���븦 ����� ���� ���� ���� �ʱ�ȭ
			crossover = nQueens.Crossover(tournament);		// �θ� ����� crossover �۾�
			mutation = nQueens.Mutation(tournament);		// �θ� ����� mutation �۾�
		
			for(int i=0; i<tournament.size(); i++) {		// �θ� ������ ��ġ���� list�� �Űܴ�´�.
				list.add((ArrayList<Integer>)tournament.get(i).clone());
			}
			for(int i=0; i<crossover.size(); i++) {			// crossover�� ������� list�� �Űܴ�´�.
				list.add((ArrayList<Integer>)crossover.get(i).clone());
			}
			for(int i=0; i<mutation.size(); i++) {			// mutation�� ������� list�� �Űܴ�´�.
				list.add((ArrayList<Integer>)mutation.get(i).clone());
			}
			tournament.clear();								// �θ� ���븦 �ʱ�ȭ�ϰ�,
			generation++;									// ���븦 ������Ų��.

		}
		
	}
	
	/* ���� �Լ�*/
	public static void main(String[] args) {

		int N = Integer.parseInt(args[0]);					// ù��° ����(����)
		String Address = args[1].replace("\\", "\\\\");		// �ι�° ����(�����ּ�)
		String Filename = "result"+N+".txt";
		String File = Address + "\\\\" + Filename;
		
		nQueens nQueens = new nQueens();
		String result = "";
		result = nQueens.Genetic(N);					// Genetic Algorithm ����
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
