using FlowLibraryNet.Logical;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LightDataModel
{
    partial class tbl_f_ch_orderItem : Shotgun.Model.Logical.DynamicDataItem, IFlowOrderInfo
    {
        public const string FIX_TABLE_NAME = "tbl_f_ch_order_";
        public const string SCHEMA = "daily_log";
        public tbl_f_ch_orderItem()
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
        public static Shotgun.Model.List.DynamicTableList<tbl_f_ch_orderItem> GetQueries(Shotgun.Database.IBaseDataClass2 dBase)
        {
            return new Shotgun.Model.List.DynamicTableList<tbl_f_ch_orderItem>(dBase, SCHEMA, FIX_TABLE_NAME);
        }

        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.DynamicTableList<tbl_f_ch_orderItem> GetQueries()
        {
            return new Shotgun.Model.List.DynamicTableList<tbl_f_ch_orderItem>(null, SCHEMA, FIX_TABLE_NAME);
        }
        /// <summary>
        /// 根据主键查找指定的行,返回指定字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <param name="fields">返回字段</param>
        /// <returns></returns>
        public static tbl_f_ch_orderItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id, string[] fields)
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
        public static tbl_f_ch_orderItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id)
        {
            return GetRowById(dBase, id, null);
        }

        #endregion

        #region IFlowOrderInfo
        /// <summary>
        /// 我方平台订单号
        /// </summary>
        string IFlowOrderInfo.OrderId { get => this.server_order; set => this.server_order = value; }

        string IFlowOrderInfo.SpErrorMsg { get => this.sp_err_msg; set => this.sp_err_msg = value; }
        string IFlowOrderInfo.SpStatus { get => this.sp_status; set => this.sp_status = value; }
        int IFlowOrderInfo.PriceId { get => this.price_id; }
        string IFlowOrderInfo.Mobile { get => this.mobile; }

        int IFlowOrderInfo.Rang { get => this.rang; }

        ChangeOrderStatusEnum IFlowOrderInfo.StatusE
        {
            get => (ChangeOrderStatusEnum)this.charge_status;
            set
            {
                switch (value)
                {
                    case ChangeOrderStatusEnum.Unkonw:
                    case ChangeOrderStatusEnum.Charging:
                        this.charge_status = 0; break;
                    case ChangeOrderStatusEnum.Success:
                        this.charge_status = 1; break;
                    case ChangeOrderStatusEnum.BalanceLow:
                    case ChangeOrderStatusEnum.ChargFail:
                    case ChangeOrderStatusEnum.GatewayError:
                    case ChangeOrderStatusEnum.GatewayTimeout:
                    case ChangeOrderStatusEnum.InnerError:
                    case ChangeOrderStatusEnum.SpUnkowError:
                    default:
                        this.charge_status = 2;
                        break;
                }
            }
        }

        OperatorType IFlowOrderInfo.Operator { get => (OperatorType)this.@operator; }

        #endregion



    }
}
