import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Calendar;
import java.util.Scanner;
public class CarSharing {
    ArrayList<Car> cars;
    CarSharing(String textFileName) throws FileNotFoundException {
        cars = new ArrayList<Car>();
        FileReader textFile = new FileReader(textFileName);
        Scanner sc = new Scanner(textFile);
        String[] currentInfo;
        while (sc.hasNextLine()){
            currentInfo=sc.nextLine().split(";");
            //???Fix this
            if (currentInfo.length==9){
                cars.add(new Car(cars.size(),currentInfo[0],currentInfo[1],Integer.parseInt(currentInfo[2]),currentInfo[3],
                        Integer.parseInt(currentInfo[4]),Integer.parseInt(currentInfo[5]),currentInfo[6],currentInfo[7],currentInfo[8]));
            } else
                if(currentInfo.length==8){
                    cars.add(new Car(cars.size(),currentInfo[0],currentInfo[1],Integer.parseInt(currentInfo[2]),currentInfo[3],
                            Integer.parseInt(currentInfo[4]),Integer.parseInt(currentInfo[5]),currentInfo[6],currentInfo[7]));
                }
                    else
                    if(currentInfo.length==7){
                        cars.add(new Car(cars.size(),currentInfo[0],currentInfo[1],Integer.parseInt(currentInfo[2]),currentInfo[3],
                                Integer.parseInt(currentInfo[4]),Integer.parseInt(currentInfo[5]),currentInfo[6]));
                    }
            //fix this ^

        }
    }
    ArrayList<Car> getAll(){
        ArrayList<Car> suitableCars = new ArrayList<Car>();
        for (Car obj : cars){
            suitableCars.add(obj);
        }
        return suitableCars;
    }
    ArrayList<Car>  getByBrand(String brand){
        ArrayList<Car> suitableCars = new ArrayList<Car>();
        for (Car obj : cars)
            if (obj.brand.equals(brand))
                suitableCars.add(obj);
        return suitableCars;
    }
    ArrayList<Car>  getByModelAndYear(String model, int operationalMinYear){
        ArrayList<Car> suitableCars = new ArrayList<Car>();
        for (Car obj: cars)
            if (obj.model.equals(model) && (Calendar.getInstance().get(Calendar.YEAR) - obj.year > operationalMinYear))
                suitableCars.add(obj);
        return suitableCars;
    }
    ArrayList<Car> getByYearAndPrice(int targetedYear, int targetedMinPrice){
        ArrayList<Car> suitableCars = new ArrayList<Car>();
        for (Car obj:cars)
            if(obj.year == targetedYear && (obj.price > targetedMinPrice))
                suitableCars.add(obj);
        return suitableCars;
    }
    ArrayList<Car> getRented(){
        ArrayList<Car> suitableCars = new ArrayList<Car>();
        for (Car obj: cars)
            if (obj.SFL!=null && obj.passportNum!=null)
                suitableCars.add(obj);
        return suitableCars;
    }
    public class Car{
        int id,year,price,regNumber;
        String brand,model,color,carNumber,SFL,passportNum;
        Car(int id, String brand, String model, int year, String color, int price, int regNumber, String carNumber, String SFL, String passportNum){
            this.id=id; this.brand=brand; this.model=model; this.year=year; this.color=color; this.price=price; this.regNumber=regNumber; this.carNumber=carNumber; this.SFL=SFL; this.passportNum=passportNum;
        }
        Car(int id, String brand, String model, int year, String color, int price, int regNumber, String carNumber, String SFL){
            this(id,brand,model,year,color,price,regNumber,carNumber,SFL,null);
        }
        Car(int id, String brand, String model, int year, String color, int price, int regNumber, String carNumber){
            this(id,brand,model,year,color,price,regNumber,carNumber,null,null);
        }
        @Override
        public String toString() {
            return "Car{" +
                    "id=" + id +
                    ", year=" + year +
                    ", price=" + price +
                    ", regNumber=" + regNumber +
                    ", brand='" + brand + '\'' +
                    ", model='" + model + '\'' +
                    ", color='" + color + '\'' +
                    ", carNumber='" + carNumber + '\'' +
                    //", SFL='" + (SFL!=null?SFL:"Undefined") + '\'' +
                    //", passportNum='" +  (passportNum!=null?passportNum:"Undefined") + '\'' +
                    '}';
        }
    }
}
