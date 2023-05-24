using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LineRendererController : MonoBehaviour
{
    public List<GameObject> rendererList = new List<GameObject>();

    // 리스트에는 노드들 순서가 저장됨.
    // 출발노드 -> (노드 ~ 노드) -> 상품 -> (노드 ~ 노드) -> 상품 -> (노드 ~ 노드) -> 도착노드

    // (로직) 이 괄호 안의 노드들의 라인을 한번에 그려준다.
    // 상품에 닿게 되면, 
    // 상품 리스트가 0보다 클 땐 상품에 닿기 전까지, 
    // 상품 리스트가 0이라면 도착노드에 닿기 전까지 라인을 그려줘야함!
    // 도착노드에 닿으면 라인 없애주기

    public List<List<GameObject>> curRenderList = new List<List<GameObject>>(); // 현재 한번에 그려줄 리스트들 (상품 닿게 되면 바뀌어야 하니까 콜라이더에서 관리)
    public int cur_idx = 0; // 현재 curRenderList idx, 콜라이더에서 상품에 부딪히면 1씩 증가해서 현재 노드 경로들을 갱신
    void Start()
    {
        /*
        NavMeshPath path2 = new NavMeshPath();
            LineRenderer line2 = lineRenderer_test.GetComponent<LineRenderer>();
            // line = transform.GetComponent<LineRenderer>();
            NavMesh.CalculatePath(new Vector3(0f, 1f, 2.464f), new Vector3(-1.26f, 1f, 2.464f), NavMesh.AllAreas, path2);
            line2.positionCount = path2.corners.Length;
            line2.SetPositions(path2.corners);
        */


    }

    public Vector3[] AddLineOffset()
    {
        UnityEngine.AI.NavMeshPath path = new UnityEngine.AI.NavMeshPath();

        Vector3[] calculatedLine = new Vector3[path.corners.Length];
        for (int i = 0; i < path.corners.Length; i++)
        {
            calculatedLine[i] = path.corners[i] + new Vector3(0f, 0.5f, 0f);
        }
        return calculatedLine;
    }

    public void AddLineRender()
    {
        for (int i = 0; i < rendererList.Count; i++)
        {
            // 각 게임 오브젝트에 LineRenderer 컴포넌트를 추가한다. 
            // => rendererList가 만들어지고 나서 되어야하므로 setNavigationTarget의 node 생성 후 호출되는 함수이다.
            if (rendererList[i].GetComponent<LineRenderer>() == null)
            {
                rendererList[i].AddComponent<LineRenderer>();
                LineRenderer lineRenderer = rendererList[i].GetComponent<LineRenderer>();
                lineRenderer.SetWidth(0.2f, 0.2f);
                lineRenderer.numCapVertices = 90;
                lineRenderer.numCornerVertices = 90;

                // lineRenderer.Position.Size

            }
            // Vector3[] calculatedPathAndOffset = AddLineOffset();
            // rendererList[i].GetComponent<LineRenderer>().SetPositions(calculatedPathAndOffset);
            // int r_count = rendererList[i].GetComponent<LineRenderer>().Positions.Count;
            // Debug.Log(rendererList[i].GetComponent<LineRenderer>().Positions[0]);
            // for (int j = 0; j < r_count; j++)
            // {

            // }



        }
    }

    // 현재 경로 라인을 그려준다.
    public void CreateLine()
    {
        List<GameObject> cur_list = curRenderList[cur_idx]; // 현재 한번에 경로를 그려줘야할 노드 리스트
        Debug.Log("현재 노드 리스트!!");
        for (int i = 0; i < cur_list.Count; i++)
        {
            Debug.Log(cur_list[i]);
        }


        for (int i = 0; i < cur_list.Count - 1; i++)
        {

            GameObject cur = cur_list[i];
            GameObject next = cur_list[i + 1];

            // cur ~ next 라인을 그려준다.
            Debug.Log(cur.name + "부터" + next.name + "까지 라인 그리기");
            UnityEngine.AI.NavMeshPath path = new UnityEngine.AI.NavMeshPath();
            LineRenderer line = cur.GetComponent<LineRenderer>();
            line.enabled = true;
            UnityEngine.AI.NavMesh.CalculatePath(cur.transform.position, next.transform.position, UnityEngine.AI.NavMesh.AllAreas, path);
            line.positionCount = path.corners.Length;
            line.SetPositions(path.corners);

            // 위치 요소 가져오기
            Vector3[] positions = new Vector3[line.positionCount];
            line.GetPositions(positions);

            // y 값 변경하기
            for (int j = 0; j < positions.Length; j++)
            {
                positions[j].y = 0.5f;
            }

            // 변경된 위치 요소 적용하기
            line.SetPositions(positions);
        }

    }

    // 현재 경로 라인을 없애준다.
    public void RemoveLine()
    {
        List<GameObject> cur_list = curRenderList[cur_idx]; // 현재 한번에 경로를 그려줘야할 노드 리스트

        for (int i = 0; i < cur_list.Count; i++)
        {
            LineRenderer line = cur_list[i].GetComponent<LineRenderer>();
            line.enabled = false;
        }

    }

    // 전체 경로 라인을 없애준다. (처음 시작할 때)
    public void ResetLine()
    {
        for (int i = 0; i < rendererList.Count; i++)
        {
            LineRenderer lineRenderer = rendererList[i].GetComponent<LineRenderer>();
            lineRenderer.enabled = false;
        }

    }


    void Update()
    {

    }
}
