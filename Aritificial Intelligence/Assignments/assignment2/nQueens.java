/*********************************************************/
/* 	                      인공지능 2차 과제(Hill Climbing)                */
/*			     융합전자공학부 2015001103 조윤상					*/
/********************************************************/


package nQueens_3;

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;


public class nQueens {

	
	/* Heuristic 계산 함수
	 * 여기서 Heuristic은 "서로 공격 가능한 여왕의 쌍의 개수"이다. */
	static int Heuristic(ArrayList<Integer> list) {
		int i, j;
		int heuristic = 0;		
		if (list.size() == 1) return heuristic;				// 1X1 사이즈인 경우 0을 반환한다.
													
		for (i = 1; i < list.size() ; i++) {						
			for (j = 0; j < i ; j++){		
				if (list.get(j) == list.get(i))				// 같은 row 값을 가지는 퀸이 있으면
					heuristic++;							// heuristic을 1 증가시킨다.
				if (Math.abs(list.get(i) - list.get(j)) == Math.abs(i - j))		// 대각선 방면에 퀸이 있으면
					heuristic++;							// heuristic을 1 증가시킨다.
			}
		}
		return heuristic;	
	}
	
	
	/* 퀸의 초기 위치 생성 함수
	 * 이때 각 열에 있는 퀸의 위치는 랜덤하게 지정한다.*/
	static ArrayList<Integer> generate(int N){
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		Random r = new Random();
		for (int i=0 ; i<N; i++) {		// 각 열에 대하여
			list.add(r.nextInt(N));		// 퀸의 위치를 랜덤하게 지정한다.
		}
		return list;
	}
	
	
	/* HillClimbing 함수
	 * 방식:	1) generate 메소드를 이용하여 초기 위치를 랜덤으로 설정한다.
	 * 		2) 초기 위치에 따른 휴리스틱 값을 계산 후 저장한다.
	 * 		3) 한 열에 대하여 퀸의 위치를 한칸씩 바꾸어 보면서 휴리스틱을 계산하고,
	 *         이 값이 저장된 휴리스틱 값보다 작으면 해당 체스판 및 휴리스틱을 새로 저장한다.
	 *   	4) 모든 열에 대하여 3) 과정을 수행한다.
	 *   	5) 체스판의 모든 칸에 대하여 4) 과정을 수행하였음에도 휴리스틱 값이 
	 *         초기와 그대로인 경우, Local optimum에 해당한다고 보고, 
	 *         Restart한다.(새롭게 체스판을 생성하여 2)부터 다시 진행한다.)
	 *   	6) 휴리스틱이 0인 경우(모든 퀸이 서로를 공격할 수 없는 위치에 있는 경우)
	 *   	      해당 체스판 및 소요 시간, Restart 횟수를 문자열에 저장하여 반환한다.*/
	
	static String HillClimbing(int N) {				// N : 체스판의 사이즈 결정
		
		long startTime = System.nanoTime();			// 시작 시각 측정
		
		String str = ">Hill Climbing\n";
		
		if(N < 1 || N == 2 || N == 3) {				// N이 음수이거나 2또는 3인 경우 해는 존재하지 않는다.
			str += "No Solution\n\n";
			return str;
		}

		ArrayList<Integer> best = new ArrayList<Integer>();	// 최선의 퀸 배치
		ArrayList<Integer> tmp = new ArrayList<Integer>();	// 탐색할 퀸 배치
		
		tmp = generate(N);							// 초기위치 생성
		best = (ArrayList<Integer>)tmp.clone();		
		
		int heuristic = Heuristic(tmp);				// 초기 휴리스틱 값
		int tmpheuristic = Heuristic(tmp);
		int countRestart = 0;
		int heuristic_before;					// Local optimal 에 빠졌는지 체크하는 인자
		
		while(true) {
			heuristic_before = heuristic;		// Hill Climb 시작 전, 현재의 휴리스틱을 저장한다.

			if(heuristic == 0) {			// 생성된 queen의 초기 위치가 처음부터 조건을 만족할 경우
				Iterator<Integer> iterator = tmp.iterator();
				while(iterator.hasNext()) {
					str = str + iterator.next() + " ";		// 해당 위치를 문자열에 저장
				}
				long endTime = System.nanoTime();			// 종료 시각 측정
				str = str + "\nTotal Elapsed Time : " + (endTime - startTime)/1000000000.0;		// 소요 시간
				str = str + "\nNumber of Restart : " + countRestart + "\n\n";			// Restart 횟수
				return str;						// 문자열 반환
			}
			
			for(int i = 0; i<N; i++) {						// 모든 열에 대하여 다음을 반복한다.
				for(int j = 0; j<N; j++) {
					tmp.set(i,j);							// tmp의 i번째 원소를 1에서 N까지 바꾸어 본다.
					tmpheuristic = Heuristic(tmp);			// 휴리스틱을 새롭게 계산한다.
					if(tmpheuristic < heuristic) {			// 기존 휴리스틱보다 새로운 휴리스틱이 작으면
						best = (ArrayList<Integer>)tmp.clone();	// 해당 위치정보를 next에 저장하고
						heuristic = tmpheuristic;			// 휴리스틱 값을 업데이트한다.
						if(tmpheuristic == 0) {				// 휴리스틱이 0인 경우(조건을 만족하는 경우)
							Iterator<Integer> iterator = best.iterator();
							while(iterator.hasNext()) {
								str = str + iterator.next() + " ";		// 해당 위치를 문자열에 저장한다.
							}
							long endTime = System.nanoTime();			// 종료 시각을 측정
							str = str + "\nTotal Elapsed Time : " + (endTime - startTime)/1000000000.0;	// 소요 시간
							str = str + "\nNumber of Restart : " + countRestart + "\n\n";	// 재시작 횟수
							return str;							// 문자열 반환
						}
					}
				}
			}
			if(heuristic_before == heuristic) {		 	// 체스판의 모든 열에 대하여 Hill Climbing 작업을 수행하여도
													// 휴리스틱 값이 변하지 않은 경우
													// Local optimum에 빠졌다고 보고, Restart 한다.
				countRestart++;						// Restart 횟수 증가
				tmp = generate(N);					// 체스판을 새롭게 생성
				heuristic = Heuristic(tmp);			// 새로운 체스판에 대한 휴리스틱
				tmpheuristic = heuristic;		
			}
		}
	}
	
	
	public static void main(String args[]) {
		
		int N = Integer.parseInt(args[0]);					// 첫번째 인자(차수)
		String Address = args[1].replace("\\", "\\\\");		// 두번째 인자(파일주소)
		String Filename = "result"+N+".txt";
		String File = Address + "\\\\" + Filename;
		

		String result = "";
		result = HillClimbing(N);					// HillClimbing 탐색 수행
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
