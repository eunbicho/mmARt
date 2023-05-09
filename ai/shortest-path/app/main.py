from fastapi import FastAPI
from pydantic import BaseModel
# input으로 List를 받기 위한 import.
from typing import List
from app.load_distance_matrix import load_distance_matrix
from app.floyd_warshall import floyd_warshall_initialize
from app.held_karp import held_karp
from app.two_opt import local_search_2_opt
from app.util import distance_calc

from copy import deepcopy

import numpy as np


class Locations(BaseModel):
    locations: List[int]


# INF 상수는 연결되어있지 않음을 의미.
INF = 10**5

# distance matrix 초기화.
DISTANCE_MATRIX = load_distance_matrix(INF)

# 총 vertex 개수.
V = len(DISTANCE_MATRIX)

# DISTANCE_MATRIX 확인.
# print("DISTANCE_MATRIX")
# print(DISTANCE_MATRIX)

# distance matrix와 floyd-warshall을 이용하여,
# a -> b로의 최단 거리와, 이동 경로 저장.
# 모든 점으로부터 모든 점으로까지 최단거리
SHORTEST_DISTANCES, SHORTEST_PATHS = floyd_warshall_initialize(DISTANCE_MATRIX,INF)

# SHORTEST_DISTANCES, SHORTEST_PATHS 확인.
# print("SHORTEST_DISTANCES")
# print(SHORTEST_DISTANCES)
# print("SHORTEST_PATHS")
# print(SHORTEST_PATHS)

app = FastAPI()


@app.post("/shortest-path")
def find_shortest_path(locations: Locations):
    # 방문할 위치들 입력 확인.
    print("locations")
    print(locations.locations)

    # 시작점과 끝점을 추가해서 TSP를 적용할 list 만들기.
    visit_list = deepcopy(locations.locations)
    visit_list.insert(0,0)
    visit_list.append(V-1)

    # TSP 알고리즘을 적용하기 위해, 방문하는 위치들만으로 완전그래프(complete graph) 만들기.
    # complete matrix 만들기.
    l = len(visit_list)
    complete_graph = [[-1 for i in range(l)] for i in range(l)]
    for r in range(l):
        for c in range(l):
            complete_graph[r][c]=SHORTEST_DISTANCES[visit_list[r]][visit_list[c]]

    # 방문해야할 장소 개수가 15이하일 때는 exact algorithm인 held-karp algorithm 적용.
    #           "          15이상일 때는 heuristic algorithm인 2-opt algorithm 적용.
    # 최단거리, 최단경로 초기화.
    shortest_distance = -1
    temp_path = []
    if l <= 5:
        shortest_distance, temp_path = held_karp(complete_graph,INF)
    else:
        # 입력 형태를 맞추기 위해, distance matrix인 complete_graph를 numpy array로 변환해줌.
        complete_graph_np_array = np.array(complete_graph)

        # seed 초기화. seed[0]는 경로를, seed[1]은 거리를 의미.
        seed=[[],[]]

        # path 초기화는 숫자가 낮은 순서로 방문.
        seed[0]=[ i for i in range(len(complete_graph_np_array))]
        seed[1] = distance_calc(complete_graph_np_array,seed)

        temp_path,shortest_distance = local_search_2_opt(complete_graph_np_array,seed,-1,True)

    # 실제 경로로 변환해주기.
    # paths를 보고, 경로 알려주기.
    real_path = [0]
    path_len = len(temp_path)
    for i in range(path_len-1):
        real_path.extend(SHORTEST_PATHS[visit_list[temp_path[i]]][visit_list[temp_path[i+1]]][1:])

    # 최단 거리 출력해보기.
    print("shortest_distance")
    print(shortest_distance)

    return {"path":real_path}