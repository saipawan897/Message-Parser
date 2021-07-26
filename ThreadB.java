import java.io.*;

class ThreadB extends Thread
{
    PipedOutputStream toA;
    PipedOutputStream toC;

    ObjectOutputStream writerA;

    ThreadB() throws IOException {
        toA = new PipedOutputStream();
        toC = new PipedOutputStream();
    }

    ThreadB(PipedOutputStream outA, PipedOutputStream outC,
       ObjectOutputStream write) {
        toA = outA;
        toC = outC;
        writerA = write;
    }

    public void run() {
        System.out.println("B- Started");
        try {
            //OUTGOING
            //TB will send objects to TA
            Message m = new Message();
            m.theMessage = "Hi A!";
            String[] arr = {"It's", "B!"};
            m.someLines = arr;
            m.someNumber = 22;

            System.out.println("B- Sending object to A { " + '\n' + m.toString());
            writerA = new ObjectOutputStream(toA);//conversion
            writerA.writeObject(m);
            writerA.flush();
            writerA.close();

            //TB will send primitive data to TC
            System.out.println("B- Sending primitive to C: " + 2);
            toC.write(2);
            toC.flush();
            toC.close();


            //INCOMING
            ////
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
}