# Python3 program to find the shortest
# path between any two nodes using
# Floyd Warshall Algorithm.

# Initializing the distance and
# Next array

def initialize(dis,Next,V,INF,GRAPH):
	for i in range(V):
		for j in range(V):
			dis[i][j] = GRAPH[i][j]

			# No edge between node
			# i and j
			if (GRAPH[i][j] == INF):
				Next[i][j] = -1
			else:
				Next[i][j] = j

# Function construct the shortest
# path between u and v
def constructPath(u, v, Next):
	
	# If there's no path between
	# node u and v, simply return
	# an empty array
	if (Next[u][v] == -1):
		return {}

	# Storing the path in a vector
	path = [u]
	while (u != v):
		u = Next[u][v]
		path.append(u)

	return path

# Standard Floyd Warshall Algorithm
# with little modification Now if we find
# that dis[i][j] > dis[i][k] + dis[k][j]
# then we modify next[i][j] = next[i][k]
def floydWarshall(dis,Next,V,INF):
	for k in range(V):
		for i in range(V):
			for j in range(V):
				
				# We cannot travel through
				# edge that doesn't exist
				if (dis[i][k] == INF or dis[k][j] == INF):
					continue
				if (dis[i][j] > dis[i][k] + dis[k][j]):
					dis[i][j] = dis[i][k] + dis[k][j]
					Next[i][j] = Next[i][k]

def floyd_warshall_initialize(DISTANCE_MATRIX,INF):
	# 총 vertices 개수
	V = len(DISTANCE_MATRIX)

	SHORTEST_DISTANCES = [[-1 for i in range(V)] for i in range(V)]
	Next = [[-1 for i in range(V)] for i in range(V)]

	initialize(SHORTEST_DISTANCES,Next,V,INF,DISTANCE_MATRIX)

	# r에서 c로 가는 최단 거리 구하기.
	floydWarshall(SHORTEST_DISTANCES,Next,V,INF)

	# r에서 c로 가는 최단거리 경로 구하기.
	SHORTEST_PATHS = [[-1 for i in range(V)] for i in range(V)]   
	for r in range(V):
		for c in range(V):
			SHORTEST_PATHS[r][c]=constructPath(r,c,Next)

	return SHORTEST_DISTANCES, SHORTEST_PATHS

# Print the shortest path
def printPath(path):
	n = len(path)
	for i in range(n - 1):
		print(path[i], end=" -> ")
	print (path[n - 1])

# Driver code
# if __name__ == '__main__':
# 	MAXM,INF = 100,10**7
# 	dis = [[-1 for i in range(MAXM)] for i in range(MAXM)]
# 	Next = [[-1 for i in range(MAXM)] for i in range(MAXM)]

# 	V = 5
# 	graph = [ [ 0, 3, INF, 7, INF ],
# 			  [ 3, 0, 2, INF, INF ],
# 			  [ INF, 2, 0, 1, INF ],
# 			  [ 7, INF, 1, 0, 5 ],
# 	          [ INF, INF, INF, 5, 0] ]

# 	# Function to initialise the
# 	# distance and Next array
# 	initialise(V)

# 	# Calling Floyd Warshall Algorithm,
# 	# this will update the shortest
# 	# distance as well as Next array
# 	floydWarshall(V)
# 	path = []
	
# 	paths = [[-1 for i in range(V)] for i in range(V)]   
# 	for r in range(V):
# 		for c in range(V):
# 			paths[r][c]=constructPath(r,c)
		


# 	# Path from node 1 to 3
# 	print("Shortest path from 1 to 3: ", end = "")
# 	for r in range(V):
# 		print(paths[r])
# 	for r in range(V):
# 		for c in range(V):
# 			print(dis[r][c],end=' ')
# 		print()
	
    
	
# 	path = constructPath(1, 3)
# 	printPath(path)
	

# 	# Path from node 0 to 2
# 	print("Shortest path from 0 to 2: ", end = "")
# 	path = constructPath(0, 2)
# 	printPath(path)

# 	# Path from node 3 to 2
# 	print("Shortest path from 3 to 2: ", end = "")
# 	path = constructPath(3, 2)
# 	printPath(path)

# 	# This code is contributed by mohit kumar 29
