using ClassLibraryFramework;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConsLib
{
    public class ConsThisLibrary
    {
        public String GetDiffVersion()
        {
            var temp = new Class1();
            return temp.GetVersion();
        }
    }
}
