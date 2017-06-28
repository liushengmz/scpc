using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.Serialization.Json;
using System.Text;

namespace Shotgun.Library
{
    partial class Static
    {
        public static T JsonParser<T>(string json)
        {
            var bin = ASCIIEncoding.UTF8.GetBytes(json);
            using (var stm = new MemoryStream(bin))
            {
                return JsonParser<T>(stm);
            }
        }

        public static T JsonParser<T>(Stream stm)
        {
            var dc = new DataContractJsonSerializer(typeof(T));
            return (T)dc.ReadObject(stm);
        }
    }
}
