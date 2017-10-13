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


            var stm = new System.IO.StreamWriter("aa.txt", false, Encoding.UTF8);
            stm.Write("xxxxx");
            
            stm.Close();


            //var rds = new FlowLibraryNet.Logical.RedisOpeartor();
            //var m = new LightDataModel.tbl_f_cp_order_listItem();

            //var xx = "SCOR_PREFIX_a5ecf0c02ce0e7e5";
            //m = rds.GetModel<tbl_f_cp_order_listItem>(xx);
            //if (m == null)
            //{
            //    m = new tbl_f_cp_order_listItem();
            //    Console.WriteLine("Redis value not found!");
            //}
            //m.create_date = DateTime.Now;
            //m.sp_status = "xx00";
            //rds.SetModel(m, xx);

            //Console.Read();
        }
    }
}
