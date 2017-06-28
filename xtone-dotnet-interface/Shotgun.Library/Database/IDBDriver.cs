using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Shotgun.Database
{
    public interface IDBDriver
    {
        BDClass CreateDBase();
    }
}
