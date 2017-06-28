using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Shotgun.Model.Filter
{
    public class DataFilterCollections : List<IDataFilter>
    {
        /// <summary>
        /// 快速添加DataFilter
        /// </summary>
        /// <param name="field"></param>
        /// <param name="value"></param>
        /// <returns></returns>
        public IDataFilter Add(string field, object value)
        {
            IDataFilter da = new DataFilter(field, value);
            this.Add(da);
            return da;
        }

        /// <summary>
        /// 快速添加DataFilter
        /// </summary>
        /// <param name="field"></param>
        /// <param name="value"></param>
        /// <returns></returns>
        public IDataFilter Add(string field, object value, EM_DataFiler_Operator Operator)
        {
            IDataFilter da = new DataFilter(field, value, Operator);
            this.Add(da);
            return da;
        }
    }
}