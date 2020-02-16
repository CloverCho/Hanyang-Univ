#######################################
#    Assignment 4 :  SAT Solver
#   융합전자공학부 2015001103 조윤상
######################################

from z3 import *
import time

# Number of Queens
print("N: ")
N = int(input())                    # 차수 N을 입력받는다.

start = time.time()                 # 시작 시각 측정


s = Solver()                        # Solver 모델 생성

# Variables :   각 컬럼의 퀸의 위치
column = IntVector("column",N)      # 각 컬럼의 퀸의 위치를 원소로 가지는 IntVector
                                    # 원소의 개수가 적으며 해당 원소를 정적인 인덱스로 접근시는
                                    # IntVector가 Array보다 좋기 때문에 이를 사용하였다.


# Domain :      {1, 2, ... , N}
domain = [And(column[i]>=1, column[i]<=N) for i in range(N)]  # {1, 2, ..., N}
s.add(domain)                       # Solver 모델에 도메인 추가

# Constraints   :  1) 가로로 만나는 퀸이 없도록 한다.  2) 대각선으로 만나는 퀸이 없도록 한다.

# 1) 가로로 만나는 퀸이 없도록 모델 추가
for i in range(N):
    for j in range(i):                  # 서로 다른 i, j
        s.add(column[i] != column[j])   # 가로로 만나는 퀸이 없도록 모델에 추가

# 2) 대각선으로 만나는 퀸이 없도록 모델 추가
for i in range(N):
    for j in range(i):                  # 서로 다른 i, j
        s.add([And(column[i] - column[j] != i-j, column[i] - column[j] != j-i)])


if s.check() == sat:                    # Solver 모델이 해를 찾을 경우
    m = s.model()                       # 생성된 모델
    r = [m.evaluate(column[i]) for i in range(N)]   # 해의 퀸의 위치를 나타내는 리스트
    print(r)                            # 퀸의 위치 리스트 출력

else:                                   # Solver 모델이 해 찾기에 실패할 경우
    print("failed to solve")

print("elapsed time: ", time.time() - start, " sec")    # 소요 시간 측정 및 프린트

