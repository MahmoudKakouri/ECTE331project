package project331;

public class Final {
	static class Data {
		boolean GoB1, GoB2, GoB3, GoA2, GoA3;
		int A1, A2, A3, B1, B2, B3;


	}

	static class Thread1 implements Runnable {
		private final Data d;

		public Thread1(Data d) {
			this.d=d;
		}

		@Override
		public void run() {
			// A synchronized block for each of A1 A2 A3
			synchronized (d) {
				d.A1=100*(100+1)/2;
				d.GoB1 = true;
				d.notify(); //notifying B1 to start
			}

			synchronized (d) {
				//waiting to be notified 
				while (!d.GoA2) {
					try {
						d.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				d.A2 = d.B2 + (400 * (400 + 1) / 2);
				d.GoB3 = true;
				d.notify(); //Notifying B3 to start
			}

			synchronized (d) {
				while (!d.GoA3) {
					try {
						d.wait();
					} 
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				d.A3=d.B3+(600*(600+1)/2);
			}
		}
	}

	static class Thread2 extends Thread {
		private final Data d;

		public Thread2(Data d) {
			this.d=d;
		}

		@Override
		public void run() {
			synchronized (d) {
				while (!d.GoB1) {
					try {
						d.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				d.B1 = d.A1 + (200*(200+1)/2);
				d.GoB2 = true;
				d.notify(); //Notifying B2 to start
			}

			synchronized (d) {
				while (!d.GoB2) {
					try {
						d.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				d.B2 = 300*(300+1)/2;
				d.GoA2 = true;
				d.notify();//Notifying A2 to start
			}

			synchronized (d) {
				while (!d.GoB3) {
					try {
						d.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				d.B3 = d.A2+(500*(500+1)/2);
				d.GoA3 = true;
				d.notify(); //Notifying A3 to start
			}
		}
	}

	public static void main(String[] args) {
		Data d = new Data();
		int i=0;
        int num_iter=65000;        
        while(i++<num_iter)
        {
		Thread2 t1 = new Thread2(d);
		Thread t2 = new Thread(new Thread1(d));

		t1.start();
		t2.start();

		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        }
		System.out.println("The final value of A1 is " + d.A1);
		System.out.println("The final value of B1 is " + d.B1);
		System.out.println("The final value of B2 is " + d.B2);
		System.out.println("The final value of A2 is " + d.A2);
		System.out.println("The final value of B3 is " + d.B3);
		System.out.println("The final value of A3 is " + d.A3);
	}
}