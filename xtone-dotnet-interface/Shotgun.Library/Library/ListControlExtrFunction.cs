using System;
using System.Collections.Generic;
using System.Text;
using System.Web.UI.WebControls;

namespace Shotgun.Library
{
    /// <summary>
    /// ListControl 常方法
    /// </summary>
    public static class ListControlFunction
    {
        /// <summary>
        /// 取得列表控件中全部选定值
        /// </summary>
        /// <param name="cnt">控件名</param>
        /// <param name="formatString">值格式字符串</param>
        /// <param name="splitSring">两个值之间分隔字符</param>
        /// <returns></returns>
        public static string GetSelectedValues(ListControl cnt, string formatString, string splitSring)
        {
            string v = string.Empty;
            foreach (ListItem item in cnt.Items)
            {
                if (!item.Selected)
                    continue;
                v += splitSring;
                v += string.Format(formatString, item.Value);
            }
            if (string.IsNullOrEmpty(v))
                return string.Empty;
            return v.Substring(splitSring.Length);
        }

        /// <summary>
        /// 取得列表控件中全部选定值
        /// </summary>
        /// <param name="cnt"></param>
        /// <returns>返回 'xx','yy'</returns>
        public static string GetSelectedValues(ListControl cnt)
        {
            return GetSelectedValues(cnt, "{0}", ",");
        }

        /// <summary>
        /// 选中值
        /// </summary>
        /// <param name="cnt"></param>
        /// <param name="values"></param>
        /// <param name="textCompare"></param>
        public static void SetSelectedItems(ListControl cnt, string[] values, bool textCompare)
        {
            string v;
            foreach (string s in values)
            {
                v = s;
                if(textCompare)
                    v=v.Trim().ToLower();
                
                foreach (ListItem item in cnt.Items)
                {
                    if (textCompare)
                    {
                        if (v == item.Value.ToLower())
                        {
                            item.Selected = true;
                            break;
                        }
                    }
                    else
                    {
                        if (v == item.Value)
                        {
                            item.Selected = true;
                            break;
                        }
                    }

                }//end for ListItem
            }
        }

        /// <summary>
        /// 全先或全部取消
        /// </summary>
        /// <param name="cnt"></param>
        /// <param name="state"></param>
        public static void SetAllCheckState(ListControl cnt, bool state)
        {
            foreach (ListItem li in cnt.Items)
            {
                li.Selected = state;
            }
        }

    }
}
