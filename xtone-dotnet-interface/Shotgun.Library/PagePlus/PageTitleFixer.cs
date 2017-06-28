using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Shotgun.Model.Data;
using Shotgun.Model.List;
using Shotgun.Model.Filter;
using Shotgun.Model.Logical;
using Shotgun.Database;
using Shotgun.Library;
using System.Reflection;
using System.Web;

namespace Shotgun.PagePlus
{
    /// <summary>
    /// 修正Page.Title为空的bug
    /// 通用于在MasterPage onlod事件中
    /// </summary>
    public class PageTitleFixer
    {
        /// <summary>
        /// 读取网页标题，等效于string a=Page.Ttile;
        /// </summary>
        /// <returns></returns>
        public static string GetTitle()
        {
            System.Web.UI.Page Page = (System.Web.UI.Page)HttpContext.Current.Handler;
            if (Page.Header != null)
                return Page.Header.Title;

            Type T = typeof(System.Web.UI.Page);
            FieldInfo[] FIs = T.GetFields();
            FieldInfo fi = T.GetField("_titleToBeSet",
                    BindingFlags.Instance | BindingFlags.GetField | BindingFlags.NonPublic | BindingFlags.ExactBinding);
            object title = fi.GetValue(Page);
            if (title != null)
                return (string)title;
            return string.Empty;

        }

        /// <summary>
        /// 在当前标题上追加 相当于Page.Title+= paddingString + fixTitle;
        /// </summary>
        /// <param name="fixTitle">追加标题字符串</param>
        /// <param name="paddingString">分隔符（原标题为空时，此值将忽略）</param>
        public static void AppendTitle(string fixTitle,string paddingString)
        {
            string _title = GetTitle();
            System.Web.UI.Page page = (System.Web.UI.Page)HttpContext.Current.Handler;
            if (!string.IsNullOrEmpty(_title))
                page.Title = _title + paddingString + fixTitle;
            else
                page.Title = fixTitle;
        }

        /// <summary>
        /// 在当前标题上追加 相当于Page.Title+= " - "  + fixTitle;
        /// </summary>
        /// <param name="fixTitle">追加标题字符串</param>
        public static void AppendTitle(string fixTitle)
        {
            AppendTitle(fixTitle, " - ");
        }


    }
}
