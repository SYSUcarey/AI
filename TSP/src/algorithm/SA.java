package algorithm;

import java.util.Date;
import java.util.List;
import java.util.Random;


public class SA {
	// TSP�����еĵ㼯
	private List<City> datas;
	// SA�㷨�ж���Ĳ���
	private double T0 = 10000;	// ��ʼ�¶�
	private double T_end = 1;	// �����¶�
	private double q = 0.96;	// ����ϵ��
	private int L = 500;		// ÿ���¶ȵ���������Ҳ������
	private volatile int[] path;	// ��ǰ�����·��
	private double T;			// ��ǰ�¶�	
	private int count = 0;		// ���´���
	private boolean isFinished;
	
	// ���캯��
	public SA(List<City> datas_) {
		this.datas = datas_;
		T = T0;
		initPath();
		isFinished = false;
		//System.out.println("------Len: " + getPathLen(path, datas) + "------");
		//System.out.println("------Len: " + getPathLen(path, datas) + "------");
		//showPath(path);
	}
	
	// SA�㷨��������
	public void search() {
		Date begin = new Date();
		int type, k;
		double old_len, new_len, df, r, p;
		while(T > T_end) {
			if(T>100) {
				q = 0.96;
			}
			else if(T>20) {
				q = 0.98;
			}
			else {
				q = 0.995;
			}
			
			//System.out.println("------T: " + T + "------");
			//System.out.println("------Len: " + getPathLen(path, datas) + "------");
			//showPath(path);
			
			for(int i = 0; i < L; i++) {
				type = new Random().nextInt(3);
				int[] newPath = createNewPath(path, type);
				old_len = getPathLen(path, datas);
				new_len = getPathLen(newPath, datas);
				//System.out.println("new_len: " + new_len);
				df = (new_len - old_len);
				if(df <= 0) {
					path = newPath;
				}
				else {
					r = new Random().nextDouble();
					p = 1/(1+Math.exp(-df/T));
					//System.out.println("r:" + r + "---p:" + p);
					if(r > p) {
						//System.out.println("r:" + r + "---p:" + p);
						path = newPath;
					}
				}
			}
			T *= q;
			count++;
		}
		isFinished = true;
		//System.out.println("count: " + count);
		Date end = new Date();
		long dur = end.getTime() - begin.getTime();
		System.out.println("[Time] " + dur + "ms");
		System.out.println("[Result] " + getPathLen(path, datas));
		double eff = ((getPathLen(path, datas)/9352) - 1) * 100;
		System.out.println("[Effect] " + eff + "%");
		System.out.println("[Path] ");
		showPath(path);
	}
	
	// չʾ·��
	public void showPath(int[] path_) {
		for(int i = 0; i < path_.length-1; i++) {
			System.out.print(String.format(path_[i] + "->"));
			//if(i % 10 == 9) System.out.println();
		}
		System.out.println(path_[path_.length-1]);
	}
	
	// ����������ɳ�ʼ��
	public void initPath() {
		path = Tools.create_random_permutation(datas.size());
	}
	
	// get & set method
	public List<City> getDatas() {
		return this.datas;
	}
	public int[] getPath() {
		return this.path;
	}
	
	// �����µĽ�
	public int[] createNewPath(int[] old_path, int type) {
		int[] res = old_path.clone();
		int p1 = new Random().nextInt(old_path.length);
		int p2 = new Random().nextInt(old_path.length);
		switch(type) {
			case 0:
				Tools.swap(res, p1, p2);
				return res;
			case 1:
				Tools.reverse(res, p1, p2);
				return res;
			case 2:
				Tools.rightShift(res, p1, p2);
				return res;
			
		}
		//System.out.println(getPathLen(copy, datas));
		return res;
	}
	
	// ����·���ܾ���
	public double getPathLen(int[] path_, List<City> data_) {
		double res = 0;
		for(int i = 0; i < path_.length; i++) {
			int p1 = path_[i] - 1;
			int p2 = path_[(i+1)%path_.length] - 1;
			res += City.getDistance(data_.get(p1), data_.get(p2));
		}
		return res;
	}

	public boolean getFinished() {
		return this.isFinished;
	}
	

}
