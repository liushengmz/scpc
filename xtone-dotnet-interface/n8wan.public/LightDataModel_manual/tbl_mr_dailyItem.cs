using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    partial class tbl_mr_dailyItem
    {
        public override string Schema
        {
            get
            {
                return C_Schema;
            }
            protected set
            {
                throw new NotImplementedException();
            }
        }

        public const string C_Schema = "daily_log";

        /// <summary>
        /// 根据 mr_id 获取虚拟 daily对像（仅主键，其它数据为空）
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id"></param>
        /// <returns></returns>
        public static tbl_mr_dailyItem GetVRDaily(Shotgun.Database.IBaseDataClass2 dBase, tbl_mrItem mr)
        {

            if (!mr.mr_date.Equals(DateTime.Today))
                return null;
            var q = tbl_mr_dailyItem.GetQueries(dBase);
            q.Schema = C_Schema;
            q.Filter.AndFilters.Add(tbl_mr_dailyItem.Fields.mr_id, mr.id);
            var idObj = q.ExecuteScalar(tbl_mr_dailyItem.Fields.PrimaryKey);
            if (idObj == null)
                return null;
            return new tbl_mr_dailyItem() { id = (int)idObj };

        }
    }
}
