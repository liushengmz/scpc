using System;
using System.Collections.Generic;
using System.Text;
using Shotgun.Library;
using System.Data;

namespace Shotgun.Security
{

    public enum ASI_PASSWORD_TYPE
    {
        Text,
        /// <summary>
        /// MD5 32
        /// </summary>
        MD5,
        MD5_16
    }

    public abstract class AdminSignIn
    {
        public abstract bool SignIn(string userName, string psw, ASI_PASSWORD_TYPE pType);

        public string useName { get; protected set; }
        public int userId { get; protected set; }
        public string Right { get; protected set; }

        public override string ToString()
        {
            return string.Format("{0}|{1}|{2}", userId, useName, Right);
        }
    }
}
