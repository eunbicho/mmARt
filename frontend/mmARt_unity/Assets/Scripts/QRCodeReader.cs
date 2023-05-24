using System.Collections;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.XR.ARFoundation;
using ZXing;

using System.Collections.Generic;
using UnityEngine.SceneManagement;

// using UnityEngine.XR.ARSubsystems;

public class QRCodeReader : MonoBehaviour
{
    [SerializeField]
    ARCameraBackground m_ARCameraBackground;

    [SerializeField]
    Text m_TextResult;

    BarcodeReader m_BarcodeReader;

    [SerializeField]
    private List<Target> navigationTargetObjects = new List<Target>();

    [SerializeField]
    private ARSession session;

    [SerializeField]
    private ARSessionOrigin sessionOrigin;

    void Start()
    {
        m_BarcodeReader = new BarcodeReader();
    }

    public void DecodeQRCode()
    {
        m_TextResult.text = "...";

        StartCoroutine("RecognizeQRCode");
    }

    // https://siddeshb88.medium.com/how-to-augment-in-real-world-using-qrcode-e1a344416288
    IEnumerator RecognizeQRCode()
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
                string QRContents = result.Text;

                if (QRContents.StartsWith("http"))
                {
                    Application.OpenURL(QRContents);
                }
                else
                {
                    m_TextResult.text = result.Text;
                    SetQrCodeRecenterTarget(result.Text);
                }
                break;
            }
            yield return new WaitForSeconds(0.05F);
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

    private void SetQrCodeRecenterTarget(string targetText)
    {
        Target currentTarget = navigationTargetObjects.Find(x => x.Name.ToLower().Equals(targetText.ToLower()));
        // Target currentTarget = navigationTargetObjects.Find(x => x.Name.ToLower().Equals("secondpoint"));
        if (currentTarget != null)
        {
            // Reset position and rotation of ARSession
            // session.Reset();
            // LoaderUtility.Deinitialize();
            // LoaderUtility.Initialize();
            // SceneManager.LoadScene("SampleScene", LoadSceneMode.Single);

            // Add offset for recentering
            sessionOrigin.transform.position = currentTarget.PositionObject.transform.position;
            sessionOrigin.transform.rotation = currentTarget.PositionObject.transform.rotation;

            GameObject.Find("Indicator").GetComponent<SetNavigationTarget>().GetPath(targetText);
        }
    }

}