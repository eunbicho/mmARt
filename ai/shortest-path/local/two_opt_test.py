from two_opt import local_search_2_opt

import os
import numpy as np

# 현재 작업 중인 디렉토리를 확인합니다.
cwd = os.getcwd()
print("cwd:",cwd)

# 파일 경로를 지정합니다.
file_path = os.path.join(cwd, 'local\TSP-02-Coordinates.txt')

# print("file_path:",file_path)

# with open(file_path) as f:
#     # 첫 번째 줄을 읽어서 도시 이름들을 추출합니다.
#     cities = f.readline().strip().split()

#     # 나머지 줄에서 거리를 추출하여 2차원 리스트로 저장합니다.
#     distances = []
#     for line in f:
#         row = list(map(float, line.strip().split()[1:]))
#         distances.append(row)

# # 2차원 리스트를 numpy 배열로 변환합니다.
# import numpy as np
# distances = np.array(distances)

# print(distances)

# Required Libraries
import pandas as pd
from util import build_distance_matrix, distance_calc

coordinates = pd.read_csv(file_path, sep = '\t')
print(coordinates)
coordinates = coordinates.values
print(coordinates)

GRAPH = build_distance_matrix(coordinates)
print(type(GRAPH))
print(GRAPH.shape)

INF=10**7
GRAPH = [ [ 0, 3, INF, 7, INF ],
			  [ 3, 0, 2, INF, INF ],
			  [ INF, 2, 0, 1, INF ],
			  [ 7, INF, 1, 0, 5 ],
	          [ INF, INF, INF, 5, 0] ]

GRAPH=np.array(GRAPH)

V=len(GRAPH)

dis = [[-1 for i in range(V)] for i in range(V)]
Next = [[-1 for i in range(V)] for i in range(V)]

# import
from floyd_warshall_test import initialize,floydWarshall,constructPath

initialize(dis,Next,V,INF,GRAPH)

# r에서 c로 가는 최단 거리 구하기.
floydWarshall(dis,Next,V,INF)

# r에서 c로 가는 최단거리 경로 구하기.
paths = [[-1 for i in range(V)] for i in range(V)]   
for r in range(V):
    for c in range(V):
        paths[r][c]=constructPath(r,c,Next)

print(paths)

# 시작점과 끝점
start = 0
# end = len(GRAPH)-1
end=2

# 방문할 곳 몇 개 뽑기.
visit = [4]

# 앞 뒤에 시작점과 끝점 붙여주기
visit.insert(0,0)
visit.append(end)

# 새로운 matrix 만들기.
l = len(visit)
temp_graph = [[-1 for i in range(l)] for i in range(l)]
for r in range(l):
    for c in range(l):
        temp_graph[r][c]=dis[visit[r]][visit[c]]

def print_matrix(matrix):
    r=len(matrix)
    c=len(matrix[0])
    for i in range(r):
        for j in range(c):
            print(matrix[i][j],end=' ')
        print()

print("temp_graph")
print_matrix(temp_graph)

temp_graph_ndarray=np.array(temp_graph)

# seed 초기화
seed=[[],[]]
seed[0]=[ i for i in range(len(temp_graph))]
seed[1] = distance_calc(temp_graph_ndarray,seed)

temp_path, opt = local_search_2_opt(temp_graph_ndarray, seed, -1, True)

print("opt:",opt)
print("temp_path:",temp_path)

# paths를 보고, 경로 알려주기.
real_path = [start]
path_len = len(temp_path)
for i in range(path_len-1):
    real_path.extend(paths[visit[temp_path[i]]][visit[temp_path[i+1]]][1:])

print("real_path:")
print(real_path)

# seed = [[ 1,  2,  3,  4,  5,  6,  7,  8,  9, 10,
#          11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
#          21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
#          31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
#          41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
#          51, 52] , 20985.0]

# route, distance = local_search_2_opt(GRAPH, seed, -1, True)

# print(route)
# print(distance)