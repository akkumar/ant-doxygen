//----------------------------------------------------------------------
// FILE: classThree.java		- ant_doxygen test case
//----------------------------------------------------------------------
// $Id: $

import pk1.classOne;
import pk2.classTwo;

//----------------------------------------------------------------------
/** This class is part of the ant-doxygen test suite and is used to
 *  demonstrate the invocation of Doxygen from within Ant.
 *
 *  @author <a href="mailto:akkumar AT users.sourceforge.net">Karthik Kumar</a>
 *  @version $Id: $ 
 */
public class classThree {

    /** classThree "has-a" classOne instance. */
    pk1.classOne obj1 = new pk1.classOne();

    /** classThree "has-a" classTwo instance. */
    pk2.classTwo obj2 = new pk2.classTwo(); 




    //----------------------------------------------------------------------
    /** This method provides the method4() behavior.  Namely, invoking
     *  the classTwo.method1() and then the classOne.doSomething1()
     *  methods.
     *
     *  @pre No preconditions are required for this method.
     *
     *  @post No postconditions are enforced for this method.
     *
     */
    public void method4() {
         obj2.method1();
         obj1.doSomething1();
    }
}
