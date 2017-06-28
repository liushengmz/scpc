using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    /// <summary>
    /// tbl_mobile_white_list数据模型
    /// </summary>
    public partial class tbl_mobile_white_listItem : Shotgun.Model.Logical.LightDataModel
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
            /// 手机号或IMSI
            /// </summary>
            public const string mobile = "mobile";

            public const string adddate = "adddate";

            #endregion

        }
        #region 表字段变量定义
        /// <summary>
        /// 手机号或IMSI
        /// </summary>
        private string _mobile;

        private DateTime _adddate;

        #endregion

        public override string IdentifyField { get { return identifyField; } }
        public override string TableName { get { return tableName; } }

        public static readonly string identifyField = "id";
        public static readonly string tableName = "tbl_mobile_white_list";


        public int id
        {
            get;
            set;
        }

        #region 表字段值存取
        /// <summary>
        /// 手机号或IMSI
        /// </summary>
        public string mobile
        {
            get { return this._mobile; }
            set
            {
#if false && false
				RemoveNullFlag(Fields.mobile);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.mobile);
                else
                    RemoveNullFlag(Fields.mobile);
#endif

                SetFieldHasUpdate(Fields.mobile, this._mobile, value);
                this._mobile = value;
            }
        }

        public DateTime adddate
        {
            get { return this._adddate; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.adddate);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.adddate);
                else
                    RemoveNullFlag(Fields.adddate);
#endif

                SetFieldHasUpdate(Fields.adddate, this._adddate, value);
                this._adddate = value;
            }
        }

        #endregion
        #region 空值相关方法
        protected override string[] GetNullableFields()
        {
            return new string[]{null
			,"adddate"
};
        }
        public bool IsadddateNull() { return IsNull(Fields.adddate); }

        public void SetadddateNull() { SetNull(Fields.adddate); }

        #endregion
        #region 静态方法
        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_mobile_white_listItem> GetQueries(Shotgun.Database.IBaseDataClass2 dBase)
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_mobile_white_listItem>(tableName, identifyField, dBase);
        }

        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_mobile_white_listItem> GetQueries()
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_mobile_white_listItem>(tableName, identifyField);
        }
        /// <summary>
        /// 根据主键查找指定的行,返回指定字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <param name="fields">返回字段</param>
        /// <returns></returns>
        public static tbl_mobile_white_listItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id, string[] fields)
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
        public static tbl_mobile_white_listItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id)
        {
            return GetRowById(dBase, id, null);
        }

        #endregion
    }
}