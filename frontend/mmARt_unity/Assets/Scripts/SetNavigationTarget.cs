using System.Collections;
using TMPro;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.AI;
using UnityEngine.UI;
using System;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading.Tasks;

public class SetNavigationTarget : MonoBehaviour
{

    public List<Target> navigationTargetObjects = new List<Target>();

    private NavMeshPath path; // current calculated path
    private LineRenderer line; // line renderer to display path
    private Vector3 targetPosition = Vector3.zero; // current target position

    private bool lineToggle = false;
    private bool listToggle = false;


    public List<GameObject> nodes = new List<GameObject>(); // 순서대로 가야할 경로 노드 리스트
    public List<Product> products = new List<Product>(); // 순서대로 가야할 상품 타겟 리스트

    public int nodes_list_length;
    public string[] products_names;

    public GameObject shoppingList;
    GameObject curProductsList_txt;

    HttpClient httpClient;

    GameObject m_TextResult;

    public GameObject LineRenderers;

    public bool completeGetPath = false;

    // public string startNode = "0";
    public int userId;

    private void Start()
    {
        path = new NavMeshPath();
        line = transform.GetComponent<LineRenderer>();
        line.enabled = lineToggle;

        LineRenderers = GameObject.Find("LineRenderers");

        // curProductsList_txt = GameObject.Find("CurProductsList");
        m_TextResult = GameObject.Find("m_TextResult");
        shoppingList = GameObject.Find("ShoppingList");
        // shoppingList.SetActive(false);

        nodes_list_length = nodes.Count;
        products_names = new string[nodes_list_length];
        for (int i = 0; i < nodes_list_length; i++)
        {
            products_names[i] = nodes[i].name;
        }




        NarvigationTargetObjectsInit(); // 지나갈 노드 세팅

        // GetPath("start");



    }

    // navigationTargetObjects 리스트에 원소 넣어주기 (Nodes child 오브젝트들 넣어주기)
    private void NarvigationTargetObjectsInit()
    {
        GameObject node = GameObject.Find("Nodes");
        Target target;
        for (int i = 0; i < 74; i++)
        {
            GameObject curNode = node.transform.Find(i.ToString()).gameObject;

            target = new Target();
            target.Name = curNode.name;
            target.PositionObject = curNode;

            navigationTargetObjects.Add(target);
        }

    }

    public async void GetPath(string startNode)
    {
        Debug.Log("getpath!!!!!!!!!!!!!!!!!!!!!!!!!!");
        LineRenderers = GameObject.Find("LineRenderers");
        LineRenderers.GetComponent<LineRendererController>().ResetLine(); // 경로 켜져있으면 다 없애주기

        if (startNode.Equals("start"))
        {
            Debug.Log("start와 같아요");
            startNode = "0";
        }
        Debug.Log("startNode갱신: " + startNode);
        // 유저 아이디 1인 사람의 장볼구니 정보 얻어오기 (아이디는 나중에 수정)
        httpClient = new HttpClient();
        userId = GameObject.Find("AndroidController").GetComponent<AndroidController>().userId;
        Debug.Log("안드컨트롤에서 넘어온 userId: " + userId);
        string getURL = $"http://k8a405.p.ssafy.io:8090/api/v1/getcarts/shortest-path/{userId}?startNode={startNode}";

        // try
        // {
        // 1) API 요청 후, 문자열 데이터 정리
        var response = await httpClient.GetAsync(getURL);
        string body = await response.Content.ReadAsStringAsync();
        Debug.Log(body);

        string[] temp = body.Split("\"itemList\":");
        string[] temp2 = temp[1].Split("\"totalPath\":");

        Debug.Log(temp2[0].Substring(2, temp2[0].Length - 5)); // itemList 출력
        Debug.Log(temp2[1].Substring(2, temp2[1].Length - 6)); // totalPath 출력

        // 2) totalPath 데이터 정리
        string totalPath = temp2[1].Substring(2, temp2[1].Length - 6);

        // 2-1) totalPath 노드숫자 순서 담은 문자열 배열 만들기
        string[] order_names = totalPath.Split("\",\""); // 노드이름 순서를 담고 있는 배열

        // 2-2) 위 배열에 맞는 이름 순서대로 Node 자식 오브젝트들을 nodes 리스트에 추가하기
        GameObject node = GameObject.Find("Nodes");
        nodes.Clear();
        for (int i = 0; i < order_names.Length; i++)
        {
            for (int j = 0; j < 74; j++)
            {
                GameObject curChild = node.transform.Find(j.ToString()).gameObject;
                if (curChild.name.Equals(order_names[i]))
                {
                    nodes.Add(curChild);
                }
            }


        }

        // 3) itemList 데이터 정리
        string itemList = temp2[0].Substring(2, temp2[0].Length - 5);

        // 3-1) item 순서대로 products 리스트에 추가하기
        products.Clear();
        string[] items = itemList.Split("},{");
        for (int i = 0; i < items.Length; i++)
        {
            string arr = items[i];
            string[] arr_2 = arr.Split(",");
            string itemName = arr_2[0].Split(":")[1];
            string placeInfo = arr_2[1].Split(":")[1];
            string count = arr_2[2].Split(":")[1];
            Debug.Log(itemName + ":" + placeInfo + ":" + count);

            Product product = new Product();
            product.Pname = itemName.Substring(1, itemName.Length - 2);
            product.pos = placeInfo.Substring(1, placeInfo.Length - 2);
            product.count = count;

            products.Add(product);

            // Debug.Log(product.Pname + ":" + product.pos + ":" + product.count);

        }

        // 4) 경로 오브젝트까지 정비 완료
        completeGetPath = true;

        // 5) 현재 타겟 지정
        SetCurrentNavigationTarget();

        // rendererList, curRenderList 지정
        // 예) 0 -> 71 -> 70 -> 3 이 경로라면, 0~71, 71~70, 70~3 이런식으로 그려주는 라인 렌더러 3개 필요
        int idx = 0;

        List<GameObject> curList = new List<GameObject>();
        LineRenderers.GetComponent<LineRendererController>().curRenderList.Clear();

        for (int i = 0; i < nodes.Count; i++)
        {
            LineRenderers.GetComponent<LineRendererController>().rendererList.Add(nodes[i]);


            // LineRenderers.GetComponent<LineRendererController>().curRenderList[idx] = new List<GameObject>();
            // 리스트를 만들어서 curRenderList에 add하기
            curList.Add(nodes[i]);

            // LineRenderers.GetComponent<LineRendererController>().curRenderList[idx].Add(nodes[i]); // 저장
            if (Int32.Parse(nodes[i].name) >= 1 && Int32.Parse(nodes[i].name) <= 28 || Int32.Parse(nodes[i].name) == 73)
            { // 상품 번호면
                LineRenderers.GetComponent<LineRendererController>().curRenderList.Add(curList);
                Debug.Log("상품번호! or 끝!");
                for (int t = 0; t < curList.Count; t++)
                {
                    Debug.Log(t + ":" + curList[t]);
                }
                curList = new List<GameObject>();
                curList.Add(nodes[i]);

            }
        }

        // Debug.Log("확인 좀 해보자");
        // for (int i = 0; i < LineRenderers.GetComponent<LineRendererController>().curRenderList.Count; i++)
        // {
        //     List<GameObject> list = LineRenderers.GetComponent<LineRendererController>().curRenderList[i];
        //     for (int j = 0; j < list.Count; j++)
        //     {
        //         Debug.Log(j + ":" + list[j]);
        //     }
        // }


        // 라인 렌더러 컴포넌트 생성 후 라인 그려주기
        LineRenderers.GetComponent<LineRendererController>().AddLineRender();
        LineRenderers.GetComponent<LineRendererController>().CreateLine();

        // }
        // catch (HttpRequestException ex)
        // {
        //     m_TextResult.GetComponent<Text>().text = "----------- 서버에 연결할 수 없습니다 -------------";
        // }
        // catch (Exception ex2)
        // {
        //     Debug.Log("에러");
        //     m_TextResult.GetComponent<Text>().text = ex2.Message;
        // }


    }


    private void Update()
    {
        // indicator는 무조건 nodes 리스트의 첫번째 항목 타겟을 가리킴
        // curTarget text는 무조건 products 리스트의 첫번째 상품이름을 가리킴
        GameObject curTarget = GameObject.Find("CurTarget");
        if (nodes.Count > 0 && products.Count > 0)
        {
            if (products.Count > 0)
            {
                int count = Int32.Parse(products[0].count) - 1;
                if (count > 0)
                {
                    curTarget.GetComponent<Text>().text = products[0].Pname + "외" + count;
                }
                else
                {
                    curTarget.GetComponent<Text>().text = products[0].Pname;
                }

            }


        }
        else if (products.Count == 0)
        {
            curTarget.GetComponent<Text>().text = "계산대로!";
        }
        if (nodes.Count == 0)
        {
            // 경로를 받아온 후, nodes.Count가 0이 되었다면 쇼핑이 끝난 경우
            if (completeGetPath)
            {
                // 쇼핑 끝났다고 알려주고
                curTarget.GetComponent<Text>().text = "쇼핑 끝!";

                // 라인 없애기
                LineRenderers.GetComponent<LineRendererController>().RemoveLine();
                line.enabled = false;
            }
            else
            {
                // 경로를 받기 전, nodes.Count가 0이라면 아직 받지 않은 경우
                curTarget.GetComponent<Text>().text = "경로를 받아오고 있어요";
            }
        }


        // GameObject IsToggle_txt = GameObject.Find("IsToggle");
        // IsToggle_txt.GetComponent<Text>().text = lineToggle.ToString();



        // List<string> objectNames = new List<string>();
        // for(int i=0; i<products_list_length; i++){
        //     if(){

        //     }
        //     objectNames.Add(products[i].name);
        // }

        // string str = String.Join(",\n", products_names);

        // curProductsList_txt.GetComponent<Text>().text = str;

        if (lineToggle)
        {
            // Debug.Log("line toggle!!");
            // Debug.Log(transform.position + ":" + targetPosition);
            // Debug.Log(path.corners + ":" + path.status);

            NavMesh.CalculatePath(transform.position, targetPosition, NavMesh.AllAreas, path);
            line.positionCount = path.corners.Length;
            line.SetPositions(path.corners);

            // NavMeshPath path2 = new NavMeshPath();
            // LineRenderer line2 = lineRenderer_test.GetComponent<LineRenderer>();
            // // line = transform.GetComponent<LineRenderer>();
            // NavMesh.CalculatePath(new Vector3(0f, 1f, 2.464f), new Vector3(-1.26f, 1f, 2.464f), NavMesh.AllAreas, path2);
            // line2.positionCount = path2.corners.Length;
            // line2.SetPositions(path2.corners);


            // Debug.Log(NavMesh.CalculatePath(transform.position, targetPosition, NavMesh.AllAreas, path));



        }
    }

    public void SetCurrentNavigationTarget()
    {

        GameObject FunctionCall = GameObject.Find("FunctionCall");


        targetPosition = Vector3.zero;

        Target currentTarget = null;
        // indicator는 무조건 nodes 리스트의 첫번째 항목 타겟을 가리킴
        if (nodes.Count > 0)
        {
            currentTarget = navigationTargetObjects.Find(x => x.Name.Equals(nodes[0].name));
        }


        if (currentTarget != null)
        {
            // products 리스트의 첫번째 항목 타겟을 가리킴
            targetPosition = currentTarget.PositionObject.transform.position;
            FunctionCall.GetComponent<Text>().text = currentTarget.Name;
        }
        else
        {
            FunctionCall.GetComponent<Text>().text = "currentTarget없어";
        }
    }

    public void ToggleVisibility()
    {
        lineToggle = !lineToggle;
        line.enabled = lineToggle;
    }

    public void ShowShoppingList()
    {

        listToggle = !listToggle;
        shoppingList.SetActive(listToggle);

    }
}
