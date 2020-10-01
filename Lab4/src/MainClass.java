import java.util.ArrayList;
import java.util.Date;

public class MainClass {
    public abstract static class Operation {
        Operation(int senderID, int handlerID, double funds, Date time) {
            this.senderID = senderID;
            this.handlerID = handlerID;
            this.funds = funds;
            this.time = time;
        }

        Date time;
        double funds;
        int senderID;
        int handlerID;

        abstract String getOperation();
    }

    public static class IncomeOperation extends Operation {
        IncomeOperation(int senderID, int handlerID, double funds, Date time) {
            super(senderID, handlerID, funds, time);
        }

        String getOperation() {
            return time.toString() + " Income operation: " + funds + " were transferred from " + senderID;
        }
    }

    public static class WithdrawOperation extends Operation {
        WithdrawOperation(int senderID, int handlerID, double funds, Date time) {
            super(senderID, handlerID, funds, time);
        }

        String getOperation() {
            return time.toString() + " Withdraw operation: " + funds + " were withdrawn from ATM machine #" + senderID + " by " + handlerID;
        }
    }

    public static class PaymentOperation extends Operation {
        PaymentOperation(int senderID, int handlerID, double funds, Date time) {
            super(senderID, handlerID, funds, time);
        }

        String getOperation() {
            return time.toString() + " Payment operation: " + funds + " were withdrawn from " + senderID + "(you) in favour of " + handlerID;
        }
    }

    public static class Account {
        Account(ArrayList<Account> accounts, String SFL) {
            this.accountID = accounts.size();
            this.SFL = SFL;
            this.balance = 0;
            this.operations = new ArrayList<Operation>();
            accounts.add(this);
        }

        //if account has balance
        Account(ArrayList<Account> accounts, String SFL, int balance) {
            this(accounts, SFL);
            this.balance = balance;
        }

        //??Difference with C++, i can call private method, but it's not inherited
        private void transferFunds(int targetID, ArrayList<Account> accounts, int funds) throws Exception {
            if (this.balance < funds) throw new Exception("Not enough funds :(");
            if (this.accountID == targetID)
                throw new Exception("No sense, don't transfer money to your own account from your own account");
            for (Account obj : accounts) {
                if (obj.accountID == targetID) {
                    if (obj instanceof ATM) throw new Exception("You can't transfer funds to the ATM machine");
                    this.balance -= funds;
                    obj.balance += funds;
                    this.operations.add(new PaymentOperation(this.accountID, targetID, funds, new Date()));
                    obj.operations.add(new IncomeOperation(this.accountID, targetID, funds, new Date()));
                    return;
                }
            }
            throw new Exception("No such target ID");
        }

        void withdrawFunds(int targetID, ArrayList<Account> accounts, int funds) throws Exception {
            if (this.balance < funds) throw new Exception("Not enough funds :(");
            for (Account obj : accounts) {
                if (obj.accountID == targetID) {
                    if (!(obj instanceof ATM)) throw new Exception("You can't withdraw money from non ATM account");
                    ((ATM) obj).withdrawMoney(this.accountID, funds);
                    this.balance -= funds;
                    this.operations.add(new WithdrawOperation(targetID, this.accountID, funds, new Date()));
                }
            }
        }

        int balance;
        String SFL;
        ArrayList<Operation> operations;
        int accountID;

        @Override
        public String toString() {
            return "User: " + this.SFL + "\n" + "ID: " + this.accountID + " Current balance: " + this.balance;
        }
    }

    public static class ATM extends Account {
        double physicalFunds;

        ATM(ArrayList<Account> accounts, String SFL, int physicalFunds) {
            super(accounts, SFL);
            this.physicalFunds = physicalFunds;
        }

        void withdrawMoney(int targetID, int sum) throws Exception {
            if (this.physicalFunds < physicalFunds) throw new Exception("ATM machine doesn't have enough currency");
            this.physicalFunds -= sum;
            this.operations.add(new WithdrawOperation(this.accountID, targetID, sum, new Date()));
        }

        void receiveMoney(int targetID, int funds) {
            this.operations.add(new IncomeOperation(targetID, this.accountID, funds, new Date()));
        }

        @Override
        public String toString() {
            return this.SFL + "\n" + "ID: " + this.accountID + " Current physical cash: " + this.physicalFunds;
        }
    }


    public static void printAllData(ArrayList<Account> accounts) {
        int i = 1;
        for (Account acc : accounts) {
            System.out.println(acc);  //testing toString method
            System.out.println("Operations: ");
            if (acc.operations.size() == 0) {
                System.out.println("-------NO OPERATIONS YET-------");
            } else
                for (Operation operation : acc.operations) {
                    System.out.println("\t" + i++ + ")" + operation.getOperation());  //testing own method
                }
            System.out.println();
            i = 1;
        }
    }

    static void task1() throws Exception {
        ArrayList<Account> accounts = new ArrayList<Account>();
        Account myAccount = new Account(accounts, "Mialik K.O.", 15);
        Account myFriendsAccount = new Account(accounts, "Py4kov D.D.");
        ATM bankAccount = new ATM(accounts, "ATM #333", 10000);

        myAccount.transferFunds(1, accounts, 15);
        Thread.sleep(2000);
        myFriendsAccount.transferFunds(0, accounts, 15);
        Thread.sleep(2000);
        myAccount.withdrawFunds(2, accounts, 15);
        Thread.sleep(2000);
        printAllData(accounts);
    }

    //////////////////////////////////ZAD2/////////////////////////////////////////
    public static class PC {
        Motherboard motherboard;
        Drive drive;
        RAM ram;
        String codeName;

        PC(String codeName, Motherboard mb, Drive dr, RAM ram) throws Exception {
            this.motherboard = mb;
            this.codeName=codeName;
            if (
                    ((mb.socketType == Motherboard.socket.LGA1150 || mb.socketType == Motherboard.socket.AM3) &&
                            ram.rangType == RAM.rang.DDR4) ||
                            ((mb.socketType == Motherboard.socket.LGA1151 || mb.socketType == Motherboard.socket.AM4) &&
                                    ram.rangType == RAM.rang.DDR3)
            ) throw new Exception("Wrong MB<->RAM setup");
            this.drive = dr;
            this.ram = ram;
        }

        String readDisk(String diskName, PC.Drive.type diskType) throws Exception {
            //imaginary checking if the disk exists <--
            //??Where should i put diskType checking? PC or Drive itself?
            if (diskType.ordinal() > this.drive.driveType.ordinal())
                throw new Exception("Your drive can't read that disk");
            return this.drive.readDisk(diskName);
        }
        void makeFunnySound(){
            System.out.print("Fabricating false error to make some noise..... ");
            this.motherboard.soundWarning();
        }
        boolean isRAMStable(){
            System.out.println("Testing RAM.......");
            return this.ram.checkIntegrity();
                //System.out.println("RAM is stable!!! Yeeeah");
            //else
                //System.out.println("Ram is not stable :(");
        }

        @Override
        public String toString() {
            return "PC ---" + codeName + "--- " + '{' + '\n' + '\t' +motherboard + '\n' + '\t' + drive + '\n' + '\t' +ram + '}';
        }

        public static class Components {
            String manufacturer;
            String model;

            Components(String manufacturer, String model) {
                this.manufacturer = manufacturer;
                this.model = model;
            }

            @Override
            public String toString() {
                return "manufacturer='" + manufacturer + '\'' +
                        ", model='" + model + "\' ";
            }
        }

        public static class Motherboard extends Components {
            enum socket {
                LGA1150,
                LGA1151,
                AM3,
                AM4,
            }
            void soundWarning(){
                System.out.println("BEEEEEEEP-BEEEEEEEP");
            }
            socket socketType;

            Motherboard(String manufacturer, String model, socket socket) {
                super(manufacturer, model);
                this.socketType = socket;
            }

            @Override
            public String toString() {
                return "Motherboard {" +
                        " socketType=" + socketType +
                        " } " + super.toString();
            }
        }

        public static class Drive extends Components {
            type driveType;

            enum type {
                CD,
                DVD,
                BR
            }

            Drive(String manufacturer, String model, type type) {
                super(manufacturer, model);
                this.driveType = type;
            }

            String readDisk(String diskName) {
                //low-lvl PL reading
                return "InfoFromDisk";
            }

            @Override
            public String toString() {
                return "Drive {" +
                        " driveType=" + driveType +
                        " } " + super.toString();
            }
        }

        public static class RAM extends Components {
            rang rangType;

            enum rang {
                DDR3,
                DDR4
            }

            RAM(String manufacturer, String model, rang rang) {
                super(manufacturer, model);
                this.rangType = rang;
            }

            boolean checkIntegrity() {
                if ((int) (Math.random() * 10) % 9 == 0)
                    return false;
                else
                    return true;
            }

            @Override
            public String toString() {
                return "RAM {" +
                        " rang=" + rangType +
                        " } " + super.toString();
            }

        }
    }
    static void task2() throws Exception {
        PC.Drive myDrive = new PC.Drive("WD", "Black558", PC.Drive.type.DVD);
        PC.Motherboard myMB = new PC.Motherboard("ASUS", "Z97-P", PC.Motherboard.socket.LGA1150);
        PC.RAM myRAM = new PC.RAM("KINGSTON", "Hyperass", PC.RAM.rang.DDR3);
        PC myPC = new PC("Ultra-mega-destroyer",myMB, myDrive, myRAM);
        System.out.println(myPC.toString());
        System.out.println("Testing methods: ");
        if (myPC.isRAMStable()) System.out.println("Ram is stable!");
        myPC.makeFunnySound();
        System.out.println("Result of disk reading : " + myPC.readDisk("diskName.disk",PC.Drive.type.CD));
    }

    public static void main(String[] args) throws Exception {
        //task1();
        //task2();
    }
}