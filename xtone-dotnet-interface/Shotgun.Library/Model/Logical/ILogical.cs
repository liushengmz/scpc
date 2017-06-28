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
            this._dBase = db;
        }
        public Logical()
        {
        }

        #region ILogical 成员
        Shotgun.Database.IBaseDataClass2 _dBase;
        public Shotgun.Database.IBaseDataClass2 dBase
        {
            get
            {
                return _dBase;
            }
            set
            {
                _dBase = value;
            }
        }

        bool _isSuccess;
        public bool IsSuccess
        {
            get { return _isSuccess; }
            protected set { _isSuccess = value; }

        }
        string _msg;
        public string ErrorMesage
        {
            get { return _msg; }
            protected set { _msg = value; }
        }
        int _lastUpadted = 0;
        /// <summary>
        /// 最后一次成功操作，影响的记录数
        /// </summary>
        public int lastUpdateCount
        {
            get { return _lastUpadted; }
            protected set { _lastUpadted = value; }
        }

        #endregion

        /// <summary>
        /// 设置错误信息
        /// </summary>
        /// <param name="err"></param>
        /// <returns>false only</returns>
        protected virtual bool SetErrorMesage(string err)
        {
            _msg = err;
            _isSuccess = false;
            return false;
        }

        /// <summary>
        /// 设置状态为成功，并设置错误信息为“成功”
        /// </summary>
        /// <returns>true only</returns>
        protected virtual bool SetSuccess()
        {
            _msg = "成功";
            _isSuccess = true;
            return true;
        }

    }
}
