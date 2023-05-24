using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MinimapController : MonoBehaviour
{
    private GameObject rawImage;
    public bool minimapToggle = false;

    void Start()
    {
        rawImage = GameObject.Find("RawImage");
    }


    void Update()
    {

    }

    public void ToggleMinimapScanner()
    {
        minimapToggle = !minimapToggle;
        rawImage.SetActive(minimapToggle);

    }
}
