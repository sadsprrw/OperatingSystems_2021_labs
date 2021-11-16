package sched;

public class sProcess {
  public int cpuTime;
  public int ioBlocking;
  public int cpuDone;
  public int ioNext;
  public int numBlocked;
  public int arrivedTime;
  public int id;
  public int predictedTime;

  public sProcess (int cputime, int ioblocking, int cpudone, int ionext, int numblocked, int arrivedtime, int id, int predictedTime) {
    this.cpuTime = cputime;
    this.ioBlocking = ioblocking;
    this.cpuDone = cpudone;
    this.ioNext = ionext;
    this.numBlocked = numblocked;
    this.arrivedTime = arrivedtime;
    this.id = id;
    this.predictedTime = predictedTime;
  } 	
}
