using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    /// <summary>
    /// tbl_custom_fee_count数据模型
    /// </summary>
    public partial class tbl_custom_fee_countItem : Shotgun.Model.Logical.LightDataModel
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
            /// 通常指 imsi  或 mobile。同一个通道必须明确标识方式
            /// </summary>
            public const string custom_id = "custom_id";

            public const string trone_id = "trone_id";
            /// <summary>
            /// 计费日期
            /// </summary>
            public const string fee_date = "fee_date";
            /// <summary>
            /// 计费条数
            /// </summary>
            public const string count = "count";

            #endregion

        }
        #region 表字段变量定义
        /// <summary>
        /// 通常指 imsi  或 mobile。同一个通道必须明确标识方式
        /// </summary>
        private string _custom_id;

        private int _trone_id;
        /// <summary>
        /// 计费日期
        /// </summary>
        private DateTime _fee_date;
        /// <summary>
        /// 计费条数
        /// </summary>
        private int _count;

        #endregion

        public override string IdentifyField { get { return identifyField; } }
        public override string TableName { get { return tableName; } }

        public static readonly string identifyField = "id";
        public static readonly string tableName = "tbl_custom_fee_count";


        public int id
        {
            get;
            set;
        }

        #region 表字段值存取
        /// <summary>
        /// 通常指 imsi  或 mobile。同一个通道必须明确标识方式
        /// </summary>
        public string custom_id
        {
            get { return this._custom_id; }
            set
            {
#if false && false
				RemoveNullFlag(Fields.custom_id);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.custom_id);
                else
                    RemoveNullFlag(Fields.custom_id);
#endif

                SetFieldHasUpdate(Fields.custom_id, this._custom_id, value);
                this._custom_id = value;
            }
        }

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
        /// 计费条数
        /// </summary>
        public int count
        {
            get { return this._count; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.count);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.count);
                else
                    RemoveNullFlag(Fields.count);
#endif

                SetFieldHasUpdate(Fields.count, this._count, value);
                this._count = value;
            }
        }

        #endregion
        #region 空值相关方法
        protected override string[] GetNullableFields()
        {
            return new string[]{null
			};
        }

        #endregion
        #region 静态方法
        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_custom_fee_countItem> GetQueries(Shotgun.Database.IBaseDataClass2 dBase)
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_custom_fee_countItem>(tableName, identifyField, dBase);
        }

        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_custom_fee_countItem> GetQueries()
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_custom_fee_countItem>(tableName, identifyField);
        }
        /// <summary>
        /// 根据主键查找指定的行,返回指定字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <param name="fields">返回字段</param>
        /// <returns></returns>
        public static tbl_custom_fee_countItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id, string[] fields)
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
        public static tbl_custom_fee_countItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id)
        {
            return GetRowById(dBase, id, null);
        }

        #endregion
    }
}