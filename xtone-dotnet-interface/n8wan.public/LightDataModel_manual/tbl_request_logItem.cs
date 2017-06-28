using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    /// <summary>
    /// tbl_request_log_201512数据模型
    /// </summary>
    public partial class tbl_request_logItem : Shotgun.Model.Logical.DynamicDataItem
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


            public const string sp_api_url_id = "sp_api_url_id";

            public const string url = "url";

            public const string data = "data";

            public const string is_post = "is_post";

            public const string createTime = "createTime";

            public const string date = "date";

            public const string linkid = "linkid";

            #endregion

        }
        #region 表字段变量定义

        private int _sp_api_url_id;

        private string _url;

        private string _data;

        private bool _is_post;

        private DateTime _createTime;

        private DateTime _date;

        private string _linkid;

        #endregion

        public override string IdentifyField { get { return identifyField; } }

        public static readonly string identifyField = "id";


        public int id
        {
            get;
            set;
        }

        #region 表字段值存取

        public int sp_api_url_id
        {
            get { return this._sp_api_url_id; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.sp_api_url_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.sp_api_url_id);
                else
                    RemoveNullFlag(Fields.sp_api_url_id);
#endif

                SetFieldHasUpdate(Fields.sp_api_url_id, this._sp_api_url_id, value);
                this._sp_api_url_id = value;
            }
        }

        public string url
        {
            get { return this._url; }
            set
            {
#if false && false
				RemoveNullFlag(Fields.url);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.url);
                else
                    RemoveNullFlag(Fields.url);
#endif

                SetFieldHasUpdate(Fields.url, this._url, value);
                this._url = value;
            }
        }

        public string data
        {
            get { return this._data; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.data);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.data);
                else
                    RemoveNullFlag(Fields.data);
#endif

                SetFieldHasUpdate(Fields.data, this._data, value);
                this._data = value;
            }
        }

        public bool is_post
        {
            get { return this._is_post; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.is_post);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.is_post);
                else
                    RemoveNullFlag(Fields.is_post);
#endif

                SetFieldHasUpdate(Fields.is_post, this._is_post, value);
                this._is_post = value;
            }
        }

        public DateTime createTime
        {
            get { return this._createTime; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.createTime);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.createTime);
                else
                    RemoveNullFlag(Fields.createTime);
#endif

                SetFieldHasUpdate(Fields.createTime, this._createTime, value);
                this._createTime = value;
            }
        }

        public DateTime date
        {
            get { return this._date; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.date);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.date);
                else
                    RemoveNullFlag(Fields.date);
#endif

                SetFieldHasUpdate(Fields.date, this._date, value);
                this._date = value;
            }
        }

        public string linkid
        {
            get { return this._linkid; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.linkid);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.linkid);
                else
                    RemoveNullFlag(Fields.linkid);
#endif

                SetFieldHasUpdate(Fields.linkid, this._linkid, value);
                this._linkid = value;
            }
        }

        #endregion
        #region 空值相关方法
        protected override string[] GetNullableFields()
        {
            return new string[]{null
			,"data"
,"is_post"
,"createTime"
,"date"
,"linkid"
};
        }
        public bool IsdataNull() { return IsNull(Fields.data); }

        public void SetdataNull() { SetNull(Fields.data); }
        public bool Isis_postNull() { return IsNull(Fields.is_post); }

        public void Setis_postNull() { SetNull(Fields.is_post); }
        public bool IscreateTimeNull() { return IsNull(Fields.createTime); }

        public void SetcreateTimeNull() { SetNull(Fields.createTime); }
        public bool IsdateNull() { return IsNull(Fields.date); }

        public void SetdateNull() { SetNull(Fields.date); }
        public bool IslinkidNull() { return IsNull(Fields.linkid); }

        public void SetlinkidNull() { SetNull(Fields.linkid); }

        #endregion

    }
}