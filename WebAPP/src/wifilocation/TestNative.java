package wifilocation;

public class TestNative {
	public native String classify(String cmd);
    
    /**
     * @param args
     */
    public static void main(String[] args) {
    	System.load("E:\\AndroidStudioProjects\\PhotoController\\WebAPP\\src\\Dll1.dll");
        TestNative tNative = new TestNative();
        String cmd = "python ./src/classify/DenseNet.py ./src/household";
        String res = tNative.classify(cmd);   
        System.out.println(res);
    }
}
