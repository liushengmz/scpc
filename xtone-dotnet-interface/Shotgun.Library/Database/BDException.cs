using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Shotgun.Database
{
    public class BDException : System.Data.Common.DbException
    {
        public BDException(string ErrorMessage):base(ErrorMessage)
        {
        }
    }
}
