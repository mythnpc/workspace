using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Collections;
using System.ComponentModel;
using System.Diagnostics.CodeAnalysis;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Runtime.Serialization;
using System.Text.RegularExpressions;
using System.Text;
using System;
using ClassLibraryFramework;
using ConsLib;

class Solution
{


    static void Main(string[] args)
    {
        var temp = new Class1();
        Console.WriteLine(temp.GetVersion());
        var temp2 = new ConsThisLibrary();
        Console.WriteLine(temp2.GetDiffVersion());
        Console.Read();
    }
}
