def load_distance_matrix(INF):
    # test
    # matrix 생성.
    distance_matrix = [[INF for j in range(5)] for i in range(5)]

    # edge 추가. (양방향)
    distance_matrix[0][1]=3
    distance_matrix[1][0]=3

    distance_matrix[0][3]=7
    distance_matrix[3][0]=7

    distance_matrix[1][2]=2
    distance_matrix[2][1]=2

    distance_matrix[2][3]=1
    distance_matrix[3][2]=1

    distance_matrix[3][4]=5
    distance_matrix[4][3]=5

    # 자기 자신과의 거리는 0
    for i in range(5):
        distance_matrix[i][i]=0

    
    # real

    return distance_matrix
