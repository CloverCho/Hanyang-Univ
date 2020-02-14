# [Assignment3 : Genetic Algorithm]
<h4>융합전자공학부 2015001103 조윤상</h4><br /><br />

<h3>1. 목표</h3>
Genetic Algorithm 기법을 이용하여 N-Queens 문제를 해결한다.<br /><br />

<h3>2. 알고리즘 구성</h3>
본 과제에서는 다음과 같은 방식으로 Genetic Algorithm을 구현하였다.<br />
1. 기본 파라미터를 설정한다.<br />
  * Population(한 세대의 인구 수) : 100
  * parent_rate(부모 세대의 비율) : 0.2(20%)
  * crossover_rate(교차 비율) : 0.4(40%)
  * mutation_rate(돌연변이 비율) : 0.4(40%)
2. NXN 체스판에 대한 퀸의 배치를 임의의 값으로 100개 생성한다.(한 세대 생성)<br />
3. 생성된 배치들 각각에 대한 fitness 값을 계산한다.<br /> 
    이때 fitness은 각 열의 퀸의 위치 Q<sub>1</sub>, Q<sub>2</sub>, ... , Q<sub>n</sub> 에 대하여
    서로 공격 가능한 퀸의 쌍 (Q<sub>i</sub>,  Q<sub>j</sub>)  (1<span>&#8804;</span>i<span>&#60;</span>j<span>&#8804;</span>n)
    의 개수를 의미한다.  <br />
4. fitness 값을 올림차순으로 정렬했을 때 상위 20% 이하의 배치들을 부모 세대로 남긴다.(토너먼트 방식 사용)  <br />
5. 부모 세대를 가지고 교차 작업을 실시하여  새로운 퀸의 배치들을 한 세대의 40%만큼 생성한다.<br />
6. 부모 세대를 가지고 돌연변이 작업을 실시하여  새로운 퀸의 배치들을 한 세대의 40%만큼 생성한다.<br />
7. 새로운 세대에 대하여 fitness가 0인 배치가 있는지 검사하고, 이를 발견할 경우 해당 퀸의 배치 및 세대 수, 소요 시간을 출력한다.<br />
8. fitness 값이 0인 객체가 없으면 새로운 자식 세대를 생성하고, 위의 과정을 반복한다.<br />
9. 반환받은 결과값을 resultN.txt 에 출력한다.  <br /><br />

세부 메소드는 다음과 같다.  
<h5>  generate :  퀸의 초기 배치를 랜덤한 위치로 생성 </h5> 
 1) 어레이리스트 및 랜덤변수를 생성한다.<br />
```
		ArrayList<Integer> list = new ArrayList<Integer>();
		Random r = new Random();				// 랜덤변수 생성
```
<br />2) 퀸의 위치를 서로 행 값이 겹치지 않게 랜덤으로 배치한다.
```
		for (int i=0 ; i<N; i++) {				// 각 열에 대하여
			list.add(i, r.nextInt(N));			// 퀸의 위치를 랜덤하게 지정한다.
			for(int j=0; j<i; j++) {
				if(list.get(i)==list.get(j)){	// 같은 행 값을 가지는 퀸이 이미 있는 경우
					list.remove(i);				// 다시 퀸을 배치한다.
					i--;
				}
			}
		}
```
<br />3. 생성된 배치를 반환한다.
```
return list;
```
<br /><br />

<h5>  fitness :  입력받은 배치에 대한 fitness 값 반환 </h5>
1) 1X1사이즈의 체스판일 경우, fitness가 0이므로 이를 반환한다.<br />
```
		int i, j;
		int fitness = 0;		
		if (list.size() == 1) return fitness;				// 1X1 사이즈인 경우 0을 반환한다.
```
<br />
2) 같은 row값을 가지거나(좌우로 만남), 위치의 차가 인덱스의 차이와 같은(대각선으로 만남) 퀸의 쌍을 발견하면 fitness를 1증가시킨다.<br />
```
		for (i = 1; i < list.size() ; i++) {						
			for (j = 0; j < i ; j++){		
				if (list.get(j) == list.get(i))				// 같은 row 값을 가지는 퀸이 있으면
					fitness++;							// fitness을 1 증가시킨다.
				if (Math.abs(list.get(i) - list.get(j)) == Math.abs(i - j))		// 대각선 방면에 퀸이 있으면
					fitness++;							// fitness을 1 증가시킨다.
			}
		}
```
<br />
3) 탐색이 끝나면 fitness를 반환한다.<br />
```
return fitness;	
```
<br /><br />
<h5>  Crossover :  입력받은 퀸의 배치들에 대하여 교차 작업을 실행한다. </h5> 
1) 어레이리스트 및 랜덤변수를 정의한다.
```
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();	
		Random random = new Random();						// 랜덤변수 생성
```
<br />
2) 입력받은 배열 리스트들에 대하여 서로 다른 배열 두 개를 고르기 위하여, 해당 배열들의 인덱스 변수를 정의한다.
```
		int array1, array2;									
```
<br />
3) 교차작업에 사용할 배열에 대하여 추출할 부분의 시작 위치 및 길이를 정의한다.
```
		int p, len;
```
<br />
4) 입력받은 배치들의 개수 및 체스판의 사이즈를 저장하고, 결과 어레이리스트를 정의한다.
```
		int size = list.size();
		int n = list.get(0).size();
		ArrayList<Integer> tmp = new ArrayList<Integer>();		// crossover의 결과물
```
<br />
5) 본격적으로 교차 작업을 실시한다.<br /> 임의의 서로 다른 배치를 선택하고(array1,array2), array1에서 추출할 부분의 시작 위치 및 길이를 랜덤으로 결정한다.	
```
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
```
<br />
6) (array2의 앞부분) + (array1에서 선택한 부분) + (array2의 뒷부분) 에 해당하는 새로운 배치를 생성한다.
```
			for(int j=0; j<p; j++) {							// array2의 앞부분을 tmp에 저장한다.
				tmp.add(list.get(array1).get(j));
			}
			for(int j=0; j<len;j++) {							// array1에서 선택한 부분을 tmp에 저장한다.
				tmp.add(list.get(array2).get(p+j));
			}
			for(int j=0; j<n-p-len;j++) {						// array2의 뒷부분을 tmp에 저장한다.
				tmp.add(list.get(array1).get(p+len+j));
			}
```
<br />
7) 완성된 배치를 저장한다.
```
			result.add((ArrayList<Integer>)tmp.clone());		// 완성된 tmp를 result에 저장한다.
			tmp.clear();										// tmp를 초기화한다.
```
<br />		
<span>&#8251;</span>5)~7)의 작업은 population * crossover_rate 의 수만큼 행하여 진다.<br />
8) 결과물을 반환한다.
```
		return result;
```
<br /><br />

<h5>  Mutation:  입력받은 퀸의 배치들에 대하여 돌연변이 생성 작업을 실행한다. </h5> 
1) 어레이리스트 및 랜덤변수를 정의한다.
```
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		Random random = new Random();						// 랜덤변수 생성
```
<br />
2) 부모로 사용할 배치의 인덱스, 유전 부분 시작 위치 및 길이를 정의한다.
```
		int array;											// 부모로 사용할 배치의 list에서의 인덱스 값
		int start, len;										// 유전되는 부분의 시작점과 길이
```
<br />
3) 입력받은 배치들의 개수 및 체스판의 사이즈를 저장하고, 새롭게 생성할 배치 어레이리스트를 정의한다.
```
		int size = list.size();								// list 안에 있는 배치들의 개수
		int n = list.get(0).size();							// 하나의 배치에 대한 열의 개수
		int plus;
		int jpast;
		ArrayList<Integer> tmp = new ArrayList<Integer>();	// 새롭게 생성할 배치
```
<br />
4) 본격적으로 돌연변이 작업을 실시한다.<br /> 임의의 서로 배치를 선택하고(array), array에서 추출할 부분의 시작 위치 및 길이를 랜덤으로 결정한다.
```		
		for (int i=0; i< population*mutation_rate; i++) {	// 뮤테이션 비율은 전역변수를 따른다.
			array = random.nextInt(size);					
	
			start = random.nextInt(n-1);					// 유전되는 부분의 시작열(마지막 열을 제외하고 랜덤으로 결정)
			len = random.nextInt(n-start)+1;				// 유전되는 부분의 길이(랜덤으로 결정)
			while(start==0 && len == n-1) {					// 유전되는 부분으로 부모 전체를 선택할 경우
															// 뮤테이션의 의미가 없으므로 다시 유전되는 부분을 결정한다.
				start = random.nextInt(n-1);
				len = random.nextInt(n-start)+1;
			}
```
<br />
5) 유전되는 부분을 array에서 복사해 오고, 나머지 부분은 랜덤의 값으로 설정한 새로운 퀸의 배치를 생성한다.
    <br /> 이때 같은 행을 가지는 퀸의 쌍이 없도록 조정한다.
```		
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
```
<br />
6) 완성된 배치를 저장한다.
```

			result.add((ArrayList<Integer>)tmp.clone());	// 새로운 배치를 저장
			tmp.clear();									// tmp 초기화
```		
<br />
<span>&#8251;</span>4)~6)의 작업은 population * mutation_rate 의 수만큼 행하여 진다.<br />

7) 결과물을 반환한다.
```
		return result;
```
<br /><br />

<h5>Tournament:  입력받은 퀸의 배치들에 대하여 상위 20%의 배치들을 반환한다(parent).</h5> 
1) 출력될 리스트 변수 및 입력 배치들의 개수, 체스판 사이즈를 정의한다.
```
		nQueens nQueens = new nQueens();
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();	// 출력되는 리스트
		int size = list.size();					// 입력 배치들의 개수
		int n = list.get(0).size();				// 체스판 사이즈
```
<br />
2) 배치들 각각에 대한 fitness 값과 인덱스 값을 담을 어레이리스트 변수를 정의하고, 첫번째 배치에 대한 값으로 초기화한다.
```		
		ArrayList<Integer> array_index = new ArrayList<Integer>();		// 인덱스값(fitness가 작은 순)
		ArrayList<Integer> array_fitness = new ArrayList<Integer>();	// fitness값(내림차순 정렬)		
		int fitness;

		array_fitness.add(nQueens.fitness(list.get(0)));		// array_fitness 초기 조건
		array_index.add(0);										// array_index 초기 조건
```
<br />
3) 각 배치들의 fitness 값을 계산하고, 삽입 정렬을 이용하여 fitness 값이 증가하는 순으로 배치 인덱스들을 정렬한다.
```	
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
```
<br />
4) 상위 20%의 배치들만 리스트에 저장하고, 이를 출력한다.
```	
		for (int N=0; N<population*parent_rate; N++) {			// parent_rate 의 비율만큼 출력한다.
			result.add((ArrayList<Integer>) list.get(array_index.get(N)).clone());
		}
		
		return result;
```
<br /><br />

<h5>  Genetic:  nQueens 문제에 대한 유전 알고리즘을 수행한다.</h5> 
1) 초기 설정 조건을 문자열에 저장한다.
```
		String str = ">Genetic Algorithm\n";
		str = str+"population : "+ population+"\nParent_rate : "+parent_rate;
		str = str+"\nCrossover rate : "+crossover_rate +"\nMutation rate : "+mutation_rate+"\n\n";
```
<br />
2) 타당하지 않은 N값을 걸러낸다.
```		
		if(N < 1 || N == 2 || N == 3) {				// N이 음수이거나 2또는 3인 경우 해는 존재하지 않는다.
			str += "No Solution\n\n";
			return str;
		}
```
<br />
3) 시작 시각을 측정한다.
```
		long startTime = System.nanoTime();			// 시작 시각 측정
```
<br />
4) 세대 수 및 작업에 사용할 배치 리스트 변수를 정의한다.
```
		nQueens nQueens = new nQueens();
		
		int generation = 1;						// 몇 세대인지 알려주는 지표
		
		ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();		// 한 세대의 배치들을 담을 리스트
		ArrayList<ArrayList<Integer>> crossover = new ArrayList<ArrayList<Integer>>();	// crossover 결과물
		ArrayList<ArrayList<Integer>> mutation = new ArrayList<ArrayList<Integer>>();	// mutation 결과물
		ArrayList<ArrayList<Integer>> tournament = new ArrayList<ArrayList<Integer>>();	// parent 결과물
```
<br />
5) generate 함수를 이용하여 초기 퀸의 배치들을 생성한다.(이때 전역변수로 사용한 population 수만큼 생성한다.)
```	
		for(int i=0; i<population; i++) {							// 초기 배치들을 생성
			list.add(nQueens.generate(N));
		}
```
<br />
6)  Tournament 함수를 이용하여 상위 20%의 값들을 골라내고, 이 중 fitness가 0인 배치가 있는지 확인한다.<br />
     fitness가 0인 배치가 존재하면 해당 배치의 값 및 작업 시간, generation 수를 출력하며,<br />
     존재하지 않은 경우 골라낸 배치들을 다음 세대의 parent로 사용한다.
```
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
```
<br />
7) 부모 세대에 대한 교차 작업 및 돌연변이 작업을 수행한다.
```
			list.clear();									// 새로운 세대를 만들기 위해 기존 정보 초기화
			crossover = nQueens.Crossover(tournament);		// 부모 세대로 crossover 작업
			mutation = nQueens.Mutation(tournament);		// 부모 세대로 mutation 작업
```
<br />
8) 부모 세대+ 교차 작업 결과 + 돌연변이 작업 결과 로 이루어진 새로운 자식 세대를 생성한다.
```		
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
```
<br />
<span>&#8251;</span>fitness값이 0인 배치를 찾을 때까지 6)~8)의 작업을 while문에서 수행한다. <br /><br />

<h5>  main :  메인 실행 함수 </h5> 
1) argument로서 첫번째 인자로 N을, 두번째 인자로 resultN.txt파일의 경로를 입력받는다.<br />
```
		int N = Integer.parseInt(args[0]);					// 첫번째 인자(차수)
		String Address = args[1].replace("\\", "\\\\");		// 두번째 인자(파일주소)
		String Filename = "result"+N+".txt";
		String File = Address + "\\\\" + Filename;
```
<br />
2) 입력받은 차수 N에 대하여 Genetic Algorithm 방식으로 n-queens 문제를 풀어 그 결과를 얻는다.<br />		
```
		nQueens nQueens = new nQueens();
		String result = "";
		result = nQueens.Genetic(N);					// Genetic Algorithm 수행
```
<br />
3) 해당 결과를 resultN.txt 이라는 파일명으로 입력받은 경로에 작성한다.
```
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
```
<br /><br /><br />
<h3>3. 실행 결과</h3>
<h5> 실행 화면 (예: N=12일 때)</h5>< br />
<img src="/uploads/704f55a3e040e7ee4f1aaf40cf0130bc/cmd_12.PNG" width="600" height="40">
<br /><br />
<h5>실행 결과</h5>
<br />N=5 일 때</br />
<img src="/uploads/163806f2b9c053fd91bd00aea5cbe2ad/result_n_5.png" width="300" height="300">
<br />N=8 일 때</br />
<img src="/uploads/d178033c777e05e047a80a47c9ce874f/result_n_8.PNG" width="300" height="300">
<br />N=12 일 때</br />
<img src="/uploads/0aa253673da1d13d3c1f6e5d5f96fd41/result_n_12.PNG" width="300" height="300">
<br />N=15 일 때</br />
<img src="/uploads/279daea7f3a23cb20c00fc675a3f8326/result_n_15.PNG" width="300" height="300">
<br />N=20 일 때</br />
<img src="/uploads/01882d7dbbde8ec61d43882ba6f23378/result_n_20.PNG" width="300" height="300">

<br /><br /><br />
<h3>4. 결과 분석</h3>
<h5>1. population과 성능의 상관관계<br /></h5>
15X15 Queens problem에 대하여 parent_rate : 0.2, crossover_rate: 0.4, mutation_rate : 0.4 로 고정하고<br />
population을 20, 40, 60, 80, 100으로 변화시켰을 때의 결과는 아래와 같다.<br />
<img src="/uploads/e8d8dd035e3d168f3a8555026832df30/test_population.PNG" width="600" height="250">
해당 결과에서 population 수가 많을 수록 결과 도출까지 요구되는 세대 수가 적은 경향을 보이는 것을 확인할 수 있다.<br />
(다만 population이 100일 때의 요구 세대 수가 80일 때의 요구 세대보다 많은 것으로 보아 무조건 성립하는 것은 아니다.)<br /><br />

<h5>2. parent_rate와 성능의 상관관계<br /></h5>
15X15 Queens problem에 대하여 population : 100, crossover_rate = mutation_rate 으로 고정하고<br />
parent_rate를 0.2, 0.4, 0.6, 0.8으로 변화시켰을 때의 결과는 아래와 같다.<br />
<img src="/uploads/62298977757c646dd039ec7942565df8/test_parent.PNG" width="600" height="250">
해당 결과에서 parent 비율이 증가할수록 요구 세대 수도 증가하는 것을 확인할 수 있다.<br />
이것은 parent 비율이 증가할 수록, 새로운 세대와 이전 세대의 유사도가 증가하기 때문이다.<br /><br />

<h5>3. crossover rate 및 mutation rate와 성능의 상관관계<br /></h5>
15X15 Queens problem에 대하여 population : 100, parent_rate : 0.2 으로 고정하고<br />
(crossover_rate, mutation_rate) 를 (0.1, 0.7), (0.3, 0.5), (0.5, 0.3), (0.7, 0.1)으로 변화시켰을 때의 결과는 아래와 같다.<br />
<img src="/uploads/0493655b4a3c59ee723609a83a2c0067/test_cross_mutate.PNG" width="600" height="250">
해당 결과에서 mutation 비율이 증가할수록 요구 세대 수가 감소하는 것을 확인할 수 있으며,<br />
반대로 crossover 비율이 증가할수록 요구 세대 수가 증가한다는 것을 알 수 있다.<br />
원인을 파악해 보자면, mutation 알고리즘의 경우 생성되는 개체 내에서는 서로 같은 행 값을 가지는 열이 없도록 조정하는 데 반해<br />
crossover 알고리즘의 경우 그러한 조정 과정이 없이 무작위로 두 개체를 조합하는 것이기 때문에<br />
올바른 답을 도출할 확률이 mutation 이 crossover보다 높기 때문이다.<br /><br />

<h5>4. 타 알고리즘과의 성능비교<br /></h5>
7X7 Queens problem에 대하여 genetic algorithm과 DFS, BFS, DFID 및 Hill Climbing algorithm의 성능을 비교해 보면 아래와 같다.<br />
<img src="/uploads/6c474087f5bc5dbb7fe8b1601ff35839/test_algorithm.PNG" width="500" height="300">
해당 결과에서 보듯이 유전 알고리즘의 성능이 월등하다는 것을 알 수 있다.<br />
실제로 7X7 에서 뿐만 아니라 15X15, 20X20 과 같은 규모에 대해서도 위의 다른 알고리즘은 결과가 쉽게 나오지 않은 반면에 <br />
유전 알고리즘은 2분 이내로 결과를 도출한다.<br /><br />




		
