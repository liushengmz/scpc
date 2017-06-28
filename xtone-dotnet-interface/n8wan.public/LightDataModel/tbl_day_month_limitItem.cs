using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    /// <summary>
    /// tbl_day_month_limit数据模型
    /// </summary>
    public partial class tbl_day_month_limitItem : Shotgun.Model.Logical.LightDataModel
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
            /// tbl_sp_trone_api_id
            /// </summary>
            public const string sp_trone_id = "sp_trone_id";

            public const string cp_id = "cp_id";
            /// <summary>
            /// 计费日期
            /// </summary>
            public const string fee_date = "fee_date";
            /// <summary>
            /// 当前日计费
            /// </summary>
            public const string cur_day_amount = "cur_day_amount";

            #endregion

        }
        #region 表字段变量定义
        /// <summary>
        /// tbl_sp_trone_api_id
        /// </summary>
        private int _sp_trone_id;

        private int _cp_id;
        /// <summary>
        /// 计费日期
        /// </summary>
        private DateTime _fee_date;
        /// <summary>
        /// 当前日计费
        /// </summary>
        private Decimal _cur_day_amount;

        #endregion

        public override string IdentifyField { get { return identifyField; } }
        public override string TableName { get { return tableName; } }

        public static readonly string identifyField = "id";
        public static readonly string tableName = "tbl_day_month_limit";


        public int id
        {
            get;
            set;
        }

        #region 表字段值存取
        /// <summary>
        /// tbl_sp_trone_api_id
        /// </summary>
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
        /// 计费日期
        /// </summary>
        public DateTime fee_date
        {
            get { return this._fee_date; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.fee_date);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.fee_date);
                else
                    RemoveNullFlag(Fields.fee_date);
#endif

                SetFieldHasUpdate(Fields.fee_date, this._fee_date, value);
                this._fee_date = value;
            }
        }
        /// <summary>
        /// 当前日计费
        /// </summary>
        public Decimal cur_day_amount
        {
            get { return this._cur_day_amount; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.cur_day_amount);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.cur_day_amount);
                else
                    RemoveNullFlag(Fields.cur_day_amount);
#endif

                SetFieldHasUpdate(Fields.cur_day_amount, this._cur_day_amount, value);
                this._cur_day_amount = value;
            }
        }

        #endregion
        #region 空值相关方法
        protected override string[] GetNullableFields()
        {
            return new string[]{null
			,"cp_id"
,"cur_day_amount"
};
        }
        public bool Iscp_idNull() { return IsNull(Fields.cp_id); }

        public void Setcp_idNull() { SetNull(Fields.cp_id); }
        public bool Iscur_day_amountNull() { return IsNull(Fields.cur_day_amount); }

        public void Setcur_day_amountNull() { SetNull(Fields.cur_day_amount); }

        #endregion
        #region 静态方法

        /// <summary>
        /// 根据主键查找指定的行,返回指定字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <param name="fields">返回字段</param>
        /// <returns></returns>
        public static tbl_day_month_limitItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id, string[] fields)
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
        public static tbl_day_month_limitItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id)
        {
            return GetRowById(dBase, id, null);
        }

        #endregion


    }
}