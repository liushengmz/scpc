using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Shotgun.Model.Filter
{
    public enum EM_Safe_Field_MASK
    {
        /// <summary>
        /// 采用MsSQL模式，中括包围
        /// </summary>
        MsSQLMode,
        /// <summary>
        /// 采用MySQL模式，左单引号包围
        /// </summary>
        MySQLMode
    }
}
