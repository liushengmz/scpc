using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    /// <summary>
    /// tbl_city数据模型
    /// </summary>
    public partial class tbl_cityItem : Shotgun.Model.Logical.LightDataModel
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
            /// 省份ID
            /// </summary>
            public const string province_id = "province_id";
            /// <summary>
            /// 城市名称
            /// </summary>
            public const string name = "name";

            #endregion

        }
        #region 表字段变量定义
        /// <summary>
        /// 省份ID
        /// </summary>
        private int _province_id;
        /// <summary>
        /// 城市名称
        /// </summary>
        private string _name;

        #endregion

        public override string IdentifyField { get { return identifyField; } }
        public override string TableName { get { return tableName; } }

        public static readonly string identifyField = "id";
        public static readonly string tableName = "tbl_city";


        public int id
        {
            get;
            set;
        }

        #region 表字段值存取
        /// <summary>
        /// 省份ID
        /// </summary>
        public int province_id
        {
            get { return this._province_id; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.province_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.province_id);
                else
                    RemoveNullFlag(Fields.province_id);
#endif

                SetFieldHasUpdate(Fields.province_id, this._province_id, value);
                this._province_id = value;
            }
        }
        /// <summary>
        /// 城市名称
        /// </summary>
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

        #endregion
        #region 空值相关方法
        protected override string[] GetNullableFields()
        {
            return new string[]{null
			,"province_id"
,"name"
};
        }
        public bool Isprovince_idNull() { return IsNull(Fields.province_id); }

        public void Setprovince_idNull() { SetNull(Fields.province_id); }
        public bool IsnameNull() { return IsNull(Fields.name); }

        public void SetnameNull() { SetNull(Fields.name); }

        #endregion
        #region 静态方法
        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_cityItem> GetQueries(Shotgun.Database.IBaseDataClass2 dBase)
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_cityItem>(tableName, identifyField, dBase);
        }

        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_cityItem> GetQueries()
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_cityItem>(tableName, identifyField);
        }


        /// <summary>
        /// 根据主键查找指定的行,返回所有字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <returns></returns>
        public static tbl_cityItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id)
        {
            return GetRowById(dBase, id, null);
        }


        #endregion
    }
}