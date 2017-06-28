using System;
using System.Collections.Generic;
using System.Text;
using System.Threading;
using System.Net;
using System.IO;

namespace Shotgun.Library
{
    /// <summary>
    /// 用于异步生成静态页面
    /// </summary>
    public class AsyncRemoteRequest
    {

        const int timerOut = 2888;

        string _url,_toFile;
        byte[] _postData;

        /// <summary>
        /// 禁止外部生成实例
        /// </summary>
        private AsyncRemoteRequest()
        {
        }

        /// <summary>
        /// 异步下载远程文件
        /// </summary>
        /// <param name="url"></param>
        /// <param name="file">保存到文件(绝对路径)</param>
        public static void DownloadFile(string url,string file)
        {
            AsyncRemoteRequest arq= new AsyncRemoteRequest();
            arq._url = url;
            arq._toFile = file;

            ThreadPool.QueueUserWorkItem(arq.DownloadFile, arq); 
        }

        /// <summary>
        /// 仅请求远程数据
        /// 当收到回应时,立即中止
        /// </summary>
        /// <param name="url"></param>
        public static void RequestOnly(string url)
        {
            AsyncRemoteRequest arq = new AsyncRemoteRequest();
            arq._url = url;

            ThreadPool.QueueUserWorkItem(arq.RequestOnly, arq);
        }
        /// <summary>
        /// 仅请求远程数据(POST)
        /// 当收到回应时,立即中止
        /// </summary>
        /// <param name="url"></param>
        public static void RequestOnly(string url, byte[] postData)
        {
            AsyncRemoteRequest arq = new AsyncRemoteRequest();
            arq._url = url;
            arq._postData = postData;

            ThreadPool.QueueUserWorkItem(arq.RequestOnly, arq);
        }

        private void RequestOnly(object t)
        {
            WebRequest web = WebRequest.Create(_url);
            web.Timeout = 888;
            WebResponse rsp = null;
            try
            {
                if (_postData != null)
                {
                    System.Net.ServicePointManager.Expect100Continue = false;
                    web.Method = "POST";
                    web.ContentType = "application/x-www-form-urlencoded";
                    web.ContentLength = _postData.Length;
                    Stream stm=   web.GetRequestStream();
                    stm.Write(_postData, 0, _postData.Length);
                    stm.Close();
                }
                rsp = web.GetResponse();
            }

            catch { }
            finally
            {
                if (rsp != null)
                    rsp.Close();
            }

        }

        private void DownloadFile(object t)
        {
            WebClient wc = new MyWebClient( timerOut );
            try
            {
                wc.DownloadFile(_url, _toFile);
            }
            catch { }
            finally
            {
                wc.Dispose();
            }
            

        }

        class MyWebClient : WebClient
        {
            int _timerOut;
            internal MyWebClient(int timerOut):base(){
                _timerOut = timerOut;
            }

            protected override WebRequest GetWebRequest(Uri address)
            {
                WebRequest web =base.GetWebRequest(address);

                web.Timeout = this._timerOut;

                return web;
            }
        }
        
    }
}
