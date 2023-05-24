using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GetPos : MonoBehaviour
{

    void Start()
    {
        for (int i = 0; i <= 73; i++)
        {
            Debug.Log(transform.GetChild(i).transform.position.x + " " + transform.GetChild(i).transform.position.z);
        }
    }


    void Update()
    {

    }
}
