using System;
using System.Collections.Generic;
using System.Text;
using System.Web.Caching;
using System.Web;
using System.IO;

namespace Shotgun.Library
{
    public class StaticFile
    {

        public static string LoadFileWithCache(string file, Encoding encoder)
        {
            string cID="LFWC_" +Static.StrId( file);

            object t=HttpContext.Current.Cache[cID];
            if (t != null)
                return (string)t;
            FileInfo fi= new FileInfo(HttpContext.Current.Server.MapPath(file));
            if(!fi.Exists)
                return null;

            StreamReader stm = new StreamReader(fi.FullName, encoder);
            t = stm.ReadToEnd();
            stm.Dispose();
            HttpContext.Current.Cache.Insert(cID, t, new CacheDependency(fi.FullName));
            return (string)t;
        }
    }
}
