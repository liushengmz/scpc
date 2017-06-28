<%@ WebHandler Language="C#" Class="rePost" %>

using System;
using System.Web;
/// <summary>
/// 数控补发
/// </summary>
public class rePost : Shotgun.PagePlus.SimpleHttpHandler<Shotgun.Database.MySqlDBClass>
{
    public override void BeginProcess()
    {
        string ids = Request["ids"];
        
    }
}