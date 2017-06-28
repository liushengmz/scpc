using System;
using System.Collections.Generic;
using Shotgun.Model.Data;
using Shotgun.Model.List;
using Shotgun.Model.Filter;
using Shotgun.Model.Logical;
using Shotgun.Database;
using Shotgun.Library;
using System.Drawing;
using System.IO;
using System.Drawing.Imaging;

namespace Shotgun.Library
{
    public static class LogoMark
    {

        public static bool PrintLogo(Image OrgImg, Stream outStm, string LogoFile)
        {
            if (!string.IsNullOrEmpty(LogoFile) && OrgImg.Width > 299 && OrgImg.Height > 149)
            {
                System.Drawing.Image Logo = null;
                try
                {
                    Logo = System.Drawing.Image.FromFile(LogoFile);
                }
                catch (Exception ex)
                {
                    OrgImg.Dispose();
                    throw ex;
                }

                Graphics g = null;
                switch (OrgImg.PixelFormat)
                {
                    case PixelFormat.Format1bppIndexed:
                    case PixelFormat.Format4bppIndexed:
                    case PixelFormat.Format8bppIndexed:
                    case PixelFormat.Format32bppArgb:
                    case PixelFormat.Indexed:
                        {
                            Image t = OrgImg;

                            OrgImg = new Bitmap(OrgImg.Size.Width, OrgImg.Size.Height, PixelFormat.Format24bppRgb);
                            g = Graphics.FromImage(OrgImg);
                            g.Clear(Color.White);
                            g.DrawImageUnscaled(t, 0, 0);
                            g.DrawImage(t, new Rectangle(0, 0, t.Width, t.Height));
                            t.Dispose();
                            break;
                        }
                    default:
                        {
                            try { g = Graphics.FromImage(OrgImg); }
                            catch (Exception ex)
                            {
                                OrgImg.Dispose();
                                if (Logo != null)
                                    Logo.Dispose();
                                throw ex;
                            }
                            break;
                        }
                }

                g.InterpolationMode = System.Drawing.Drawing2D.InterpolationMode.High;
                g.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.Default;
                int x, y;
                x = OrgImg.Width - Logo.Width;
                y = OrgImg.Height - Logo.Height;
                g.DrawImageUnscaledAndClipped(Logo, new Rectangle(new Point(x, y), Logo.Size));
                g.Dispose();
                Logo.Dispose();
            }
            ImageCodecInfo[] ics = ImageCodecInfo.GetImageEncoders();

            ImageCodecInfo IC = null;
            foreach (ImageCodecInfo ici in ics)
            {
                if (ici.MimeType == @"image/jpeg")
                {
                    IC = ici;
                    break;
                }
            }

            if (IC != null)
            {
                EncoderParameters ep = new EncoderParameters(3);
                ep.Param[0] = new EncoderParameter(Encoder.Quality, 80L);
                ep.Param[1] = new EncoderParameter(Encoder.ScanMethod, (int)EncoderValue.ScanMethodInterlaced);
                ep.Param[2] = new EncoderParameter(Encoder.RenderMethod, (int)EncoderValue.RenderProgressive);
                OrgImg.Save(outStm, IC, ep);

            }
            else
                OrgImg.Save(outStm, ImageFormat.Jpeg);
            return true;

        }

        /// <summary>
        /// 为宽在大于200,高大于100的图片添加水印
        /// </summary>
        /// <param name="imgStm">指向原图的流</param>
        /// <param name="outStm">添加了水印的图片输出流(参数为null时将输出到原图流上)</param>
        /// <param name="LogoFile">用于叠加的水印图片(完整路径)</param>
        /// <returns>输出只能是Jpeg格式,质量:80%</returns>
        public static bool PrintLogo(Stream imgStm, Stream outStm, string LogoFile)
        {
            Image OrgImg = Image.FromStream(imgStm);
            if (outStm == null)
            {
                outStm = imgStm;
                outStm.Position = 0;
            }
            bool ret = PrintLogo(OrgImg, outStm, LogoFile);
            OrgImg.Dispose();
            return ret;
        }


    }
}
