using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class CollisionCamera : MonoBehaviour
{
    public int index = 0;
    public List<GameObject> collObjs = new List<GameObject>(); // 부딪힌 오브젝트
    public int coll_index = 0;

    public GameObject LineRenderers;

    void Start()
    {
        LineRenderers = GameObject.Find("LineRenderers");
    }

    private void Update()
    {
        this.GetComponent<Rigidbody>().velocity = Vector3.zero; // 충돌 시 밀림 방지용 가속도 0으로 설정
        this.GetComponent<Rigidbody>().angularVelocity = Vector3.zero;
        // Debug.Log("현재" + this.GetComponent<Rigidbody>().isKinematic);
    }


    private void OnTriggerEnter(Collider other)
    {
        this.GetComponent<Rigidbody>().isKinematic = true;

        // 이 컴포넌트가 부착된 게임 오브젝트의 콜라이더와 충돌한 게임 오브젝트 가져오기
        var obj = other.gameObject;

        // 부딪힌 게임오브젝트 이름에 Target이 포함되어있을 때 이름 띄워주기
        if (obj.name.Contains("(1)"))
        {
            // GameObject collider_txt = GameObject.Find("Collider");
            // collider_txt.GetComponent<Text>().text = "collisionTarget: "+obj.name;
            // Debug.Log("enter" + obj.name);

            GameObject indicator = GameObject.Find("Indicator");

            // 현재 가는 타겟이랑 부딪힌 게임 오브젝트가 일치하면
            if (indicator.GetComponent<SetNavigationTarget>().nodes.Count != 0 && indicator.GetComponent<SetNavigationTarget>().nodes[0].name + " (1)" == obj.name)
            {

                Debug.Log(this.GetComponent<Rigidbody>().isKinematic);
                // Debug.Log("가려고 한 타겟이랑 부딪혔어!" + obj.name);

                // 라인 지워주기
                // 1) LineRenderers.GetComponent<LineRendererController>().curRenderList에서 현재 부딪힌 거랑 이름이 같은 노드를 찾는다.
                // 2) 그 노드 앞 인덱스에 있는 노드의 라인을 지워준다.
                int cur_idx = LineRenderers.GetComponent<LineRendererController>().cur_idx;
                List<GameObject> list = LineRenderers.GetComponent<LineRendererController>().curRenderList[cur_idx];
                for (int i = 1; i < list.Count; i++)
                {
                    if (list[i].name + " (1)" == obj.name)
                    {
                        list[i - 1].GetComponent<LineRenderer>().enabled = false;
                    }
                }


                // products_names에 [v] 체크표시 추가      
                // indicator.GetComponent<SetNavigationTarget>().products_names[index] = "[v] " + obj.name;
                // index++;

                // Indicator가 가지고 있는 SetNavigationTarget의 nodes리스트에서 첫 번째 항목 삭제
                indicator.GetComponent<SetNavigationTarget>().nodes.RemoveAt(0);

                // 만약 부딪힌 노드가 products 리스트의 첫 번째 상품 위치와 같다면, products 리스트에서 첫 번째 상품 삭제
                if (indicator.GetComponent<SetNavigationTarget>().products.Count != 0 && obj.name == indicator.GetComponent<SetNavigationTarget>().products[0].pos + " (1)")
                {
                    indicator.GetComponent<SetNavigationTarget>().products.RemoveAt(0);

                    // 기존꺼 지우고 경로 갱신!
                    LineRenderers.GetComponent<LineRendererController>().RemoveLine();
                    LineRenderers.GetComponent<LineRendererController>().cur_idx += 1;
                    LineRenderers.GetComponent<LineRendererController>().CreateLine();
                }



                // setCurrentTarget변경
                indicator.GetComponent<SetNavigationTarget>().SetCurrentNavigationTarget();
            }



        }

    }

    private void OnTriggerExit(Collider other)
    {
        this.GetComponent<Rigidbody>().isKinematic = false;
        var obj = other.gameObject;
        // Debug.Log("exit" + obj.name);

    }

    // void manageCollisionObj() // index: 부딪힌 오브젝트의 리스트 인덱스
    // {
    //     Invoke("generateObj", 2f);
    //     collObjs[coll_index].SetActive(false);

    // }

    // void generateObj()
    // {
    //     Debug.Log(coll_index);
    //     collObjs[coll_index].SetActive(true);
    // }
}
