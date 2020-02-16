// Assignment1 : NQueens (DFS, BFS, DFID)
// 융합전자공학부 2015001103 조윤상

package nQueens;

import java.util.*;					// Queue, Stack, LinkedList
import java.io.FileWriter;			// resultN.txt에 결과물을 작성하기 위함
import java.io.IOException;			// resultN.txt에 결과물을 작성하기 위함
import java.io.File;


public class nQueens {


	/* [들어가기 전에]
	 * 
	 * 1. 체스판의 퀸들의 row 위치를 나타내는 자료구조로 LinkedList<Integer>를 사용한다(삽입 및 수정에 용이하기 때문).
	 *    이 때 i 번째 column 내부의 퀸의 row 값을 LinkedList의 i 번째 값으로 설정하며(i는 0부터 계산한다.), 
	 *    비어 있는 column에 대해서는 값을 대입하지 않는다.
	 *    가령, 4X4 체스판에 대하여 퀸의 위치가 다음과 같다고 가정하자.
	 *    x x x x
	 *    o x x x
	 *    x o x x
	 *    x x x x
	 *    이 경우 해당 체스판의 퀸의 상태를 나타내면 [1, 2] 가 된다.
	 *    
	 * 2. BFS의 경우 자료구조로 큐를 사용하며, DFS 및 DFID의 경우 자료구조로 스택을 사용한다.
	 *    이때 각각의 큐 또는 스택에 들어가는 자료형은 앞서 1.에서 설명하였던 LinkedList<Integer>에 해당한다.
	 *
	 * 3. 체스판의 상태를 인수로 받아, 해당 체스판이 정답에 해당하는 지를 판별하는 CheckSafe 메소드를 구성하였다.
	 * 	  BFS, DFS 및 DFID는 탐색을 수행할 때 해당 메소드를 사용한다.
	 * 
	 * 4. BFS, DFS 및 DFID 메소드는 모두 문자열을 반환한다.
	 *    탐색 수행 도중 해를 발견한 경우 해당 해와 수행된 시간을 알려주는 문자열을 반환하며,
	 *    해를 발견하는 것을 실패한 경우 No Solution 이라는 메세지와 수행된 시간을 나타내는 문자열을 반환한다.
	 *    
	 * 5. main 메소드에서는 Argument로 N(차수) 및 결과 출력 파일의 절대 경로를 입력받으며,
	 *    DFS, BFS 및 DFID 메소드를 수행하여 얻은 결과값을 해당 경로의 resultN.txt파일에 작성한다. 
	 *
	 * */
	
	
	
	/* CheckSafe 메소드
	 * 목적: 체스판의 상태정보를 입력받고, 이것이 찾고자 하는 해에 해당하는지를 판별한다.
	 * 절차: 1) 퀸의 개수가 N보다 작은지 판별한다.(N보다 작으면 찾고자 하는 해가 될 수 없다.)
	 *      2) 퀸의 개수가 N에 해당할 경우, 각 퀸이 이전 열들의 퀸들에 대해 안전한지를 확인한다.
	 *      3) 서로 공격할 수 있는 퀸의 집합이 하나라도 있을 경우 false를, 모든 퀸이 안전한 경우 true를 반환한다.
	 *      
	 *         
	 * */
	public boolean CheckSafe(LinkedList<Integer> list, int N) {
		int i, j;
		
		if (list.size() < N)								// 퀸의 개수가 N보다 작은지 판별
			return false;
		
															// 퀸의 개수가 N개일 경우,
		for (i = 1; i < N ; i++) {							// 각 퀸이 이전 퀸들에 대해 안전한지 판별
			for (j = 0; j < i ; j++){		
				if (list.get(j) == list.get(i))				// 같은 row 값을 가지는 퀸이 있는지 판별
					return false;
				if (list.get(j) == list.get(i) + i - j)		// 아래쪽 대각선 방면에 또다른 퀸이 있는지 판별
					return false;
				if (list.get(j) == list.get(i) - i + j)		// 위쪽 대각선 방면에 또다른 퀸이 있는지 판별
					return false;
			}
		}
		return true;										// 모든 퀸이 안전하면(해가 존재하면) true를 반환
	}
	
	
	/* BFS 메소드 
	 * 목적: NXN 체스판에 대하여 BFS 작업을 수행한 결과값을 출력한다.
	 * 절차: 1) 정수를 저장하는 연결 리스트 및 해당 리스트를 저장하는 큐를 생성한다.
	 *      2) 큐에 빈 리스트 1개를 저장한다(퀸이 하나도 없는 처음의 체스판 상태)
	 *      3) 큐에서 원소(리스트)를 꺼내어 해당 원소가 찾는 해에 해당하는지를 판별하고, 
	 *         찾는 해가 아닐 경우 expand 하여 큐에 추가한다.
	 *         (탐색을 수행한 리스트의 원소 개수가 N보다 작음을 검사하여 조건을 만족하면,
	 *         	 해당 리스트에 각각 0 ~ (N-1)을 추가한 N개의 새로운 리스트 생성하여 큐에 저장한다.)
	 *      4) 큐에 저장된 원소가 없을 때까지 3)의 과정을 반복한다.
	 * */
	
	String BFS(int N) {
		
		long startTime = System.nanoTime();										// 시작 시각 측정
		String str_bfs = "";													// 반환을 위한 문자열 생성
		if(N < 1) {																// N이 1보다 작으면(애초에 체스판이 형성될 수 없으면)
			long endTime = System.nanoTime();									// 종료 시각을 측정하고
			str_bfs = str_bfs + ">BFS\nNo Solution\nTime : " + (endTime - startTime)/1000000000.0 + "\n\n";		// 소요 시간을 측정하여 문자열에 저장한다.
			return str_bfs;														// 문자열을 반환한다.
		}
		
		str_bfs += ">BFS \n";
		int i;
		Queue<LinkedList<Integer>> q = new LinkedList<LinkedList<Integer>>();		// 연결 리스트를 원소로 가지는 큐 생성
		LinkedList<Integer> container = new LinkedList<Integer>();					// 정수를 원소로 가지는 연결 리스트 생성
		q.add((LinkedList<Integer>)container.clone());								// 큐에 빈 리스트([])를 저장 : 퀸이 0개인 체스판


		while(!q.isEmpty()) {														// 큐에 원소가 없을 때까지 반복			
		
		
				container = q.remove();											// 큐의 원소를 하나 꺼낸다.
				if(CheckSafe(container, N)) {									// 꺼낸 원소(리스트)가 찾는 해에 해당하는지를 판별하여,
																				// 찾는 해에 해당하면 Location을 문자열에 저장한다.
					Iterator<Integer> iterator = container.iterator();
					str_bfs += "Location : ";
					while(iterator.hasNext()) {
						str_bfs = str_bfs + iterator.next() + " ";
					}
					long endTime = System.nanoTime();												// 종료 시각을 측정하여
					str_bfs = str_bfs + "\nTime : " + (endTime - startTime)/1000000000.0 + "\n\n";	// 작업 수행시간을 계산하여 문자열에 저장한다.

					return str_bfs;																	// 문자열을 반환한다.
				}
			
				if(container.size() < N) {										// 큐에서 꺼낸 원소(리스트)의 사이즈가 N보다 작을 경우(퀸의 개수가 부족한 경우)
					for (i = 0; i < N ; i++) {									// 해당 리스트에 각각 0 ~ (N-1) 을 추가한 N개의 새로운 리스트를 큐에 저장한다.
						container.addLast(i);
						q.add((LinkedList<Integer>)container.clone());
						container.removeLast();
					}
				}		
			}
		
																										// 큐의 모든 원소에 대하여 탐색을 수행하였음에도 해를 찾지 못한 경우,
			long endTime = System.nanoTime();															// 종료 시각을 측정하고
			str_bfs = str_bfs + "No Solution\nTime : " + (endTime - startTime)/1000000000.0 + "\n\n";	// 해를 찾지 못하였다는 메세지와 함께 소요시간을 문자열에 저장한다.

		return str_bfs;															// 문자열을 반환한다.
	}
	
	
	
	/* DFS 메소드 
	 * 목적: NXN 체스판에 대하여 DFS 작업을 수행한 결과값을 출력한다.
	 * 절차: 1) 정수를 저장하는 연결 리스트 및 해당 리스트를 저장하는 스택을 생성한다.
	 *      2) 스택에 빈 리스트 1개를 저장한다(퀸이 하나도 없는 처음의 체스판 상태)
	 *      3) 스택에서 원소(리스트)를 꺼내어 해당 원소가 찾는 해에 해당하는지를 판별하고, 
	 *         찾는 해가 아닐 경우 expand 하여 스택에 추가한다.
	 *         (탐색을 수행한 리스트의 원소 개수가 N보다 작음을 검사하여 조건을 만족하면,
	 *         	 해당 리스트에 각각 0 ~ (N-1)을 추가한 N개의 새로운 리스트 생성하여 스택에 저장한다.)
	 *      4) 스택에 저장된 원소가 없을 때까지 3)의 과정을 반복한다.
	 * */
	
	
	String DFS(int N) {
		long startTime = System.nanoTime();										// 시작 시각 측정
		String str_dfs = "";													// 반환을 위한 문자열 생성
		if(N < 1) {																// N이 1보다 작으면(애초에 체스판이 형성될 수 없으면)
			long endTime = System.nanoTime();									// 종료 시각을 측정하고
			str_dfs = str_dfs + ">DFS\nNo Solution\nTime : " + (endTime - startTime)/1000000000.0 + "\n\n";		// 소요 시간을 측정하여 문자열에 저장한다.
			return str_dfs;														// 문자열을 반환한다.
		}
		str_dfs += ">DFS \n";
		int i;
		Stack<LinkedList<Integer>> s = new Stack<LinkedList<Integer>>();		// 연결 리스트를 원소로 가지는 스택 생성
		LinkedList<Integer> container = new LinkedList<Integer>();				// 정수를 원소로 가지는 연결 리스트 생성
		
		s.push((LinkedList<Integer>)container.clone());							// 스택에 빈 리스트([])를 저장 : 퀸이 0개인 체스판
			
		while(!s.isEmpty()) {													// 스택에 원소가 없을 때까지 반복
				
			container = s.pop();												// 스택의 원소를 하나 꺼낸다.
			if(CheckSafe(container, N)) {										// 꺼낸 원소가 찾는 해에 해당한 경우
				Iterator<Integer> iterator = container.iterator();
				str_dfs += "Location : ";		
				while(iterator.hasNext()) {										// 퀸의 위치를 문자열에 저장한다.
					str_dfs = str_dfs + iterator.next() + " ";
				}
				long endTime = System.nanoTime();								// 종료 시각을 측정한다.
				str_dfs = str_dfs + "\nTime : " + (endTime - startTime)/1000000000.0 + "\n\n";		// 소요 시간을 계산하여 문자열에 저장한다.
				return str_dfs;													// 문자열을 반환한다.
			}
			
			if(container.size() < N) {											// 스택에서 꺼낸 원소(리스트)의 사이즈가 N보다 작은 경우(퀸의 개수가 부족한 경우)
				for (i = 0; i < N ; i++) {										// 해당 리스트에 각각 0 ~ (N-1) 을 추가한 N개의 새로운 리스트를 스택에 저장한다.
					container.addLast(i);
					s.push((LinkedList<Integer>)container.clone());
					container.removeLast();
				}
			}		
		}		
																				// 큐의 모든 원소에 대하여 탐색을 수행하였음에도 해를 찾지 못한 경우,
		long endTime = System.nanoTime();										// 종료 시각을 측정하고,
		str_dfs = str_dfs + "No Solution\nTime : " + (endTime - startTime)/1000000000.0 + "\n\n";	// 소요 시간을 계산하여 문자열에 저장한다.

		return str_dfs;															// 문자열을 반환한다.
		
	}

	
	
	/* DFID 메소드 
	 * 목적: NXN 체스판에 대하여 DFID 작업을 수행한 결과값을 출력한다.
	 * 절차:	1 ~ N 의 limit에 대하여 다음 작업을 수행한다(각 limit에 대한 DFS 탐색 수행).
	 * 
	 * 		1) 정수를 저장하는 연결 리스트 및 해당 리스트를 저장하는 스택을 생성한다.
	 *      2) 스택에 빈 리스트 1개를 저장한다(퀸이 하나도 없는 처음의 체스판 상태)
	 *      3) 스택에서 원소(리스트)를 꺼내어 해당 원소가 찾는 해에 해당하는지를 판별하고, 
	 *         찾는 해가 아닐 경우 expand 하여 스택에 추가한다.
	 *         (탐색을 수행한 리스트의 원소 개수가 limit보다 작은지 검사하여 조건을 만족하면,
	 *         	 해당 리스트에 각각 0 ~ (N-1)을 추가한 N개의 새로운 리스트 생성하여 스택에 저장한다.)
	 *      4) 스택에 저장된 원소가 없을 때까지 3)의 과정을 반복한다.
	 * */
	
	String DFID(int N) {
		
		long startTime = System.nanoTime();										// 시작 시각 측정
		String str_dfid = "";													// 반환을 위한 문자열 생성
		if(N < 1) {																// N이 1보다 작으면(애초에 체스판이 형성될 수 없으면)
			long endTime = System.nanoTime();									// 종료 시각을 측정하고
			str_dfid = str_dfid + ">DFID\nNo Solution\nTime : " + (endTime - startTime)/1000000000.0 + "\n\n";		// 소요 시간을 측정하여 문자열에 저장한다.
			return str_dfid;														// 문자열을 반환한다.
		}
		str_dfid += ">DFID\n";
		int i, limit;

		for (limit = 1 ; limit <= N ; limit++) {								// 1부터 N까지의 limit에 대한 DFS 탐색을 수행한다.
			Stack<LinkedList<Integer>> s = new Stack<LinkedList<Integer>>();	// 연결 리스트를 원소로 가지는 스택 생성
			LinkedList<Integer> container = new LinkedList<Integer>();			// 정수를 원소로 가지는 연결 리스트 생성
	
			s.push((LinkedList<Integer>)container.clone());						// 스택에 빈 리스트([])를 저장 : 퀸이 0개인 체스판
					
			while(!s.isEmpty()) {												// 스택에 원소가 없을 때까지 반복
					
				container = s.pop();											// 스택의 원소를 하나 꺼낸다.
				if(CheckSafe(container, N)) {									// 꺼낸 원소가 찾는 해에 해당하는 경우
					Iterator<Integer> iterator = container.iterator();
					str_dfid += "Location : ";
					while(iterator.hasNext()) {									// 퀸의 위치를 문자열에 저장한다.
						str_dfid = str_dfid + iterator.next() + " ";
					}
					long endTime = System.nanoTime();							// 종료 시각을 측정한다.
					str_dfid = str_dfid + "\nTime : " + (endTime - startTime)/1000000000.0 + "\n\n";	// 소요 시간을 계산하여 문자열에 저장한다.
					return str_dfid;											// 문자열을 반환한다.
				}
				
				if(container.size() < limit) {									// 스택에서 꺼낸 원소(리스트)의 사이즈가 limit보다 작은 경우(퀸의 개수가 부족한 경우)							
					for (i = 0; i < N ; i++) {									// 해당 리스트에 각각 0 ~ (N-1) 을 추가한 N개의 새로운 리스트를 스택에 저장한다.
						container.addLast(i);
						s.push((LinkedList<Integer>)container.clone());
						container.removeLast();
					}
				}		
			}
			
		}
																				// 모든 limit에 대하여 작업을 수행하였는데도 해를 찾지 못한 경우
		long endTime = System.nanoTime();										// 종료 시각을 측정한다.
		str_dfid = str_dfid + "No Solution\nTime : " + (endTime - startTime)/1000000000.0 + "\n\n";		// 소요 시간을 계산하여 문자열에 저장한다.
		return str_dfid;														// 문자열을 반환한다.
		
	}
	
	
	/* 메인 메소드 
	 * 절차: 1) N과 resultN.txt의 경로값을 Argument로 받는다
	 * 		2) NXN에 대한 DFS, BFS 및 DFID 탐색을 수행하여 그 결과를 입력받은 경로의 resultN.txt에 업데이트한다.
	 * */
	
	public static void main(String args[]) {

		int N = Integer.parseInt(args[0]);					// N을 정수형으로 변환한다.
		String Address = args[1].replace("\\", "\\\\");		// 올바른 경로명을 만들기 위하여, 입력받은 경로 문자열에 대하여 replace 작업을 수행한다.
															// 자바에서 파일에 접근할 경우 경로명은 C:\\AI\\Assignment1\\resultN.txt 와 같이 \\ 를 사용하나,
															// 윈도우에서는 파일 경로를 C:\AI\Assignment1\resultN.txt와 같이 \dmf 사용하므로
															// \를 \\으로 변환시켜 주어야 한다.
		String Filename = "result" + args[0] + ".txt";
		String textfile = Address + "\\\\" + Filename;			// 쓰기 작업을 수행할 txt파일의 최종 경로

		File file = new File(textfile);						// 입력받은 위치에 resultN.txt 파일 생성
		nQueens NQueens = new nQueens();					// nQueens 클래스를 생성한다.
		
		String str_bfs, str_dfs, str_dfid;
		String str;

		str_dfs = NQueens.DFS(N);							// NXN 체스판에 대한 DFS 작업을 수행하여 결과를 저장한다.
		str_bfs = NQueens.BFS(N);							// NXN 체스판에 대한 BFS 작업을 수행하여 결과를 저장한다.
		str_dfid = NQueens.DFID(N);							// NXN 체스판에 대한 DFID 작업을 수행하여 결과를 저장한다.
		
		str = str_dfs + str_bfs + str_dfid;					// 파일에 작성할 최종 결과물
	

		FileWriter fw = null;								// FileWriter 변수를 생성한다.
		try {
			fw = new FileWriter(file, false);				// true: 이어 쓰기			false: 이전의 내용을 삭제하고 새로 쓰기			
			fw.write(str);									// DFS, BFS, DFID 작업을 수행한 결과물을 write한다.
			fw.flush();										// 버퍼를 비운다.							
		} catch(IOException e) {							// 오류가 발생할 경우
			e.printStackTrace();							// 오류에 대한 자세한 정보를 화면에 출력한다.
		} finally {
			try {
				if(fw != null) fw.close();					// I/O 스트림을 닫는다
			} catch(IOException e) {						// 오류가 발생할 경우
				e.printStackTrace();						// 오류에 대한 자세한 정보를 화면에 출력한다.
			}
		}
		
	}
	
}
