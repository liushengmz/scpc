using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Shotgun.Model.Logical
{
    public abstract class DynamicDataItem : Shotgun.Model.Logical.LightDataModel
    {

        private string _tabName;

        public override string TableName
        {
            get { return _tabName; }
        }

        /// <summary>
        /// 表示固定的开始表名,后面将自动接入日期
        /// </summary>
        protected abstract string FixTableName { get; }

        /// <summary>
        /// 设置真实的表名
        /// </summary>
        /// <param name="date"></param>
        public void SetTableName(DateTime date)
        {
            _tabName = string.Format("{0}{1:yyyyMM}", FixTableName, date);
        }

        public void SetTableName(Shotgun.Model.List.IDBSQLHelper dbHelper)
        {
            _tabName = dbHelper.table;
        }

    }
}
