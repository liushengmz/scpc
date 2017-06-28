using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    /// <summary>
    /// tbl_sp_api_url数据模型
    /// </summary>
    public partial class tbl_sp_api_urlItem : Shotgun.Model.Logical.LightDataModel
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


            public const string sp_id = "sp_id";
            /// <summary>
            /// （虛似）文件名
            /// </summary>
            public const string virtual_page = "virtual_page";
            /// <summary>
            /// 是否物理文件
            /// </summary>
            public const string phy_file = "phy_file";
            /// <summary>
            /// 检查不是为MO同步,格式:FieldName:Regex
            /// </summary>
            public const string MoCheck = "MoCheck";

            public const string MoLink = "MoLink";

            public const string MrLink = "MrLink";
            /// <summary>
            /// Mo记录同步到Mr的字段;格式:SqlField1,SqlField2
            /// </summary>
            public const string MoToMr = "MoToMr";
            /// <summary>
            /// 字段映射(不含:link,price字段) 格式:urlField1:sqlField1,urlField2,sqlField2,.....
            /// </summary>
            public const string MoFieldMap = "MoFieldMap";
            /// <summary>
            /// 字段映射(不含:link,price字段) 格式:urlField1:sqlField1,urlField2,sqlField2,.....
            /// </summary>
            public const string MrFidldMap = "MrFidldMap";
            /// <summary>
            /// Mo同步时,可接收的状态值.格式,正则表达式
            /// </summary>
            public const string MoStatus = "MoStatus";
            /// <summary>
            /// Mr同步时,可接收的状态值.格式,正则表达式
            /// </summary>
            public const string MrStatus = "MrStatus";
            /// <summary>
            /// 同步结果输出:格式:ok/existed/error
            /// </summary>
            public const string MsgOutput = "MsgOutput";
            /// <summary>
            /// 是否失效
            /// </summary>
            public const string Disable = "Disable";
            /// <summary>
            /// 同步地址备注名
            /// </summary>
            public const string name = "name";

            public const string urlPath = "urlPath";
            /// <summary>
            /// 传入价格字段,格式:urlField,0/urlField,1/urlField,2,1a?:100,2a*:200
            /// </summary>
            public const string MrPrice = "MrPrice";
            /// <summary>
            /// 传入价格字段,格式:urlField,0/urlField,1/urlField,2,1a?:100,2a*:200
            /// </summary>
            public const string MoPrice = "MoPrice";
            /// <summary>
            /// SP服务器IP,多个IP以逗号分割
            /// </summary>
            public const string sp_server_ips = "sp_server_ips";

            #endregion

        }
        #region 表字段变量定义

        private int _sp_id;
        /// <summary>
        /// （虛似）文件名
        /// </summary>
        private string _virtual_page;
        /// <summary>
        /// 是否物理文件
        /// </summary>
        private bool _phy_file;
        /// <summary>
        /// 检查不是为MO同步,格式:FieldName:Regex
        /// </summary>
        private string _MoCheck;

        private string _MoLink;

        private string _MrLink;
        /// <summary>
        /// Mo记录同步到Mr的字段;格式:SqlField1,SqlField2
        /// </summary>
        private string _MoToMr;
        /// <summary>
        /// 字段映射(不含:link,price字段) 格式:urlField1:sqlField1,urlField2,sqlField2,.....
        /// </summary>
        private string _MoFieldMap;
        /// <summary>
        /// 字段映射(不含:link,price字段) 格式:urlField1:sqlField1,urlField2,sqlField2,.....
        /// </summary>
        private string _MrFidldMap;
        /// <summary>
        /// Mo同步时,可接收的状态值.格式,正则表达式
        /// </summary>
        private string _MoStatus;
        /// <summary>
        /// Mr同步时,可接收的状态值.格式,正则表达式
        /// </summary>
        private string _MrStatus;
        /// <summary>
        /// 同步结果输出:格式:ok/existed/error
        /// </summary>
        private string _MsgOutput;
        /// <summary>
        /// 是否失效
        /// </summary>
        private bool _Disable;
        /// <summary>
        /// 同步地址备注名
        /// </summary>
        private string _name;

        private string _urlPath;
        /// <summary>
        /// 传入价格字段,格式:urlField,0/urlField,1/urlField,2,1a?:100,2a*:200
        /// </summary>
        private string _MrPrice;
        /// <summary>
        /// 传入价格字段,格式:urlField,0/urlField,1/urlField,2,1a?:100,2a*:200
        /// </summary>
        private string _MoPrice;
        /// <summary>
        /// SP服务器IP,多个IP以逗号分割
        /// </summary>
        private string _sp_server_ips;

        #endregion

        public override string IdentifyField { get { return identifyField; } }
        public override string TableName { get { return tableName; } }

        public static readonly string identifyField = "id";
        public static readonly string tableName = "tbl_sp_api_url";


        public int id
        {
            get;
            set;
        }

        #region 表字段值存取

        public int sp_id
        {
            get { return this._sp_id; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.sp_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.sp_id);
                else
                    RemoveNullFlag(Fields.sp_id);
#endif

                SetFieldHasUpdate(Fields.sp_id, this._sp_id, value);
                this._sp_id = value;
            }
        }
        /// <summary>
        /// （虛似）文件名
        /// </summary>
        public string virtual_page
        {
            get { return this._virtual_page; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.virtual_page);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.virtual_page);
                else
                    RemoveNullFlag(Fields.virtual_page);
#endif

                SetFieldHasUpdate(Fields.virtual_page, this._virtual_page, value);
                this._virtual_page = value;
            }
        }
        /// <summary>
        /// 是否物理文件
        /// </summary>
        public bool phy_file
        {
            get { return this._phy_file; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.phy_file);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.phy_file);
                else
                    RemoveNullFlag(Fields.phy_file);
#endif

                SetFieldHasUpdate(Fields.phy_file, this._phy_file, value);
                this._phy_file = value;
            }
        }
        /// <summary>
        /// 检查不是为MO同步,格式:FieldName:Regex
        /// </summary>
        public string MoCheck
        {
            get { return this._MoCheck; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.MoCheck);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.MoCheck);
                else
                    RemoveNullFlag(Fields.MoCheck);
#endif

                SetFieldHasUpdate(Fields.MoCheck, this._MoCheck, value);
                this._MoCheck = value;
            }
        }

        public string MoLink
        {
            get { return this._MoLink; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.MoLink);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.MoLink);
                else
                    RemoveNullFlag(Fields.MoLink);
#endif

                SetFieldHasUpdate(Fields.MoLink, this._MoLink, value);
                this._MoLink = value;
            }
        }

        public string MrLink
        {
            get { return this._MrLink; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.MrLink);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.MrLink);
                else
                    RemoveNullFlag(Fields.MrLink);
#endif

                SetFieldHasUpdate(Fields.MrLink, this._MrLink, value);
                this._MrLink = value;
            }
        }
        /// <summary>
        /// Mo记录同步到Mr的字段;格式:SqlField1,SqlField2
        /// </summary>
        public string MoToMr
        {
            get { return this._MoToMr; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.MoToMr);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.MoToMr);
                else
                    RemoveNullFlag(Fields.MoToMr);
#endif

                SetFieldHasUpdate(Fields.MoToMr, this._MoToMr, value);
                this._MoToMr = value;
            }
        }
        /// <summary>
        /// 字段映射(不含:link,price字段) 格式:urlField1:sqlField1,urlField2,sqlField2,.....
        /// </summary>
        public string MoFieldMap
        {
            get { return this._MoFieldMap; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.MoFieldMap);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.MoFieldMap);
                else
                    RemoveNullFlag(Fields.MoFieldMap);
#endif

                SetFieldHasUpdate(Fields.MoFieldMap, this._MoFieldMap, value);
                this._MoFieldMap = value;
            }
        }
        /// <summary>
        /// 字段映射(不含:link,price字段) 格式:urlField1:sqlField1,urlField2,sqlField2,.....
        /// </summary>
        public string MrFidldMap
        {
            get { return this._MrFidldMap; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.MrFidldMap);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.MrFidldMap);
                else
                    RemoveNullFlag(Fields.MrFidldMap);
#endif

                SetFieldHasUpdate(Fields.MrFidldMap, this._MrFidldMap, value);
                this._MrFidldMap = value;
            }
        }
        /// <summary>
        /// Mo同步时,可接收的状态值.格式,正则表达式
        /// </summary>
        public string MoStatus
        {
            get { return this._MoStatus; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.MoStatus);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.MoStatus);
                else
                    RemoveNullFlag(Fields.MoStatus);
#endif

                SetFieldHasUpdate(Fields.MoStatus, this._MoStatus, value);
                this._MoStatus = value;
            }
        }
        /// <summary>
        /// Mr同步时,可接收的状态值.格式,正则表达式
        /// </summary>
        public string MrStatus
        {
            get { return this._MrStatus; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.MrStatus);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.MrStatus);
                else
                    RemoveNullFlag(Fields.MrStatus);
#endif

                SetFieldHasUpdate(Fields.MrStatus, this._MrStatus, value);
                this._MrStatus = value;
            }
        }
        /// <summary>
        /// 同步结果输出:格式:ok/existed/error
        /// </summary>
        public string MsgOutput
        {
            get { return this._MsgOutput; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.MsgOutput);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.MsgOutput);
                else
                    RemoveNullFlag(Fields.MsgOutput);
#endif

                SetFieldHasUpdate(Fields.MsgOutput, this._MsgOutput, value);
                this._MsgOutput = value;
            }
        }
        /// <summary>
        /// 是否失效
        /// </summary>
        public bool Disable
        {
            get { return this._Disable; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.Disable);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.Disable);
                else
                    RemoveNullFlag(Fields.Disable);
#endif

                SetFieldHasUpdate(Fields.Disable, this._Disable, value);
                this._Disable = value;
            }
        }
        /// <summary>
        /// 同步地址备注名
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

        public string urlPath
        {
            get { return this._urlPath; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.urlPath);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.urlPath);
                else
                    RemoveNullFlag(Fields.urlPath);
#endif

                SetFieldHasUpdate(Fields.urlPath, this._urlPath, value);
                this._urlPath = value;
            }
        }
        /// <summary>
        /// 传入价格字段,格式:urlField,0/urlField,1/urlField,2,1a?:100,2a*:200
        /// </summary>
        public string MrPrice
        {
            get { return this._MrPrice; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.MrPrice);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.MrPrice);
                else
                    RemoveNullFlag(Fields.MrPrice);
#endif

                SetFieldHasUpdate(Fields.MrPrice, this._MrPrice, value);
                this._MrPrice = value;
            }
        }
        /// <summary>
        /// 传入价格字段,格式:urlField,0/urlField,1/urlField,2,1a?:100,2a*:200
        /// </summary>
        public string MoPrice
        {
            get { return this._MoPrice; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.MoPrice);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.MoPrice);
                else
                    RemoveNullFlag(Fields.MoPrice);
#endif

                SetFieldHasUpdate(Fields.MoPrice, this._MoPrice, value);
                this._MoPrice = value;
            }
        }
        /// <summary>
        /// SP服务器IP,多个IP以逗号分割
        /// </summary>
        public string sp_server_ips
        {
            get { return this._sp_server_ips; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.sp_server_ips);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.sp_server_ips);
                else
                    RemoveNullFlag(Fields.sp_server_ips);
#endif

                SetFieldHasUpdate(Fields.sp_server_ips, this._sp_server_ips, value);
                this._sp_server_ips = value;
            }
        }

        #endregion
        #region 空值相关方法
        protected override string[] GetNullableFields()
        {
            return new string[]{null
			,"virtual_page"
,"phy_file"
,"MoCheck"
,"MoLink"
,"MrLink"
,"MoToMr"
,"MoFieldMap"
,"MrFidldMap"
,"MoStatus"
,"MrStatus"
,"MsgOutput"
,"Disable"
,"name"
,"urlPath"
,"MrPrice"
,"MoPrice"
,"sp_server_ips"
};
        }
        public bool Isvirtual_pageNull() { return IsNull(Fields.virtual_page); }

        public void Setvirtual_pageNull() { SetNull(Fields.virtual_page); }
        public bool Isphy_fileNull() { return IsNull(Fields.phy_file); }

        public void Setphy_fileNull() { SetNull(Fields.phy_file); }
        public bool IsMoCheckNull() { return IsNull(Fields.MoCheck); }

        public void SetMoCheckNull() { SetNull(Fields.MoCheck); }
        public bool IsMoLinkNull() { return IsNull(Fields.MoLink); }

        public void SetMoLinkNull() { SetNull(Fields.MoLink); }
        public bool IsMrLinkNull() { return IsNull(Fields.MrLink); }

        public void SetMrLinkNull() { SetNull(Fields.MrLink); }
        public bool IsMoToMrNull() { return IsNull(Fields.MoToMr); }

        public void SetMoToMrNull() { SetNull(Fields.MoToMr); }
        public bool IsMoFieldMapNull() { return IsNull(Fields.MoFieldMap); }

        public void SetMoFieldMapNull() { SetNull(Fields.MoFieldMap); }
        public bool IsMrFidldMapNull() { return IsNull(Fields.MrFidldMap); }

        public void SetMrFidldMapNull() { SetNull(Fields.MrFidldMap); }
        public bool IsMoStatusNull() { return IsNull(Fields.MoStatus); }

        public void SetMoStatusNull() { SetNull(Fields.MoStatus); }
        public bool IsMrStatusNull() { return IsNull(Fields.MrStatus); }

        public void SetMrStatusNull() { SetNull(Fields.MrStatus); }
        public bool IsMsgOutputNull() { return IsNull(Fields.MsgOutput); }

        public void SetMsgOutputNull() { SetNull(Fields.MsgOutput); }
        public bool IsDisableNull() { return IsNull(Fields.Disable); }

        public void SetDisableNull() { SetNull(Fields.Disable); }
        public bool IsnameNull() { return IsNull(Fields.name); }

        public void SetnameNull() { SetNull(Fields.name); }
        public bool IsurlPathNull() { return IsNull(Fields.urlPath); }

        public void SeturlPathNull() { SetNull(Fields.urlPath); }
        public bool IsMrPriceNull() { return IsNull(Fields.MrPrice); }

        public void SetMrPriceNull() { SetNull(Fields.MrPrice); }
        public bool IsMoPriceNull() { return IsNull(Fields.MoPrice); }

        public void SetMoPriceNull() { SetNull(Fields.MoPrice); }
        public bool Issp_server_ipsNull() { return IsNull(Fields.sp_server_ips); }

        public void Setsp_server_ipsNull() { SetNull(Fields.sp_server_ips); }

        #endregion
        #region 静态方法
        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_sp_api_urlItem> GetQueries(Shotgun.Database.IBaseDataClass2 dBase)
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_sp_api_urlItem>(tableName, identifyField, dBase);
        }

        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_sp_api_urlItem> GetQueries()
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_sp_api_urlItem>(tableName, identifyField);
        }


        /// <summary>
        /// 根据主键查找指定的行,返回所有字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <returns></returns>
        public static tbl_sp_api_urlItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id)
        {
            return GetRowById(dBase, id, null);
        }

        #endregion
    }
}