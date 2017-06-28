using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    /// <summary>
    /// tbl_xypay_201509数据模型
    /// </summary>
    public partial class tbl_xypayItem : Shotgun.Model.Logical.DynamicDataItem
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
            /// 添加应用时生成的唯一值
            /// </summary>
            public const string appkey = "appkey";
            /// <summary>
            /// 金额（以分为单位）
            /// </summary>
            public const string amount = "amount";
            /// <summary>
            /// 订单号
            /// </summary>
            public const string orderid = "orderid";
            /// <summary>
            /// 应用名
            /// </summary>
            public const string appname = "appname";
            /// <summary>
            /// 计费道具名称
            /// </summary>
            public const string appsubject = "appsubject";
            /// <summary>
            /// 渠道id
            /// </summary>
            public const string channelid = "channelid";
            /// <summary>
            /// 订单创建时间
            /// </summary>
            public const string createdate = "createdate";
            /// <summary>
            /// 1:移动 2联通 3.电信
            /// </summary>
            public const string oprator = "oprator";
            /// <summary>
            /// 能获取到手机号的情况下传手机号，不能获取到的情况下为空字符串
            /// </summary>
            public const string mobileno = "mobileno";
            /// <summary>
            /// 开发者自定义订单号
            /// </summary>
            public const string userorderid = "userorderid";
            /// <summary>
            /// 订单状态,0为成功 其他均为失败
            /// </summary>
            public const string status = "status";

            public const string imsi = "imsi";

            public const string imei = "imei";

            #endregion

        }
        #region 表字段变量定义
        /// <summary>
        /// 添加应用时生成的唯一值
        /// </summary>
        private string _appkey;
        /// <summary>
        /// 金额（以分为单位）
        /// </summary>
        private string _amount;
        /// <summary>
        /// 订单号
        /// </summary>
        private string _orderid;
        /// <summary>
        /// 应用名
        /// </summary>
        private string _appname;
        /// <summary>
        /// 计费道具名称
        /// </summary>
        private string _appsubject;
        /// <summary>
        /// 渠道id
        /// </summary>
        private string _channelid;
        /// <summary>
        /// 订单创建时间
        /// </summary>
        private DateTime _createdate;
        /// <summary>
        /// 1:移动 2联通 3.电信
        /// </summary>
        private string _oprator;
        /// <summary>
        /// 能获取到手机号的情况下传手机号，不能获取到的情况下为空字符串
        /// </summary>
        private string _mobileno;
        /// <summary>
        /// 开发者自定义订单号
        /// </summary>
        private string _userorderid;
        /// <summary>
        /// 订单状态,0为成功 其他均为失败
        /// </summary>
        private string _status;

        private string _imsi;

        private string _imei;

        #endregion

        public override string IdentifyField { get { return identifyField; } }

        public static readonly string identifyField = "id";


        public int id
        {
            get;
            set;
        }

        #region 表字段值存取
        /// <summary>
        /// 添加应用时生成的唯一值
        /// </summary>
        public string appkey
        {
            get { return this._appkey; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.appkey);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.appkey);
                else
                    RemoveNullFlag(Fields.appkey);
#endif

                SetFieldHasUpdate(Fields.appkey, this._appkey, value);
                this._appkey = value;
            }
        }
        /// <summary>
        /// 金额（以分为单位）
        /// </summary>
        public string amount
        {
            get { return this._amount; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.amount);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.amount);
                else
                    RemoveNullFlag(Fields.amount);
#endif

                SetFieldHasUpdate(Fields.amount, this._amount, value);
                this._amount = value;
            }
        }
        /// <summary>
        /// 订单号
        /// </summary>
        public string orderid
        {
            get { return this._orderid; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.orderid);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.orderid);
                else
                    RemoveNullFlag(Fields.orderid);
#endif

                SetFieldHasUpdate(Fields.orderid, this._orderid, value);
                this._orderid = value;
            }
        }
        /// <summary>
        /// 应用名
        /// </summary>
        public string appname
        {
            get { return this._appname; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.appname);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.appname);
                else
                    RemoveNullFlag(Fields.appname);
#endif

                SetFieldHasUpdate(Fields.appname, this._appname, value);
                this._appname = value;
            }
        }
        /// <summary>
        /// 计费道具名称
        /// </summary>
        public string appsubject
        {
            get { return this._appsubject; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.appsubject);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.appsubject);
                else
                    RemoveNullFlag(Fields.appsubject);
#endif

                SetFieldHasUpdate(Fields.appsubject, this._appsubject, value);
                this._appsubject = value;
            }
        }
        /// <summary>
        /// 渠道id
        /// </summary>
        public string channelid
        {
            get { return this._channelid; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.channelid);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.channelid);
                else
                    RemoveNullFlag(Fields.channelid);
#endif

                SetFieldHasUpdate(Fields.channelid, this._channelid, value);
                this._channelid = value;
            }
        }
        /// <summary>
        /// 订单创建时间
        /// </summary>
        public DateTime createdate
        {
            get { return this._createdate; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.createdate);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.createdate);
                else
                    RemoveNullFlag(Fields.createdate);
#endif

                SetFieldHasUpdate(Fields.createdate, this._createdate, value);
                this._createdate = value;
            }
        }
        /// <summary>
        /// 1:移动 2联通 3.电信
        /// </summary>
        public string oprator
        {
            get { return this._oprator; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.oprator);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.oprator);
                else
                    RemoveNullFlag(Fields.oprator);
#endif

                SetFieldHasUpdate(Fields.oprator, this._oprator, value);
                this._oprator = value;
            }
        }
        /// <summary>
        /// 能获取到手机号的情况下传手机号，不能获取到的情况下为空字符串
        /// </summary>
        public string mobileno
        {
            get { return this._mobileno; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.mobileno);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.mobileno);
                else
                    RemoveNullFlag(Fields.mobileno);
#endif

                SetFieldHasUpdate(Fields.mobileno, this._mobileno, value);
                this._mobileno = value;
            }
        }
        /// <summary>
        /// 开发者自定义订单号
        /// </summary>
        public string userorderid
        {
            get { return this._userorderid; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.userorderid);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.userorderid);
                else
                    RemoveNullFlag(Fields.userorderid);
#endif

                SetFieldHasUpdate(Fields.userorderid, this._userorderid, value);
                this._userorderid = value;
            }
        }
        /// <summary>
        /// 订单状态,0为成功 其他均为失败
        /// </summary>
        public string status
        {
            get { return this._status; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.status);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.status);
                else
                    RemoveNullFlag(Fields.status);
#endif

                SetFieldHasUpdate(Fields.status, this._status, value);
                this._status = value;
            }
        }

        public string imsi
        {
            get { return this._imsi; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.imsi);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.imsi);
                else
                    RemoveNullFlag(Fields.imsi);
#endif

                SetFieldHasUpdate(Fields.imsi, this._imsi, value);
                this._imsi = value;
            }
        }

        public string imei
        {
            get { return this._imei; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.imei);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.imei);
                else
                    RemoveNullFlag(Fields.imei);
#endif

                SetFieldHasUpdate(Fields.imei, this._imei, value);
                this._imei = value;
            }
        }

        #endregion
        #region 空值相关方法
        protected override string[] GetNullableFields()
        {
            return new string[]{null
			,"appkey"
,"amount"
,"orderid"
,"appname"
,"appsubject"
,"channelid"
,"createdate"
,"oprator"
,"mobileno"
,"userorderid"
,"status"
,"imsi"
,"imei"
};
        }
        public bool IsappkeyNull() { return IsNull(Fields.appkey); }

        public void SetappkeyNull() { SetNull(Fields.appkey); }
        public bool IsamountNull() { return IsNull(Fields.amount); }

        public void SetamountNull() { SetNull(Fields.amount); }
        public bool IsorderidNull() { return IsNull(Fields.orderid); }

        public void SetorderidNull() { SetNull(Fields.orderid); }
        public bool IsappnameNull() { return IsNull(Fields.appname); }

        public void SetappnameNull() { SetNull(Fields.appname); }
        public bool IsappsubjectNull() { return IsNull(Fields.appsubject); }

        public void SetappsubjectNull() { SetNull(Fields.appsubject); }
        public bool IschannelidNull() { return IsNull(Fields.channelid); }

        public void SetchannelidNull() { SetNull(Fields.channelid); }
        public bool IscreatedateNull() { return IsNull(Fields.createdate); }

        public void SetcreatedateNull() { SetNull(Fields.createdate); }
        public bool IsopratorNull() { return IsNull(Fields.oprator); }

        public void SetopratorNull() { SetNull(Fields.oprator); }
        public bool IsmobilenoNull() { return IsNull(Fields.mobileno); }

        public void SetmobilenoNull() { SetNull(Fields.mobileno); }
        public bool IsuserorderidNull() { return IsNull(Fields.userorderid); }

        public void SetuserorderidNull() { SetNull(Fields.userorderid); }
        public bool IsstatusNull() { return IsNull(Fields.status); }

        public void SetstatusNull() { SetNull(Fields.status); }
        public bool IsimsiNull() { return IsNull(Fields.imsi); }

        public void SetimsiNull() { SetNull(Fields.imsi); }
        public bool IsimeiNull() { return IsNull(Fields.imei); }

        public void SetimeiNull() { SetNull(Fields.imei); }

        #endregion

    }
}