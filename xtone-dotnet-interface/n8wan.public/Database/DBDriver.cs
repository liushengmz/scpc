using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Shotgun.Database
{
    /// <summary>
    /// 用于反射，调用默认的数据驱动
    /// </summary>
    public class DBDriver : Shotgun.Database.IDBDriver
    {
        public Database.BDClass CreateDBase()
        {
            return new Shotgun.Database.MySqlDBClass();
        }

    }
}
