using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    /// <summary>
    /// tbl_xy_user_201509数据模型
    /// </summary>
    public partial class tbl_xy_userItem : Shotgun.Model.Logical.DynamicDataItem
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
            /// 渠道名
            /// </summary>
            public const string channelkey = "channelkey";
            /// <summary>
            /// 手机卡唯一编码
            /// </summary>
            public const string imsi = "imsi";
            /// <summary>
            /// 手机唯一编码
            /// </summary>
            public const string imei = "imei";
            /// <summary>
            /// 用户新增时间
            /// </summary>
            public const string addTime = "addTime";

            #endregion

        }
        #region 表字段变量定义
        /// <summary>
        /// 添加应用时生成的唯一值
        /// </summary>
        private string _appkey;
        /// <summary>
        /// 渠道名
        /// </summary>
        private string _channelkey;
        /// <summary>
        /// 手机卡唯一编码
        /// </summary>
        private string _imsi;
        /// <summary>
        /// 手机唯一编码
        /// </summary>
        private string _imei;
        /// <summary>
        /// 用户新增时间
        /// </summary>
        private DateTime _addTime;

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
        /// 渠道名
        /// </summary>
        public string channelkey
        {
            get { return this._channelkey; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.channelkey);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.channelkey);
                else
                    RemoveNullFlag(Fields.channelkey);
#endif

                SetFieldHasUpdate(Fields.channelkey, this._channelkey, value);
                this._channelkey = value;
            }
        }
        /// <summary>
        /// 手机卡唯一编码
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
        /// 手机唯一编码
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
        /// 用户新增时间
        /// </summary>
        public DateTime addTime
        {
            get { return this._addTime; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.addTime);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.addTime);
                else
                    RemoveNullFlag(Fields.addTime);
#endif

                SetFieldHasUpdate(Fields.addTime, this._addTime, value);
                this._addTime = value;
            }
        }

        #endregion
        #region 空值相关方法
        protected override string[] GetNullableFields()
        {
            return new string[]{null
			,"appkey"
,"channelkey"
,"imsi"
,"imei"
,"addTime"
};
        }
        public bool IsappkeyNull() { return IsNull(Fields.appkey); }

        public void SetappkeyNull() { SetNull(Fields.appkey); }
        public bool IschannelkeyNull() { return IsNull(Fields.channelkey); }

        public void SetchannelkeyNull() { SetNull(Fields.channelkey); }
        public bool IsimsiNull() { return IsNull(Fields.imsi); }

        public void SetimsiNull() { SetNull(Fields.imsi); }
        public bool IsimeiNull() { return IsNull(Fields.imei); }

        public void SetimeiNull() { SetNull(Fields.imei); }
        public bool IsaddTimeNull() { return IsNull(Fields.addTime); }

        public void SetaddTimeNull() { SetNull(Fields.addTime); }

        #endregion

    }
}