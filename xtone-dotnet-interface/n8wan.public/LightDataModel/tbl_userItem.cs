using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    /// <summary>
    /// tbl_user数据模型
    /// </summary>
    public partial class tbl_userItem : Shotgun.Model.Logical.LightDataModel
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
            /// 用户登录名
            /// </summary>
            public const string name = "name";
            /// <summary>
            /// password用MD5加密
            /// </summary>
            public const string pwd = "pwd";
            /// <summary>
            /// 显示在系统的昵称
            /// </summary>
            public const string nick_name = "nick_name";
            /// <summary>
            /// mail
            /// </summary>
            public const string mail = "mail";
            /// <summary>
            /// qq
            /// </summary>
            public const string qq = "qq";
            /// <summary>
            /// phone
            /// </summary>
            public const string phone = "phone";
            /// <summary>
            /// 所属组别，ID对应为tbl_group的主键，用英文逗号隔开，表示同一用户可以从属于多个组别
            /// </summary>
            public const string group_list = "group_list";
            /// <summary>
            /// 状态，1为正常，0为锁定，锁定状态下不能登录
            /// </summary>
            public const string status = "status";
            /// <summary>
            /// 创建时间
            /// </summary>
            public const string create_date = "create_date";

            #endregion

        }
        #region 表字段变量定义
        /// <summary>
        /// 用户登录名
        /// </summary>
        private string _name;
        /// <summary>
        /// password用MD5加密
        /// </summary>
        private string _pwd;
        /// <summary>
        /// 显示在系统的昵称
        /// </summary>
        private string _nick_name;
        /// <summary>
        /// mail
        /// </summary>
        private string _mail;
        /// <summary>
        /// qq
        /// </summary>
        private string _qq;
        /// <summary>
        /// phone
        /// </summary>
        private string _phone;
        /// <summary>
        /// 所属组别，ID对应为tbl_group的主键，用英文逗号隔开，表示同一用户可以从属于多个组别
        /// </summary>
        private string _group_list;
        /// <summary>
        /// 状态，1为正常，0为锁定，锁定状态下不能登录
        /// </summary>
        private int _status;
        /// <summary>
        /// 创建时间
        /// </summary>
        private DateTime _create_date;

        #endregion

        public override string IdentifyField { get { return identifyField; } }
        public override string TableName { get { return tableName; } }

        public static readonly string identifyField = "id";
        public static readonly string tableName = "tbl_user";


        public int id
        {
            get;
            set;
        }

        #region 表字段值存取
        /// <summary>
        /// 用户登录名
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
        /// <summary>
        /// password用MD5加密
        /// </summary>
        public string pwd
        {
            get { return this._pwd; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.pwd);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.pwd);
                else
                    RemoveNullFlag(Fields.pwd);
#endif

                SetFieldHasUpdate(Fields.pwd, this._pwd, value);
                this._pwd = value;
            }
        }
        /// <summary>
        /// 显示在系统的昵称
        /// </summary>
        public string nick_name
        {
            get { return this._nick_name; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.nick_name);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.nick_name);
                else
                    RemoveNullFlag(Fields.nick_name);
#endif

                SetFieldHasUpdate(Fields.nick_name, this._nick_name, value);
                this._nick_name = value;
            }
        }
        /// <summary>
        /// mail
        /// </summary>
        public string mail
        {
            get { return this._mail; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.mail);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.mail);
                else
                    RemoveNullFlag(Fields.mail);
#endif

                SetFieldHasUpdate(Fields.mail, this._mail, value);
                this._mail = value;
            }
        }
        /// <summary>
        /// qq
        /// </summary>
        public string qq
        {
            get { return this._qq; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.qq);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.qq);
                else
                    RemoveNullFlag(Fields.qq);
#endif

                SetFieldHasUpdate(Fields.qq, this._qq, value);
                this._qq = value;
            }
        }
        /// <summary>
        /// phone
        /// </summary>
        public string phone
        {
            get { return this._phone; }
            set
            {
#if false && true
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
        /// 所属组别，ID对应为tbl_group的主键，用英文逗号隔开，表示同一用户可以从属于多个组别
        /// </summary>
        public string group_list
        {
            get { return this._group_list; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.group_list);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.group_list);
                else
                    RemoveNullFlag(Fields.group_list);
#endif

                SetFieldHasUpdate(Fields.group_list, this._group_list, value);
                this._group_list = value;
            }
        }
        /// <summary>
        /// 状态，1为正常，0为锁定，锁定状态下不能登录
        /// </summary>
        public int status
        {
            get { return this._status; }
            set
            {
#if true && true
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
        /// 创建时间
        /// </summary>
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
			,"name"
,"pwd"
,"nick_name"
,"mail"
,"qq"
,"phone"
,"group_list"
,"status"
};
        }
        public bool IsnameNull() { return IsNull(Fields.name); }

        public void SetnameNull() { SetNull(Fields.name); }
        public bool IspwdNull() { return IsNull(Fields.pwd); }

        public void SetpwdNull() { SetNull(Fields.pwd); }
        public bool Isnick_nameNull() { return IsNull(Fields.nick_name); }

        public void Setnick_nameNull() { SetNull(Fields.nick_name); }
        public bool IsmailNull() { return IsNull(Fields.mail); }

        public void SetmailNull() { SetNull(Fields.mail); }
        public bool IsqqNull() { return IsNull(Fields.qq); }

        public void SetqqNull() { SetNull(Fields.qq); }
        public bool IsphoneNull() { return IsNull(Fields.phone); }

        public void SetphoneNull() { SetNull(Fields.phone); }
        public bool Isgroup_listNull() { return IsNull(Fields.group_list); }

        public void Setgroup_listNull() { SetNull(Fields.group_list); }
        public bool IsstatusNull() { return IsNull(Fields.status); }

        public void SetstatusNull() { SetNull(Fields.status); }

        #endregion
        #region 静态方法
        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_userItem> GetQueries(Shotgun.Database.IBaseDataClass2 dBase)
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_userItem>(tableName, identifyField, dBase);
        }

        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_userItem> GetQueries()
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_userItem>(tableName, identifyField);
        }
        /// <summary>
        /// 根据主键查找指定的行,返回指定字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <param name="fields">返回字段</param>
        /// <returns></returns>
        public static tbl_userItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id, string[] fields)
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
        public static tbl_userItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id)
        {
            return GetRowById(dBase, id, null);
        }

        #endregion
    }
}