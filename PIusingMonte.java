/**
 * Estimating the value of Pi using Monte Carlo method in parallel fashion with the help of MPJ Express library.
 * Algorithm Url: https://www.geeksforgeeks.org/estimating-value-pi-using-monte-carlo/
 * @author Atiq ur Rehman
 */
package mpjexpressdemo;

import java.util.Random;
import mpi.*;


public class PIusingMonte {
    
    public static void main(String[] args) {
        final double INTERVALS=100000l;
        
        int rank, size;
        
        int circlePoint[]=new int[1];
        int totalCirclePoint[]=new int[1];
        
        double pi=0.0,  x, y;
        
        MPI.Init(args);
        rank=MPI.COMM_WORLD.Rank();
        size=MPI.COMM_WORLD.Size();
        MPI.COMM_WORLD.Barrier();
        
        Random random=new Random();
        //Each slave process will calculate the part of the sum.
        
        for (int i=rank; i<INTERVALS; i+=size)
        {
            x=random.nextDouble(); //generate random b/w 0.0 to 1.0 for x coordinate
            y=random.nextDouble();
            //System.out.println("X_Coord= "+ x +" Y_Coord= "+y);
            if(x*x+y*y<1.0)
                circlePoint[0]++;
        }
        
        //Sum up all results
        MPI.COMM_WORLD.Reduce(circlePoint,0, totalCirclePoint, 0,1, MPI.INT, MPI.SUM, 0);
        
        
        
        //Master rank will calcualte the PI and print it.
        if (rank==0)
        {
            
            /** by using formula;
             *
             * Pi = 4 * (number of points generated inside the circle / number of points generated inside the square)
             */
            pi=4 * (totalCirclePoint[0] / INTERVALS); // INTERVALS = number of points generated inside the square
            
            System.out.println("np= "+size+"\t PI= "+pi);
        }
        
        MPI.Finalize();
    }
    
}
