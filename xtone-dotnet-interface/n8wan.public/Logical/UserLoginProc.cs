using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace n8wan.Public.Logical
{
    class UserLoginProc : Shotgun.Security.LoginHandler
    {
        protected override Shotgun.Database.BDClass CreateDBase()
        {
            return new Shotgun.Database.MySqlDBClass();
        }

        protected override string DoLogin(string userName, string password, string pType)
        {
            var q = LightDataModel.tbl_userItem.GetQueries(base.dBase);
            q.Filter.AndFilters.Add(LightDataModel.tbl_userItem.Fields.name, userName);
            q.Filter.AndFilters.Add(LightDataModel.tbl_userItem.Fields.status, 1);
            var r = q.GetRowByFilters();
            if (r == null)
                return null;
            if (pType == "md5")
            {
                if (!r.pwd.Equals(password, StringComparison.OrdinalIgnoreCase))
                    return null;
            }
            else
            {
                var md5 = Shotgun.Library.Static.StrId(password);
                if (!r.pwd.ToUpper().Contains(md5))
                {
                    return null;
                }
            }
            return string.Format("{0}|{1}|{1}", r.id, r.name, r.group_list);
        }

    }
}
