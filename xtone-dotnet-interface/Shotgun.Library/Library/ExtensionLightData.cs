using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using Shotgun.Model.Logical;

namespace Shotgun.Library
{
    public static class ExtensionLightDataExtension
    {
        /// <summary>
        /// 转换为DataTable对像
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="list"></param>
        /// <returns></returns>
        public static T ToDataTable<T>(this IEnumerable<LightDataModel> list) where T : DataTable, new()
        {
            var dt = new T();
            System.Data.DataRow row;

            row = dt.NewRow();
            object val;
            foreach (var item in list)
            {
                foreach (System.Data.DataColumn col in dt.Columns)
                {
                    val = item[col.ColumnName];
                    if (val == null)
                        row[col] = DBNull.Value;
                    else
                        row[col] = val;
                }
                dt.Rows.Add(row);
            }
            return dt;
        }

        /// <summary>
        /// 转换为DataTable对像
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="list"></param>
        /// <returns></returns>
        public static T[] GetFieldValueArray<T>(this IEnumerable<LightDataModel> list, string Field)
        {
            T[] ret = new T[list.Count()];
            if (ret.Length == 0)
                return ret;

            var t = list.First().GetType();

            var p = t.GetProperty(Field,
                System.Reflection.BindingFlags.Instance |
                System.Reflection.BindingFlags.Public |
                System.Reflection.BindingFlags.IgnoreCase |
                System.Reflection.BindingFlags.GetProperty);
            if (p == null)
                throw new KeyNotFoundException("不存在字段:" + Field);
            int i = 0;
            foreach (var m in list)
            {
                ret[i++] = (T)p.GetValue(m, null);
            }
            return ret;
        }

    }
}
