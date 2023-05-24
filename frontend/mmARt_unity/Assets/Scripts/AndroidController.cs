using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using System;

public class AndroidController : MonoBehaviour
{
    public Text m_TextResult;
    public int userId = 1; // 일단 1로 초기화 (안드에서 유니티로 넘어올때 테스트해야함)

    void Start()
    {
        userId = 1;
        // m_TextResult = GameObject.Find("m_TextResult");
        Debug.Log("AndroidController: " + userId);
        // receivedStr("1");
    }


    void Update()
    {

    }

    public void BackButton()
    {
        Application.Quit();
    }

    public static void CallAndroidMethod(string methodName, string str)
    {
        using (var clsUnityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer")) // "com.pingtech.swingtracker.UnityPlayerActivity"))
        {
            using (var objActivity = clsUnityPlayer.GetStatic<AndroidJavaObject>("currentActivity"))
            {
                objActivity.Call(methodName, str);
            }
        }
    }

    public static void ToHomeBtn()
    {
#if !UNITY_EDITOR
#if UNITY_ANDROID
                CallAndroidMethod("receiveStr", "1");
                Application.Quit();
#endif
#endif
    }

    public static void ToGotCartBtn()
    {
#if !UNITY_EDITOR
#if UNITY_ANDROID
                CallAndroidMethod("receiveStr", "2");
                Application.Quit();
#endif
#endif
    }

    public void receivedStr(string userIdstr)
    {
        m_TextResult.text = "userId : " + userIdstr + " 잘 도착했어요!!!";
        Debug.Log("userIdstr: " + userIdstr);
        userId = Int32.Parse(userIdstr);
        Debug.Log("userId: " + userId);
        // CallAndroidMethod("receiveStr", userIdstr+" 잘 도착했어요!!!");
        GameObject.Find("Indicator").GetComponent<SetNavigationTarget>().GetPath("start");
    }
}
