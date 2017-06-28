using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;

namespace n8wan.Public.Logical
{
    public class JSonBaseSync : BaseSPCallback
    {
        protected JToken jRoot;

        protected override bool OnInit()
        {
            System.IO.StreamReader stm = null;
            try
            {
                using (stm = new System.IO.StreamReader(Request.InputStream))
                    jRoot = Newtonsoft.Json.Linq.JToken.Parse(stm.ReadToEnd());
            }
            catch
            {
                return false;
            }
            finally
            {
                if (stm != null)
                    stm.Dispose();
            }
            return base.OnInit();
        }


        public override string GetParamValue(string Field)
        {
            // Field 格式： NodeName/[ArrayIndex]/.....

            var ars = Field.Split(new char[] { '/' }, StringSplitOptions.RemoveEmptyEntries);
            JToken jtk = jRoot;

            for (var i = 0; i < ars.Length; i++)
            {
                if (jtk == null)
                    break;
                if (!ars[i].StartsWith("["))
                {//一般的数据
                    jtk = jtk[ars[i]];
                    continue;
                }

                //数组下标
                if (!(jtk is JArray))
                {//配置数组，但实际不是数据的同步
                    jtk = null;
                    break;
                }

                int k;
                if (!int.TryParse(ars[i].Substring(1, ars[i].Length - 2), out k))
                {
                    jtk = null;
                    break;
                }
                jtk = ((JArray)jtk)[k];
            }

            if (jtk != null)
            {
                object objValue = null;

                if (jtk is JValue)
                    objValue = ((JValue)jtk).Value;
                else if (jtk is JProperty)
                    objValue = ((JProperty)jtk).Value;

                if (objValue == null)
                    return null;
                return objValue.ToString();

            }

            return base.GetParamValue(Field);
        }

    }
}
