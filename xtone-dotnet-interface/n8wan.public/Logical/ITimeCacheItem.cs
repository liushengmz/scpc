using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace n8wan.Public.Logical
{
    public interface ITimeCacheItem
    {
        int ItemExpire { get; set; }
    }
}
