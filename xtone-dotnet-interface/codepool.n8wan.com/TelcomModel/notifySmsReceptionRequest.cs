using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace n8wan.codepool.TelcomModel
{
    public class notifySmsReceptionRequest
    {
        public Body body { get; set; }

        public MoHeader header { get; set; }
    }
}
