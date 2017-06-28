using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    /// <summary>
    /// tbl_country数据模型
    /// </summary>
    public partial class tbl_countryItem : Shotgun.Model.Logical.LightDataModel
    {
		/// <summary>
        /// 数据表字段列表对像
        /// </summary>
		public sealed class Fields{
			private Fields(){}
			#region 表字段名称
			
			public const string id="id";
			///<summary>
			///主键
			///</summary>
			public const string PrimaryKey="id";

			/// <summary>
		/// 国家码
		/// </summary>
			public const string mcc="mcc";
/// <summary>
		/// 国家中文名称
		/// </summary>
			public const string name_cn="name_cn";
/// <summary>
		/// 国家英文名称
		/// </summary>
			public const string name_en="name_en";
/// <summary>
		/// 简称
		/// </summary>
			public const string short_name="short_name";

			#endregion

		}
		#region 表字段变量定义
		/// <summary>
		/// 国家码
		/// </summary>
        private string _mcc;
/// <summary>
		/// 国家中文名称
		/// </summary>
        private string _name_cn;
/// <summary>
		/// 国家英文名称
		/// </summary>
        private string _name_en;
/// <summary>
		/// 简称
		/// </summary>
        private string _short_name;

		#endregion

        public override string IdentifyField { get { return identifyField; } }
        public override string TableName { get { return tableName; } }

		public static readonly string identifyField ="id";
		public static readonly string tableName ="tbl_country";

		
		public int id{
			get;set;
		}

		#region 表字段值存取
		/// <summary>
		/// 国家码
		/// </summary>
        public string mcc{
            get { return this._mcc; }
            set { 
				#if false && false
				RemoveNullFlag(Fields.mcc);
				#elif !false
			    if (value == null)
                    SetNullFlag(Fields.mcc);
                else
                    RemoveNullFlag(Fields.mcc);
				#endif

				SetFieldHasUpdate(Fields.mcc, this._mcc, value); 
				this._mcc = value; }
		}
/// <summary>
		/// 国家中文名称
		/// </summary>
        public string name_cn{
            get { return this._name_cn; }
            set { 
				#if false && true
				RemoveNullFlag(Fields.name_cn);
				#elif !false
			    if (value == null)
                    SetNullFlag(Fields.name_cn);
                else
                    RemoveNullFlag(Fields.name_cn);
				#endif

				SetFieldHasUpdate(Fields.name_cn, this._name_cn, value); 
				this._name_cn = value; }
		}
/// <summary>
		/// 国家英文名称
		/// </summary>
        public string name_en{
            get { return this._name_en; }
            set { 
				#if false && true
				RemoveNullFlag(Fields.name_en);
				#elif !false
			    if (value == null)
                    SetNullFlag(Fields.name_en);
                else
                    RemoveNullFlag(Fields.name_en);
				#endif

				SetFieldHasUpdate(Fields.name_en, this._name_en, value); 
				this._name_en = value; }
		}
/// <summary>
		/// 简称
		/// </summary>
        public string short_name{
            get { return this._short_name; }
            set { 
				#if false && true
				RemoveNullFlag(Fields.short_name);
				#elif !false
			    if (value == null)
                    SetNullFlag(Fields.short_name);
                else
                    RemoveNullFlag(Fields.short_name);
				#endif

				SetFieldHasUpdate(Fields.short_name, this._short_name, value); 
				this._short_name = value; }
		}

		#endregion
		#region 空值相关方法
		protected override string[] GetNullableFields(){
			return new string[]{null
			,"name_cn"
,"name_en"
,"short_name"
};
		}
		public bool Isname_cnNull(){ return IsNull(Fields.name_cn);}

		public void Setname_cnNull(){ SetNull(Fields.name_cn);}
public bool Isname_enNull(){ return IsNull(Fields.name_en);}

		public void Setname_enNull(){ SetNull(Fields.name_en);}
public bool Isshort_nameNull(){ return IsNull(Fields.short_name);}

		public void Setshort_nameNull(){ SetNull(Fields.short_name);}

		#endregion
		#region 静态方法
        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_countryItem> GetQueries(Shotgun.Database.IBaseDataClass2 dBase)
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_countryItem>(tableName, identifyField, dBase);
        }

        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_countryItem> GetQueries()
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_countryItem>(tableName, identifyField);
        }
		/// <summary>
        /// 根据主键查找指定的行,返回指定字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <param name="fields">返回字段</param>
        /// <returns></returns>
        public static tbl_countryItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id, string[] fields)
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
        public static tbl_countryItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id)
        {
            return GetRowById(dBase, id, null);
        }

		#endregion
    }
}