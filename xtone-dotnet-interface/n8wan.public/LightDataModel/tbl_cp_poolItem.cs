using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    /// <summary>
    /// tbl_cp_pool数据模型
    /// </summary>
    public partial class tbl_cp_poolItem : Shotgun.Model.Logical.LightDataModel
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


            public const string cp_id = "cp_id";

            public const string create_date = "create_date";

            public const string name = "name";

            public const string status = "status";
            /// <summary>
            /// 价格,单位：分
            /// </summary>
            public const string fee = "fee";

            #endregion

        }
        #region 表字段变量定义

        private int _cp_id;

        private DateTime _create_date;

        private string _name;

        private bool _status;
        /// <summary>
        /// 价格,单位：分
        /// </summary>
        private int _fee;

        #endregion

        public override string IdentifyField { get { return identifyField; } }
        public override string TableName { get { return tableName; } }

        public static readonly string identifyField = "id";
        public static readonly string tableName = "tbl_cp_pool";


        public int id
        {
            get;
            set;
        }

        #region 表字段值存取

        public int cp_id
        {
            get { return this._cp_id; }
            set
            {
#if true && false
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

        public bool status
        {
            get { return this._status; }
            set
            {
#if true && false
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
        /// 价格,单位：分
        /// </summary>
        public int fee
        {
            get { return this._fee; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.fee);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.fee);
                else
                    RemoveNullFlag(Fields.fee);
#endif

                SetFieldHasUpdate(Fields.fee, this._fee, value);
                this._fee = value;
            }
        }

        #endregion
        #region 空值相关方法
        protected override string[] GetNullableFields()
        {
            return new string[]{null
			,"name"
,"fee"
};
        }
        public bool IsnameNull() { return IsNull(Fields.name); }

        public void SetnameNull() { SetNull(Fields.name); }
        public bool IsfeeNull() { return IsNull(Fields.fee); }

        public void SetfeeNull() { SetNull(Fields.fee); }

        #endregion
        #region 静态方法
        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_cp_poolItem> GetQueries(Shotgun.Database.IBaseDataClass2 dBase)
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_cp_poolItem>(tableName, identifyField, dBase);
        }

        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_cp_poolItem> GetQueries()
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_cp_poolItem>(tableName, identifyField);
        }

        /// <summary>
        /// 根据主键查找指定的行,返回所有字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <returns></returns>
        public static tbl_cp_poolItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id)
        {
            return GetRowById(dBase, id, null);
        }

        #endregion
    }
}