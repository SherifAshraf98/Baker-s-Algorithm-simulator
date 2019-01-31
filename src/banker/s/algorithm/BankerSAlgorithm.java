/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banker.s.algorithm;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Sherif Ashraf
 */
class BankerAlgorithm {

    int numproc, bits, reqproc;
    int availarr[];
    int allocationarr[][];
    int needarr[][];
    int reqarr[];
    int newavailarr[];
    int newneedarr[][];
    int newallocationarr[][];
    boolean reslist[];

    boolean safeavail;
    boolean safeneed;
    boolean finish;
    boolean loop;
    boolean reqcompleted;
    boolean last;
    boolean dorelease;
    
    int releasearr[];
    int releaseproc;

    Random rand = new Random();
    Scanner sc = new Scanner(System.in);

    public BankerAlgorithm() {
        System.out.println("Enter Number of Costumers");
        numproc = sc.nextInt();
        System.out.println("Enter Number of Resources");
        bits = sc.nextInt();
        System.out.println("Enter value of available");

        //create available
        availarr = new int[bits];
        for (int i = 0; i < bits; i++) {
            availarr[i] = sc.nextInt();
        }

        //create Allocation
        allocationarr = new int[numproc][bits];
        for (int i = 0; i < numproc; i++) {
            for (int j = 0; j < bits; j++) {
                allocationarr[i][j] = 0;
            }
        }
        //create Maximum = Need
        needarr = new int[numproc][bits];
        for (int i = 0; i < numproc; i++) {
            for (int j = 0; j < bits; j++) {
                needarr[i][j] = rand.nextInt(availarr[j]-1);
            }
        }
        //test print
        System.out.println("Available");
        print(availarr);
        System.out.println("Need");
        print2d(needarr);
        System.out.println("Allocation");
        print2d(allocationarr);
       // generate_req();
        //System.out.println("request process" + reqproc);
        //System.out.println("requested value:");
        //print(reqarr);

        execute();

    }

    void print(int arr[]) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    void print2d(int arr[][]) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
    }

    void generate_req() {
        reqproc = rand.nextInt(numproc);
        reqarr = new int[bits];
        for (int i = 0; i < reqarr.length; i++) {
            reqarr[i] = rand.nextInt((int)Math.ceil((needarr[reqproc][i])*1.15)+1);
        }
       // System.out.println("Request for "+reqproc);
       // print(reqarr);

    }

    void fillreslist() {
        reslist = new boolean[numproc];
        for (int i = 0; i < numproc; i++) {
            reslist[i] = false;
        }
//        System.out.println("in reslist");
//        for (int i = 0; i < numproc; i++) {
//            System.out.print(reslist[i]+" ");
//        }
//        System.out.println();
        }

    void checkrequest(int need[][], int avail[], int req[])//checks if req is <= both available and need
    {
        safeavail = true;
        safeneed = true;
        for (int i = 0; i < bits; i++) {
            if (reqarr[i] > availarr[i]) {
                safeavail = false;
            }
            if (reqarr[i] > needarr[reqproc][i]) {
                this.safeneed = false;
            }

        }
    }

    void isfinished(int need[][]) {
        finish = true;
        for (int i = 0; i < numproc; i++) {
            for (int j = 0; j < bits; j++) {
                if (needarr[i][j] != 0) {
                    finish = false;
                    break;
                }
            }
        }
    }

    boolean check(int flag)
    {
        for (int i = 0; i < bits; i++) {
            if(newneedarr[flag][i]>newavailarr[i])
                return false;
        }
        return true;
    }
    void issafe() {

       // System.out.println("in issafe ");
        newavailarr = new int[bits];
        newneedarr = new int[numproc][bits];
        newallocationarr = new int[numproc][bits];
        for(int i=0;i<bits;i++)
        {
            newavailarr[i]=availarr[i];
        }
        for(int i=0;i<numproc;i++)
        {
            for(int j=0;j<bits;j++)
            newneedarr[i][j]=needarr[i][j];
        }
        for(int i=0;i<numproc;i++)
        {
            for(int j=0;j<bits;j++)
            newallocationarr[i][j]=allocationarr[i][j];
        }
        reqcompleted = true;
        fillreslist();
        for (int i = 0; i < bits; i++) {
            newneedarr[reqproc][i] -= reqarr[i];
            newavailarr[i] -= reqarr[i];
        }
        int count=0;
        while (count<numproc) {
           // System.out.println("in loop ");
            boolean error=true;
            for (int i = 0; i < numproc; i++) 
            {
                if (reslist[i] == false) 
                {
                    //System.out.println(check(i));
                    if(check(i))
                    {
                        for (int j = 0; j < bits; j++) 
                         {
                            newavailarr[j] += newallocationarr[i][j];
                        }
                        count++;
                        reslist[i] = true;
                        error=false;
                    }
                    
                }
             }
            if(error)
                break;
        }
        if(count==numproc)
            reqcompleted=true;
        else
            reqcompleted=false;

    }

    void safe() {

        for (int i = 0; i < bits; i++) {
            allocationarr[reqproc][i] += reqarr[i];
            availarr[i] -= reqarr[i];
            needarr[reqproc][i] -= reqarr[i];
        }
    }
    void moveallocattoavail()
    {
        boolean x=true;
        for(int i=0;i<bits;i++)
        {
            if(needarr[numproc][i]!=0)
            {
                x=false;
            }
        }
        if(x)
        {
            for(int i=0;i<bits;i++)
            {
                availarr[i]+=allocationarr[reqproc][i];
            }
        }
    }
    boolean checkprocessend()
    {
       // System.out.println("in checkprocessend");
      for(int i=0;i<bits;i++)
      {
          if(needarr[reqproc][i]!=0)
              return false;
      }
      return true;
    }
    void alloctoavail()
    {
        //System.out.println("in allocatoavail");
        for(int i=0;i<bits;i++)
        {
            availarr[i]+=allocationarr[reqproc][i];
            allocationarr[reqproc][i]=0;
           // System.out.print(availarr[i]+" ");
        }
    }
    void generate_release()
    {
        releaseproc=rand.nextInt(numproc);
        releasearr=new int[bits];
        
        for(int i=0;i<bits;i++)
        {
            releasearr[i]=rand.nextInt(allocationarr[releaseproc][i]+1);
        }
    }
    
    boolean generateboolrel()
    {
        dorelease=rand.nextBoolean();
        return dorelease;
    }
    void release()
    {
        for(int i=0;i<bits;i++)
        {
            availarr[i]+=releasearr[i];
            allocationarr[releaseproc][i]-=releasearr[i];
        }
    }

    void execute() {
        finish = false;
        while (!finish) {
            generate_req();
            checkrequest(needarr, availarr, reqarr);
           
            if (safeavail && safeneed) {
              //  System.out.println("excute safeavail");
                issafe();
                if (reqcompleted) {
                    safe();
                    if(checkprocessend())
                    {
                        alloctoavail();
                    }
                 //   System.out.println("request completed");
                    System.out.println("Request for resource " + reqproc + " Accpeted");
                    print(reqarr);
                    System.out.println("Allocation");
                    print2d(allocationarr);
                    System.out.println("Need");
                    print2d(needarr);
                    System.out.println("available");
                    print(availarr);
                    isfinished(needarr);
                    System.out.println("System Finish ? " + finish);
                }
                else
                {
                    System.out.println("Request for resource " + reqproc + " Rejected");
                    print(reqarr);
                   // System.out.println("request uncompleted");
                }

                if (finish == true) {
                   // System.out.println("excute finishtrue");
                    System.out.println("Allocation");
                    print2d(allocationarr);
                    System.out.println("System Finished completely");
                    System.out.println("Need");
                    print2d(needarr);
                }
            } else {
                //System.out.println("excute else");
                System.out.println("Request Rejected , Request for process "+reqproc+" > Available or Need ");
                print(reqarr);
                //System.out.println("request rejected");
            }
            
            if(!finish)
            {
            if(generateboolrel())
            {
                generate_release();
                System.out.println("Release for resource "+releaseproc +" Accepted");
                print(releasearr);
                release();
                System.out.println("Available");
                print(availarr);
                System.out.println("Allocation");
                print2d(allocationarr);
                System.out.println("Need");
                print2d(needarr);
                
            }
              }

        }

    }

    public class BankerSAlgorithm {

    }

    public static void main(String[] args) {

        BankerAlgorithm b = new BankerAlgorithm();
    }

}
