using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    partial class tbl_cpItem
    {
        /// <summary>
        /// 根据userId查找指定的行,返回所有字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="userId">tbl_user的ID</param>
        /// <returns></returns>
        public static tbl_cpItem GetRowByUserId(Shotgun.Database.IBaseDataClass2 dBase, int userId)
        {
            return GetRowByUserId(dBase, userId, null);
        }

        /// <summary>
        /// 根据userId查找指定的行,返回所有字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="userId">主键值</param>
        /// <param name="fields">返回字段</param>
        /// <returns></returns>
        public static tbl_cpItem GetRowByUserId(Shotgun.Database.IBaseDataClass2 dBase, int userId, string[] fields)
        {
            var q = GetQueries(dBase);
            q.Fields = fields;
            q.Filter.AndFilters.Add(Fields.user_id, userId);
            return q.GetRowByFilters();
        }


        /// <summary>
        /// 
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="cpId"></param>
        /// <param name="filed">0：short_name,1:full_name</param>
        /// <returns></returns>
        public static string GetCPNameById(Shotgun.Database.BDClass dBase, int id, string filed)
        {
            var l = GetQueries(dBase);
            l.Filter.AndFilters.Add(Fields.id, id);
            var t = l.ExecuteScalar(filed);
            if (t == null || DBNull.Value.Equals(t))
                return null;
            if (t is string)
                return (string)t;
            return t.ToString();
        }

        public static string GetCPNameById(Shotgun.Database.BDClass dBase, int id, bool isFullName)
        {
            return GetCPNameById(dBase, id, (isFullName ? Fields.full_name : Fields.short_name));
        }

        public static List<tbl_cpItem> GetCPsByIds(Shotgun.Database.BDClass dBase, IList<int> ids, string[] fields)
        {
            if (ids == null || ids.Count() == 0)
                return null;
            var l = GetQueries(dBase);
            l.Fields = fields;
            l.Filter.AndFilters.Add(Fields.id, ids);
            l.PageSize = int.MaxValue;

            return l.GetDataList();
        }
    }
}
