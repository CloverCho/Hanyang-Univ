/*********************************************************/
/* 	                      인공지능 3차 과제(Genetic Algorithm)                */

package nQueens_4;

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

public class nQueens {

	
	static int population = 100;							// 한 세대의 인구
	static double parent_rate = 0.2;						// 부모 세대의 비율						
	static double crossover_rate = 0.4;						// crossover의 비율
	static double mutation_rate = 0.4;						// mutation의 비율
	
	
	/* Fitness 계산 함수
	 * 여기서 Fitness은 "서로 공격 가능한 여왕의 쌍의 개수"이다. */
	int fitness(ArrayList<Integer> list) {
		int i, j;
		int fitness = 0;		
		if (list.size() == 1) return fitness;				// 1X1 사이즈인 경우 0을 반환한다.
													
		for (i = 1; i < list.size() ; i++) {						
			for (j = 0; j < i ; j++){		
				if (list.get(j) == list.get(i))				// 같은 row 값을 가지는 퀸이 있으면
					fitness++;							// fitness을 1 증가시킨다.
				if (Math.abs(list.get(i) - list.get(j)) == Math.abs(i - j))		// 대각선 방면에 퀸이 있으면
					fitness++;							// fitness을 1 증가시킨다.
			}
		}
		return fitness;	
	}
	
	
	/* 퀸의 초기 위치 생성 함수
	 * 모든 열에 퀸을 하나씩 랜덤으로 배치하되,
	 * 같은 행 값을 가지는 퀸이 없도록 배정한다.*/
	static ArrayList<Integer> generate(int N){
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		Random r = new Random();				// 랜덤변수 생성
		for (int i=0 ; i<N; i++) {				// 각 열에 대하여
			list.add(i, r.nextInt(N));			// 퀸의 위치를 랜덤하게 지정한다.
			for(int j=0; j<i; j++) {
				if(list.get(i)==list.get(j)){	// 같은 행 값을 가지는 퀸이 이미 있는 경우
					list.remove(i);				// 다시 퀸을 배치한다.
					i--;
				}
			}
		}
		return list;
	}
	
	
	/* Crossover 함수 
	 * 1) 랜덤으로 서로 다른 배치 array1, array2를 선택하며, 
	 * 	  array1에서 값을 취한다.(이때 시작 지점과 길이는 랜덤으로 설정한다.)
	 * 2) (array2의 앞부분) + (array1에서 선택한 부분) + (array2의 뒷부분)으로 새로운 배치를 구성한다.
	   ## crossover 비율은 앞서 정의한 전역변수를 따른다.
	 * */
	
	ArrayList<ArrayList<Integer>> Crossover(ArrayList<ArrayList<Integer>> list){
		
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();	
		Random random = new Random();						// 랜덤변수 생성
		int array1, array2;									
		int p, len;
		int size = list.size();
		int n = list.get(0).size();
		ArrayList<Integer> tmp = new ArrayList<Integer>();		// crossover의 결과물
		
		for(int i=0; i<population*crossover_rate; i++) {		// 반복 횟수는 전역 변수로 설정한 population 및 
																// crossover_rate(크로스오버 비율)을 따른다.
			array1 = random.nextInt(size);						// 첫번째 배치에 대한 인덱스 번호
			array2 = random.nextInt(size);						// 두번째 배치에 대한 인덱스 번호
			while(array2 == array1)	array2=random.nextInt(size);	// 선택된 두 배치가 동일할 경우 다시 랜덤으로 선택한다.
			p = random.nextInt(n-1);								// 첫번째 배치에서 취할 값의 시작 열의 위치
			len = random.nextInt(n-p)+1;							// 첫번째 배치에서 취할 값의 길이
			while(p==0 && len == n-1) {							// 배치 전체에서 값을 취할 경우
																// Crossover의 의미가 없으므로
				p = random.nextInt(n-1);							// 취할 값을 다시 랜덤으로 결정한다.
				len = random.nextInt(n-p)+1;
			}

			for(int j=0; j<p; j++) {							// array2의 앞부분을 tmp에 저장한다.
				tmp.add(list.get(array1).get(j));
			}
			for(int j=0; j<len;j++) {							// array1에서 선택한 부분을 tmp에 저장한다.
				tmp.add(list.get(array2).get(p+j));
			}
			for(int j=0; j<n-p-len;j++) {						// array2의 뒷부분을 tmp에 저장한다.
				tmp.add(list.get(array1).get(p+len+j));
			}
			
			result.add((ArrayList<Integer>)tmp.clone());		// 완성된 tmp를 result에 저장한다.
			tmp.clear();										// tmp를 초기화한다.
		}
		
		return result;
	}
	
	
	/* Mutation 함수 
	 * 1) 랜덤으로 하나의 배치 array를 선택한다.
	 * 2) array에서 취할 값을 선택한다.(시작점 및 길이)
	 * 3) 새로운 배치 tmp를 생성하여, array에서 선택된 값을 array와 동일한 인덱스에 설정하고,
	 * 	   나머지 부분은 랜덤으로 설정한다.
	   ## Mutation 비율은 앞서 정의한 전역변수 값 mutation_rate를 따른다.*/
	ArrayList<ArrayList<Integer>>  Mutation(ArrayList<ArrayList<Integer>> list) {
		
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		Random random = new Random();						// 랜덤변수 생성
		int array;											// 부모로 사용할 배치의 list에서의 인덱스 값
		int start, len;										// 유전되는 부분의 시작점과 길이
		int size = list.size();								// list 안에 있는 배치들의 개수
		int n = list.get(0).size();							// 하나의 배치에 대한 열의 개수
		int plus;
		int jpast;
		ArrayList<Integer> tmp = new ArrayList<Integer>();	// 새롭게 생성할 배치
		
		for (int i=0; i< population*mutation_rate; i++) {	// 뮤테이션 비율은 전역변수를 따른다.
			array = random.nextInt(size);					
	
			start = random.nextInt(n-1);					// 유전되는 부분의 시작열(마지막 열을 제외하고 랜덤으로 결정)
			len = random.nextInt(n-start)+1;				// 유전되는 부분의 길이(랜덤으로 결정)
			while(start==0 && len == n-1) {					// 유전되는 부분으로 부모 전체를 선택할 경우
															// 뮤테이션의 의미가 없으므로 다시 유전되는 부분을 결정한다.
				start = random.nextInt(n-1);
				len = random.nextInt(n-start)+1;
			}
			
			for(int j=0; j<len;j++) {						// 유전되는 부분 배치
				tmp.add(list.get(array).get(start+j));
			}
			
			for(int j=0; j<start; j++) {					// 유전되지 않은 부분을 랜덤의 값으로 배치
															// 이때 같은 행을 가지는 퀸의 쌍이 없도록 설정한다.
				
				plus = random.nextInt(n);					// 유전되지 않은 부분의 퀸의 위치(랜덤으로 결정)
				jpast = j;
				
				for(int k=0; k < len+j; k++) {
					if(plus == tmp.get(k)) {				// 같은 행 값을 가지는 퀸의 쌍이 존재한다면
						j--; break;							// 퀸의 위치를 다시 결정한다/
					}
				}
				if (jpast == j) tmp.add(0,plus);
				
			}
			
			for(int j=0; j<n-start-len;j++) {				// 유전되지 않은 부분을 랜덤의 값으로 배치
				plus = random.nextInt(n);
				jpast = j;
				for (int k=0; k < start+len+j; k++) {		// 같은 행 값을 가지는 퀸의 쌍이 존재한다면 퀸의 위치를 다시 결정한다.
					if(plus == tmp.get(k)) {				
						j--; break;
					}
				}
				if (jpast == j) tmp.add(plus);
			}
			result.add((ArrayList<Integer>)tmp.clone());	// 새로운 배치를 저장
			tmp.clear();									// tmp 초기화
		}
		
		return result;
	}
	
	
	/* Parent Selection을 위한 Tournament 함수
	 * 목적: 입력받은 배치 리스트에 대해  피트니스 값이 상위 20%인 배치들을 걸러낸다.
	 * 원리: 입력받은 리스트의 배치들에 관해 피트니스 값을 계산하며,
	 * 		삽입 정렬을 이용하여 피트니스 값이 작은 순서대로 상위 20%의 배치들을 반환한다.*/
	ArrayList<ArrayList<Integer>>  Tournament(ArrayList<ArrayList<Integer>> list) {
		
		nQueens nQueens = new nQueens();
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();	// 출력되는 리스트
		int size = list.size();					// 입력 배치들의 개수
		int n = list.get(0).size();				// 체스판 사이즈
		
		ArrayList<Integer> array_index = new ArrayList<Integer>();		// 인덱스값(fitness가 작은 순)
		ArrayList<Integer> array_fitness = new ArrayList<Integer>();	// fitness값(내림차순 정렬)
		
		int fitness;
		
		array_fitness.add(nQueens.fitness(list.get(0)));		// array_fitness 초기 조건
		array_index.add(0);										// array_index 초기 조건
	
		for(int i=1; i<size; i++) {								// 입력 배치들에 대하여
			fitness = nQueens.fitness(list.get(i));				// fitness 값 계산
			for(int j=0; j<i; j++) {							// 삽입 정렬 방식으로
				if(fitness <= array_fitness.get(j)) {			// fitness 값이 작은 순서대로
					array_fitness.add(j,fitness);				// array_fitness 및
					array_index.add(j,i);						// array_index를 설정한다.
					break;
				}
				array_fitness.add(fitness);						// fitness 값이  가장 큰 경우 배열의 뒷부분에 삽입한다.
				array_index.add(i);
			}
		}
		
		for (int N=0; N<population*parent_rate; N++) {			// parent_rate 의 비율만큼 출력한다.
			result.add((ArrayList<Integer>) list.get(array_index.get(N)).clone());
		}
		
		return result;
	}
	
	/* Genetic 알고리즘 실행 함수 
	 * 절차: 1) generate 함수를 이용하여 초기 배치 리스트를 생성한다.
	 * 		2) 토너먼트 방식을 이용하여 상위 20%의 fitness 값을 가지는 개체들을 parent로 남기고,
	 * 		3) parent를 가지고 crossover 및 mutation 작업을 통해 새로운 배치 리스트를 생성한다.
	 * 		4) fitness가 0인 것이 있으면(모든 퀸이 서로를 공격할 수 없는 경우) 해당 배치 및 동작 시간, generation을 출력한다.
	 * 		5) generation을 1씩 늘려 나가면서 2)~4)를 반복한다.*/
	String Genetic(int N) {
		
		String str = ">Genetic Algorithm\n";
		str = str+"population : "+ population+"\nParent_rate : "+parent_rate;
		str = str+"\nCrossover rate : "+crossover_rate +"\nMutation rate : "+mutation_rate+"\n\n";
		
		if(N < 1 || N == 2 || N == 3) {				// N이 음수이거나 2또는 3인 경우 해는 존재하지 않는다.
			str += "No Solution\n\n";
			return str;
		}
		
		long startTime = System.nanoTime();			// 시작 시각 측정
		nQueens nQueens = new nQueens();
		
		

		int generation = 1;						// 몇 세대인지 알려주는 지표
		
		ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();		// 한 세대의 배치들을 담을 리스트
		ArrayList<ArrayList<Integer>> crossover = new ArrayList<ArrayList<Integer>>();	// crossover 결과물
		ArrayList<ArrayList<Integer>> mutation = new ArrayList<ArrayList<Integer>>();	// mutation 결과물
		ArrayList<ArrayList<Integer>> tournament = new ArrayList<ArrayList<Integer>>();	// parent 결과물
		
		for(int i=0; i<population; i++) {							// 초기 배치들을 생성
			list.add(nQueens.generate(N));
		}
		
		while(true) {
			tournament = nQueens.Tournament(list);							// 토너먼트 방식을 통하여 부모 세대를 결정
			if(nQueens.fitness(tournament.get(0)) == 0) {					// fitness가 0인 개체가 있는 경우
				Iterator<Integer> iterator = tournament.get(0).iterator();	
				while(iterator.hasNext()) {									// 해당 배치 및 동작 시간, 세대를 출력
					str = str + iterator.next() + " ";		// 해당 위치를 문자열에 저장
				}
				long endTime = System.nanoTime();			// 종료 시각 측정
				str = str + "\nTotal Elapsed Time : " + (endTime - startTime)/1000000000.0;		// 소요 시간
				str = str + "\nGeneration : " + generation + "\n\n";			// 몇 세대를 거쳤는지 출력
				return str;
			}
	
			list.clear();									// 새로운 세대를 만들기 위해 기존 정보 초기화
			crossover = nQueens.Crossover(tournament);		// 부모 세대로 crossover 작업
			mutation = nQueens.Mutation(tournament);		// 부모 세대로 mutation 작업
		
			for(int i=0; i<tournament.size(); i++) {		// 부모 세대의 배치들을 list에 옮겨닮는다.
				list.add((ArrayList<Integer>)tournament.get(i).clone());
			}
			for(int i=0; i<crossover.size(); i++) {			// crossover의 결과물을 list에 옮겨닮는다.
				list.add((ArrayList<Integer>)crossover.get(i).clone());
			}
			for(int i=0; i<mutation.size(); i++) {			// mutation의 결과물을 list에 옮겨닮는다.
				list.add((ArrayList<Integer>)mutation.get(i).clone());
			}
			tournament.clear();								// 부모 세대를 초기화하고,
			generation++;									// 세대를 증가시킨다.

		}
		
	}
	
	/* 메인 함수*/
	public static void main(String[] args) {

		int N = Integer.parseInt(args[0]);					// 첫번째 인자(차수)
		String Address = args[1].replace("\\", "\\\\");		// 두번째 인자(파일주소)
		String Filename = "result"+N+".txt";
		String File = Address + "\\\\" + Filename;
		
		nQueens nQueens = new nQueens();
		String result = "";
		result = nQueens.Genetic(N);					// Genetic Algorithm 수행
		FileWriter fw = null;

		try {
			fw = new FileWriter(File, true);		
			fw.write(result);						// 해당 위치에 resultN.txt 생성 후 결과 작성
			fw.flush();								
		} catch(IOException e) {					// 입출력 에러 발생시
			e.printStackTrace();
		} finally {
			try {
				if(fw != null) fw.close();			
			} catch(IOException e) {				// 입출력 에러 발생시
				e.printStackTrace();
			}
		}
		
	}
		
}
