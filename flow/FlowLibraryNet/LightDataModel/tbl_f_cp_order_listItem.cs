using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    /// <summary>
    /// tbl_f_cp_order_list_201709数据模型
    /// </summary>
    public partial class tbl_f_cp_order_listItem
    {
        /// <summary>
        /// 数据表字段列表对像
        /// </summary>
        public sealed class Fields
        {
            private Fields() { }
            #region 表字段名称

            public const string id = "id";
            ///<summary>
            ///主键
            ///</summary>
            public const string PrimaryKey = "id";

            /// <summary>
            /// 父表的月份是哪个
            /// </summary>
            public const string month = "month";
            /// <summary>
            /// >0则是CP订单下的子订单，<=0则是CHANNEL USER 订单
            /// </summary>
            public const string p_id = "p_id";
            /// <summary>
            /// 手机号
            /// </summary>
            public const string mobile = "mobile";
            /// <summary>
            /// CP订单号
            /// </summary>
            public const string cp_order_id = "cp_order_id";
            /// <summary>
            /// 我方订单号
            /// </summary>
            public const string sp_order_id = "sp_order_id";
            /// <summary>
            /// 运营商
            /// </summary>
            public const string @operator = "operator";
            /// <summary>
            /// 流量大小
            /// </summary>
            public const string flowsize = "flowsize";
            /// <summary>
            /// 流量使用范围,0全国,1充值手机省份
            /// </summary>
            public const string use_rang = "use_rang";
            /// <summary>
            /// 使用时间类型,0当月,1是20天,2是1天,3是2天
            /// </summary>
            public const string time_type = "time_type";
            /// <summary>
            /// 符合该TRONE_ID
            /// </summary>
            public const string trone_id = "trone_id";
            /// <summary>
            /// 订单状态(0:未处理，１处理中，２，提交供应商成功，３提交ＳＰ失败，４充值成功,5退款)
            /// </summary>
            public const string status = "status";

            public const string create_date = "create_date";

            public const string cp_id = "cp_id";

            public const string sp_status = "sp_status";

            public const string sp_error_msg = "sp_error_msg";
            /// <summary>
            /// cp 折扣
            /// </summary>
            public const string cp_ratio = "cp_ratio";
            /// <summary>
            /// sp 折扣
            /// </summary>
            public const string sp_ratio = "sp_ratio";

            public const string sp_trone_id = "sp_trone_id";
            /// <summary>
            /// tbl_f_trone.id
            /// </summary>
            public const string cp_trone_id = "cp_trone_id";

            public const string base_price_id = "base_price_id";

            public const string sp_id = "sp_id";

            public const string sp_api_id = "sp_api_id";


            #endregion

        }
        #region 表字段变量定义
        /// <summary>
        /// 父表的月份是哪个
        /// </summary>
        private string _month;
        /// <summary>
        /// >0则是CP订单下的子订单，<=0则是CHANNEL USER 订单
        /// </summary>
        private int _p_id;
        /// <summary>
        /// 手机号
        /// </summary>
        private string _mobile;
        /// <summary>
        /// CP订单号
        /// </summary>
        private string _cp_order_id;
        /// <summary>
        /// 我方订单号
        /// </summary>
        private string _sp_order_id;
        /// <summary>
        /// 运营商
        /// </summary>
        private byte _operator;
        /// <summary>
        /// 流量大小
        /// </summary>
        private int _flowsize;
        /// <summary>
        /// 流量使用范围,0全国,1充值手机省份
        /// </summary>
        private byte _use_rang;
        /// <summary>
        /// 使用时间类型,0当月,1是20天,2是1天,3是2天
        /// </summary>
        private byte _time_type;
        /// <summary>
        /// 符合该TRONE_ID
        /// </summary>
        private int _trone_id;
        /// <summary>
        /// 订单状态(0:未处理，１处理中，２，提交供应商成功，３提交ＳＰ失败，４充值成功,5退款)
        /// </summary>
        private int _status;

        private DateTime _create_date;

        private int _cp_id;

        private string _sp_status;

        private string _sp_error_msg;
        /// <summary>
        /// cp 折扣
        /// </summary>
        private int _cp_ratio;
        /// <summary>
        /// sp 折扣
        /// </summary>
        private int _sp_ratio;

        private int _sp_trone_id;
        /// <summary>
        /// tbl_f_trone.id
        /// </summary>
        private int _cp_trone_id;

        private int _base_price_id;

        private int _sp_id;

        #endregion

        public override string IdentifyField { get { return identifyField; } }

        public static readonly string identifyField = "id";
        private int _sp_api_id;

        public int id
        {
            get; set;
        }

        #region 表字段值存取
        /// <summary>
        /// 父表的月份是哪个
        /// </summary>
        public string month
        {
            get { return this._month; }
            set
            {
#if false && false
				RemoveNullFlag(Fields.month);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.month);
                else
                    RemoveNullFlag(Fields.month);
#endif

                SetFieldHasUpdate(Fields.month, this._month, value);
                this._month = value;
            }
        }
        /// <summary>
        /// >0则是CP订单下的子订单，<=0则是CHANNEL USER 订单
        /// </summary>
        public int p_id
        {
            get { return this._p_id; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.p_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.p_id);
                else
                    RemoveNullFlag(Fields.p_id);
#endif

                SetFieldHasUpdate(Fields.p_id, this._p_id, value);
                this._p_id = value;
            }
        }
        /// <summary>
        /// 手机号
        /// </summary>
        public string mobile
        {
            get { return this._mobile; }
            set
            {
#if false && false
				RemoveNullFlag(Fields.mobile);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.mobile);
                else
                    RemoveNullFlag(Fields.mobile);
#endif

                SetFieldHasUpdate(Fields.mobile, this._mobile, value);
                this._mobile = value;
            }
        }
        /// <summary>
        /// CP订单号
        /// </summary>
        public string cp_order_id
        {
            get { return this._cp_order_id; }
            set
            {
#if false && false
				RemoveNullFlag(Fields.cp_order_id);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.cp_order_id);
                else
                    RemoveNullFlag(Fields.cp_order_id);
#endif

                SetFieldHasUpdate(Fields.cp_order_id, this._cp_order_id, value);
                this._cp_order_id = value;
            }
        }

        public int sp_api_id
        {
            get { return this._sp_api_id; }
            set
            {
                SetFieldHasUpdate(Fields.sp_api_id, this._sp_api_id, value);
                this._sp_api_id = value;
            }
        }



        /// <summary>
        /// 我方订单号
        /// </summary>
        public string sp_order_id
        {
            get { return this._sp_order_id; }
            set
            {
#if false && false
				RemoveNullFlag(Fields.sp_order_id);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.sp_order_id);
                else
                    RemoveNullFlag(Fields.sp_order_id);
#endif

                SetFieldHasUpdate(Fields.sp_order_id, this._sp_order_id, value);
                this._sp_order_id = value;
            }
        }
        /// <summary>
        /// 运营商
        /// </summary>
        public byte @operator
        {
            get { return this._operator; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.@operator);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.@operator);
                else
                    RemoveNullFlag(Fields.@operator);
#endif

                SetFieldHasUpdate(Fields.@operator, this._operator, value);
                this._operator = value;
            }
        }
        /// <summary>
        /// 流量大小
        /// </summary>
        public int flowsize
        {
            get { return this._flowsize; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.flowsize);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.flowsize);
                else
                    RemoveNullFlag(Fields.flowsize);
#endif

                SetFieldHasUpdate(Fields.flowsize, this._flowsize, value);
                this._flowsize = value;
            }
        }
        /// <summary>
        /// 流量使用范围,0全国,1充值手机省份
        /// </summary>
        public byte use_rang
        {
            get { return this._use_rang; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.use_rang);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.use_rang);
                else
                    RemoveNullFlag(Fields.use_rang);
#endif

                SetFieldHasUpdate(Fields.use_rang, this._use_rang, value);
                this._use_rang = value;
            }
        }
        /// <summary>
        /// 使用时间类型,0当月,1是20天,2是1天,3是2天
        /// </summary>
        public byte time_type
        {
            get { return this._time_type; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.time_type);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.time_type);
                else
                    RemoveNullFlag(Fields.time_type);
#endif

                SetFieldHasUpdate(Fields.time_type, this._time_type, value);
                this._time_type = value;
            }
        }
        /// <summary>
        /// 符合该TRONE_ID
        /// </summary>
        public int trone_id
        {
            get { return this._trone_id; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.trone_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.trone_id);
                else
                    RemoveNullFlag(Fields.trone_id);
#endif

                SetFieldHasUpdate(Fields.trone_id, this._trone_id, value);
                this._trone_id = value;
            }
        }
        /// <summary>
        /// 订单状态(0:未处理，１处理中，２，提交供应商成功，３提交ＳＰ失败，４充值成功,5退款)
        /// </summary>
        public int status
        {
            get { return this._status; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.status);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.status);
                else
                    RemoveNullFlag(Fields.status);
#endif

                SetFieldHasUpdate(Fields.status, this._status, value);
                this._status = value;
            }
        }

        public DateTime create_date
        {
            get { return this._create_date; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.create_date);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.create_date);
                else
                    RemoveNullFlag(Fields.create_date);
#endif

                SetFieldHasUpdate(Fields.create_date, this._create_date, value);
                this._create_date = value;
            }
        }

        public int cp_id
        {
            get { return this._cp_id; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.cp_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.cp_id);
                else
                    RemoveNullFlag(Fields.cp_id);
#endif

                SetFieldHasUpdate(Fields.cp_id, this._cp_id, value);
                this._cp_id = value;
            }
        }

        public string sp_status
        {
            get { return this._sp_status; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.sp_status);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.sp_status);
                else
                    RemoveNullFlag(Fields.sp_status);
#endif

                SetFieldHasUpdate(Fields.sp_status, this._sp_status, value);
                this._sp_status = value;
            }
        }

        public string sp_error_msg
        {
            get { return this._sp_error_msg; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.sp_error_msg);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.sp_error_msg);
                else
                    RemoveNullFlag(Fields.sp_error_msg);
#endif

                SetFieldHasUpdate(Fields.sp_error_msg, this._sp_error_msg, value);
                this._sp_error_msg = value;
            }
        }
        /// <summary>
        /// cp 折扣
        /// </summary>
        public int cp_ratio
        {
            get { return this._cp_ratio; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.cp_ratio);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.cp_ratio);
                else
                    RemoveNullFlag(Fields.cp_ratio);
#endif

                SetFieldHasUpdate(Fields.cp_ratio, this._cp_ratio, value);
                this._cp_ratio = value;
            }
        }
        /// <summary>
        /// sp 折扣
        /// </summary>
        public int sp_ratio
        {
            get { return this._sp_ratio; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.sp_ratio);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.sp_ratio);
                else
                    RemoveNullFlag(Fields.sp_ratio);
#endif

                SetFieldHasUpdate(Fields.sp_ratio, this._sp_ratio, value);
                this._sp_ratio = value;
            }
        }

        public int sp_trone_id
        {
            get { return this._sp_trone_id; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.sp_trone_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.sp_trone_id);
                else
                    RemoveNullFlag(Fields.sp_trone_id);
#endif

                SetFieldHasUpdate(Fields.sp_trone_id, this._sp_trone_id, value);
                this._sp_trone_id = value;
            }
        }
        /// <summary>
        /// tbl_f_trone.id
        /// </summary>
        public int cp_trone_id
        {
            get { return this._cp_trone_id; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.cp_trone_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.cp_trone_id);
                else
                    RemoveNullFlag(Fields.cp_trone_id);
#endif

                SetFieldHasUpdate(Fields.cp_trone_id, this._cp_trone_id, value);
                this._cp_trone_id = value;
            }
        }

        public int base_price_id
        {
            get { return this._base_price_id; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.base_price_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.base_price_id);
                else
                    RemoveNullFlag(Fields.base_price_id);
#endif

                SetFieldHasUpdate(Fields.base_price_id, this._base_price_id, value);
                this._base_price_id = value;
            }
        }

        public int sp_id
        {
            get { return this._sp_id; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.sp_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.sp_id);
                else
                    RemoveNullFlag(Fields.sp_id);
#endif

                SetFieldHasUpdate(Fields.sp_id, this._sp_id, value);
                this._sp_id = value;
            }
        }

        #endregion
        #region 空值相关方法
        protected override string[] GetNullableFields()
        {
            return new string[]{null
            ,"operator"
,"sp_status"
,"sp_error_msg"
,"base_price_id"
,"sp_id"
};
        }
        public bool IsoperatorNull() { return IsNull(Fields.@operator); }

        public void SetoperatorNull() { SetNull(Fields.@operator); }
        public bool Issp_statusNull() { return IsNull(Fields.sp_status); }

        public void Setsp_statusNull() { SetNull(Fields.sp_status); }
        public bool Issp_error_msgNull() { return IsNull(Fields.sp_error_msg); }

        public void Setsp_error_msgNull() { SetNull(Fields.sp_error_msg); }
        public bool Isbase_price_idNull() { return IsNull(Fields.base_price_id); }

        public void Setbase_price_idNull() { SetNull(Fields.base_price_id); }
        public bool Issp_idNull() { return IsNull(Fields.sp_id); }

        public void Setsp_idNull() { SetNull(Fields.sp_id); }

        #endregion

    }
}