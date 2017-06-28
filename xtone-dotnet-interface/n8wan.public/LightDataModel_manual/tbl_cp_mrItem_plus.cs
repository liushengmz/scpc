using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    partial class tbl_cp_mrItem
    {
        public const string FIX_TABLE_NAME = "tbl_cp_mr_";
        public const string SCHEMA = "daily_log";
        public tbl_cp_mrItem()
        {
            Schema = "daily_log";
            base.SetTableName(DateTime.Today);
        }

        protected override string FixTableName
        {
            get { return FIX_TABLE_NAME; }
        }

        public tbl_cp_mr_dailyItem CopyToDailyCpMr(tbl_cp_mr_dailyItem daily)
        {
            bool isNew = daily == null;
            if (isNew)
                daily = new tbl_cp_mr_dailyItem();
            string[] fields = new string[] {
                "imei"	,"imsi"	,"mobile"	,"mcc"	,"province_id"	,"city_id"	,"trone_order_id"	,"ori_trone"	,
                "ori_order"	,"linkid"	,"cp_param"	,"service_code"	,"price"	,"ip"	,"syn_status"	,
                "mr_table"	,"mr_id"	,"ivr_time"	,"trone_type"	,"mr_date"	,"create_date"	};

            daily.IgnoreEquals = true;
            foreach (var f in fields)
            {
                object t = this[f];
                if (isNew && t == null)
                    continue;
                daily[f] = this[f];
            }
            if (isNew)
                daily.cp_mr_id = this.id;
            return daily;
        }

        #region 静态方法
        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.DynamicTableList<tbl_cp_mrItem> GetQueries(Shotgun.Database.IBaseDataClass2 dBase)
        {
            return new Shotgun.Model.List.DynamicTableList<tbl_cp_mrItem>(dBase, SCHEMA, FIX_TABLE_NAME);
        }

        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.DynamicTableList<tbl_cp_mrItem> GetQueries()
        {
            return new Shotgun.Model.List.DynamicTableList<tbl_cp_mrItem>(null, SCHEMA, FIX_TABLE_NAME);
        }
        /// <summary>
        /// 根据主键查找指定的行,返回指定字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <param name="fields">返回字段</param>
        /// <returns></returns>
        public static tbl_cp_mrItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id, string[] fields)
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
        public static tbl_cp_mrItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id)
        {
            return GetRowById(dBase, id, null);
        }

        #endregion

    }
}
