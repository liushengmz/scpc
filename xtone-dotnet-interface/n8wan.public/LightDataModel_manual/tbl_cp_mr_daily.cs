using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{

    public partial class tbl_cp_mr_dailyItem
    {
        public override string Schema
        {
            get
            {
                return "daily_log";
            }
            protected set
            {
                throw new NotImplementedException();
            }
        }
    }
}
