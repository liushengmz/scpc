using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web.UI.WebControls;

namespace Shotgun.Library
{
    public static class ExtensionListitem
    {
        /// <summary>
        /// 选写指定的项
        /// </summary>
        /// <param name="items"></param>
        /// <param name="value"></param>
        public static void SelectByValue(this ListItemCollection items, string value)
        {
            var item = items.FindByValue(value);
            if (item != null)
                item.Selected = true;
        }

        /// <summary>
        /// 选写指定的项
        /// </summary>
        /// <param name="items"></param>
        /// <param name="value"></param>
        /// <param name="defaultValue"></param>
        public static void SelectByValue(this ListItemCollection items, string value, string defaultValue)
        {
            var item = items.FindByValue(value);
            if (item != null)
                item = items.FindByValue(defaultValue);
            if (item != null)
                item.Selected = true;
        }

        /// <summary>
        /// 选写指定的项（多先）
        /// </summary>
        /// <param name="items"></param>
        /// <param name="values"></param>
        public static void SelectByValue(this ListItemCollection items, string[] values)
        {
            foreach (var s in values)
            {
                items.SelectByValue(s);
            }
        }

        /// <summary>
        /// 取得所有选写值，与html form格式一至
        /// </summary>
        /// <param name="items"></param>
        /// <returns></returns>
        public static string GetSelectedValues(this ListItemCollection items)
        {
            return items.GetSelectedValues(", ", e => e.Value);
        }

        /// <summary>
        /// 取得所有选写值
        /// </summary>
        /// <param name="items"></param>
        /// <param name="separator">分割符</param>
        /// <param name="formatFun">选定值格式处理</param>
        /// <returns></returns>
        public static string GetSelectedValues(this ListItemCollection items, string separator, Func<ListItem, string> formatFun)
        {
            StringBuilder sb = new StringBuilder(64);
            foreach (ListItem item in items)
            {
                if (!item.Selected)
                    continue;
                sb.Append(separator);
                sb.Append(formatFun(item));
            }
            if (sb.Length == 0)
                return string.Empty;
            sb.Remove(0, separator.Length);
            return sb.ToString();
        }
    }
}
