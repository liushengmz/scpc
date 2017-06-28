using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;


namespace LightDataModel
{
    /// <summary>
    /// tbl_api_order_201705数据模型
    /// </summary>
    public partial class tbl_api_orderItem : Shotgun.Model.Logical.DynamicDataItem
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
            /// trone_id
            /// </summary>
            public const string trone_id = "trone_id";
            /// <summary>
            /// trone_order_id
            /// </summary>
            public const string trone_order_id = "trone_order_id";

            public const string api_id = "api_id";
            /// <summary>
            /// IMSI
            /// </summary>
            public const string imsi = "imsi";
            /// <summary>
            /// IMEI
            /// </summary>
            public const string imei = "imei";
            /// <summary>
            /// 手机号
            /// </summary>
            public const string mobile = "mobile";
            /// <summary>
            /// MAC
            /// </summary>
            public const string mac = "mac";
            /// <summary>
            /// 伪手机码
            /// </summary>
            public const string fake_mobile = "fake_mobile";
            /// <summary>
            /// 城市ID
            /// </summary>
            public const string city = "city";
            /// <summary>
            /// 区域码
            /// </summary>
            public const string lac = "lac";
            /// <summary>
            /// 基站编号
            /// </summary>
            public const string cid = "cid";
            /// <summary>
            /// ICCID
            /// </summary>
            public const string iccid = "iccid";
            /// <summary>
            /// 浏览器代理
            /// </summary>
            public const string user_agent = "user_agent";
            /// <summary>
            /// CP透参
            /// </summary>
            public const string ExtrData = "ExtrData";

            public const string sdkversion = "sdkversion";

            public const string packagename = "packagename";

            public const string ip = "ip";

            public const string clientip = "clientip";

            public const string nettype = "nettype";
            /// <summary>
            /// sp产生的订单号，通常在二次http或回传时匹配使用
            /// </summary>
            public const string sp_linkid = "sp_linkid";
            /// <summary>
            /// 存储除了LINKID之外的信息
            /// </summary>
            public const string sp_exField = "sp_exField";
            /// <summary>
            /// 验证码
            /// </summary>
            public const string cp_verifyCode = "cp_verifyCode";
            /// <summary>
            /// 首次请求的时间
            /// </summary>
            public const string FirstDate = "FirstDate";
            /// <summary>
            /// 二次请求时间
            /// </summary>
            public const string SecondDate = "SecondDate";
            /// <summary>
            /// 一次指令，上行端口
            /// </summary>
            public const string port = "port";
            /// <summary>
            /// 一次指令，上行指令
            /// </summary>
            public const string msg = "msg";
            /// <summary>
            /// 此会传递给SP，并在SP计费成功时回传。用于api回传匹配使用透传参数
            /// </summary>
            public const string api_exdata = "api_exdata";
            /// <summary>
            /// 后面再确认
            /// </summary>
            public const string status = "status";
            /// <summary>
            /// 额外的参数，防止SP的变态数据要求
            /// </summary>
            public const string extra_param = "extra_param";
            /// <summary>
            /// 是否直接扣量
            /// </summary>
            public const string is_hidden = "is_hidden";

            public const string cp_pool_id = "cp_pool_id";

            public const string description = "description";
            /// <summary>
            /// 处理请求消耗的时间（ms）
            /// </summary>
            public const string duration = "duration";

            #endregion

        }
        #region 表字段变量定义
        /// <summary>
        /// trone_id
        /// </summary>
        private int _trone_id;
        /// <summary>
        /// trone_order_id
        /// </summary>
        private int _trone_order_id;

        private int _api_id;
        /// <summary>
        /// IMSI
        /// </summary>
        private string _imsi;
        /// <summary>
        /// IMEI
        /// </summary>
        private string _imei;
        /// <summary>
        /// 手机号
        /// </summary>
        private string _mobile;
        /// <summary>
        /// MAC
        /// </summary>
        private string _mac;
        /// <summary>
        /// 伪手机码
        /// </summary>
        private string _fake_mobile;
        /// <summary>
        /// 城市ID
        /// </summary>
        private int _city;
        /// <summary>
        /// 区域码
        /// </summary>
        private int _lac;
        /// <summary>
        /// 基站编号
        /// </summary>
        private int _cid;
        /// <summary>
        /// ICCID
        /// </summary>
        private string _iccid;
        /// <summary>
        /// 浏览器代理
        /// </summary>
        private string _user_agent;
        /// <summary>
        /// CP透参
        /// </summary>
        private string _ExtrData;

        private string _sdkversion;

        private string _packagename;

        private string _ip;

        private string _clientip;

        private string _nettype;
        /// <summary>
        /// sp产生的订单号，通常在二次http或回传时匹配使用
        /// </summary>
        private string _sp_linkid;
        /// <summary>
        /// 存储除了LINKID之外的信息
        /// </summary>
        private string _sp_exField;
        /// <summary>
        /// 验证码
        /// </summary>
        private string _cp_verifyCode;
        /// <summary>
        /// 首次请求的时间
        /// </summary>
        private DateTime _FirstDate;
        /// <summary>
        /// 二次请求时间
        /// </summary>
        private DateTime _SecondDate;
        /// <summary>
        /// 一次指令，上行端口
        /// </summary>
        private string _port;
        /// <summary>
        /// 一次指令，上行指令
        /// </summary>
        private string _msg;
        /// <summary>
        /// 此会传递给SP，并在SP计费成功时回传。用于api回传匹配使用透传参数
        /// </summary>
        private string _api_exdata;
        /// <summary>
        /// 后面再确认
        /// </summary>
        private int _status;
        /// <summary>
        /// 额外的参数，防止SP的变态数据要求
        /// </summary>
        private string _extra_param;
        /// <summary>
        /// 是否直接扣量
        /// </summary>
        private string _is_hidden;

        private int _cp_pool_id;

        private string _description;
        /// <summary>
        /// 处理请求消耗的时间（ms）
        /// </summary>
        private int _duration;

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
        /// trone_id
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
        /// trone_order_id
        /// </summary>
        public int trone_order_id
        {
            get { return this._trone_order_id; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.trone_order_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.trone_order_id);
                else
                    RemoveNullFlag(Fields.trone_order_id);
#endif

                SetFieldHasUpdate(Fields.trone_order_id, this._trone_order_id, value);
                this._trone_order_id = value;
            }
        }

        public int api_id
        {
            get { return this._api_id; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.api_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.api_id);
                else
                    RemoveNullFlag(Fields.api_id);
#endif

                SetFieldHasUpdate(Fields.api_id, this._api_id, value);
                this._api_id = value;
            }
        }
        /// <summary>
        /// IMSI
        /// </summary>
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
        /// <summary>
        /// IMEI
        /// </summary>
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
        /// <summary>
        /// 手机号
        /// </summary>
        public string mobile
        {
            get { return this._mobile; }
            set
            {
#if false && true
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
        /// MAC
        /// </summary>
        public string mac
        {
            get { return this._mac; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.mac);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.mac);
                else
                    RemoveNullFlag(Fields.mac);
#endif

                SetFieldHasUpdate(Fields.mac, this._mac, value);
                this._mac = value;
            }
        }
        /// <summary>
        /// 伪手机码
        /// </summary>
        public string fake_mobile
        {
            get { return this._fake_mobile; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.fake_mobile);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.fake_mobile);
                else
                    RemoveNullFlag(Fields.fake_mobile);
#endif

                SetFieldHasUpdate(Fields.fake_mobile, this._fake_mobile, value);
                this._fake_mobile = value;
            }
        }
        /// <summary>
        /// 城市ID
        /// </summary>
        public int city
        {
            get { return this._city; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.city);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.city);
                else
                    RemoveNullFlag(Fields.city);
#endif

                SetFieldHasUpdate(Fields.city, this._city, value);
                this._city = value;
            }
        }
        /// <summary>
        /// 区域码
        /// </summary>
        public int lac
        {
            get { return this._lac; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.lac);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.lac);
                else
                    RemoveNullFlag(Fields.lac);
#endif

                SetFieldHasUpdate(Fields.lac, this._lac, value);
                this._lac = value;
            }
        }
        /// <summary>
        /// 基站编号
        /// </summary>
        public int cid
        {
            get { return this._cid; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.cid);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.cid);
                else
                    RemoveNullFlag(Fields.cid);
#endif

                SetFieldHasUpdate(Fields.cid, this._cid, value);
                this._cid = value;
            }
        }
        /// <summary>
        /// ICCID
        /// </summary>
        public string iccid
        {
            get { return this._iccid; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.iccid);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.iccid);
                else
                    RemoveNullFlag(Fields.iccid);
#endif

                SetFieldHasUpdate(Fields.iccid, this._iccid, value);
                this._iccid = value;
            }
        }
        /// <summary>
        /// 浏览器代理
        /// </summary>
        public string user_agent
        {
            get { return this._user_agent; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.user_agent);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.user_agent);
                else
                    RemoveNullFlag(Fields.user_agent);
#endif

                SetFieldHasUpdate(Fields.user_agent, this._user_agent, value);
                this._user_agent = value;
            }
        }
        /// <summary>
        /// CP透参
        /// </summary>
        public string ExtrData
        {
            get { return this._ExtrData; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.ExtrData);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.ExtrData);
                else
                    RemoveNullFlag(Fields.ExtrData);
#endif

                SetFieldHasUpdate(Fields.ExtrData, this._ExtrData, value);
                this._ExtrData = value;
            }
        }

        public string sdkversion
        {
            get { return this._sdkversion; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.sdkversion);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.sdkversion);
                else
                    RemoveNullFlag(Fields.sdkversion);
#endif

                SetFieldHasUpdate(Fields.sdkversion, this._sdkversion, value);
                this._sdkversion = value;
            }
        }

        public string packagename
        {
            get { return this._packagename; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.packagename);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.packagename);
                else
                    RemoveNullFlag(Fields.packagename);
#endif

                SetFieldHasUpdate(Fields.packagename, this._packagename, value);
                this._packagename = value;
            }
        }

        public string ip
        {
            get { return this._ip; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.ip);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.ip);
                else
                    RemoveNullFlag(Fields.ip);
#endif

                SetFieldHasUpdate(Fields.ip, this._ip, value);
                this._ip = value;
            }
        }

        public string clientip
        {
            get { return this._clientip; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.clientip);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.clientip);
                else
                    RemoveNullFlag(Fields.clientip);
#endif

                SetFieldHasUpdate(Fields.clientip, this._clientip, value);
                this._clientip = value;
            }
        }

        public string nettype
        {
            get { return this._nettype; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.nettype);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.nettype);
                else
                    RemoveNullFlag(Fields.nettype);
#endif

                SetFieldHasUpdate(Fields.nettype, this._nettype, value);
                this._nettype = value;
            }
        }
        /// <summary>
        /// sp产生的订单号，通常在二次http或回传时匹配使用
        /// </summary>
        public string sp_linkid
        {
            get { return this._sp_linkid; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.sp_linkid);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.sp_linkid);
                else
                    RemoveNullFlag(Fields.sp_linkid);
#endif

                SetFieldHasUpdate(Fields.sp_linkid, this._sp_linkid, value);
                this._sp_linkid = value;
            }
        }
        /// <summary>
        /// 存储除了LINKID之外的信息
        /// </summary>
        public string sp_exField
        {
            get { return this._sp_exField; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.sp_exField);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.sp_exField);
                else
                    RemoveNullFlag(Fields.sp_exField);
#endif

                SetFieldHasUpdate(Fields.sp_exField, this._sp_exField, value);
                this._sp_exField = value;
            }
        }
        /// <summary>
        /// 验证码
        /// </summary>
        public string cp_verifyCode
        {
            get { return this._cp_verifyCode; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.cp_verifyCode);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.cp_verifyCode);
                else
                    RemoveNullFlag(Fields.cp_verifyCode);
#endif

                SetFieldHasUpdate(Fields.cp_verifyCode, this._cp_verifyCode, value);
                this._cp_verifyCode = value;
            }
        }
        /// <summary>
        /// 首次请求的时间
        /// </summary>
        public DateTime FirstDate
        {
            get { return this._FirstDate; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.FirstDate);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.FirstDate);
                else
                    RemoveNullFlag(Fields.FirstDate);
#endif

                SetFieldHasUpdate(Fields.FirstDate, this._FirstDate, value);
                this._FirstDate = value;
            }
        }
        /// <summary>
        /// 二次请求时间
        /// </summary>
        public DateTime SecondDate
        {
            get { return this._SecondDate; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.SecondDate);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.SecondDate);
                else
                    RemoveNullFlag(Fields.SecondDate);
#endif

                SetFieldHasUpdate(Fields.SecondDate, this._SecondDate, value);
                this._SecondDate = value;
            }
        }
        /// <summary>
        /// 一次指令，上行端口
        /// </summary>
        public string port
        {
            get { return this._port; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.port);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.port);
                else
                    RemoveNullFlag(Fields.port);
#endif

                SetFieldHasUpdate(Fields.port, this._port, value);
                this._port = value;
            }
        }
        /// <summary>
        /// 一次指令，上行指令
        /// </summary>
        public string msg
        {
            get { return this._msg; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.msg);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.msg);
                else
                    RemoveNullFlag(Fields.msg);
#endif

                SetFieldHasUpdate(Fields.msg, this._msg, value);
                this._msg = value;
            }
        }
        /// <summary>
        /// 此会传递给SP，并在SP计费成功时回传。用于api回传匹配使用透传参数
        /// </summary>
        public string api_exdata
        {
            get { return this._api_exdata; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.api_exdata);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.api_exdata);
                else
                    RemoveNullFlag(Fields.api_exdata);
#endif

                SetFieldHasUpdate(Fields.api_exdata, this._api_exdata, value);
                this._api_exdata = value;
            }
        }
        /// <summary>
        /// 后面再确认
        /// </summary>
        public int status
        {
            get { return this._status; }
            set
            {
#if true && true
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
        /// <summary>
        /// 额外的参数，防止SP的变态数据要求
        /// </summary>
        public string extra_param
        {
            get { return this._extra_param; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.extra_param);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.extra_param);
                else
                    RemoveNullFlag(Fields.extra_param);
#endif

                SetFieldHasUpdate(Fields.extra_param, this._extra_param, value);
                this._extra_param = value;
            }
        }
        /// <summary>
        /// 是否直接扣量
        /// </summary>
        public string is_hidden
        {
            get { return this._is_hidden; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.is_hidden);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.is_hidden);
                else
                    RemoveNullFlag(Fields.is_hidden);
#endif

                SetFieldHasUpdate(Fields.is_hidden, this._is_hidden, value);
                this._is_hidden = value;
            }
        }

        public int cp_pool_id
        {
            get { return this._cp_pool_id; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.cp_pool_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.cp_pool_id);
                else
                    RemoveNullFlag(Fields.cp_pool_id);
#endif

                SetFieldHasUpdate(Fields.cp_pool_id, this._cp_pool_id, value);
                this._cp_pool_id = value;
            }
        }

        public string description
        {
            get { return this._description; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.description);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.description);
                else
                    RemoveNullFlag(Fields.description);
#endif

                SetFieldHasUpdate(Fields.description, this._description, value);
                this._description = value;
            }
        }
        /// <summary>
        /// 处理请求消耗的时间（ms）
        /// </summary>
        public int duration
        {
            get { return this._duration; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.duration);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.duration);
                else
                    RemoveNullFlag(Fields.duration);
#endif

                SetFieldHasUpdate(Fields.duration, this._duration, value);
                this._duration = value;
            }
        }

        #endregion
        #region 空值相关方法
        protected override string[] GetNullableFields()
        {
            return new string[]{null
			,"api_id"
,"imsi"
,"imei"
,"mobile"
,"mac"
,"fake_mobile"
,"city"
,"lac"
,"cid"
,"iccid"
,"user_agent"
,"ExtrData"
,"sdkversion"
,"packagename"
,"ip"
,"clientip"
,"nettype"
,"sp_linkid"
,"sp_exField"
,"cp_verifyCode"
,"FirstDate"
,"SecondDate"
,"port"
,"msg"
,"api_exdata"
,"status"
,"extra_param"
,"is_hidden"
,"description"
,"duration"
};
        }
        public bool Isapi_idNull() { return IsNull(Fields.api_id); }

        public void Setapi_idNull() { SetNull(Fields.api_id); }
        public bool IsimsiNull() { return IsNull(Fields.imsi); }

        public void SetimsiNull() { SetNull(Fields.imsi); }
        public bool IsimeiNull() { return IsNull(Fields.imei); }

        public void SetimeiNull() { SetNull(Fields.imei); }
        public bool IsmobileNull() { return IsNull(Fields.mobile); }

        public void SetmobileNull() { SetNull(Fields.mobile); }
        public bool IsmacNull() { return IsNull(Fields.mac); }

        public void SetmacNull() { SetNull(Fields.mac); }
        public bool Isfake_mobileNull() { return IsNull(Fields.fake_mobile); }

        public void Setfake_mobileNull() { SetNull(Fields.fake_mobile); }
        public bool IscityNull() { return IsNull(Fields.city); }

        public void SetcityNull() { SetNull(Fields.city); }
        public bool IslacNull() { return IsNull(Fields.lac); }

        public void SetlacNull() { SetNull(Fields.lac); }
        public bool IscidNull() { return IsNull(Fields.cid); }

        public void SetcidNull() { SetNull(Fields.cid); }
        public bool IsiccidNull() { return IsNull(Fields.iccid); }

        public void SeticcidNull() { SetNull(Fields.iccid); }
        public bool Isuser_agentNull() { return IsNull(Fields.user_agent); }

        public void Setuser_agentNull() { SetNull(Fields.user_agent); }
        public bool IsExtrDataNull() { return IsNull(Fields.ExtrData); }

        public void SetExtrDataNull() { SetNull(Fields.ExtrData); }
        public bool IssdkversionNull() { return IsNull(Fields.sdkversion); }

        public void SetsdkversionNull() { SetNull(Fields.sdkversion); }
        public bool IspackagenameNull() { return IsNull(Fields.packagename); }

        public void SetpackagenameNull() { SetNull(Fields.packagename); }
        public bool IsipNull() { return IsNull(Fields.ip); }

        public void SetipNull() { SetNull(Fields.ip); }
        public bool IsclientipNull() { return IsNull(Fields.clientip); }

        public void SetclientipNull() { SetNull(Fields.clientip); }
        public bool IsnettypeNull() { return IsNull(Fields.nettype); }

        public void SetnettypeNull() { SetNull(Fields.nettype); }
        public bool Issp_linkidNull() { return IsNull(Fields.sp_linkid); }

        public void Setsp_linkidNull() { SetNull(Fields.sp_linkid); }
        public bool Issp_exFieldNull() { return IsNull(Fields.sp_exField); }

        public void Setsp_exFieldNull() { SetNull(Fields.sp_exField); }
        public bool Iscp_verifyCodeNull() { return IsNull(Fields.cp_verifyCode); }

        public void Setcp_verifyCodeNull() { SetNull(Fields.cp_verifyCode); }
        public bool IsFirstDateNull() { return IsNull(Fields.FirstDate); }

        public void SetFirstDateNull() { SetNull(Fields.FirstDate); }
        public bool IsSecondDateNull() { return IsNull(Fields.SecondDate); }

        public void SetSecondDateNull() { SetNull(Fields.SecondDate); }
        public bool IsportNull() { return IsNull(Fields.port); }

        public void SetportNull() { SetNull(Fields.port); }
        public bool IsmsgNull() { return IsNull(Fields.msg); }

        public void SetmsgNull() { SetNull(Fields.msg); }
        public bool Isapi_exdataNull() { return IsNull(Fields.api_exdata); }

        public void Setapi_exdataNull() { SetNull(Fields.api_exdata); }
        public bool IsstatusNull() { return IsNull(Fields.status); }

        public void SetstatusNull() { SetNull(Fields.status); }
        public bool Isextra_paramNull() { return IsNull(Fields.extra_param); }

        public void Setextra_paramNull() { SetNull(Fields.extra_param); }
        public bool Isis_hiddenNull() { return IsNull(Fields.is_hidden); }

        public void Setis_hiddenNull() { SetNull(Fields.is_hidden); }
        public bool IsdescriptionNull() { return IsNull(Fields.description); }

        public void SetdescriptionNull() { SetNull(Fields.description); }
        public bool IsdurationNull() { return IsNull(Fields.duration); }

        public void SetdurationNull() { SetNull(Fields.duration); }

        #endregion

    }
}
