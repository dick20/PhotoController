package classify;

import java.io.IOException;

import org.python.core.Py;
import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class conpy {
	public static void main(String[] args) throws IOException, InterruptedException {
	 // 1. Python������ʽ���: ��Java�е���Python����
	    String pythonFunc = "./src/test/calculator.py";
	    
	    PythonInterpreter pi1 = new PythonInterpreter();
	    // ����python����
	    //pi1.execfile(pythonFunc);
	    pi1.execfile(pythonFunc);
	    // ����Python�����еĺ���
	    PyFunction pyf = pi1.get("power", PyFunction.class);
	    PyObject dddRes = pyf.__call__(Py.newInteger(2), Py.newInteger(3));
	    System.out.println(dddRes);
	    pi1.cleanup();
	    
	    //pi1.close();
	    
	    // 2. �������ʽ���: ��Java�е���Python����ʵ���ķ���
	    String pythonClass = "./src/test/calculator2.py";
	    // python������
	    String pythonObjName = "cal";
	    // python����
	    String pythonClazzName = "Calculator";
	    PythonInterpreter pi2 = new PythonInterpreter();
	    // ����python����
	    pi2.execfile(pythonClass);
	    // ʵ����python����
	    pi2.exec(pythonObjName + "=" + pythonClazzName + "()");
	    // ��ȡʵ������python����
	    PyObject pyObj = pi2.get(pythonObjName);
	    // ����python���󷽷�,���ݲ��������շ���ֵ
	    PyObject result = pyObj.invoke("power", new PyObject[] {Py.newInteger(2), Py.newInteger(3)}); 
	    double power = Py.py2double(result);
	    System.out.println(power);
	    
	    pi2.cleanup();
	    //pi2.close();
    }
}
