<%@ WebHandler Language="C#" Class="test" %>

using System;
using System.Web;
using LightDataModel;

public class test : Shotgun.PagePlus.SimpleHttpHandler<Shotgun.Database.MySqlDBClass>
{

    public override void BeginProcess()
    {
        
    }
}