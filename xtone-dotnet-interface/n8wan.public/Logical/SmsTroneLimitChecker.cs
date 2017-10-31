using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using LightDataModel;
using Shotgun.Database;
using Shotgun.Model.Filter;

namespace n8wan.Public.Logical
{
    /// <summary>
    /// 短信业务的日月限控制程序
    /// </summary>
    public class SmsTroneLimitChecker
    {
        private tbl_sp_troneItem _spTrone;

        public SmsTroneLimitChecker(Shotgun.Database.IBaseDataClass2 db)
        {
            this.DBase = db;
        }

        public IBaseDataClass2 DBase { get; }

        public LightDataModel.tbl_troneItem Trone { get; set; }
        public LightDataModel.tbl_sp_troneItem SpTrone
        {
            get
            {
                if (_spTrone != null)
                    return _spTrone;
                if (Trone == null)
                    return null;
                return _spTrone = LightDataModel.tbl_sp_troneItem.GetRowById(DBase, Trone.sp_trone_id);
            }
            set { _spTrone = value; }
        }

        /// <summary>
        /// 检查是否超日月限
        /// 短信通道由不能控制上量，所以只检查单独渠道（未扣理数据）是否超限，不检查渠道间（整体）的数据
        /// </summary>
        /// <param name="mr"></param>
        /// <returns></returns>
        public bool DoCheck(LightDataModel.tbl_mrItem mr)
        {
            if (mr == null || string.IsNullOrEmpty(mr.mobile) || SpTrone == null || _spTrone.up_data_type != 1)
                return false;

            if (SpTrone.user_day_limit == 0 && SpTrone.user_month_limit == 0)
                return false;

            var q = tbl_mrItem.GetQueries(DBase);
            q.TableDate = mr.create_date;
            q.Filter.AndFilters.Add(tbl_mrItem.Fields.trone_id, Trone.id);
            q.Filter.AndFilters.Add(tbl_mrItem.Fields.PrimaryKey, mr.id, EM_DataFiler_Operator.Less);
            q.Filter.AndFilters.Add(tbl_mrItem.Fields.mobile, mr.mobile);
            q.Filter.AndFilters.Add(tbl_mrItem.Fields.syn_flag, 1);

            if (mr.trone_order_id != 0)
                q.Filter.AndFilters.Add(tbl_mrItem.Fields.trone_order_id, mr.trone_order_id);
            var data = q.GetColumnList<DateTime>(tbl_mrItem.Fields.mr_date);

            int dCount = 1, mCount;
            mCount = 1 + data.Count(e =>
                  {
                      if (e.Date == mr.create_date.Date)
                          dCount++;
                      return true;
                  });

            decimal dData, mData;
            if (SpTrone.limit_type == 0)//元
            {
                dData = Trone.price * dCount;
                mData = Trone.price * mCount;
            }
            else //按条
            {
                dData = dCount;
                mData = mCount;
            }
            if (mData > SpTrone.user_month_limit && SpTrone.user_month_limit > 0)
                return true;
            return dData > SpTrone.user_day_limit;

        }
    }
}
