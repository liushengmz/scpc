using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using n8wan.Public.Logical;

namespace LightDataModel
{
    partial class tbl_mrItem : n8wan.Public.Logical.ISMS_DataItem, ICPPushModel
    {
        public const string FIX_TABLE_NAME = "tbl_mr_";
        public const string SCHEMA = "daily_log";

        public tbl_mrItem()
        {
            Schema = "daily_log";
            base.SetTableName(DateTime.Today);

        }

        protected override string FixTableName
        {
            get { return FIX_TABLE_NAME; }
        }

        #region 静态方法
        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.DynamicTableList<tbl_mrItem> GetQueries(Shotgun.Database.IBaseDataClass2 dBase)
        {
            return new Shotgun.Model.List.DynamicTableList<tbl_mrItem>(dBase, SCHEMA, FIX_TABLE_NAME);
        }

        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.DynamicTableList<tbl_mrItem> GetQueries()
        {
            return new Shotgun.Model.List.DynamicTableList<tbl_mrItem>(null, SCHEMA, FIX_TABLE_NAME);

        }

        /// <summary>
        /// 根据主键查找指定的行,返回指定字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <param name="fields">返回字段</param>
        /// <returns></returns>
        public static tbl_mrItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id, string[] fields)
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
        public static tbl_mrItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id)
        {
            return GetRowById(dBase, id, null);
        }

        #endregion


        public DateTime recdate
        {
            get
            {
                return this.mr_date;
            }
            set
            {
                this.mr_date = value;
            }
        }

        /// <summary>
        /// 复制自身到mr_daily中
        /// </summary>
        /// <param name="daily">为空时，返回新实体，否则将改变实体的值</param>
        /// <returns></returns>
        public tbl_mr_dailyItem CopyToDailyMr(tbl_mr_dailyItem daily)
        {
            bool isNew = daily == null;
            if (isNew)
                daily = new tbl_mr_dailyItem();
            string[] fields = new string[] {
                    "imei","imsi","mobile","user_md10","mcc","province_id","city_id","sp_trone_id","trone_id",
                    "trone_order_id","ori_trone","ori_order","linkid","cp_param","service_code","price","ip","status",
                    "syn_flag","mo_table","mo_id","mr_date","create_date","IsMatch","sp_api_url_id","sp_id","cp_id",
                    "ivr_time","trone_type","api_order_id" };
            daily.IgnoreEquals = true;
            foreach (var f in fields)
            {
                object t = this[f];
                if (isNew && t == null)
                    continue;
                daily[f] = this[f];
            }
            if (isNew)
                daily.mr_id = this.id;
            return daily;
        }

        string ICPPushModel.GetValue(EPushField f)
        {
            switch (f)
            {
                case EPushField.cpParam: return this.cp_param;
                case EPushField.LinkID: return this.linkid;
                case EPushField.Mobile: return this.mobile;
                case EPushField.Msg: return this.ori_order;
                case EPushField.port: return this.ori_trone;
                case EPushField.price: return this.price.ToString();
                case EPushField.ServiceCode: return this.service_code;
                case EPushField.Status: return this.status;
                case EPushField.province: return this.province_id.ToString();
                case EPushField.ApiOrderId: return this.api_order_id.ToString();
                case EPushField.city: return this.city_id.ToString();
            }
            throw new NotImplementedException();
        }

        tbl_cp_mrItem ICPPushModel.SetPushed(Shotgun.Database.IBaseDataClass2 dBase, tbl_trone_orderItem tCfg)
        {
            this.syn_flag = 1;
            this.trone_order_id = tCfg.id;
            this.cp_id = tCfg.cp_id;
            dBase.SaveData(this);
            return this.CopyToCP();
        }

        void ICPPushModel.SetHidden(Shotgun.Database.IBaseDataClass2 dBase, tbl_trone_orderItem tCfg)
        {
            this.syn_flag = 0;
            this.trone_order_id = tCfg.id;
            this.cp_id = tCfg.cp_id;
            dBase.SaveData(this);
        }

        public tbl_cp_mrItem CopyToCP()
        {
            tbl_cp_mrItem cpmr = new tbl_cp_mrItem();
            if (!this.IsimeiNull()) cpmr.imei = this.imei;
            if (!this.IsimsiNull()) cpmr.imsi = this.imsi;
            if (!this.IsmobileNull()) cpmr.mobile = this.mobile;
            if (!this.IsmccNull()) cpmr.mcc = this.mcc;
            if (!this.Isprovince_idNull()) cpmr.province_id = this.province_id;
            if (!this.Iscity_idNull()) cpmr.city_id = this.city_id;
            if (!this.Istrone_order_idNull()) cpmr.trone_order_id = this.trone_order_id;
            if (!this.Isori_troneNull()) cpmr.ori_trone = this.ori_trone;
            if (!this.Isori_orderNull()) cpmr.ori_order = this.ori_order;
            if (!this.IslinkidNull()) cpmr.linkid = this.linkid;
            if (!this.Iscp_paramNull()) cpmr.cp_param = this.cp_param;
            if (!this.Isservice_codeNull()) cpmr.service_code = this.service_code;
            if (!this.IspriceNull()) cpmr.price = this.price;
            if (!this.IsipNull()) cpmr.ip = this.ip;
            if (!this.Ismr_dateNull()) cpmr.mr_date = this.mr_date;
            if (!this.Isivr_timeNull()) cpmr.ivr_time = this.ivr_time;

            cpmr.mr_table = this.TableName;
            cpmr.mr_id = this.id;
            cpmr.trone_type = this.trone_type;
            return cpmr;
        }
    }
}
