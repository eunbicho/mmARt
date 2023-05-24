using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.XR.ARFoundation;
using ZXing;
using System;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading.Tasks;
using System.Collections.Generic;
using System.Text;

public class BarcodeScanner : MonoBehaviour
{
    // 큐알 인식용 변수들
    [SerializeField]
    ARCameraBackground m_ARCameraBackground;

    // [SerializeField]
    public Text m_TextResult;

    BarcodeReader m_BarcodeReader;
    HttpClient httpClient;


    // 토글용 변수들
    public bool barcodeToggle = false;
    private GameObject barcodeScanImage;

    private GameObject barcodeBtn;
    private GameObject barcodeBtn_closed;

    private GameObject rawImage;

    void Start()
    {
        barcodeScanImage = GameObject.Find("BarcodeScanImage");
        barcodeScanImage.SetActive(false);

        barcodeBtn = GameObject.Find("BarcodeBtn");

        barcodeBtn_closed = GameObject.Find("BarcodeBtn_closed");
        barcodeBtn_closed.SetActive(false);

        rawImage = GameObject.Find("RawImage");

        m_BarcodeReader = new BarcodeReader();
    }

    public void DecodeQRCode()
    {
        m_TextResult.text = "11111";
        StartCoroutine("RecognizeQRCode");
    }

    // https://siddeshb88.medium.com/how-to-augment-in-real-world-using-qrcode-e1a344416288
    public async Task RecognizeQRCode()
    {
        Texture2D texture = new Texture2D(Screen.width, Screen.height, TextureFormat.RGB24, true);
        for (int i = 0; i < 100; i++)  // Time out
        {

            UpdateCameraTexture(texture);

            byte[] barcodeBitmap = texture.GetRawTextureData();

            LuminanceSource source = new RGBLuminanceSource(barcodeBitmap, texture.width, texture.height);
            var result = m_BarcodeReader.Decode(source);

            if (result != null && result.Text != "")
            {
                /*바코드가 이 시점에 있을 예정이기 때문에,
                1. 바코드를 이용해 item 정보를 get 해온다.
                2. 유저정보를 어떻게 받아온다고 가정하자.일단은 하드코딩으로 넣어주기
                3. gotCart에 두 정보를 post해서 추가시켜준다.
                */
                httpClient = new HttpClient();
                string QRContents = result.Text;
                //1. 바코드를 이용해 아이템 정보를 먼저 가져와보자.
                string getURL = "http://k8a405.p.ssafy.io:8090/api/v1/items/barcode?barcode=" + QRContents;
                try
                {
                    var response = await httpClient.GetAsync(getURL);
                    string body = await response.Content.ReadAsStringAsync();
                    string[] temp = body.Split(':');
                    if (temp[1].Split('"')[1] == "ERROR")
                    {
                        m_TextResult.text = "해당하는 상품을 찾을 수 없습니다.";
                        break;
                    }
                    string[] temp2 = temp[3].Split(',');
                    int itemIdx = int.Parse(temp2[0]);//itemIdx 파싱
                    int userIdx = GameObject.Find("AndroidController").GetComponent<AndroidController>().userId;

                    String deleteURL = "http://k8a405.p.ssafy.io:8090/api/v1/getcarts?userIdx=" + userIdx.ToString() + "&itemIdx=" + itemIdx.ToString();
                    var temp3 = await httpClient.DeleteAsync(deleteURL);

                    string json = "{\"itemIdx\":\"" + itemIdx.ToString() + "\", "
                        + "\"userIdx\":\"" + userIdx.ToString() + "\""
                        + "}";

                    var content = new StringContent(json, Encoding.UTF8, "application/json"); // JSON 데이터를 StringContent 객체로 변환
                    String postURL = "http://k8a405.p.ssafy.io:8090/api/v1/gotcarts";
                    response = await httpClient.PostAsync(postURL, content);
                    body = await response.Content.ReadAsStringAsync();
                    m_TextResult.text = body;
                }
                catch (HttpRequestException ex)
                {
                    m_TextResult.text = "----------- 서버에 연결할수없습니다 ---------------------";
                }
                break;
            }
            await Task.Delay(TimeSpan.FromSeconds(0.05f));
        }

        Destroy(texture);
    }

    void UpdateCameraTexture(Texture2D texture)
    {
        RenderTexture renderTexture = new RenderTexture(texture.width, texture.height, 24, RenderTextureFormat.ARGB32);

        // Copy the camera background to a RenderTexture
        Graphics.Blit(null, renderTexture, m_ARCameraBackground.material);

        // Copy the RenderTexture from GPU to CPU
        var activeRenderTexture = RenderTexture.active;
        RenderTexture.active = renderTexture;
        texture.ReadPixels(new Rect(0, 0, renderTexture.width, renderTexture.height), 0, 0);
        texture.Apply();
        RenderTexture.active = activeRenderTexture;

        Destroy(renderTexture);
    }


    void Update()
    {
        // 바코드 스캔 버튼 조절
        if (barcodeToggle)
        {
            barcodeBtn.SetActive(false);
            barcodeBtn_closed.SetActive(true);
            rawImage.SetActive(false);

        }
        else
        {
            barcodeBtn.SetActive(true);
            barcodeBtn_closed.SetActive(false);


        }

    }

    public void ToggleBarcodeScanner()
    {
        barcodeToggle = !barcodeToggle;
        barcodeScanImage.SetActive(barcodeToggle);

    }
}
