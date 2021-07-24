import java.io.*;

class dimuMessage implements Serializable
{
    public int number, id;

    public dimuMessage(int number, int id)
    {
        this.number = number; this.id = id;
    }

    @Override
    public String toString() {
        return "Object Message {" +
                "number=" + number +
                ", id=" + id +
                '}';
    }
}
public class Main
{
    static ObjectOutputStream objectOutputAB = null;
    static ObjectInputStream objectInputAB = null;

    static ObjectOutputStream objectOutputBC = null;
    static ObjectInputStream objectInputBC = null;

    public static void main(String[] args) throws Exception
    {
        PipedOutputStream posAB = new PipedOutputStream();      // A sends objects to B
        PipedInputStream pisBA = new PipedInputStream(posAB);

        PipedOutputStream posBA = new PipedOutputStream();      // B sends primitives to A
        PipedInputStream pisAB = new PipedInputStream(posBA);

        PipedOutputStream posBC = new PipedOutputStream();      // B sends objects to C
        PipedInputStream pisCB = new PipedInputStream(posBC);

        PipedOutputStream posCA = new PipedOutputStream();      // C sends primitives to A
        PipedInputStream pisAC = new PipedInputStream(posCA);


        Thread threadA = new Thread()
        {
            public void run()
            {
                try {

                    //A sends OBJECTS to B
                    dimuMessage m = new dimuMessage(777, 1);

                    System.out.println("A Sending object to B.... " + "\n" + m.toString());
                    objectOutputAB = new ObjectOutputStream(posAB);
                    objectOutputAB.writeObject(m);
                    objectOutputAB.flush();
                    objectOutputAB.close();

                    System.out.println("A Receiving primitive from B: " + pisBA.read());
                    pisBA.close();

                    System.out.println("A Receiving primitive from C: " + pisAC.read());
                    pisAC.close();

                }

                catch (Exception e) {

                }

            }
        };

        Thread threadB = new Thread()
        {
            public void run()
            {
                try {
                    //B sends primitive to A
                    System.out.println("B Sending primitive to A: " + 2);
                    posBA.write(2);
                    posBA.flush();
                    posBA.close();


                    //B sends OBJECTS to C
                    dimuMessage m = new dimuMessage(666, 2);

                    System.out.println("B Sending object to C... " + "\n" + m.toString());
                    objectOutputBC = new ObjectOutputStream(posBC);
                    objectOutputBC.writeObject(m);
                    objectOutputBC.flush();
                    objectOutputBC.close();

                    //B receives objects from A
                    objectInputBC = new ObjectInputStream(pisAB);
                    System.out.println("B Receiving object from A " + '\n' + objectInputBC.readObject().toString());
                    objectInputBC.close();
                }

                catch (Exception e) {

                }

            }
        };

        Thread threadC = new Thread()
        {
            public void run()
            {
                try
                {
                    //C sends primitive to A
                    System.out.println("C Sending primitive to A: " + 3);
                    posCA.write(3);
                    posCA.flush();
                    posCA.close();

                    //C receives object from B
                    objectInputAB = new ObjectInputStream(pisCB);
                    System.out.println("C Receiving object from B: " + objectInputAB.readObject().toString());
                    objectInputAB.close();
                }

                catch (Exception e) {

                }

            }
        };

        threadA.start();
        threadB.start();
        threadC.start();

    }

}