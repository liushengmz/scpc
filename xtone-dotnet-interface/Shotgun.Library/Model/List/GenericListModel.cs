using System;
using System.Collections.Generic;
using System.Text;
using Shotgun.Model.Data;
using System.Data;

namespace Shotgun.Model.List
{
    public class GenericListModel<T> : BaseListModel<T>
        where T : DataTable, new()
    {
        readonly string _tableName, _identityField;

        public GenericListModel(string TableName)
        {
            _tableName = TableName;
            _identityField = "id";
        }

        public GenericListModel(string TableName, string IdentityField)
        {
            _tableName = TableName;
            _identityField = IdentityField;

        }

        protected override string TableName
        {
            get { return _tableName; }
        }

        protected override string IdentityField
        {
            get { return _identityField; }
        }
    }
}
