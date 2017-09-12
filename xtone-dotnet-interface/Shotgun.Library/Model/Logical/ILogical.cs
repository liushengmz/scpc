using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Shotgun.Model.Data;

namespace Shotgun.Model.Logical
{
    public interface ILogical
    {
        Shotgun.Database.IBaseDataClass2 dBase { get; set; }
        bool IsSuccess { get; }
        string ErrorMesage { get; }
        /// <summary>
        /// 最后一次操作，更新的记录数
        /// </summary>
        int lastUpdateCount { get; }

    }

    public abstract class Logical : ILogical
    {

        public Logical(Shotgun.Database.IBaseDataClass2 db)
        {
            this.dBase = db;
        }
        public Logical()
        {
        }

        #region ILogical 成员
        public virtual Shotgun.Database.IBaseDataClass2 dBase { get; set; }

        public bool IsSuccess { get; protected set; }

        public string ErrorMesage { get; protected set; }

        /// <summary>
        /// 最后一次成功操作，影响的记录数
        /// </summary>
        public int lastUpdateCount { get; protected set; }

        #endregion

        /// <summary>
        /// 设置错误信息
        /// </summary>
        /// <param name="err"></param>
        /// <returns>false only</returns>
        protected virtual bool SetErrorMesage(string err)
        {
            ErrorMesage = err;
            IsSuccess = false;
            return false;
        }

        /// <summary>
        /// 设置状态为成功，并设置错误信息为“成功”
        /// </summary>
        /// <returns>true only</returns>
        protected virtual bool SetSuccess()
        {
            ErrorMesage = "成功";
            IsSuccess = true;
            return true;
        }

    }
}
