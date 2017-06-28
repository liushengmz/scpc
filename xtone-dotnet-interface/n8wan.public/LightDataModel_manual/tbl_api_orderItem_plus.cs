using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    partial class tbl_api_orderItem
    {
        public const string FIX_TABLE_NAME = "tbl_api_order_";
        public const string SCHEMA = "daily_log";
        public tbl_api_orderItem()
        {
            Schema = "daily_log";
            base.SetTableName(DateTime.Today);
        }

        protected override string FixTableName
        {
            get { return FIX_TABLE_NAME; }
        }

        /// <summary>
        /// 根据情况，强行截断部分超长字段值
        /// </summary>
        public void TruncationVarChar()
        {
            TruncationFunc(ref _extra_param, 200);
            TruncationFunc(ref _ExtrData, 32);
            TruncationFunc(ref _extra_param, 200);
            TruncationFunc(ref _user_agent, 100);
            TruncationFunc(ref _packagename, 50);
            TruncationFunc(ref _sp_exField, 512);
            TruncationFunc(ref _port, 32);
            TruncationFunc(ref _msg, 200);
        }
        void TruncationFunc(ref string value, int maxLen)
        {
            if (value == null)
                return;
            if (value.Length <= maxLen)
                return;
            value = value.Substring(0, maxLen - 5) + "...";
        }


        #region 静态方法
        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.DynamicTableList<tbl_api_orderItem> GetQueries(Shotgun.Database.IBaseDataClass2 dBase)
        {
            return new Shotgun.Model.List.DynamicTableList<tbl_api_orderItem>(dBase, SCHEMA, FIX_TABLE_NAME);
        }

        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.DynamicTableList<tbl_api_orderItem> GetQueries()
        {
            return new Shotgun.Model.List.DynamicTableList<tbl_api_orderItem>(null, SCHEMA, FIX_TABLE_NAME);
        }
        /// <summary>
        /// 根据主键查找指定的行,返回指定字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <param name="fields">返回字段</param>
        /// <returns></returns>
        public static tbl_api_orderItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id, string[] fields)
        {
            var q = GetQueries(dBase);
            q.Fields = fields;
            q.Filter.AndFilters.Add(identifyField, id);
            return q.GetRowByFilters();
        }

        /// <summary>
        /// 根据主键查找指定的行,返回所有字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <returns></returns>
        public static tbl_api_orderItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id)
        {
            return GetRowById(dBase, id, null);
        }

        #endregion
    }
}
