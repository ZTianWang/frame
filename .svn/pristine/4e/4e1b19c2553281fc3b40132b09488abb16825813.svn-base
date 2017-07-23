package com.frame.test;

import java.util.HashSet;
import java.util.Set;

import com.frame.utils.idworker.IdWorker;

public class IdWorkerTest {
	private  final static IdWorker idWorker1= new IdWorker(31,31);
	  
	static class IdWorkThread implements Runnable {
		 private Set<Long> set;
		 private IdWorker idWorker;

		 public IdWorkThread(Set<Long> set, IdWorker idWorker) {
		 this.set = set;
		 this.idWorker = idWorker;
		}

		@Override
		 public void run() {
		 while (true) {
		 long id = idWorker.nextId();
		 System.out.println(id);
		 if (!set.add(id)) {
		 System.out.println("++++++++++++++++++++++++++duplicate:"+ id+"++++++++++++++++++++++++");
		  System.exit(0);
		 }
		}
		}
		}

		 public static void main(String[] args) {
 
 		 Set<Long> set = new HashSet<Long>();
		 
	 
		 Thread t1 = new Thread(new IdWorkThread(set, idWorker1));
		 Thread t2 = new Thread(new IdWorkThread(set, idWorker1));
		 Thread t3 = new Thread(new IdWorkThread(set, idWorker1));
		t1.setDaemon(true);
		t2.setDaemon(true);
		t3.setDaemon(true);
		t1.start();
		t2.start();
		t3.start();
		 try {
		Thread.sleep(1);
		 } catch (InterruptedException e) {
		e.printStackTrace();
		}
		}
}
