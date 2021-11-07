package sched;

public class sProcess {
  public int cputime;
  public int ioblocking;
  public int cpudone;
  public int ionext;
  public int numblocked;
  public int arrivedtime;
  public int id;
  public int bursttime;

  public sProcess (int cputime, int ioblocking, int cpudone, int ionext, int numblocked, int arrivedtime, int id) {
    this.cputime = cputime;
    this.ioblocking = ioblocking;
    this.cpudone = cpudone;
    this.ionext = ionext;
    this.numblocked = numblocked;
    this.arrivedtime = arrivedtime;
    this.id = id;
    this.bursttime = cputime/ioblocking + 1;
  } 	
}
