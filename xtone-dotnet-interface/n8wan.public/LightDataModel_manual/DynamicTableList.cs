using LightDataModel;
using Shotgun.Database;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Shotgun.Model.List
{
    public class DynamicTableList<T> : Shotgun.Model.List.LightDataQueries<T> where T : Shotgun.Model.Logical.DynamicDataItem, new()
    {
        private string _fixTableName;
        private DateTime _TableDate;

        /// <summary>
        /// 用于指定表名前缀
        /// </summary>
        public string FixTableName
        {
            get { return _fixTableName; }
            private set
            {
                _fixTableName = value;
                this.TableName = string.Format("{0}{1:yyyyMM}", _fixTableName, _TableDate);
            }
        }

        /// <summary>
        /// 用于指定表名后缀
        /// </summary>
        public DateTime TableDate
        {
            get { return _TableDate; }
            set
            {
                _TableDate = value;
                this.TableName = string.Format("{0}{1:yyyyMM}", _fixTableName, _TableDate);
            }
        }


        /// <summary>
        /// 
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="fixTabName">固定的表头名</param>
        public DynamicTableList(IBaseDataClass2 dBase, string SchemaName, string fixTabName)
            : this(dBase, SchemaName, fixTabName, DateTime.Today)
        {
        }

        public DynamicTableList(IBaseDataClass2 dBase, string SchemaName, string fixTabName, DateTime date)
        {
            this.Schema = SchemaName;
            this._fixTableName = fixTabName;
            this.TableDate = date;
            base.dBase = dBase;
        }


        public override List<T> GetDataList()
        {
            var dt = base.GetDataList();
            foreach (var m in dt)
            {
                m.SetTableName(this);
            }
            return dt;
        }
        public override T GetRowByFilters()
        {
            var m = base.GetRowByFilters();
            if (m != null)
                m.SetTableName(this);
            return m;
        }
        public override T GetRowById(int id)
        {
            var m = base.GetRowById(id);
            if (m != null)
                m.SetTableName(this);
            return m;
        }
    }
}
