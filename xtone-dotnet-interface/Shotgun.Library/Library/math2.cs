using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Shotgun.Library
{
    public static class math2
    {
        /// <summary>
        /// 求两个数的最大公约数
        /// </summary>
        /// <param name="a"></param>
        /// <param name="b"></param>
        /// <returns></returns>
        public static int GCD(int a, int b)
        {
            int t;
            if (a < b)
            {
                t = a;
                a = b;
                b = t;
            }
            while (b != 0)
            {
                t = a % b;
                a = b;
                b = t;
            }
            return a;
        }

    }
}
