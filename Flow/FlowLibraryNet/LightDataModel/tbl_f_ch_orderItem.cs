using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    /// <summary>
    /// tbl_f_ch_order_201711数据模型
    /// </summary>
    public partial class tbl_f_ch_orderItem
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


            public const string trone_id = "trone_id";

            public const string sp_trone_id = "sp_trone_id";

            public const string sp_id = "sp_id";

            public const string sp_api_id = "sp_api_id";

            public const string flow_size = "flow_size";

            public const string flow_name = "flow_name";

            public const string price = "price";

            public const string sp_ratio = "sp_ratio";
            /// <summary>
            /// 0全国1省内
            /// </summary>
            public const string rang = "rang";

            public const string price_id = "price_id";
            /// <summary>
            /// 0否1是
            /// </summary>
            public const string send_sms = "send_sms";

            public const string mobile = "mobile";

            public const string province_id = "province_id";

            public const string city_id = "city_id";

            public const string @operator = "operator";

            public const string channel_child_id = "channel_child_id";

            public const string channel_id = "channel_id";

            public const string channel_child_ratio = "channel_child_ratio";

            public const string channel_ratio = "channel_ratio";

            public const string pay_money = "pay_money";
            /// <summary>
            /// 0处理中，1成功，2失败
            /// </summary>
            public const string pay_status = "pay_status";
            /// <summary>
            /// 0未发送，1已发送
            /// </summary>
            public const string send_sms_status = "send_sms_status";
            /// <summary>
            /// 流量充值供应商返回结果
            /// </summary>
            public const string sp_status = "sp_status";
            /// <summary>
            /// 流量充值供应商返回状态说明
            /// </summary>
            public const string sp_err_msg = "sp_err_msg";
            /// <summary>
            /// 0待充值，1充值成功，2充值失败
            /// </summary>
            public const string charge_status = "charge_status";

            public const string server_order = "server_order";

            public const string create_date = "create_date";

            #endregion

        }
        #region 表字段变量定义

        private int _trone_id;

        private int _sp_trone_id;

        private int _sp_id;

        private int _sp_api_id;

        private int _flow_size;

        private string _flow_name;

        private int _price;

        private int _sp_ratio;
        /// <summary>
        /// 0全国1省内
        /// </summary>
        private int _rang;

        private int _price_id;
        /// <summary>
        /// 0否1是
        /// </summary>
        private int _send_sms;

        private string _mobile;

        private int _province_id;

        private int _city_id;

        private int _operator;

        private int _channel_child_id;

        private int _channel_id;

        private int _channel_child_ratio;

        private int _channel_ratio;

        private int _pay_money;
        /// <summary>
        /// 0处理中，1成功，2失败
        /// </summary>
        private int _pay_status;
        /// <summary>
        /// 0未发送，1已发送
        /// </summary>
        private int _send_sms_status;
        /// <summary>
        /// 流量充值供应商返回结果
        /// </summary>
        private string _sp_status;
        /// <summary>
        /// 流量充值供应商返回状态说明
        /// </summary>
        private string _sp_err_msg;
        /// <summary>
        /// 0待充值，1充值成功，2充值失败
        /// </summary>
        private int _charge_status;

        private string _server_order;

        private DateTime _create_date;

        #endregion

        public override string IdentifyField { get { return identifyField; } }

        public static readonly string identifyField = "id";


        public int id
        {
            get; set;
        }

        #region 表字段值存取

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

        public int sp_id
        {
            get { return this._sp_id; }
            set
            {
#if true && false
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

        public int sp_api_id
        {
            get { return this._sp_api_id; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.sp_api_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.sp_api_id);
                else
                    RemoveNullFlag(Fields.sp_api_id);
#endif

                SetFieldHasUpdate(Fields.sp_api_id, this._sp_api_id, value);
                this._sp_api_id = value;
            }
        }

        public int flow_size
        {
            get { return this._flow_size; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.flow_size);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.flow_size);
                else
                    RemoveNullFlag(Fields.flow_size);
#endif

                SetFieldHasUpdate(Fields.flow_size, this._flow_size, value);
                this._flow_size = value;
            }
        }

        public string flow_name
        {
            get { return this._flow_name; }
            set
            {
#if false && false
				RemoveNullFlag(Fields.flow_name);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.flow_name);
                else
                    RemoveNullFlag(Fields.flow_name);
#endif

                SetFieldHasUpdate(Fields.flow_name, this._flow_name, value);
                this._flow_name = value;
            }
        }

        public int price
        {
            get { return this._price; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.price);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.price);
                else
                    RemoveNullFlag(Fields.price);
#endif

                SetFieldHasUpdate(Fields.price, this._price, value);
                this._price = value;
            }
        }

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
        /// <summary>
        /// 0全国1省内
        /// </summary>
        public int rang
        {
            get { return this._rang; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.rang);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.rang);
                else
                    RemoveNullFlag(Fields.rang);
#endif

                SetFieldHasUpdate(Fields.rang, this._rang, value);
                this._rang = value;
            }
        }

        public int price_id
        {
            get { return this._price_id; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.price_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.price_id);
                else
                    RemoveNullFlag(Fields.price_id);
#endif

                SetFieldHasUpdate(Fields.price_id, this._price_id, value);
                this._price_id = value;
            }
        }
        /// <summary>
        /// 0否1是
        /// </summary>
        public int send_sms
        {
            get { return this._send_sms; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.send_sms);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.send_sms);
                else
                    RemoveNullFlag(Fields.send_sms);
#endif

                SetFieldHasUpdate(Fields.send_sms, this._send_sms, value);
                this._send_sms = value;
            }
        }

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

        public int province_id
        {
            get { return this._province_id; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.province_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.province_id);
                else
                    RemoveNullFlag(Fields.province_id);
#endif

                SetFieldHasUpdate(Fields.province_id, this._province_id, value);
                this._province_id = value;
            }
        }

        public int city_id
        {
            get { return this._city_id; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.city_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.city_id);
                else
                    RemoveNullFlag(Fields.city_id);
#endif

                SetFieldHasUpdate(Fields.city_id, this._city_id, value);
                this._city_id = value;
            }
        }

        public int @operator
        {
            get { return this._operator; }
            set
            {
#if true && false
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

        public int channel_child_id
        {
            get { return this._channel_child_id; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.channel_child_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.channel_child_id);
                else
                    RemoveNullFlag(Fields.channel_child_id);
#endif

                SetFieldHasUpdate(Fields.channel_child_id, this._channel_child_id, value);
                this._channel_child_id = value;
            }
        }

        public int channel_id
        {
            get { return this._channel_id; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.channel_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.channel_id);
                else
                    RemoveNullFlag(Fields.channel_id);
#endif

                SetFieldHasUpdate(Fields.channel_id, this._channel_id, value);
                this._channel_id = value;
            }
        }

        public int channel_child_ratio
        {
            get { return this._channel_child_ratio; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.channel_child_ratio);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.channel_child_ratio);
                else
                    RemoveNullFlag(Fields.channel_child_ratio);
#endif

                SetFieldHasUpdate(Fields.channel_child_ratio, this._channel_child_ratio, value);
                this._channel_child_ratio = value;
            }
        }

        public int channel_ratio
        {
            get { return this._channel_ratio; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.channel_ratio);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.channel_ratio);
                else
                    RemoveNullFlag(Fields.channel_ratio);
#endif

                SetFieldHasUpdate(Fields.channel_ratio, this._channel_ratio, value);
                this._channel_ratio = value;
            }
        }

        public int pay_money
        {
            get { return this._pay_money; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.pay_money);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.pay_money);
                else
                    RemoveNullFlag(Fields.pay_money);
#endif

                SetFieldHasUpdate(Fields.pay_money, this._pay_money, value);
                this._pay_money = value;
            }
        }
        /// <summary>
        /// 0处理中，1成功，2失败
        /// </summary>
        public int pay_status
        {
            get { return this._pay_status; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.pay_status);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.pay_status);
                else
                    RemoveNullFlag(Fields.pay_status);
#endif

                SetFieldHasUpdate(Fields.pay_status, this._pay_status, value);
                this._pay_status = value;
            }
        }
        /// <summary>
        /// 0未发送，1已发送
        /// </summary>
        public int send_sms_status
        {
            get { return this._send_sms_status; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.send_sms_status);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.send_sms_status);
                else
                    RemoveNullFlag(Fields.send_sms_status);
#endif

                SetFieldHasUpdate(Fields.send_sms_status, this._send_sms_status, value);
                this._send_sms_status = value;
            }
        }
        /// <summary>
        /// 流量充值供应商返回结果
        /// </summary>
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
        /// <summary>
        /// 流量充值供应商返回状态说明
        /// </summary>
        public string sp_err_msg
        {
            get { return this._sp_err_msg; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.sp_err_msg);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.sp_err_msg);
                else
                    RemoveNullFlag(Fields.sp_err_msg);
#endif

                SetFieldHasUpdate(Fields.sp_err_msg, this._sp_err_msg, value);
                this._sp_err_msg = value;
            }
        }
        /// <summary>
        /// 0待充值，1充值成功，2充值失败
        /// </summary>
        public int charge_status
        {
            get { return this._charge_status; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.charge_status);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.charge_status);
                else
                    RemoveNullFlag(Fields.charge_status);
#endif

                SetFieldHasUpdate(Fields.charge_status, this._charge_status, value);
                this._charge_status = value;
            }
        }

        public string server_order
        {
            get { return this._server_order; }
            set
            {
#if false && false
				RemoveNullFlag(Fields.server_order);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.server_order);
                else
                    RemoveNullFlag(Fields.server_order);
#endif

                SetFieldHasUpdate(Fields.server_order, this._server_order, value);
                this._server_order = value;
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

        #endregion
        #region 空值相关方法
        protected override string[] GetNullableFields()
        {
            return new string[]{null
            ,"sp_status"
,"sp_err_msg"
};
        }
        public bool Issp_statusNull() { return IsNull(Fields.sp_status); }

        public void Setsp_statusNull() { SetNull(Fields.sp_status); }
        public bool Issp_err_msgNull() { return IsNull(Fields.sp_err_msg); }

        public void Setsp_err_msgNull() { SetNull(Fields.sp_err_msg); }

        #endregion

    }
}