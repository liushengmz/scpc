using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace Shotgun.Library
{
    public partial class PagePlus
    {
        /// <summary>
        /// 自动调用控件的DataSource.Dispose()
        /// </summary>
        /// <param name="page"></param>
        public static void DataSouceAutoCollect(Control rootControl)
        {
            if ((rootControl is BaseDataBoundControl))
            {
                BaseDataBoundControl bdbc = (BaseDataBoundControl)rootControl;
                if ((bdbc.DataSource is IDisposable))
                {
                    IDisposable d = (IDisposable)rootControl;
                    d.Dispose();
                    bdbc.DataSource = null;
                }
            }
            if (rootControl.Controls.Count == 0)
                return;
            foreach (Control c in rootControl.Controls)
            {
                DataSouceAutoCollect(c);
            }

        }

         

    }
}
