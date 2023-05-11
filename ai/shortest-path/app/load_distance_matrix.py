import os
import pandas as pd
from app.util import build_distance_matrix


def load_distance_matrix(INF):
    # # held-karp test
    # # matrix 생성.
    # distance_matrix = [[INF for j in range(5)] for i in range(5)]

    # # edge 추가. (양방향)
    # distance_matrix[0][1]=3
    # distance_matrix[1][0]=3

    # distance_matrix[0][3]=7
    # distance_matrix[3][0]=7

    # distance_matrix[1][2]=2
    # distance_matrix[2][1]=2

    # distance_matrix[2][3]=1
    # distance_matrix[3][2]=1

    # distance_matrix[3][4]=5
    # distance_matrix[4][3]=5

    # # 자기 자신과의 거리는 0
    # for i in range(5):
    #     distance_matrix[i][i]=0


    # # 2-opt test
    # # 현재 작업 중인 디렉토리를 확인합니다.
    # cwd = os.getcwd()

    # # 파일 경로를 지정합니다.
    # file_path = os.path.join(cwd, 'app/TSP-02-Coordinates.txt')

    # coordinates = pd.read_csv(file_path, sep = '\t')
    # coordinates = coordinates.values

    # distance_matrix = build_distance_matrix(coordinates)
    

    # real
        # 현재 작업 중인 디렉토리를 확인합니다.
    cwd = os.getcwd()

    # 파일 경로를 지정합니다.
    file_path = os.path.join(cwd, 'app/map_coordinates.txt')

    coordinates = pd.read_csv(file_path, sep = ' ')
    coordinates = coordinates.values

    distance_matrix = build_distance_matrix(coordinates)

    return distance_matrix
