from floyd_warshall_test import initialize,floydWarshall,constructPath
from held_karp import held_karp

def print_matrix(matrix):
    r=len(matrix)
    c=len(matrix[0])
    for i in range(r):
        for j in range(c):
            print(matrix[i][j],end=' ')
        print()

INF=10**7
GRAPH = [ [ 0, 3, INF, 7, INF ],
			  [ 3, 0, 2, INF, INF ],
			  [ INF, 2, 0, 1, INF ],
			  [ 7, INF, 1, 0, 5 ],
	          [ INF, INF, INF, 5, 0] ]
V=len(GRAPH)

dis = [[-1 for i in range(V)] for i in range(V)]
Next = [[-1 for i in range(V)] for i in range(V)]

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
end = 2

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

# 시작과 끝을 지정하기 위해 시작점으로 돌아오는 distance 조정하기.
for r in range(l):
    temp_graph[r][0]=INF

# 자신과 끝점에서는 0
temp_graph[0][0]=0
temp_graph[end][0]=0

print("temp_graph")
print_matrix(temp_graph)

# tsp 알고리즘.
opt, temp_path = held_karp(temp_graph)
# opt,temp_path = bellman_held_karp_exact_algorithm(temp_graph)

print("opt:",opt)
print("temp_path:",temp_path)

# paths를 보고, 경로 알려주기.
real_path = [start]
path_len = len(temp_path)
for i in range(path_len-1):
    real_path.extend(paths[visit[temp_path[i]]][visit[temp_path[i+1]]][1:])

print("real_path:")
print(real_path)

