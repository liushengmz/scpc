using LightDataModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ClassTester
{
    class Program
    {
        static void Main(string[] args)
        {
            var rds = new FlowLibraryNet.Logical.RedisOpeartor();
            var m = new LightDataModel.tbl_f_cp_order_listItem();
            //m.id = "cp_2o3u49234023";
            m.flowsize = 123;
            m.mobile = "13800138000";
            m.orderid = "abc_lkcasdfa";
            m.create_date = DateTime.Now;

            rds.SetModel(m, "test");
            //rds.Dispose();

            //rds = new FlowLibraryNet.Logical.RedisOpeartor();

            m = rds.GetModel<tbl_f_cp_order_listItem>("test");
            m.create_date = DateTime.Now;
            rds.SetModel(m);

            Console.Read();
        }
    }
}
