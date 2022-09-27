package util.com.vilcart.util;

public class CurrentMethod {
	public static String methodName() {
		// getStackTrace() method return
		// current method name at 0th index
		String nameofCurrMethod = new Throwable().getStackTrace()[1].getMethodName();

//	        System.out.println("Name of current method: "
//	            + nameofCurrMethod);
		return nameofCurrMethod;
	}
}
