<%@ WebHandler Language="C#" Class="test" %>

using System;
using System.Web;
using System.Collections.Generic;

public class test : Shotgun.PagePlus.SimpleHttpHandler<Shotgun.Database.MySqlDBClass>
{


    public override void BeginProcess()
    {
        Response.Write(AppDomain.CurrentDomain.BaseDirectory);

    }
}