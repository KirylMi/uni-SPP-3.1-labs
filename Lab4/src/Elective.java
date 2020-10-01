import java.util.*;
//////////////////////////////
//1)Men have intel and will //
//  which are in range from //
//  one to ten              //
//2)Study = chance to get   //
//  additional intel based  //
//  on current intel and    //
//  will. f(in,wl)=in*wl    //
//  /100+teacherCoefficient //
//3)Teacher coefficient=will//
//  of the teacher / (2+size//
//  of the curated courses) //
//  * teacher intelligence  //
//  divided by 10 (both)    //
//4) Course coefficient ==  //
//  teacher coefficient (fn)//
//////////////////////////////

public class Elective {

    private static final int intelligenceRegained = 5;
    private static final int amountOfTries = 3;
    private static final int minMark = 4;

    abstract static class Human {
        Human(String SFL, int age, int intelligence, int will) {
            this.SFL = SFL;
            this.age = age;
            if (will < 1 || will > 10) try {
                throw new Exception("Wrong will parameter");
            } catch (Exception e) {
                if (will < 1) will = 1;
                if (will > 10) will = 10;
                System.out.println("Error: " + e.getMessage() + "New will assigned for " + this.SFL + " - " + will);
            }
            this.will = will;
            if (intelligence < 1 || intelligence > 10) try {
                throw new Exception("Wrong int parameter");
            } catch (Exception e) {
                if (intelligence < 1) intelligence = 1;
                if (intelligence > 10) intelligence = 10;
                System.out.println("Error: " + e.getMessage() + "New intelligence " +
                        "assigned for " + this.SFL + " - " + intelligence);
            }
            this.intelligence = intelligence;
        }

        void increaseIntel(int value) {
            this.intelligence += value;
            if (this.intelligence > 10) this.intelligence = 10;
        }

        void decreaseIntel(int value) throws Exception {
            if (this.canDecreaseIntel(value)) {
                this.intelligence -= value;
            } else {
                throw new Exception("Intelligence can't be decreased");
            }
        }

        boolean canDecreaseIntel(int value) {
            return (this.intelligence > value);
        }

        String SFL;
        int age;
        int intelligence; //1-10
        int will; //1-10
    }

    static class Student extends Human {
        Student(String SFL, int age, int intelligence, int will) {
            super(SFL, age, intelligence, will);
        }

        ArrayList<Course> enlistedCourses = new ArrayList<>();

        void enlistCourse(Course course) {
            try {
                if (this.hasCourse(course)) throw new Exception(this.SFL + " is already enlisted in" +
                        " the " + course.subject);
                if (this.canDecreaseIntel(1))
                    this.decreaseIntel(1);
                else {
                    //if student can't handle the course, and the course has already enlisted him
                    if (course.hasStudent(this)) course.studentsAndTries.remove(this);
                    throw new Exception(this.SFL + " can't handle" + course.subject);
                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return;
            }
            this.enlistedCourses.add(course);
            if (!course.hasStudent(this))
                course.enlist(this);
        }

        double getStudentCoefficient() {
            return (double) this.will * (double) this.intelligence / 100;
        }

        void study(Course course) {  //chance for a student to increase intel stat based on the will and intel
            if (!this.hasCourse(course)) try {
                throw new Exception(this.SFL + "isn't enlisted in the course " + course.subject);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            double successChance = this.getStudentCoefficient() + course.getCourseCoefficient();
            if (Math.random() < successChance) {
                this.increaseIntel(intelligenceRegained);
            }
        }

        boolean hasCourse(Course course) {
            return this.enlistedCourses.contains(course);
        }

        private void graduated(Course course, Teacher.Approval approval) {
            Objects.requireNonNull(approval);
            this.enlistedCourses.remove(course);
            this.increaseIntel(intelligenceRegained);
        }

        boolean canPassExam() {
            return (Math.random() < getStudentCoefficient());
        }
    }

    static class Teacher extends Human {
        Teacher(String SFL, int age, int intelligence, int will) {
            super(SFL, age, intelligence, will);
        }

        ArrayList<Course> curatedCourses = new ArrayList<>();

        //addutional "check given param" class, insures that only teacher can call "graduate" method
        public static final class Approval {
            private Approval() {
            }
        } //empty construct

        private static final Approval approval = new Approval();

        void openCourse(Course course) {
            if (this.hasCourse(course)) {
                course.isOpen = true;
                course.archive = new Course.Archive(course);
            } else {
                System.out.println(this.SFL + " can't open " + course.subject + " bcs he(she) isn't the teacher! ");
            }
            //course.startArchive();
        }

        void closeCourse(Course course) {
            if (this.curatedCourses.contains(course)) {
                if (course.studentsAndTries.isEmpty()) {
                    this.curatedCourses.remove(course);
                    course.archive.closeDate = new Date();
                    //System.gc();
                } else {
                    System.out.println(course.subject + "is not empty to close it!");
                    return;  //useless, but makes 'return' more obvious
                }
            } else {
                System.out.println(course.subject + " is not " + this.SFL + " course to close ");
                return; //useless, but makes 'return' more obvious
            }
        }

        void curateCourse(Course course) {
            if (this.hasCourse(course))
                return;
            else
                this.curatedCourses.add(course);
        }

        boolean hasCourse(Course course) {
            return this.curatedCourses.contains(course);
        }

        void teach(Course course) {
            for (Student student : course.studentsAndTries.keySet()) {
                student.study(course);
            }
        }

        private void banishStudent(Course course, Student student) {
            course.studentsAndTries.remove(student); //removing stud from course
            student.enlistedCourses.remove(course);
        }

        void graduateStudent(Student student, Course course) {
            student.graduated(course, approval);
        }

        void startExam(Course course) throws Exception {
            if (!this.hasCourse(course)) throw new Exception("No such course to start Exam for");
            ArrayList<Student> passedStudents = new ArrayList<Student>();
            ArrayList<Student> banishedStudents = new ArrayList<Student>();
            for (Map.Entry<Student, Integer> studAndTries : course.studentsAndTries.entrySet()) {
                //if student has tries
                if (studAndTries.getValue() > 0) {
                    //if student has passed the exam
                    if (studAndTries.getKey().canPassExam()) {
                        passedStudents.add(studAndTries.getKey());
                    } else {
                        //if he didn't -> -- the amount of tries left
                        course.studentsAndTries.put(studAndTries.getKey(), studAndTries.getValue() - 1);
                    }
                }
                //if student doesn't have tries left
                else {
                    banishedStudents.add(studAndTries.getKey());
                }
            }
            for (Student passedStud : passedStudents) {
                course.archive.giveMark(passedStud, Course.Archive.studType.passed);
                course.studentsAndTries.remove(passedStud);  //course related thing
                this.graduateStudent(passedStud, course); //student related things
            }
            for (Student banishedStud : banishedStudents) {
                course.archive.giveMark(banishedStud, Course.Archive.studType.banished);
                course.studentsAndTries.remove(banishedStud);
                this.banishStudent(course, banishedStud);
            }
        }

        void startSession(Course course) throws Exception {
            while (!course.studentsAndTries.isEmpty()) {
                startExam(course);
            }
        }

        double getTeacherCoefficient() {
            return (double) this.intelligence / 10 * (double) this.will / 10 / (2 + curatedCourses.size());
        }
    }

    static class Course {
        private Map<Student, Integer> studentsAndTries = new HashMap<Student, Integer>();
        private String subject;
        private Teacher teacher;
        private Archive archive;
        private boolean isOpen = false;

        Course(String subject, Teacher teacher) {
            this.subject = subject;
            this.teacher = teacher;
            this.teacher.curateCourse(this);
        }

        boolean hasStudent(Student stud) {
            return this.studentsAndTries.containsKey(stud);
        }

        void enlist(Student newStudent) {
            try {
                if (!this.isOpen)
                    throw new Exception(this.subject + " is not opened yet." +
                            " Awaiting for the confirmation from" + " the teacher");
                if (this.hasStudent(newStudent))
                    throw new Exception(newStudent.SFL + " is already enlisted in the course");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            studentsAndTries.put(newStudent, amountOfTries);
            if (newStudent.hasCourse(this))
                return;
            else
                newStudent.enlistCourse(this);
        }

        void enlist(ArrayList<Student> newStudents) {
            try {
                if (!this.isOpen)
                    throw new Exception(this.subject + " is not opened yet. Awaiting for the confirmation from" +
                            "the teacher");
                //null is not working?
                //for (Student enlistedStudent : studentsAndTries == null ? null : studentsAndTries.keySet()) {
                for (Student newStudent : newStudents) {
                    if (this.hasStudent(newStudent))
                        throw new Exception(newStudent.SFL + " is already enlisted in the course");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            for (Student newStudent : newStudents) {
                studentsAndTries.put(newStudent, amountOfTries);
                newStudent.enlistCourse(this);
            }
        }

        double getCourseCoefficient() {
            //more func. tbd
            return this.teacher.getTeacherCoefficient();
        }

        static class Archive {
            enum studType {
                passed,
                banished
            }

            Archive(Course course) {
                this.subject = course.subject;
                this.startDate = new Date();
                this.teacher = course.teacher;
            }

            String getArchive() {
                String retStat = this.subject + " was opened at " + this.startDate +
                        (this.closeDate != null ? (" and closed at " + this.closeDate) : " and not finished yet ") +
                        "\nTeacher: " + this.teacher.SFL +
                        "\nStudents: ";
                for (Map.Entry<Student, Mark> entry : studentsMarks.entrySet()) {
                    retStat += "\n\t" + entry.getKey().SFL + " received " + entry.getValue().mark;
                }
                return retStat;
            }

            void giveMark(Student stud, studType type) {
                if (type == studType.banished) {
                    studentsMarks.put(stud, new Mark(0));
                } else
                    studentsMarks.put(stud, new Mark(stud.getStudentCoefficient()));
            }

            String subject;
            Date startDate;
            Map<Student, Mark> studentsMarks = new HashMap<Student, Mark>();
            Date closeDate;
            Teacher teacher;

            static class Mark {
                Integer mark;

                Mark(double studentCoefficient) {
                    this.mark = (minMark - 1) + (int) (studentCoefficient * 10);
                    if (mark > 10) mark = 10;
                    if (mark < minMark) mark = minMark;
                }

                Mark(int i) {
                    this.mark = i;
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ArrayList<Student> allStudents = new ArrayList<Student>();

        allStudents.add(new Student("Mialik K.O.", 19, 7, 5));
        allStudents.add(new Student("Lenin V.I.", 50, 9, 10));
        allStudents.add(new Student("Ivanov I.I.", 25, 7, 9));
        allStudents.add(new Student("Progylshik P.F.", 25, 5, 3));
        allStudents.add(new Student("Kanterovnaua R.F.", 25, 3, 8));

        //Checking error handling
        Student strangeStudent = new Student("Surname F.L.", 19, 12, -5);

        Teacher mathematician = new Teacher("Rough I.I.", 35, 9, 10);
        Teacher physic = new Teacher("Lavor L.L.", 35, 7, 8);
        Teacher magician = new Teacher("Dotanov L.P.", 35, 8, 10);

        Course POIT = new Course("Software Engineering", mathematician);
        Course MAGIC = new Course("Flame education", magician);
        Course SCIENCE = new Course("Radio-Electronics", physic);

        mathematician.openCourse(POIT);
        magician.openCourse(MAGIC);
        physic.openCourse(SCIENCE);

        //3 different methods to enlist students
        for (Student stud : allStudents) stud.enlistCourse(POIT);
        for (Student stud : allStudents) MAGIC.enlist(stud);
        SCIENCE.enlist(allStudents);

        mathematician.teach(POIT);
        MAGIC.teacher.teach(MAGIC);
        physic.teach(SCIENCE);
        Thread.sleep(2000);

        mathematician.startSession(POIT);
        magician.startSession(MAGIC);
        physic.startSession(SCIENCE);
        Thread.sleep(2000);

        mathematician.closeCourse(POIT);
        magician.closeCourse(MAGIC);
        physic.closeCourse(SCIENCE);
        System.out.println(POIT.archive.getArchive());
        System.out.println(MAGIC.archive.getArchive());
        System.out.println(SCIENCE.archive.getArchive());
    }
}