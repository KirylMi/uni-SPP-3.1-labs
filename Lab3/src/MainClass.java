import java.io.FileNotFoundException;

public class MainClass{
    public static void task1(){
        eqTriangle first = new eqTriangle(45,45,45);
        System.out.println("First Triangle: " + first.toString());
        System.out.println("First is" + (first.exist() ? " " : " not ") + "equilateral");

        eqTriangle second = new eqTriangle(45,35,55);
        System.out.println("Second Triangle: " + second.toString());
        System.out.println("Second is" + (second.exist()?" ":" not ") + "equilateral");

        eqTriangle third = new eqTriangle(first);
        System.out.println("Third triangle " + third.toString());
        System.out.println("Third is" + (third.exist() ? " " : " not ") + "equilateral");

        System.out.println("First and second are : " + ((first.equals(second))?"equal":"not equal"));
        System.out.println("Changing the third one...");
        third.setSide2(45);
        third.setSide3(45);
        System.out.println("New sides of the third: " + third.toString());
        System.out.println("First and third are : "  + ((first.equals(third))?"equal" : "not equal"));
        System.out.println("Specs of the third one are: \nS:" + third.S() + "\nP:"+third.P());
    }
    public static void task2() throws FileNotFoundException {
        CarSharing CSService = new CarSharing("text.txt");
        System.out.println("1. All cars:");
        for(CarSharing.Car obj : CSService.getAll())
            System.out.println(obj);
        System.out.println("2. BMW cars:");
        for (Object obj : CSService.getByBrand("BMW"))
            System.out.println(obj);
        System.out.println("3. Ulisses, older than 10 years:");
        for (var obj : CSService.getByModelAndYear("Ulisse",10))
            System.out.println(obj);
        System.out.println("4. 2005 and more expensive than 10000$:");
        for (var obj : CSService.getByYearAndPrice(2005,10000))
            System.out.println(obj);
        System.out.println("5. Rented cars:");
        for (var obj : CSService.getRented())
            System.out.println(obj);
        System.out.println("6. Rented with personal data:");
        for (CarSharing.Car obj : CSService.getRented())
            System.out.println(obj.SFL + '\t' + obj.passportNum + '\t' + obj);

    }
    public static void main(String[] args) throws FileNotFoundException{
        //task1();
        task2();
    }
}