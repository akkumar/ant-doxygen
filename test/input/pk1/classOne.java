//----------------------------------------------------------------------
// FILE: classOne.java			- ant_doxygen test case
//----------------------------------------------------------------------
// $Id: $

package pk1;

import pk2.classTwo;

//----------------------------------------------------------------------
/** This class is part of the ant-doxygen test suite and is used to
 *  demonstrate the invocation of Doxygen from within Ant.
 *
 *  @author <a href="mailto:akkumar AT users.sourceforge.net">Karthik Kumar</a>
 *  @version $Id: $ 
 */
public class classOne {
    /** classOne "has-a" count. */
    public int count;





    //----------------------------------------------------------------------
    /** This method does something uninteresting with a classTwo
     * instance.
     *
     */
    public void doSomething1() {
        classTwo obj2 = new classTwo();
        obj2.method1();
        count = obj2.level;
    }


    //----------------------------------------------------------------------
    /** This is a Java application entry point for this class.
     *
     *  @param args passed to this application on the command line.
     */
    public static void main( String args[] ) {
        classOne obj1 = new classOne();
    }
}
