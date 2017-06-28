using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace n8wan.codepool
{
    public class ResultModelManager
    {
        static Type modelType;
        public static IResultResponseModel CreateModel()
        {
            if (modelType != null)
                return (IResultResponseModel)Activator.CreateInstance(modelType);
            var appset = System.Configuration.ConfigurationManager.AppSettings["ResultModel"];
            if (string.IsNullOrEmpty(appset))
                modelType = typeof(n8wan.codepool.Model.PoolResultModel);
            else
                modelType = System.Reflection.Assembly.GetExecutingAssembly().GetType(appset);

            if (modelType == null)
                throw new System.IO.FileNotFoundException("ResultModel 未配置或配置错误");

            return (IResultResponseModel)Activator.CreateInstance(modelType);
        }
    }
}
