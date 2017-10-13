using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    /// <summary>
    /// tbl_f_basic_price数据模型
    /// </summary>
    public partial class tbl_f_basic_priceItem : Shotgun.Model.Logical.LightDataModel
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
            /// 名称
            /// </summary>
            public const string name = "name";
            /// <summary>
            /// 流量数,单位M
            /// </summary>
            public const string num = "num";
            /// <summary>
            /// 价格（分）
            /// </summary>
            public const string price = "price";
            /// <summary>
            /// 运营商daily_config.tbl_operator.id
            /// </summary>
            public const string @operator = "operator";

            public const string create_date = "create_date";

            #endregion

        }
        #region 表字段变量定义
        /// <summary>
        /// 名称
        /// </summary>
        private string _name;
        /// <summary>
        /// 流量数,单位M
        /// </summary>
        private int _num;
        /// <summary>
        /// 价格（分）
        /// </summary>
        private int _price;
        /// <summary>
        /// 运营商daily_config.tbl_operator.id
        /// </summary>
        private short _operator;

        private DateTime _create_date;

        #endregion

        public override string IdentifyField { get { return identifyField; } }
        public override string TableName { get { return tableName; } }

        public static readonly string identifyField = "id";
        public static readonly string tableName = "tbl_f_basic_price";


        public int id
        {
            get; set;
        }

        #region 表字段值存取
        /// <summary>
        /// 名称
        /// </summary>
        public string name
        {
            get { return this._name; }
            set
            {
#if false && false
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
        /// 流量数,单位M
        /// </summary>
        public int num
        {
            get { return this._num; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.num);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.num);
                else
                    RemoveNullFlag(Fields.num);
#endif

                SetFieldHasUpdate(Fields.num, this._num, value);
                this._num = value;
            }
        }
        /// <summary>
        /// 价格（分）
        /// </summary>
        public int price
        {
            get { return this._price; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.price);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.price);
                else
                    RemoveNullFlag(Fields.price);
#endif

                SetFieldHasUpdate(Fields.price, this._price, value);
                this._price = value;
            }
        }
        /// <summary>
        /// 运营商daily_config.tbl_operator.id
        /// </summary>
        public short @operator
        {
            get { return this._operator; }
            set
            {
#if true && false
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
        public static Shotgun.Model.List.LightDataQueries<tbl_f_basic_priceItem> GetQueries(Shotgun.Database.IBaseDataClass2 dBase)
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_f_basic_priceItem>(tableName, identifyField, dBase);
        }

        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_f_basic_priceItem> GetQueries()
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_f_basic_priceItem>(tableName, identifyField);
        }

        /// <summary>
        /// 根据主键查找指定的行,返回所有字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <returns></returns>
        public static tbl_f_basic_priceItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id)
        {
            return GetRowById(dBase, id, null);
        }

        #endregion
    }
}