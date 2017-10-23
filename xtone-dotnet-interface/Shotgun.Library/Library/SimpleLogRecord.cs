using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace Shotgun.Library
{
    public class SimpleLogRecord
    {
        /// <summary>
        /// 写入日志，屏蔽了写入错误
        /// </summary>
        /// <param name="logFile">完整路径 或 仅文件名</param>
        /// <param name="msg"></param>
        public static void WriteLog(string logFile, string msg)
        {
            if (logFile == null)
                logFile = string.Empty;
            FileInfo fi;
            //是否绝路径
            bool isAbsPath;
            switch (Environment.OSVersion.Platform)
            {
                case PlatformID.Win32NT:
                case PlatformID.Win32S:
                case PlatformID.Win32Windows:
                case PlatformID.WinCE:
                    isAbsPath = logFile.Length > 2 && logFile.Substring(1, 1) == ":";
                    break;
                default:
                    isAbsPath = logFile.StartsWith(AppDomain.CurrentDomain.BaseDirectory);
                    break;
            }
            if (isAbsPath)
            {
                fi = new FileInfo(logFile);
            }
            else
            {
                logFile += "_" + DateTime.Today.ToString("yyyy-MM-dd") + ".log";
                fi = new FileInfo(AppDomain.CurrentDomain.BaseDirectory + "\\log\\" + logFile);
            }
            StreamWriter sWrite = null;
            try
            {
                DirectoryInfo di = fi.Directory;
                if (!di.Exists)
                    di.Create();

                sWrite = new StreamWriter(fi.FullName, true, Encoding.UTF8);
                sWrite.NewLine = "\r\n";
                sWrite.WriteLine("{0},{1}", DateTime.Now.ToString("HH:mm:ss"), msg);
                sWrite.Flush();
            }
            catch { }//防止写入出错,并发、权限或错误路径等问题
            finally
            {
                if (sWrite != null)
                {
                    try
                    {
                        sWrite.Close();
                        sWrite.Dispose();
                    }
                    catch { }
                    sWrite = null;
                }
            }



        }

        public static void WriteLog(string msg)
        {
            WriteLog(null, msg);
        }
    }
}
