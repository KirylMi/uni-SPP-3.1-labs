public  class eqTriangle {
    private final int[] sides = new int[3];
    {
        sides[0]=sides[1]=sides[2]=0;
    }
    eqTriangle(int side1,int side2,int side3){
        //bad practice. Should be throw exception if side1!=side2 || side2!=side3 and even better
        //look like eqTriangle(int side)
        //bcs S and P won't work correctly
        sides[0]=side1;
        sides[1]=side2;
        sides[2]=side3;
    }
    eqTriangle(eqTriangle another){
        sides[0]=another.sides[0];
        sides[1]=another.sides[1];
        sides[2]=another.sides[2];
    }
    double  S()                 { return Math.pow(sides[0],2)*Math.sqrt(3)/4; }
    double  P()                 { return sides[0]*3; }
    void    setSides(int side)  { sides[0]=sides[1]=sides[2]=side; }
    void    setSide1(int side)  { sides[0] = side;}
    void    setSide2(int side)  { sides[1] = side;}
    void    setSide3(int side)  { sides[2] = side;}
    int     getSide1()          { return this.sides[0];}
    int     getSide2()          { return this.sides[1];}
    int     getSide3()          { return this.sides[2];}
    boolean exist()             {return (sides[0]==sides[1]&&sides[1]==sides[2]);}
    @Override
    public String toString(){
        return "{"+getSide1()+","+getSide2()+","+getSide3()+"}";
    }
    @Override
    public boolean equals(Object another){
        if (getClass()!=another.getClass()) return false; //check if another is the same type
        return (this.sides[0] == ((eqTriangle)another).sides[0] &&
                this.sides[1] == ((eqTriangle)another).sides[1] &&
                this.sides[2] == ((eqTriangle)another).sides[2]
        );
    }
}