using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    /// <summary>
    /// tbl_trone_paycode数据模型
    /// </summary>
    public partial class tbl_trone_paycodeItem : Shotgun.Model.Logical.LightDataModel
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
            /// 业务通道ID
            /// </summary>
            public const string trone_id = "trone_id";
            /// <summary>
            /// SP对接的计费点
            /// </summary>
            public const string paycode = "paycode";

            public const string appid = "appid";

            public const string channelid = "channelid";

            public const string create_date = "create_date";
            /// <summary>
            /// 扩展的配置信息,通常以json格式存储
            /// </summary>
            public const string extr_config = "extr_config";

            #endregion

        }
        #region 表字段变量定义
        /// <summary>
        /// 业务通道ID
        /// </summary>
        private int _trone_id;
        /// <summary>
        /// SP对接的计费点
        /// </summary>
        private string _paycode;

        private string _appid;

        private string _channelid;

        private DateTime _create_date;
        /// <summary>
        /// 扩展的配置信息,通常以json格式存储
        /// </summary>
        private string _extr_config;

        #endregion

        public override string IdentifyField { get { return identifyField; } }
        public override string TableName { get { return tableName; } }

        public static readonly string identifyField = "id";
        public static readonly string tableName = "tbl_trone_paycode";


        public int id
        {
            get;
            set;
        }

        #region 表字段值存取
        /// <summary>
        /// 业务通道ID
        /// </summary>
        public int trone_id
        {
            get { return this._trone_id; }
            set
            {
#if true && true
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
        /// SP对接的计费点
        /// </summary>
        public string paycode
        {
            get { return this._paycode; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.paycode);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.paycode);
                else
                    RemoveNullFlag(Fields.paycode);
#endif

                SetFieldHasUpdate(Fields.paycode, this._paycode, value);
                this._paycode = value;
            }
        }

        public string appid
        {
            get { return this._appid; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.appid);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.appid);
                else
                    RemoveNullFlag(Fields.appid);
#endif

                SetFieldHasUpdate(Fields.appid, this._appid, value);
                this._appid = value;
            }
        }

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
        /// <summary>
        /// 扩展的配置信息,通常以json格式存储
        /// </summary>
        public string extr_config
        {
            get { return this._extr_config; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.extr_config);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.extr_config);
                else
                    RemoveNullFlag(Fields.extr_config);
#endif

                SetFieldHasUpdate(Fields.extr_config, this._extr_config, value);
                this._extr_config = value;
            }
        }

        #endregion
        #region 空值相关方法
        protected override string[] GetNullableFields()
        {
            return new string[]{null
			,"trone_id"
,"paycode"
,"appid"
,"channelid"
,"extr_config"
};
        }
        public bool Istrone_idNull() { return IsNull(Fields.trone_id); }

        public void Settrone_idNull() { SetNull(Fields.trone_id); }
        public bool IspaycodeNull() { return IsNull(Fields.paycode); }

        public void SetpaycodeNull() { SetNull(Fields.paycode); }
        public bool IsappidNull() { return IsNull(Fields.appid); }

        public void SetappidNull() { SetNull(Fields.appid); }
        public bool IschannelidNull() { return IsNull(Fields.channelid); }

        public void SetchannelidNull() { SetNull(Fields.channelid); }
        public bool Isextr_configNull() { return IsNull(Fields.extr_config); }

        public void Setextr_configNull() { SetNull(Fields.extr_config); }

        #endregion
        #region 静态方法
        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_trone_paycodeItem> GetQueries(Shotgun.Database.IBaseDataClass2 dBase)
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_trone_paycodeItem>(tableName, identifyField, dBase);
        }

        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_trone_paycodeItem> GetQueries()
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_trone_paycodeItem>(tableName, identifyField);
        }
        /// <summary>
        /// 根据主键查找指定的行,返回指定字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <param name="fields">返回字段</param>
        /// <returns></returns>
        public static tbl_trone_paycodeItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id, string[] fields)
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
        public static tbl_trone_paycodeItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id)
        {
            return GetRowById(dBase, id, null);
        }

        #endregion
    }
}