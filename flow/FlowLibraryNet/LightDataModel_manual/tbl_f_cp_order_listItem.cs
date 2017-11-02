using FlowLibraryNet.Logical;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LightDataModel
{
    partial class tbl_f_cp_order_listItem : Shotgun.Model.Logical.DynamicDataItem, IFlowOrderInfo
    {

        public const string FIX_TABLE_NAME = "tbl_f_cp_order_list_";
        public const string SCHEMA = "daily_log";
        public tbl_f_cp_order_listItem()
        {
            Schema = SCHEMA;
            base.SetTableName(DateTime.Today);
        }

        protected override string FixTableName
        {
            get { return FIX_TABLE_NAME; }
        }


        #region 静态方法
        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.DynamicTableList<tbl_f_cp_order_listItem> GetQueries(Shotgun.Database.IBaseDataClass2 dBase)
        {
            return new Shotgun.Model.List.DynamicTableList<tbl_f_cp_order_listItem>(dBase, SCHEMA, FIX_TABLE_NAME);
        }

        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.DynamicTableList<tbl_f_cp_order_listItem> GetQueries()
        {
            return new Shotgun.Model.List.DynamicTableList<tbl_f_cp_order_listItem>(null, SCHEMA, FIX_TABLE_NAME);
        }
        /// <summary>
        /// 根据主键查找指定的行,返回指定字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <param name="fields">返回字段</param>
        /// <returns></returns>
        public static tbl_f_cp_order_listItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id, string[] fields)
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
        public static tbl_f_cp_order_listItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id)
        {
            return GetRowById(dBase, id, null);
        }

        #endregion

        #region IFlowOrderInfo

        string IFlowOrderInfo.SpOrderId { get { return this.sp_order_id; } set { sp_order_id = value; } }

        ChangeOrderStatusEnum IFlowOrderInfo.StatusE { get => (ChangeOrderStatusEnum)status; set => status = (int)value; }
        string IFlowOrderInfo.SpErrorMsg { get => this.sp_error_msg; set => this.sp_error_msg = value; }
        string IFlowOrderInfo.SpStatus { get => this.sp_status; set => this.sp_status = value; }
        int IFlowOrderInfo.PriceId { get => this.base_price_id; set => this.base_price_id = value; }
        string IFlowOrderInfo.Mobile { get => this.sp_order_id; set => sp_order_id = value; }

        #endregion

    }
}
