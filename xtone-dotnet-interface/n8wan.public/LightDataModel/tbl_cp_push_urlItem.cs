using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    /// <summary>
    /// tbl_cp_push_url数据模型
    /// </summary>
    public partial class tbl_cp_push_urlItem : Shotgun.Model.Logical.LightDataModel
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


            public const string name = "name";
            /// <summary>
            /// 指令引用数
            /// </summary>
            public const string ref_count = "ref_count";
            /// <summary>
            /// CP接收指令的URL
            /// </summary>
            public const string url = "url";
            /// <summary>
            /// cp表主键
            /// </summary>
            public const string cp_id = "cp_id";
            /// <summary>
            /// 扣量比量(0~99)百分比
            /// </summary>
            public const string hold_percent = "hold_percent";
            /// <summary>
            /// 当日最大同步金额
            /// </summary>
            public const string hold_amount = "hold_amount";

            public const string lastDate = "lastDate";
            /// <summary>
            /// 当日已经同步金额
            /// </summary>
            public const string amount = "amount";
            /// <summary>
            /// 起扣条数
            /// </summary>
            public const string hold_start = "hold_start";
            /// <summary>
            /// 是否实时同步
            /// </summary>
            public const string is_realtime = "is_realtime";
            /// <summary>
            /// 扣量周期，已经处理多少条数据
            /// </summary>
            public const string hold_CycCount = "hold_CycCount";
            /// <summary>
            /// 扣量周期，已经扣除量
            /// </summary>
            public const string hold_CycProc = "hold_CycProc";
            /// <summary>
            /// 已经推送条数
            /// </summary>
            public const string push_count = "push_count";

            #endregion

        }
        #region 表字段变量定义

        private string _name;
        /// <summary>
        /// 指令引用数
        /// </summary>
        private int _ref_count;
        /// <summary>
        /// CP接收指令的URL
        /// </summary>
        private string _url;
        /// <summary>
        /// cp表主键
        /// </summary>
        private int _cp_id;
        /// <summary>
        /// 扣量比量(0~99)百分比
        /// </summary>
        private int _hold_percent;
        /// <summary>
        /// 当日最大同步金额
        /// </summary>
        private Decimal _hold_amount;

        private DateTime _lastDate;
        /// <summary>
        /// 当日已经同步金额
        /// </summary>
        private Decimal _amount;
        /// <summary>
        /// 起扣条数
        /// </summary>
        private int _hold_start;
        /// <summary>
        /// 是否实时同步
        /// </summary>
        private bool _is_realtime;
        /// <summary>
        /// 扣量周期，已经处理多少条数据
        /// </summary>
        private int _hold_CycCount;
        /// <summary>
        /// 扣量周期，已经扣除量
        /// </summary>
        private int _hold_CycProc;
        /// <summary>
        /// 已经推送条数
        /// </summary>
        private int _push_count;

        #endregion

        public override string IdentifyField { get { return identifyField; } }
        public override string TableName { get { return tableName; } }

        public static readonly string identifyField = "id";
        public static readonly string tableName = "tbl_cp_push_url";


        public int id
        {
            get;
            set;
        }

        #region 表字段值存取

        public string name
        {
            get { return this._name; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.name);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.name);
                else
                    RemoveNullFlag(Fields.name);
#endif

                SetFieldHasUpdate(Fields.name, this._name, value);
                this._name = value;
            }
        }
        /// <summary>
        /// 指令引用数
        /// </summary>
        public int ref_count
        {
            get { return this._ref_count; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.ref_count);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.ref_count);
                else
                    RemoveNullFlag(Fields.ref_count);
#endif

                SetFieldHasUpdate(Fields.ref_count, this._ref_count, value);
                this._ref_count = value;
            }
        }
        /// <summary>
        /// CP接收指令的URL
        /// </summary>
        public string url
        {
            get { return this._url; }
            set
            {
#if false && true
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
        /// <summary>
        /// cp表主键
        /// </summary>
        public int cp_id
        {
            get { return this._cp_id; }
            set
            {
#if true && true
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
        /// <summary>
        /// 扣量比量(0~99)百分比
        /// </summary>
        public int hold_percent
        {
            get { return this._hold_percent; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.hold_percent);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.hold_percent);
                else
                    RemoveNullFlag(Fields.hold_percent);
#endif

                SetFieldHasUpdate(Fields.hold_percent, this._hold_percent, value);
                this._hold_percent = value;
            }
        }
        /// <summary>
        /// 当日最大同步金额
        /// </summary>
        public Decimal hold_amount
        {
            get { return this._hold_amount; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.hold_amount);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.hold_amount);
                else
                    RemoveNullFlag(Fields.hold_amount);
#endif

                SetFieldHasUpdate(Fields.hold_amount, this._hold_amount, value);
                this._hold_amount = value;
            }
        }

        public DateTime lastDate
        {
            get { return this._lastDate; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.lastDate);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.lastDate);
                else
                    RemoveNullFlag(Fields.lastDate);
#endif

                SetFieldHasUpdate(Fields.lastDate, this._lastDate, value);
                this._lastDate = value;
            }
        }
        /// <summary>
        /// 当日已经同步金额
        /// </summary>
        public Decimal amount
        {
            get { return this._amount; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.amount);
#elif !true
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
        /// 起扣条数
        /// </summary>
        public int hold_start
        {
            get { return this._hold_start; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.hold_start);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.hold_start);
                else
                    RemoveNullFlag(Fields.hold_start);
#endif

                SetFieldHasUpdate(Fields.hold_start, this._hold_start, value);
                this._hold_start = value;
            }
        }
        /// <summary>
        /// 是否实时同步
        /// </summary>
        public bool is_realtime
        {
            get { return this._is_realtime; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.is_realtime);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.is_realtime);
                else
                    RemoveNullFlag(Fields.is_realtime);
#endif

                SetFieldHasUpdate(Fields.is_realtime, this._is_realtime, value);
                this._is_realtime = value;
            }
        }
        /// <summary>
        /// 扣量周期，已经处理多少条数据
        /// </summary>
        public int hold_CycCount
        {
            get { return this._hold_CycCount; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.hold_CycCount);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.hold_CycCount);
                else
                    RemoveNullFlag(Fields.hold_CycCount);
#endif

                SetFieldHasUpdate(Fields.hold_CycCount, this._hold_CycCount, value);
                this._hold_CycCount = value;
            }
        }
        /// <summary>
        /// 扣量周期，已经扣除量
        /// </summary>
        public int hold_CycProc
        {
            get { return this._hold_CycProc; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.hold_CycProc);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.hold_CycProc);
                else
                    RemoveNullFlag(Fields.hold_CycProc);
#endif

                SetFieldHasUpdate(Fields.hold_CycProc, this._hold_CycProc, value);
                this._hold_CycProc = value;
            }
        }
        /// <summary>
        /// 已经推送条数
        /// </summary>
        public int push_count
        {
            get { return this._push_count; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.push_count);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.push_count);
                else
                    RemoveNullFlag(Fields.push_count);
#endif

                SetFieldHasUpdate(Fields.push_count, this._push_count, value);
                this._push_count = value;
            }
        }

        #endregion
        #region 空值相关方法
        protected override string[] GetNullableFields()
        {
            return new string[]{null
			,"name"
,"ref_count"
,"url"
,"cp_id"
,"hold_percent"
,"hold_amount"
,"lastDate"
,"amount"
,"hold_start"
,"is_realtime"
,"hold_CycCount"
,"hold_CycProc"
,"push_count"
};
        }
        public bool IsnameNull() { return IsNull(Fields.name); }

        public void SetnameNull() { SetNull(Fields.name); }
        public bool Isref_countNull() { return IsNull(Fields.ref_count); }

        public void Setref_countNull() { SetNull(Fields.ref_count); }
        public bool IsurlNull() { return IsNull(Fields.url); }

        public void SeturlNull() { SetNull(Fields.url); }
        public bool Iscp_idNull() { return IsNull(Fields.cp_id); }

        public void Setcp_idNull() { SetNull(Fields.cp_id); }
        public bool Ishold_percentNull() { return IsNull(Fields.hold_percent); }

        public void Sethold_percentNull() { SetNull(Fields.hold_percent); }
        public bool Ishold_amountNull() { return IsNull(Fields.hold_amount); }

        public void Sethold_amountNull() { SetNull(Fields.hold_amount); }
        public bool IslastDateNull() { return IsNull(Fields.lastDate); }

        public void SetlastDateNull() { SetNull(Fields.lastDate); }
        public bool IsamountNull() { return IsNull(Fields.amount); }

        public void SetamountNull() { SetNull(Fields.amount); }
        public bool Ishold_startNull() { return IsNull(Fields.hold_start); }

        public void Sethold_startNull() { SetNull(Fields.hold_start); }
        public bool Isis_realtimeNull() { return IsNull(Fields.is_realtime); }

        public void Setis_realtimeNull() { SetNull(Fields.is_realtime); }
        public bool Ishold_CycCountNull() { return IsNull(Fields.hold_CycCount); }

        public void Sethold_CycCountNull() { SetNull(Fields.hold_CycCount); }
        public bool Ishold_CycProcNull() { return IsNull(Fields.hold_CycProc); }

        public void Sethold_CycProcNull() { SetNull(Fields.hold_CycProc); }
        public bool Ispush_countNull() { return IsNull(Fields.push_count); }

        public void Setpush_countNull() { SetNull(Fields.push_count); }

        #endregion
        #region 静态方法
        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_cp_push_urlItem> GetQueries(Shotgun.Database.IBaseDataClass2 dBase)
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_cp_push_urlItem>(tableName, identifyField, dBase);
        }

        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_cp_push_urlItem> GetQueries()
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_cp_push_urlItem>(tableName, identifyField);
        }
        /// <summary>
        /// 根据主键查找指定的行,返回指定字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <param name="fields">返回字段</param>
        /// <returns></returns>
        public static tbl_cp_push_urlItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id, string[] fields)
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
        public static tbl_cp_push_urlItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id)
        {
            return GetRowById(dBase, id, null);
        }

        #endregion
    }
}