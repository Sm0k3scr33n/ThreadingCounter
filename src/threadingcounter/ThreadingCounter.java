/*
This program represents concurrency.
a random number is selected and added to the CopyOnWriteArrayList if it is not already there.
for each iteration it takes longer and longer to check for entries as the list grows to the maximumRange
I represent concureecy and atomic variables by counting each iteration done by each thread so that
we know how many iterations were counted.


 */
package threadingcounter;

import static java.lang.Thread.sleep;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael Gabbard
 */

class NameRunnable extends ThreadingCounter implements Runnable{
    
    
   synchronized public static int randomNumber(int min, int max) 
    {
    // Usually this can be a field rather than a method variable
    Random rand = new Random();
    // nextInt is normally exclusive of the top value,
    // so add 1 to make it inclusive
    int randomNum = rand.nextInt((max - min) + 1) ;
    //System.out.println(randomNum);
    return randomNum;
    }
   
   public void run() {
         
        int maximumRange=250;
       // int count = 0;
        int retval=0;//initial value
        long executeTime = 0;
        //long successTime =0;
        long successStartTime = 0;
        AtomicInteger finalCount;
        ///maximumRange + 1 allows us to do an extra iteration to ensure that all values are on the list.
        while (retval < (maximumRange + 1)) 
        {long startTime = System.currentTimeMillis();
            try{
                Thread.sleep(10);
                    } catch (InterruptedException ex)
                    {}catch (ConcurrentModificationException ex){}
                     
            int theListValue = randomNumber(0,maximumRange);
            
            //count++;
            count.getAndIncrement();
            retval = randomArray.size(); 
           
            ////add to the list if it doesnt already exist
             ///resets the number of entries for each run of the loop
             if (randomArray.contains(theListValue))
            {   long endTime = System.currentTimeMillis();
                //successTime = successTime + (endTime - successStartTime);
                successTime.getAndIncrement();
                executeTime = executeTime + (endTime-startTime);
               // System.out.println("+-----------------------------------------------------------------+");
                //System.out.format("%-15s%-15s%-15s%-20s","|"+ Thread.currentThread().getName(), "| count: "+count+" | ", "Try #: " + theListValue,"|ON LIST!| Took"+(startTime-endTime)+"ms |\n");
                //System.out.println("Run by: "+Thread.currentThread().getName()+" Count is: " + count +  " Trying # "+theListValue+" aborting...already on list... Took"+(startTime-endTime)+"ms to execute");
          }
             else if (!randomArray.contains(theListValue))
            {
                successStartTime = System.currentTimeMillis();
                randomArray.add(theListValue);
                long endTime = System.currentTimeMillis();
                 executeTime = executeTime + (endTime-startTime);
                
                //System.out.println(Thread.currentThread().getName()+" Count is: " + count + " Trying # " +theListValue+" took " +(successTime/3)+" ms to Succeed...Adding to List... Took "+(executeTime)+"ms so far and "+(endTime-startTime)+"ms for this iteration");
               // System.out.println("+------------------------------------------------------------+");
                System.out.format("%-15s%-15s%-15s%-20s","|"+ Thread.currentThread().getName(), "| trial count: "+count+" | ", "Try #: "+theListValue+"","|ADDING!| Took "+successTime+"ms |\n");
                successTime.equals(0);
            }retval = randomArray.size();finalCount = count;
             
        }       
          System.out.println(Thread.currentThread().getName() + "finished at count: "+ count +" iterations to solve the list");
        
    }   
}

public class ThreadingCounter  {
    public static CopyOnWriteArrayList<Integer> randomArray  = new CopyOnWriteArrayList<Integer>();
    public AtomicInteger count = new AtomicInteger(0);
   
    public AtomicInteger successTime = new AtomicInteger(0);
public static void countIT(int countedIterations){

}
    /**
     * @param args the command line arguments
     */
    public static void displayList(){
     ///lets provide proof that the program generated our hashlist correctly
        Iterator <Integer> iterator = randomArray.iterator();
           int itnum=0;
           while(iterator.hasNext())
           {
           System.out.print(itnum + ". " + iterator.next()+" ");
           
           itnum++;
           };System.out.println();
    }
    public static void main(String[] args) {
        // TODO code application logic here
        long startProgramTime = System.currentTimeMillis();
         NameRunnable funwiththreads = new NameRunnable();
        Thread one = new Thread(funwiththreads);
        Thread two = new Thread(funwiththreads);
        Thread three = new Thread(funwiththreads);
        one.setName("Thread_1");
        two.setName("Thread_2");
        three.setName("Thread_3");
        one.start();
        two.start();
        three.start();
       
    try {
        one.join();two.join();three.join();
    } catch (InterruptedException ex) {
        Logger.getLogger(ThreadingCounter.class.getName()).log(Level.SEVERE, null, ex);
    }   
    try {
        while(one.isAlive()||two.isAlive()||three.isAlive()){sleep(200);}
       
    } catch (InterruptedException ex) {
        Logger.getLogger(ThreadingCounter.class.getName()).log(Level.SEVERE, null, ex);
    }
    
   long endProgramTime = System.currentTimeMillis();
    displayList(); 
   System.out.println("Program Executed in "+((endProgramTime - startProgramTime)/1000.00)+" Seconds");
   
    }
    
}
