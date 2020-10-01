import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;
import java.lang.reflect.Method;
public class MainCLass{

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        String[] words = str.split(" ");
        //Part below is just for testing method invoke feature in java.
        //Would be useful only if futher functional will be added
        String [] arguments = subarr(words,1,words.length);
        Method m = MainCLass.class.getDeclaredMethod(words[0],String[].class);
        m.invoke(null, (Object) arguments);
    }
    public static String[] subarr(String[] arr, int startIndex, int endIndex){
        String[] retArr = new String[endIndex-startIndex];
        for (int i=0,j=startIndex;i<endIndex-startIndex;i++,j++){
            retArr[i]=arr[j];
        }
        return retArr;
    }
    public static boolean checkKeys(String[] keys, String givenKey){
        for (String patternKey : keys){
            if (givenKey.equals(patternKey)) return true;
        }
        return false;
    }
    public static boolean matchesPattern(String[][] patterns, String[] keys, String[] args){  //lots of crutches
        int i;
        boolean correctLastElement = true;
        boolean correctKey = true;
        try{
            for (String[] pattern : patterns){
                i=0;
                if (pattern.length != args.length) continue;
                for (String patternArg : pattern){
                    if (!args[i++].matches(patternArg)){
                        correctLastElement=false;
                        break;
                    }
                    else{
                        if (args[i-1].matches("-(.*)")) correctKey = checkKeys(keys,args[i-1]);
                        correctLastElement=true;
                    }
                    if (!correctKey) throw new Exception ("Wrong key");
                }
                if (correctLastElement) return true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }
    public static void head(String[] args){
        try{
            String[] keys = {"-n"};
            String[][] patterns = {
                    {"-(.*)", "[0-9]+", "(.*).txt"},
                    {"[0-9]+","(.*).txt"},
                    {"(.*).txt"}};
            if (!matchesPattern(patterns,keys,args)) throw new Exception ("Wrong arguments");
            int numberOfLines=10;
            if (args[0].matches("[0-9]+")) numberOfLines=Integer.parseInt(args[0]);
            if (args.length > 1 && args[1].matches("[0-9]+")) numberOfLines = Integer.parseInt(args[1]);
            String fileName="";
            for (int i=0;i<3;i++){
                if (args[i].matches("(.*).txt")){
                    fileName=args[i]; break;
                }
            }
            java.io.RandomAccessFile file = new java.io.RandomAccessFile(fileName,"r");
            String output;
            for (int i=0;i<numberOfLines;i++){
                output=file.readLine();
                if (output!=null) System.out.println(output);
                else break;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }
}