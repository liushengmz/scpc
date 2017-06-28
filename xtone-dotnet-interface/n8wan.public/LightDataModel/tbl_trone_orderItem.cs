using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    /// <summary>
    /// tbl_trone_order数据模型
    /// </summary>
    public partial class tbl_trone_orderItem : Shotgun.Model.Logical.LightDataModel
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
            /// 通道表id
            /// </summary>
            public const string trone_id = "trone_id";
            /// <summary>
            /// 指令
            /// </summary>
            public const string order_num = "order_num";
            /// <summary>
            /// cp表主键
            /// </summary>
            public const string cp_id = "cp_id";
            /// <summary>
            /// 指令通道名称（和CP业务挂钩）
            /// </summary>
            public const string order_trone_name = "order_trone_name";

            public const string create_date = "create_date";
            /// <summary>
            /// 是否模糊指令，即是否允许使用通匹符进行配对
            /// </summary>
            public const string is_dynamic = "is_dynamic";
            /// <summary>
            /// 使用那个同步URL进行同步(tbl_cp_push_url.id)
            /// </summary>
            public const string push_url_id = "push_url_id";
            /// <summary>
            /// 指令此条配置是否有效
            /// </summary>
            public const string disable = "disable";
            /// <summary>
            /// 未知CP的数据，1为无主孤儿，0是有主的
            /// </summary>
            public const string is_unknow = "is_unknow";
            /// <summary>
            /// -1表示同步URL扣量设置，0~99表示扣量百分比
            /// </summary>
            public const string hold_percent = "hold_percent";
            /// <summary>
            /// 最多同步金额,-1表示按同步URL扣除设置
            /// </summary>
            public const string hold_amount = "hold_amount";
            /// <summary>
            /// 已经同步金额
            /// </summary>
            public const string amount = "amount";
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
            /// <summary>
            /// 是否采用同步URL的扣量设置,0:是;1:否
            /// </summary>
            public const string hold_is_Custom = "hold_is_Custom";

            public const string hold_start = "hold_start";

            public const string lastDate = "lastDate";

            #endregion

        }
        #region 表字段变量定义
        /// <summary>
        /// 通道表id
        /// </summary>
        private int _trone_id;
        /// <summary>
        /// 指令
        /// </summary>
        private string _order_num;
        /// <summary>
        /// cp表主键
        /// </summary>
        private int _cp_id;
        /// <summary>
        /// 指令通道名称（和CP业务挂钩）
        /// </summary>
        private string _order_trone_name;

        private DateTime _create_date;
        /// <summary>
        /// 是否模糊指令，即是否允许使用通匹符进行配对
        /// </summary>
        private bool _is_dynamic;
        /// <summary>
        /// 使用那个同步URL进行同步(tbl_cp_push_url.id)
        /// </summary>
        private int _push_url_id;
        /// <summary>
        /// 指令此条配置是否有效
        /// </summary>
        private bool _disable;
        /// <summary>
        /// 未知CP的数据，1为无主孤儿，0是有主的
        /// </summary>
        private bool _is_unknow;
        /// <summary>
        /// -1表示同步URL扣量设置，0~99表示扣量百分比
        /// </summary>
        private int _hold_percent;
        /// <summary>
        /// 最多同步金额,-1表示按同步URL扣除设置
        /// </summary>
        private Decimal _hold_amount;
        /// <summary>
        /// 已经同步金额
        /// </summary>
        private Decimal _amount;
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
        /// <summary>
        /// 是否采用同步URL的扣量设置,0:是;1:否
        /// </summary>
        private bool _hold_is_Custom;

        private int _hold_start;

        private DateTime _lastDate;

        #endregion

        public override string IdentifyField { get { return identifyField; } }
        public override string TableName { get { return tableName; } }

        public static readonly string identifyField = "id";
        public static readonly string tableName = "tbl_trone_order";


        public int id
        {
            get;
            set;
        }

        #region 表字段值存取
        /// <summary>
        /// 通道表id
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
        /// 指令
        /// </summary>
        public string order_num
        {
            get { return this._order_num; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.order_num);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.order_num);
                else
                    RemoveNullFlag(Fields.order_num);
#endif

                SetFieldHasUpdate(Fields.order_num, this._order_num, value);
                this._order_num = value;
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
        /// 指令通道名称（和CP业务挂钩）
        /// </summary>
        public string order_trone_name
        {
            get { return this._order_trone_name; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.order_trone_name);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.order_trone_name);
                else
                    RemoveNullFlag(Fields.order_trone_name);
#endif

                SetFieldHasUpdate(Fields.order_trone_name, this._order_trone_name, value);
                this._order_trone_name = value;
            }
        }

        public DateTime create_date
        {
            get { return this._create_date; }
            set
            {
#if true && true
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
        /// 是否模糊指令，即是否允许使用通匹符进行配对
        /// </summary>
        public bool is_dynamic
        {
            get { return this._is_dynamic; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.is_dynamic);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.is_dynamic);
                else
                    RemoveNullFlag(Fields.is_dynamic);
#endif

                SetFieldHasUpdate(Fields.is_dynamic, this._is_dynamic, value);
                this._is_dynamic = value;
            }
        }
        /// <summary>
        /// 使用那个同步URL进行同步(tbl_cp_push_url.id)
        /// </summary>
        public int push_url_id
        {
            get { return this._push_url_id; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.push_url_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.push_url_id);
                else
                    RemoveNullFlag(Fields.push_url_id);
#endif

                SetFieldHasUpdate(Fields.push_url_id, this._push_url_id, value);
                this._push_url_id = value;
            }
        }
        /// <summary>
        /// 指令此条配置是否有效
        /// </summary>
        public bool disable
        {
            get { return this._disable; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.disable);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.disable);
                else
                    RemoveNullFlag(Fields.disable);
#endif

                SetFieldHasUpdate(Fields.disable, this._disable, value);
                this._disable = value;
            }
        }
        /// <summary>
        /// 未知CP的数据，1为无主孤儿，0是有主的
        /// </summary>
        public bool is_unknow
        {
            get { return this._is_unknow; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.is_unknow);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.is_unknow);
                else
                    RemoveNullFlag(Fields.is_unknow);
#endif

                SetFieldHasUpdate(Fields.is_unknow, this._is_unknow, value);
                this._is_unknow = value;
            }
        }
        /// <summary>
        /// -1表示同步URL扣量设置，0~99表示扣量百分比
        /// </summary>
        public int hold_percent
        {
            get { return this._hold_percent; }
            set
            {
#if true && false
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
        /// 最多同步金额,-1表示按同步URL扣除设置
        /// </summary>
        public Decimal hold_amount
        {
            get { return this._hold_amount; }
            set
            {
#if true && false
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
        /// <summary>
        /// 已经同步金额
        /// </summary>
        public Decimal amount
        {
            get { return this._amount; }
            set
            {
#if true && false
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
        /// <summary>
        /// 是否采用同步URL的扣量设置,0:是;1:否
        /// </summary>
        public bool hold_is_Custom
        {
            get { return this._hold_is_Custom; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.hold_is_Custom);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.hold_is_Custom);
                else
                    RemoveNullFlag(Fields.hold_is_Custom);
#endif

                SetFieldHasUpdate(Fields.hold_is_Custom, this._hold_is_Custom, value);
                this._hold_is_Custom = value;
            }
        }

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

        #endregion
        #region 空值相关方法
        protected override string[] GetNullableFields()
        {
            return new string[]{null
			,"order_num"
,"cp_id"
,"order_trone_name"
,"create_date"
,"is_dynamic"
,"push_url_id"
,"disable"
,"is_unknow"
,"hold_CycCount"
,"hold_CycProc"
,"push_count"
,"hold_is_Custom"
,"hold_start"
,"lastDate"
};
        }
        public bool Isorder_numNull() { return IsNull(Fields.order_num); }

        public void Setorder_numNull() { SetNull(Fields.order_num); }
        public bool Iscp_idNull() { return IsNull(Fields.cp_id); }

        public void Setcp_idNull() { SetNull(Fields.cp_id); }
        public bool Isorder_trone_nameNull() { return IsNull(Fields.order_trone_name); }

        public void Setorder_trone_nameNull() { SetNull(Fields.order_trone_name); }
        public bool Iscreate_dateNull() { return IsNull(Fields.create_date); }

        public void Setcreate_dateNull() { SetNull(Fields.create_date); }
        public bool Isis_dynamicNull() { return IsNull(Fields.is_dynamic); }

        public void Setis_dynamicNull() { SetNull(Fields.is_dynamic); }
        public bool Ispush_url_idNull() { return IsNull(Fields.push_url_id); }

        public void Setpush_url_idNull() { SetNull(Fields.push_url_id); }
        public bool IsdisableNull() { return IsNull(Fields.disable); }

        public void SetdisableNull() { SetNull(Fields.disable); }
        public bool Isis_unknowNull() { return IsNull(Fields.is_unknow); }

        public void Setis_unknowNull() { SetNull(Fields.is_unknow); }
        public bool Ishold_CycCountNull() { return IsNull(Fields.hold_CycCount); }

        public void Sethold_CycCountNull() { SetNull(Fields.hold_CycCount); }
        public bool Ishold_CycProcNull() { return IsNull(Fields.hold_CycProc); }

        public void Sethold_CycProcNull() { SetNull(Fields.hold_CycProc); }
        public bool Ispush_countNull() { return IsNull(Fields.push_count); }

        public void Setpush_countNull() { SetNull(Fields.push_count); }
        public bool Ishold_is_CustomNull() { return IsNull(Fields.hold_is_Custom); }

        public void Sethold_is_CustomNull() { SetNull(Fields.hold_is_Custom); }
        public bool Ishold_startNull() { return IsNull(Fields.hold_start); }

        public void Sethold_startNull() { SetNull(Fields.hold_start); }
        public bool IslastDateNull() { return IsNull(Fields.lastDate); }

        public void SetlastDateNull() { SetNull(Fields.lastDate); }

        #endregion
        #region 静态方法
        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_trone_orderItem> GetQueries(Shotgun.Database.IBaseDataClass2 dBase)
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_trone_orderItem>(tableName, identifyField, dBase);
        }

        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_trone_orderItem> GetQueries()
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_trone_orderItem>(tableName, identifyField);
        }
        /// <summary>
        /// 根据主键查找指定的行,返回指定字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <param name="fields">返回字段</param>
        /// <returns></returns>
        public static tbl_trone_orderItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id, string[] fields)
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
        public static tbl_trone_orderItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id)
        {
            return GetRowById(dBase, id, null);
        }

        #endregion
    }
}