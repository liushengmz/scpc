using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    /// <summary>
    /// tbl_phone_locate数据模型
    /// </summary>
    public partial class tbl_phone_locateItem : Shotgun.Model.Logical.LightDataModel
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
            /// 电话号码前缀
            /// </summary>
            public const string phone = "phone";
            /// <summary>
            /// tbl_city主键ID
            /// </summary>
            public const string city_id = "city_id";
            /// <summary>
            /// 运营商
            /// </summary>
            public const string @operator = "operator";

            #endregion

        }
        #region 表字段变量定义
        /// <summary>
        /// 电话号码前缀
        /// </summary>
        private string _phone;
        /// <summary>
        /// tbl_city主键ID
        /// </summary>
        private int _city_id;
        /// <summary>
        /// 运营商
        /// </summary>
        private int _operator;

        #endregion

        public override string IdentifyField { get { return identifyField; } }
        public override string TableName { get { return tableName; } }

        public static readonly string identifyField = "id";
        public static readonly string tableName = "tbl_phone_locate";


        public int id
        {
            get;
            set;
        }

        #region 表字段值存取
        /// <summary>
        /// 电话号码前缀
        /// </summary>
        public string phone
        {
            get { return this._phone; }
            set
            {
#if false && false
				RemoveNullFlag(Fields.phone);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.phone);
                else
                    RemoveNullFlag(Fields.phone);
#endif

                SetFieldHasUpdate(Fields.phone, this._phone, value);
                this._phone = value;
            }
        }
        /// <summary>
        /// tbl_city主键ID
        /// </summary>
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
        /// <summary>
        /// 运营商
        /// </summary>
        public int @operator
        {
            get { return this._operator; }
            set
            {
#if true && true
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

        #endregion
        #region 空值相关方法
        protected override string[] GetNullableFields()
        {
            return new string[]{null
			,"operator"
};
        }
        public bool IsoperatorNull() { return IsNull(Fields.@operator); }

        public void SetoperatorNull() { SetNull(Fields.@operator); }

        #endregion
        #region 静态方法
        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_phone_locateItem> GetQueries(Shotgun.Database.IBaseDataClass2 dBase)
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_phone_locateItem>(tableName, identifyField, dBase);
        }

        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_phone_locateItem> GetQueries()
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_phone_locateItem>(tableName, identifyField);
        }
        /// <summary>
        /// 根据主键查找指定的行,返回指定字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <param name="fields">返回字段</param>
        /// <returns></returns>
        public static tbl_phone_locateItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id, string[] fields)
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
        public static tbl_phone_locateItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id)
        {
            return GetRowById(dBase, id, null);
        }

        #endregion
    }
}