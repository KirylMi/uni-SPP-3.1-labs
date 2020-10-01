public class MainClass{
    public static void main(String[] args) throws Exception{
        if (args[0].length()<1) throw new Exception("Nice try (null)"); //checking for the null
        System.out.println(ShiftRight(args[0],Integer.parseInt(args[1])));
    }
    public static String ShiftRight(String str, int shift){
        String resStr = "";
        shift%=str.length();  //if shift is greater than the length
        for (int i=(str.length()-shift)%str.length();i<str.length();i++)
            resStr+=str.charAt(i);
        for (int i=0;i<(str.length()-shift)%str.length();i++)
            resStr+=str.charAt(i);
        return resStr;
    }
}