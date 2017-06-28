using System;
namespace Shotgun.Database
{
    public interface IBaseDataClass
    {
        void BeginTransaction();
        void Commit();
        int ExecuteNonQuery(string sql);
        object ExecuteScalar(string sql);
        System.Data.DataTable GetDataTable(string sql);
        bool IsTransaction { get; }
        void Rollback();
        string SqlEncode(System.Web.UI.ITextControl txtContrl);
        string SqlEncode(string vData);
        string SqlEncode(string vData, bool AddQut);
    }
}
