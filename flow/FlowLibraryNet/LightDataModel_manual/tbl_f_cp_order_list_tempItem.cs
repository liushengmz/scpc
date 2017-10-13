using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LightDataModel
{
    public class tbl_f_cp_order_list_tempItem : Shotgun.Model.Logical.LightDataModel
    {
        private string _sp_status;
        private string _sp_error_msg;
        private int _status;

        public const string SCHEMA = "daily_log";

        public override string IdentifyField => "id";

        public override string TableName => "tbl_f_cp_order_list";

        public override string Schema { get => SCHEMA; protected set => throw new NotSupportedException(); }


        protected override string[] GetNullableFields()
        {
            return null;
        }

        public int id { get; set; }

        public string sp_status
        {
            get { return this._sp_status; }
            set
            {
                SetFieldHasUpdate(Fields.sp_status, this._sp_status, value);
                this._sp_status = value;
            }
        }
        public string sp_error_msg
        {
            get { return this._sp_error_msg; }
            set
            {
                SetFieldHasUpdate(Fields.sp_error_msg, this._sp_error_msg, value);
                this._sp_error_msg = value;
            }
        }
        public int status
        {
            get { return this._status; }
            set
            {
                SetFieldHasUpdate(Fields.status, this._status, value);
                this._status = value;
            }
        }


        public class Fields
        {
            public const string id = "id";
            ///<summary>
            ///主键
            ///</summary>
            public const string PrimaryKey = "id";

            public const string sp_status = "sp_status";
            public const string sp_error_msg = "sp_error_msg";
            public const string status = "status";

        }


    }
}
